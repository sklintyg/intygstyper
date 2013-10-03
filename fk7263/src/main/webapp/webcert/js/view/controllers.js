'use strict';

/* Controllers */
angular.module('controllers.fk7263.webcert', []);
angular.module('controllers.fk7263.webcert').controller('EditCertCtrl', [ '$scope', '$filter', '$location', '$rootScope', function EditCertCtrl($scope, $filter, $location, $rootScope) {
    $scope.cert = {};
} ]);

/*
 * ViewCertCtrl - Controller for logic related to viewing a certificate
 * 
 */
angular.module('controllers.fk7263.webcert').controller('ViewCertCtrl',
        [ '$scope', '$log', '$timeout', 'viewCertificateService', function CreateCertCtrl($scope, $log, $timeout, viewCertificateService) {

            // init state
            $scope.widgetState = {
                doneLoading : false,
                hasError : false
            }
            $scope.cert = {};
            $scope.cert.filledAlways = true;

            // Load certificate json
            $timeout(function() { // wrap in timeout to simulate latency -
                                    // remove soon
                viewCertificateService.getCertificate($scope.MODULE_CONFIG.CERT_ID_PARAMETER, function(result) {
                    $log.debug("Got getCertificate data:" + result);
                    $scope.widgetState.doneLoading = true;
                    if (result != null) {
                        $scope.cert = result;
                    } else {
                        // show error view
                        $scope.widgetState.hasError = true;
                    }
                });
            }, 2000);
        } ]);

/*******************************************************************************
 * QACtrl - Controller for logic related to viewing a Fraga/Svar for a
 * certificate
 * 
 */
angular.module('controllers.fk7263.webcert').controller('QACtrl', [ '$scope', '$log', '$timeout', 'fragaSvarService', function CreateCertCtrl($scope, $log, $timeout, fragaSvarService) {

    // init state
    $scope.qaList = {};
    $scope.widgetState = {
        doneLoading : false,
        hasError : false
    }
    // Request loading of QA's for this certificate
    $timeout(function() { // wrap in timeout to simulate latency - remove soon
        fragaSvarService.getQAForCertificate($scope.MODULE_CONFIG.CERT_ID_PARAMETER, function(result) {
            $log.debug("Got getQAForCertificate data:" + result);
            $scope.widgetState.doneLoading = true;
            if (result != null) {
                $scope.qaList = result;
            } else {
                // show error view
                $scope.widgetState.hasError = true;
            }
        });
    }, 500);

} ]);