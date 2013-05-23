'use strict';

/* Controllers */
RLIApp.controller('ViewCertCtrl', [ '$scope', '$filter', '$location', 'certService', function ViewCertCtrl($scope, $filter, $location, certService) {
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
        backdropFade : true,
        dialogFade : true
    };

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
