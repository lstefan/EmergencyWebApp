angular
		.module('app', [ 'ngRoute', 'auth', 'home', 'dashboard', 'navigation' ])
		.config(

				function($routeProvider, $httpProvider, $locationProvider, uiGmapGoogleMapApiProvider) {

					$locationProvider.html5Mode(true);

					$routeProvider.when('/', {
						templateUrl : 'js/home/home.html',
						controller : 'home',
						controllerAs : 'controller'
					}).when('/dashboard', {
						templateUrl : 'js/dashboard/dashboard.html',
						controller : 'dashboard',
						controllerAs : 'controller'
					}).when('/login', {
						templateUrl : 'js/navigation/login.html',
						controller : 'navigation',
						controllerAs : 'controller'
					}).otherwise('/');

					$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

					uiGmapGoogleMapApiProvider.configure({
						key: 'AIzaSyBMAJiWFCniLx7sMY0kHYURaTy_JAgm1Mk',
						v: '3.28', //defaults to latest 3.X anyhow
						libraries: 'weather,geometry,visualization'
					});

				}).run(function(auth) {

			// Initialize auth module with the home page and login/logout path
			// respectively
			auth.init('/', '/login', '/logout');


		});

