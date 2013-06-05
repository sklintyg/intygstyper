'use strict';

/* Controllers */
angular.module('controllers.fk7263.ViewCertCtrl', []);
angular.module('controllers.fk7263.ViewCertCtrl').controller('ViewCertCtrl', [ '$scope', '$filter', '$location', 'certService', function ViewCertCtrl($scope, $filter, $location, certService) {
        $scope.cert = {};
        $scope.doneLoading = false;
        $scope.shouldBeOpen = false;

        $scope.open = function () {
            //$scope.shouldBeOpen = true;

            $location.path("/recipients");
        };

        $scope.close = function () {
            $scope.closeMsg = 'I was closed at: ' + new Date();
            $scope.shouldBeOpen = false;
        };

        $scope.opts = {
            backdropFade: true,
            dialogFade: true
        };

        console.log("Loading certificate " + $scope.MODULE_CONFIG.CERT_ID_PARAMETER);
        certService.getCertificate($scope.MODULE_CONFIG.CERT_ID_PARAMETER, function (result) {
            $scope.doneLoading = true;
            if (result != null) {
                $scope.cert = result;
            } else {
                // show error view
                $location.path("/fel");
            }
        });
    } ])
    .filter('smittskydd', function () {
        return function (activities) {
            if (angular.isObject(activities)) {

                // collect all smittskydd actitivies
                var smittskyddActivities = activities.filter(function(aktivitet) {
                    return aktivitet.aktivitetskod == "AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA";
                }).length;

                if (smittskyddActivities > 0) {
                    return "yes";
                }
                else {
                    return "no";
                }
            }

        };
    });
;

angular.module('controllers.fk7263.RecipientCertCtrl').controller('RecipientCertCtrl', [ '$scope', '$filter', '$location', 'certService', function RecipientCertCtrl($scope, $filter, $location, certService) {

} ]);
