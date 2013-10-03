'use strict';

/* Controllers */
angular.module('controllers.fk7263.webcert', []);
angular.module('controllers.fk7263.webcert').controller('EditCertCtrl', [ '$scope', '$filter', '$location', '$rootScope', function EditCertCtrl($scope, $filter, $location, $rootScope) {
    $scope.cert = {};
} ]);

/*
 *  ViewCertCtrl - Controller for logic related to viewing a certificate 
 * 
 */
angular.module('controllers.fk7263.webcert').controller('ViewCertCtrl', [ '$scope', '$window', function ViewCertCtrl($scope, $window) {
	$scope.cert = {}
	$scope.cert.filledAlways = true;
	
} ]);