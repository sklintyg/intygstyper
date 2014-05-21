define([], function() {
    'use strict';

    return ['$scope', '$rootScope', '$filter', '$location', 'CertificateService', '$http', '$routeParams', '$log', 'ManageCertView',
        function($scope, $rootScope, $filter, $location, CertificateService, http, $routeParams, $log, ManageCertView) {

        // init state
        $scope.widgetState = {
            doneLoading: false,
            activeErrorMessageKey: null,
            showTemplate: true
        };

        $scope.cert = {};
        $scope.shouldBeOpen = false;
        $scope.cert.filledAlways = true;

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

        $log.debug('Loading certificate ' + $routeParams.certificateId);

        $scope.certProperties = {
            sentToFK: false,
            isRevoked: false
        };

        CertificateService.getCertificate($routeParams.certificateId, function(result) {
            $scope.widgetState.doneLoading = true;
            if (result !== null && result !== '') {
                $scope.cert = result.contents;
                $rootScope.$emit('fk7263.ViewCertCtrl.load', result.metaData);
                $scope.certProperties.sentToFK = ManageCertView.isSentToFK(result.metaData.statuses);
                $scope.certProperties.isRevoked = ManageCertView.isRevoked(result.metaData.statuses);

                $scope.pdfUrl = '/moduleapi/intyg/signed/' + $scope.cert.id + '/pdf';
            } else {
                $scope.widgetState.activeErrorMessageKey = 'error.could_not_load_cert';
            }
        }, function(error) {
            $scope.widgetState.doneLoading = true;
            if (error.errorCode === 'DATA_NOT_FOUND') {
                $scope.widgetState.activeErrorMessageKey = 'error.data_not_found';
            } else {
                $scope.widgetState.activeErrorMessageKey = 'error.could_not_load_cert';
            }
            $log.debug('Got error while loading cert');
            $log.debug(error.message);
        });
    }];
});
