define([ 'angular', 'ts-diabetes/intyg/js/services', 'ts-diabetes/intyg/js/controllers/ViewCertCtrl',
        'ts-diabetes/intyg/js/controllers/SendCertWizardCtrl' ], function(angular, services, ViewCertCtrl, SendCertWizardCtrl) {
    'use strict';

    /* Controllers */
    angular.module('controllers.ts-diabetes.ViewCertCtrl', []);
    angular.module('controllers.ts-diabetes.ViewCertCtrl').controller('ViewCertCtrl',
            [ '$scope', '$filter', '$location', 'certService', '$log', function ViewCertCtrl($scope, $filter, $location, certService, $log) {
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
                    backdropFade: true,
                    dialogFade: true
                };

                $log.debug('Loading certificate ' + $scope.MODULE_CONFIG.CERT_ID_PARAMETER);

                certService.getCertificate($scope.MODULE_CONFIG.CERT_ID_PARAMETER, function(result) {
                    $scope.doneLoading = true;
                    if (result !== null) {
                        $scope.cert = result;
                    } else {
                        // show error view
                        $location.path('/fel');
                    }
                });
            } ]);
    var moduleName = "ts-diabetes.controllers";

    angular.module(moduleName, [ services ]).controller('ts-diabetes.ViewCertCtrl', ViewCertCtrl).controller('ts-diabetes.SendCertWizardCtrl',
            SendCertWizardCtrl);

    return moduleName;
});
