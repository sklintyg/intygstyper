define([], function() {
	'use strict';

	return [ '$scope', '$filter', '$location', '$routeParams', 'ts-bas.certificateService', '$http',
			function($scope, $filter, $location, $routeParams, certificateService, http) {
				$scope.cert = {};
				$scope.doneLoading = false;
				$scope.shouldBeOpen = false;

				$scope.open = function() {
					$scope.shouldBeOpen = true;
				};

				$scope.close = function() {
					$scope.closeMsg = 'I was closed at: ' + new Date();
					$scope.shouldBeOpen = false;
				};

				$scope.opts = {
					backdropFade : true,
					dialogFade : true
				};

				certificateService.getCertificate($routeParams.certificateId, function(result) {
					$scope.doneLoading = true;
					if (result != null) {
						$scope.cert = result;
					} else {
						// show error view
						$location.path("/fel");
					}
			}];
});
