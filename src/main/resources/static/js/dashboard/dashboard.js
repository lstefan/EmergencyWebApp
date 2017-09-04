angular.module('dashboard', ['uiGmapgoogle-maps']).controller('dashboard', function($http, $scope, uiGmapGoogleMapApi, DataService, IncidentService, ChatService) {
	var self = this;
	var currentUsername;
	var incident = {};

	$http.get('/resource/').then(function(response) {
		self.greeting = response.data;
	});

	$http.get('/username/').then(function(response) {
		currentUsername = response.data.user;
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

	$scope.addMessage = function() {
		var messageObject = {};
		messageObject.message = $scope.message;
		messageObject.time = new Date();
		messageObject.user = currentUsername;
        messageObject.mine = true;
		$scope.messages.push(messageObject);

		var chatMessage = {};
		chatMessage.type = 506;
		chatMessage.doctorID = currentUsername;
		chatMessage.requesterID = incident.requesterID;
		chatMessage.message = $scope.message;
		ChatService.send(chatMessage);

		$scope.message = "";
	};

    $scope.accept = function() {
		var acceptMessage = incident;
		acceptMessage.type = 504;
		acceptMessage.doctorID = currentUsername;
        IncidentService.send(acceptMessage);
        $('#myModal').modal('hide');
		acceptMessage = {};
    };

	ChatService.receive().then(null, null, function(message) {
		var messageObject = {};
		messageObject.message = message.message
		messageObject.time = new Date();
		messageObject.user = message.user;
        messageObject.mine = false;
        $scope.messages.push(messageObject);
	});

    IncidentService.receive().then(null, null, function(message) {
		incident = message;
		$scope.incident = message;
        $('#myModal').modal('show');
    });

	DataService.receive().then(null, null, function(image) {
		//console.log("Am primit " + image)
		var imageSrc = "data:image/jpg;base64," + image;
		$scope.imageSrc = imageSrc;
		//console.log(imageSrc)
		//$scope.imageSrcList.push(imageSrc);
	});

    //AudioService.receive().then(null, null, function(audio) {
    //	var audioSrc = "data:audio/wav;base64," + audio;
    //	$scope.audioSrc = audioSrc;
    //	//console.log(audioSrc)
    //    var snd = new Audio("data:audio/wav;base64," + audio);
    //    snd.play();
    //});


}).service("DataService", function ($q, $timeout) {
	var service = {}, listener = $q.defer(), socket = {
		client: null,
		stomp: null
	}, messageIds = [];


	service.RECONNECT_TIMEOUT = 30000;
	service.SOCKET_URL = "/data";
	service.CHAT_TOPIC = "/topic/message";
	service.FRAME_TOPIC = "/topic/frame";
    service.INCIDENT_TOPIC = "/topic/incident";
	service.CHAT_BROKER = "/app/data";

	service.receive = function () {
		return listener.promise;
	};

	service.send = function (message) {
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
		out.time = new Date();
		if (_.contains(messageIds, message.id)) {
			out.self = true;
			messageIds = _.remove(messageIds, message.id);
		}
		return out;
	};

	var getFrame = function (data) {
		var message = JSON.parse(data), image = {};
		image = message.payload;
		return image;
	};

	var startListener = function () {

		socket.stomp.subscribe(service.FRAME_TOPIC, function (data) {
			listener.notify(getFrame(data.body));
		});
	};

	var initialize = function () {
		socket.client = new SockJS(service.SOCKET_URL);
		socket.client.debug = null
		socket.stomp = Stomp.over(socket.client);
		socket.stomp.connect({}, startListener);
		socket.stomp.onclose = reconnect;
	};

	initialize();
	return service;
}).service("ChatService", function ($q, $timeout) {
	var service = {}, listener = $q.defer(), socket = {
		client: null,
		stomp: null
	}, messageIds = [];

	service.RECONNECT_TIMEOUT = 30000;
	service.SOCKET_URL = "/data";
	service.CHAT_TOPIC = "/topic/chat";
	service.CHAT_BROKER = "/app/data";

	service.receive = function () {
		return listener.promise;
	};

	service.send = function (message) {
		socket.stomp.send(service.CHAT_BROKER, {
			priority: 9
		}, JSON.stringify(message));
	};

	var reconnect = function () {
		$timeout(function () {
			initialize();
		}, this.RECONNECT_TIMEOUT);
	};

	var getMessage = function (data) {
		var message = JSON.parse(data), out = {};
        console.log("mesaj " + message);
		out.message = message.message;
		out.time = new Date();
		out.user = message.requesterID;
		return out;
	};

	var startListener = function () {

		socket.stomp.subscribe(service.CHAT_TOPIC, function (data) {
			listener.notify(getMessage(data.body));
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
}).service("IncidentService", function ($q, $timeout) {
    var service = {}, listener = $q.defer(), socket = {
        client: null,
        stomp: null
    }, messageIds = [];

    service.RECONNECT_TIMEOUT = 30000;
    service.SOCKET_URL = "/data";
    service.CHAT_TOPIC = "/topic/message";
    service.INCIDENT_TOPIC = "/topic/incident";
    service.CHAT_BROKER = "/app/data";

    service.receive = function () {
        return listener.promise;
    };

    service.send = function (message) {
        socket.stomp.send(service.CHAT_BROKER, {
            priority: 9
        }, JSON.stringify(message));
    };

    var reconnect = function () {
        $timeout(function () {
            initialize();
        }, this.RECONNECT_TIMEOUT);
    };

    var getIncident = function (data) {
        var incident = JSON.parse(data);
        console.log("incident " + incident)
        return incident;
    };

    var startListener = function () {
        socket.stomp.subscribe(service.INCIDENT_TOPIC, function (data) {
            //console.log(getIncident(data.body))
            listener.notify(getIncident(data.body));
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
//    .service("AudioService", function ($q, $timeout, $window) {
//    var service = {}, listener = $q.defer(), socket = {
//        client: null,
//        stomp: null
//    }, messageIds = [];
//
//    var audioContext;
//    var buf;
//
//    service.RECONNECT_TIMEOUT = 30000;
//    service.SOCKET_URL = "/data";
//    service.CHAT_TOPIC = "/topic/message";
//    service.FRAME_TOPIC = "/topic/audio";
//    service.CHAT_BROKER = "/app/data";
//
//    service.receive = function () {
//        return listener.promise;
//    };
//
//    service.send = function (message) {
//        var id = Math.floor(Math.random() * 1000000);
//        socket.stomp.send(service.CHAT_BROKER, {
//            priority: 9
//        }, JSON.stringify({
//            message: message,
//            id: id
//        }));
//        messageIds.push(id);
//    };
//
//    var reconnect = function () {
//        $timeout(function () {
//            initialize();
//        }, this.RECONNECT_TIMEOUT);
//    };
//
//    var getMessage = function (data) {
//        var message = JSON.parse(data), out = {};
//        out.message = message.message;
//        out.time = new Date(message.time);
//        if (_.contains(messageIds, message.id)) {
//            out.self = true;
//            messageIds = _.remove(messageIds, message.id);
//        }
//        return out;
//    };
//
//    var getFrame = function (data) {
//        var message = JSON.parse(data), image = {};
//        image = message.payload;
//        console.log("Data " + image);
//        return image;
//    };
//
//    var startListener = function () {
//        //socket.stomp.subscribe(service.CHAT_TOPIC, function (data) {
//        //	listener.notify(getMessage(data.body));
//        //});
//
//        socket.stomp.subscribe(service.FRAME_TOPIC, function (data) {
//
//            //listener.notify(getFrame(data.body));
//            playByteArray(getFrame(data.body));
//        });
//    };
//
//    var b64DecodeUnicode = function(str) {
//        // Going backwards: from bytestream, to percent-encoding, to original string.
//        return decodeURIComponent(atob(str).split('').map(function(c) {
//            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
//        }).join(''));
//    };
//
//    var stringToByteArray = function(str) {
//        var data = [];
//        for (var i = 0; i < str.length; i++){
//            data.push(str.charCodeAt(i));
//        }
//
//        return data;
//    };
//
//    var _base64ToArrayBuffer = function(base64) {
//        var binary_string =  $window.atob(base64);
//        var len = binary_string.length;
//        var bytes = new Uint8Array( len );
//        for (var i = 0; i < len; i++)        {
//            bytes[i] = binary_string.charCodeAt(i);
//        }
//        return bytes.buffer;
//    }
//
//    //var playByteArray = function(byteArray) {
//    //
//    //    var arrayBuffer = new ArrayBuffer(byteArray.length);
//    //    //var bufferView = new Uint8Array(arrayBuffer);
//    //    //for (i = 0; i < byteArray.length; i++) {
//    //    //    bufferView[i] = byteArray[i];
//    //    //}
//    //
//    //    audioContext.decodeAudioData(arrayBuffer, function(buffer) {
//    //        buf = buffer;
//    //        play();
//    //    });
//    //};
//
//    var playByteArray = function( bytes ) {
//        //var buffer = new Uint8Array( bytes.length );
//        //buffer.set( new Uint8Array(bytes), 0 );
//        //
//        //audioContext.decodeAudioData(buffer.buffer, play);
//
//       // var decodedBytes = b64DecodeUnicode(bytes);
//
//        var length = bytes.length;
//        var bytearray = new Uint8Array(bytes.length);
//        for (var i=0;i<length;++i) {
//            bytearray[i] = bytes[i];
//        }
//
//        console.log(bytearray);
//        //var convertedBytes = bytearray.buffer;
//        //console.log("Buffer " + convertedBytes.buffer)
//        var convertedBytes = bytes;
//        console.log("Received " + bytes);
//
//        // Stereo
//        var channels = 1;
//        var frameCount = convertedBytes.length / 2;
//        var sampleRate = 8000;
//        var myAudioBuffer = audioContext.createBuffer(channels, frameCount, sampleRate);
//
//        var nowBuffering = myAudioBuffer.getChannelData(0);
//        for (var i = 0; i < frameCount; i+=2) {
//            var word = ((convertedBytes.charCodeAt(i * 2) & 0xff) << 8) + (convertedBytes.charCodeAt(i * 2 + 1) & 0xff);
//            var signedWord = (word + 32768) % 65536 - 32768;
//            nowBuffering[i] = signedWord / 32768.0;
//        }
//
//        console.log("Converted " + nowBuffering);
//        play(myAudioBuffer);
//
//    };
//
//    // Play the loaded file
//    var play = function(audioBuffer) {
//        // Create a source node from the buffer
//        var source = audioContext.createBufferSource();
//        source.buffer = audioBuffer;
//        // Connect to the final output node (the speakers)
//        source.connect(audioContext.destination);
//        // Play immediately
//        source.start(0);
//    };
//
//    var initialize = function () {
//        socket.client = new WebSocket("ws://localhost:8080/data/websocket");
//        socket.client.binaryType = 'arraybuffer';
//        //socket.client.debug = null
//        socket.stomp = Stomp.over(socket.client);
//        socket.stomp.connect({}, startListener);
//        socket.stomp.onclose = reconnect;
//
//        function init() {
//            if (!$window.AudioContext) {
//                if (!$window.webkitAudioContext) {
//                    alert("Your browser does not support any AudioContext and cannot play back this audio.");
//                    return;
//                }
//                $window.AudioContext = window.webkitAudioContext;
//            }
//
//            audioContext = new AudioContext();
//        }
//
//        init();
//    };
//
//    initialize();
//    return service;
//});
