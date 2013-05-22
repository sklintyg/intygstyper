'use strict';

/* Controllers */
RLIApp.controller('ViewCertCtrl', [ '$scope', '$filter', '$location', 'certService', function ViewCertCtrl($scope, $filter, $location, certService) {
    $scope.certificate = {};
    $scope.doneLoading = false;




    // fetch list of certs initially
    certService.getCertificate(function(result) {
        $scope.certificate = result;
        // filtering is done in view
        $scope.doneLoading = true;
    });
} ]);
