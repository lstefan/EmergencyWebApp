angular.module('dashboard', ['uiGmapgoogle-maps']).controller('dashboard', function($http, $scope, uiGmapGoogleMapApi, DataService) {
	var self = this;
	$http.get('/resource/').then(function(response) {
		self.greeting = response.data;
	});

	// Define variables for our Map object
	var areaLat      = 44.435719,
		areaLng      = 26.047784,
		areaZoom     = 10;



	uiGmapGoogleMapApi.then(function (maps) {
		maps.visualRefresh = true;
		$scope.map     = {
			center: { latitude: areaLat, longitude: areaLng },
			zoom: areaZoom ,
			options: {
				mapTypeId: google.maps.MapTypeId.ROADMAP, // This is an example of a variable that cannot be placed outside of uiGmapGooogleMapApi without forcing of calling ( like ugly people ) the google.map helper outside of the function
				streetViewControl: false,
				mapTypeControl: false,
				scaleControl: false,
				rotateControl: false,
				zoomControl: true
			},
			showTraficLayer:true
		};

		$scope.marker = {coords: angular.copy($scope.map.center)}
		console.log($scope.map);
		$scope.isOffline = false;
	});

	$scope.messages = [];
	$scope.message = "";
	$scope.max = 140;

	$scope.imageSrcList = [];
	$scope.imageSrc = "";

	$scope.addMessage = function() {
		DataService.send($scope.message);
		$scope.message = "";
	};

	//DataService.receive().then(null, null, function(message) {
	//	$scope.messages.push(message);
	//});

	DataService.receive().then(null, null, function(image) {
		var imageSrc = "data:image/jpg;base64," + image;
		$scope.imageSrcList.push(imageSrc);
	});

}).service("DataService", function ($q, $timeout) {

	var service = {}, listener = $q.defer(), socket = {
		client: null,
		stomp: null
	}, messageIds = [];

	service.RECONNECT_TIMEOUT = 30000;
	service.SOCKET_URL = "/data";
	service.CHAT_TOPIC = "/topic/message";
	service.FRAME_TOPIC = "/topic/frame";
	service.CHAT_BROKER = "/app/data";

	service.receive = function () {
		return listener.promise;
	};

	service.send = function (message) {
		var id = Math.floor(Math.random() * 1000000);
		socket.stomp.send(service.CHAT_BROKER, {
			priority: 9
		}, JSON.stringify({
			message: message,
			id: id
		}));
		messageIds.push(id);
	};

	var reconnect = function () {
		$timeout(function () {
			initialize();
		}, this.RECONNECT_TIMEOUT);
	};

	var getMessage = function (data) {
		var message = JSON.parse(data), out = {};
		out.message = message.message;
		out.time = new Date(message.time);
		if (_.contains(messageIds, message.id)) {
			out.self = true;
			messageIds = _.remove(messageIds, message.id);
		}
		return out;
	};

	var getFrame = function (data) {
		var message = JSON.parse(data), out = {};
		out.message = message.payload;
		return out;
	};

	var startListener = function () {
		//socket.stomp.subscribe(service.CHAT_TOPIC, function (data) {
		//	listener.notify(getMessage(data.body));
		//});

		socket.stomp.subscribe(service.FRAME_TOPIC, function (data) {
			listener.notify(getFrame(data.body));
		});
	};

	var initialize = function () {
		socket.client = new SockJS(service.SOCKET_URL);
		socket.stomp = Stomp.over(socket.client);
		socket.stomp.connect({}, startListener);
		socket.stomp.onclose = reconnect;
	};

	initialize();
	return service;
});
