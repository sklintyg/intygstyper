define([], function() {
	'use strict';
	return [ '$scope', '$routeParams', '$route', function SentCertWizardCtrl($scope, $routeParams, $route) {

		// set a default if no errorCode is given in routeparams
		$scope.errorCode = $routeParams.errorCode || "generic";
		$scope.backLink = $route.current.backLink || "#view";
		$scope.pagefocus = true;
	} ];
});
