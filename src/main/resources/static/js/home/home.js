angular.module('home', []).controller('home', function($http) {
	var self = this;
	$http.get('/username/').then(function(response) {
		self.user = response.data.user;
	});
});
