angular.module('ts-diabetes').controller('ts-diabetes.ViewCertCtrl',
    [ '$location', '$log', '$rootScope', '$routeParams', '$scope', '$cookieStore', 'common.CertificateService',
        'common.ManageCertView', 'common.messageService', 'webcert.ManageCertificate',
        function($location, $log, $rootScope, $routeParams, $scope, $cookieStore, CertificateService, ManageCertView,
            messageService, ManageCertificate) {
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
                showTemplate: true,
                printStatus: 'notloaded'
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

            $scope.certProperties = {
                isSent: false,
                isRevoked: false
            };

            function loadCertificate() {
                CertificateService.getCertificate($routeParams.certificateId, function(result) {
                    $scope.widgetState.doneLoading = true;
                    if (result !== null && result !== '') {
                        $scope.cert = result.contents;
                        $rootScope.$emit('ts-diabetes.ViewCertCtrl.load', result.metaData);
                        $scope.certProperties.isSent = ManageCertView.isSentToTarget(result.metaData.statuses, 'TS');
                        $scope.certProperties.isRevoked = ManageCertView.isRevoked(result.metaData.statuses);
                        if($scope.certProperties.isRevoked) {
                            $scope.widgetState.printStatus = 'revoked';
                        } else {
                            $scope.widgetState.printStatus = 'signed';
                        }

                        $scope.pdfUrl = '/moduleapi/intyg/signed/' + $scope.cert.id + '/pdf';

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
            }
            loadCertificate();

            /**
             * Exposed scope interaction functions
             */

            ManageCertificate.initSend($scope);
            $scope.send = function(cert) {
                ManageCertificate.send($scope, cert, 'TS', 'ts-diabetes.label.send', function() {
                        loadCertificate();
                    });
            };

            ManageCertificate.initMakulera($scope);
            $scope.makulera = function(cert) {
                var confirmationMessage = messageService.getProperty('ts-diabetes.label.makulera.confirmation', {
                    namn: cert.patient.fullstandigtNamn, personnummer: cert.patient.personid });

                ManageCertificate.makulera($scope, cert, confirmationMessage, function() {
                    loadCertificate();
                });
            };

            $scope.copy = function(cert) {
                cert.intygType = 'ts-diabetes';
                copyDialog = ManageCertificate.copy($scope, cert, COPY_DIALOG_COOKIE);
            };

            $scope.print = function(cert) {

                if ($scope.certProperties.isRevoked) {
                    ManageCertView.printDraft(cert.id);
                } else {
                    document.pdfForm.submit();
                }
            }
        }]);
