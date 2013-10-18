'use strict';

/* Controllers */
angular.module('wc.fk7263.controllers', []);

/*
 * EditCertCtrl - Controller for logic related to editing a certificate
 */
angular.module('wc.fk7263.controllers').controller('EditCertCtrl', [ '$scope', '$filter', '$location', '$rootScope', function EditCertCtrl($scope, $filter, $location, $rootScope) {

    // init state
    $scope.widgetState = {
        doneLoading : false,
        hasError : false
    }

    $scope.cert = {};
    $scope.widgetState.doneLoading = true;

} ]);

/*
 * ViewCertCtrl - Controller for logic related to viewing a certificate
 */
angular.module('wc.fk7263.controllers').controller('ViewCertCtrl', [ '$scope', '$log', '$timeout', 'viewCertificateService', function CreateCertCtrl($scope, $log, $timeout, viewCertificateService) {

    // init state
    $scope.widgetState = {
        doneLoading : false,
        activeErrorMessageKey : null
    }
    $scope.cert = {};
    $scope.cert.filledAlways = true;

    // Load certificate json
    viewCertificateService.getCertificate($scope.MODULE_CONFIG.CERT_ID_PARAMETER, function(result) {
        $log.debug("Got getCertificate data:" + result);
        $scope.widgetState.doneLoading = true;
        if (result != null) {
            $scope.cert = result;
        }
    }, function(errorData) {
        // show error view
        $scope.widgetState.doneLoading = true;
        $scope.widgetState.activeErrorMessageKey = "error.could_not_load_cert";
    });
} ]);
