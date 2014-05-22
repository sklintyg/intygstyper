define(['angular'], function(angular) {
    'use strict';

    return ['$scope', '$rootScope', '$filter', '$location', 'CertificateService', '$http', '$routeParams', '$log',
        'ManageCertView',
        function($scope, $rootScope, $filter, $location, CertificateService, http, $routeParams, $log, ManageCertView) {

            $scope.cert = {};

            $scope.widgetState = {
                doneLoading: false,
                activeErrorMessageKey: null,
                showTemplate: true
            };

            $scope.intygAvser = '';
            $scope.intygAvserList = [];

            $scope.$watch('cert.intygAvser.korkortstyp', function() {
                if (!$scope.cert || !$scope.cert.intygAvser || !$scope.cert.intygAvser.korkortstyp) {
                    return;
                }
                angular.forEach($scope.cert.intygAvser.korkortstyp, function(key) {
                    if (key.selected) {
                        this.push(key);
                    }
                }, $scope.intygAvserList);

                for (var i = 0; i < $scope.intygAvserList.length; i++) {
                    if (i < $scope.intygAvserList.length - 1) {
                        $scope.intygAvser += $scope.intygAvserList[i].type + (', ');
                    }
                    else {
                        $scope.intygAvser += $scope.intygAvserList[i].type;
                    }
                }
            }, true);

            // expose calculated static link for pdf download
            $scope.downloadAsPdfLink = '/moduleapi/certificate/' + $routeParams.certificateId + '/pdf';

            // Decide if helptext related to field 1.a) - 1.c)
            $scope.achelptext = false;

            $scope.certProperties = {
                isSent: false,
                isRevoked: false
            };

            CertificateService.getCertificate($routeParams.certificateId, function(result) {
                $scope.widgetState.doneLoading = true;
                if (result !== null) {
                    $scope.cert = result.contents;
                    if ($scope.cert.syn.synfaltsdefekter === true || $scope.cert.syn.nattblindhet === true ||
                        $scope.cert.syn.progressivOgonsjukdom === true) {
                        $scope.achelptext = true;
                    }
                    $rootScope.$emit('ts-bas.ViewCertCtrl.load', result.metaData);
                    $scope.certProperties.isSent = ManageCertView.isSentToTarget(result.metaData.statuses, 'TS');
                    $scope.certProperties.isRevoked = ManageCertView.isRevoked(result.metaData.statuses);
                } else {
                    $log.debug('Got error while loading cert - invalid data');
                    $scope.widgetState.activeErrorMessageKey = 'common.error.data_not_found';
                }
            }, function(error) {
                $scope.widgetState.doneLoading = true;
                $log.debug('Got error while loading cert: ' + error.message);
                if (error.errorCode === 'DATA_NOT_FOUND') {
                    $scope.widgetState.activeErrorMessageKey = 'common.error.data_not_found';
                } else {
                    $scope.widgetState.activeErrorMessageKey = 'common.error.data_not_found';
                }
            });
        }];
});
