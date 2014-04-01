'use strict';

/* Controllers */
angular.module('wc.fk7263.controllers', []);

/*
 * ViewCertCtrl - Controller for logic related to viewing a certificate
 */
angular.module('wc.fk7263.controllers').controller('ViewCertCtrl', [ '$scope', '$log', '$timeout', 'viewCertificateService', function CreateCertCtrl($scope, $log, $timeout, viewCertificateService) {

    // init state
    $scope.widgetState = {
        doneLoading : false,
        activeErrorMessageKey : null,
    }
    $scope.certProperties = {
        sentToFK : undefined
    }
    $scope.cert = {};
    $scope.cert.filledAlways = true;

    var isSentToFK = function(statusArr) {
        if (statusArr) {
            for ( var i = 0; i < statusArr.length; i++) {
                if (statusArr[i].target === "FK" && statusArr[i].type === "SENT") {
                    return true;
                }
            }
        }
        return false;
    }
    var isRevoked = function(statusArr) {
        if (statusArr) {
            for ( var i = 0; i < statusArr.length; i++) {
                if (statusArr[i].type === "CANCELLED") {
                    return true;
                }
            }
        }
        return false;
    }

    // Load certificate json
    viewCertificateService.getCertificate($scope.MODULE_CONFIG.CERT_ID_PARAMETER, function(result) {
        $log.debug("Got getCertificate data:" + result);
        $scope.widgetState.doneLoading = true;
        if (result != null) {
            $scope.cert = result.certificateContent;

            $scope.certProperties.sentToFK = isSentToFK(result.certificateContentMeta.statuses);
            $scope.certProperties.isRevoked = isRevoked(result.certificateContentMeta.statuses);
        }
    }, function(errorData) {
        // show error view
        $scope.widgetState.doneLoading = true;
        if (errorData && errorData.errorCode === "AUTHORIZATION_PROBLEM") {
            $scope.widgetState.activeErrorMessageKey = "error.could_not_load_cert_not_auth";
        } else if (errorData && errorData.errorCode) {
            $scope.widgetState.activeErrorMessageKey = "error." + errorData.errorCode;
        } else {
            $scope.widgetState.activeErrorMessageKey = "error.could_not_load_cert";
        }
    });
} ]);
