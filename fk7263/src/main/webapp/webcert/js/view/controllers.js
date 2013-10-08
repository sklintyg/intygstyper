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
angular.module('wc.fk7263.controllers').controller('ViewCertCtrl',
        [ '$scope', '$log', '$timeout', 'viewCertificateService', function CreateCertCtrl($scope, $log, $timeout, viewCertificateService) {

            // init state
            $scope.widgetState = {
                doneLoading : false,
                hasError : false
            }
            $scope.cert = {};
            $scope.cert.filledAlways = true;
            $scope.newQuestionOpen = false;
            
            $scope.toggleQuestionForm = function() {
            	$scope.newQuestionOpen = !$scope.newQuestionOpen;
            }

            // Load certificate json
           // $timeout(function() { // wrap in timeout to simulate latency -
                
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
            //}, 2000);
        } ]);
