angular.module('dashboard', ['uiGmapgoogle-maps']).controller('dashboard', function($http, $scope, uiGmapGoogleMapApi) {
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
});
