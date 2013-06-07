'use strict';

/* Controllers */
angular.module('controllers.fk7263.ViewCertCtrl', []);
angular.module('controllers.fk7263.ViewCertCtrl').controller('ViewCertCtrl', [ '$scope', '$filter', '$location', 'certService', function ViewCertCtrl($scope, $filter, $location, certService) {
    $scope.cert = {};

    $scope.doneLoading = false;
    $scope.shouldBeOpen = false;

    $scope.open = function() {
        // $scope.shouldBeOpen = true;

        $location.path("/recipients");
    };

    $scope.close = function() {
        $scope.closeMsg = 'I was closed at: ' + new Date();
        $scope.shouldBeOpen = false;
    };

    $scope.opts = {
        backdropFade : true,
        dialogFade : true
    };

    $scope.smittskydd = function() {
        if (angular.isObject($scope.cert.aktiviteter)) {

            // collect all smittskydd actitivies
            var smittskyddActivities = $scope.cert.aktiviteter.filter(function(aktivitet) {
                return aktivitet.aktivitetskod == "AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA";
            }).length;

            if (smittskyddActivities > 0) {
                return "yes";
            } else {
                return "no";
            }
        }
    }
    // expose calculated static link for pdf download
    $scope.downloadAsPdfLink = $scope.MODULE_CONFIG.MI_COMMON_API_CONTEXT_PATH + $scope.MODULE_CONFIG.CERT_ID_PARAMETER + "/pdf";

    console.log("Loading certificate " + $scope.MODULE_CONFIG.CERT_ID_PARAMETER);
    certService.getCertificate($scope.MODULE_CONFIG.CERT_ID_PARAMETER, function(result) {
        $scope.doneLoading = true;
        if (result != null) {
            $scope.cert = result;
        } else {
            // show error view
            $location.path("/fel");
        }
    });
} ]);

angular.module('controllers.fk7263.RecipientCertCtrl').controller('RecipientCertCtrl',
        [ '$scope', '$filter', '$location', 'certService', function RecipientCertCtrl($scope, $filter, $location, certService) {

        } ]);
