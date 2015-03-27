define([], function() {
    'use strict';

    return ['$scope', '$filter', '$location', 'ts-diabetes.certificateService', '$http', '$stateParams', '$log',
        function($scope, $filter, $location, certService, http, $stateParams, $log) {

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

            $log.debug('Loading certificate ' + $stateParams.certificateId);

            certService.getCertificate($stateParams.certificateId, function(result) {
                $scope.doneLoading = true;
                if (result !== null) {
                    $scope.cert = result;
                } else {
                    // show error view
                    $location.path('/fel');
                }
            });
        }
    ];
});
