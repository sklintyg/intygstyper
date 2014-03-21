define([
], function () {
    'use strict';

    return ['$scope', '$filter', '$location', 'ts-diabetes.certificateService', '$http', '$routeParams',
        function ($scope, $filter, $location, certService, http, $routeParams) {

            $scope.cert = {};
            $scope.doneLoading = false;
            $scope.shouldBeOpen = false;

            $scope.open = function () {
                $scope.shouldBeOpen = true;
            };

            $scope.close = function () {
                $scope.closeMsg = 'I was closed at: ' + new Date();
                $scope.shouldBeOpen = false;
            };

            $scope.opts = {
                backdropFade : true,
                dialogFade : true
            };

            console.log("Loading certificate " + $routeParams.certificateId);

            // http.get('http://localhost:8088/m/rli/api/view/utlatande/' + $scope.MODULE_CONFIG.CERT_ID_PARAMETER).then(function(res) {
            //   $scope.cert = res.data;
            //   $scope.doneLoading = true;
            // });

            certService.getCertificate($routeParams.certificateId, function (result) {
                $scope.doneLoading = true;
                if (result != null) {
                    $scope.cert = result;
                } else {
                    // show error view
                    $location.path("/fel");
                }
            });
        }];
});
