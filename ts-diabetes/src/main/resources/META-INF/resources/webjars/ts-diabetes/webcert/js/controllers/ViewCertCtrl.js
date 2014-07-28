angular.module('ts-diabetes').controller('ts-diabetes.ViewCertCtrl',
    [ '$location', '$log', '$rootScope', '$routeParams', '$scope', '$cookieStore', 'common.CertificateService',
        'common.ManageCertView', 'webcert.ManageCertificate',
        function($location, $log, $rootScope, $routeParams, $scope, $cookieStore, CertificateService, ManageCertView,
            ManageCertificate) {
            'use strict';

            // Copy dialog setup
            var COPY_DIALOG_COOKIE = 'wc.dontShowCopyDialog';
            var copyDialog = {
                isOpen: false
            };
            $scope.dialog = {
                acceptprogressdone: true,
                focus: false,
                errormessageid: 'error.failedtocopyintyg',
                showerror: false,
                dontShowCopyInfo: $cookieStore.get(COPY_DIALOG_COOKIE)
            };

            // Page setup
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
                    } else {
                        $scope.intygAvser += $scope.intygAvserList[i].type;
                    }
                }
            }, true);

            // expose calculated static link for pdf download
            $scope.downloadAsPdfLink = '/moduleapi/certificate/' + $routeParams.certificateId + '/pdf';

            $scope.certProperties = {
                isSent: false,
                isRevoked: false
            };

            CertificateService.getCertificate($routeParams.certificateId, function(result) {
                $scope.widgetState.doneLoading = true;
                if (result !== null && result !== '') {
                    $scope.cert = result.contents;
                    $rootScope.$emit('ts-diabetes.ViewCertCtrl.load', result.metaData);
                    $scope.certProperties.isSent = ManageCertView.isSentToTarget(result.metaData.statuses, 'TS');
                    $scope.certProperties.isRevoked = ManageCertView.isRevoked(result.metaData.statuses);
                } else {
                    $scope.widgetState.activeErrorMessageKey = 'common.error.data_not_found';
                }
            }, function(error) {
                $scope.widgetState.doneLoading = true;
                $log.debug('got error' + error.message);
                if (error.errorCode === 'DATA_NOT_FOUND') {
                    $scope.widgetState.activeErrorMessageKey = 'common.error.data_not_found';
                } else {
                    $scope.widgetState.activeErrorMessageKey = 'common.error.data_not_found';
                }
            });

            /**
             * Exposed scope interaction functions
             */

            ManageCertificate.initSend($scope);
            $scope.send = function(cert) {
                ManageCertificate.send($scope, cert, 'ts-diabetes.label.send');
            };

            $scope.copy = function(cert) {
                cert.intygType = 'ts-diabetes'
                copyDialog = ManageCertificate.copy($scope, cert, COPY_DIALOG_COOKIE);
            };
        }]);
