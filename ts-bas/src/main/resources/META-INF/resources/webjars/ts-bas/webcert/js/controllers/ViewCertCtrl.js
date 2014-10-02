angular.module('ts-bas').controller('ts-bas.ViewCertCtrl',
    [ '$log', '$rootScope', '$routeParams', '$scope', '$cookieStore', 'common.CertificateService',
        'common.ManageCertView', 'common.messageService', 'webcert.ManageCertificate',
        function($log, $rootScope, $routeParams, $scope, $cookieStore, CertificateService, ManageCertView,
            messageService, ManageCertificate) {
            'use strict';

            /*********************************************************************
             * Page state
             *********************************************************************/

            $scope.cert = {};
            $scope.widgetState = {
                doneLoading: false,
                activeErrorMessageKey: null,
                showTemplate: true,
                printStatus: 'notloaded'
            };

            $scope.view = {
                intygAvser: '',
                bedomning: ''
            };

            // expose calculated static link for pdf download
            $scope.downloadAsPdfLink = '/moduleapi/certificate/' + $routeParams.certificateId + '/pdf';

            // Decide if helptext related to field 1.a) - 1.c)
            $scope.achelptext = false;

            $scope.certProperties = {
                isSent: false,
                isRevoked: false
            };

            /*********************************************************************
             * Private support functions
             *********************************************************************/

            function createKorkortstypListString(list) {

                var tempList = [];
                angular.forEach(list, function(key) {
                    if (key.selected) {
                        this.push(key);
                    }
                }, tempList);

                var resultString = '';
                for (var i = 0; i < tempList.length; i++) {
                    if (i < tempList.length - 1) {
                        resultString += tempList[i].type + (', ');
                    } else {
                        resultString += tempList[i].type;
                    }
                }

                return resultString;
            }

            function loadCertificate() {
                CertificateService.getCertificate($routeParams.certificateId, function(result) {
                    $scope.widgetState.doneLoading = true;
                    if (result !== null) {
                        $scope.cert = result.contents;
                        if ($scope.cert.syn.synfaltsdefekter === true || $scope.cert.syn.nattblindhet === true ||
                            $scope.cert.syn.progressivOgonsjukdom === true) {
                            $scope.achelptext = true;
                        }
                        $scope.view.intygAvser = createKorkortstypListString($scope.cert.intygAvser.korkortstyp);
                        $scope.view.bedomning = createKorkortstypListString($scope.cert.bedomning.korkortstyp);

                        $rootScope.$emit('ts-bas.ViewCertCtrl.load', result.metaData);
                        $scope.certProperties.isSent = ManageCertView.isSentToTarget(result.metaData.statuses, 'TS');
                        $scope.certProperties.isRevoked = ManageCertView.isRevoked(result.metaData.statuses);
                        if($scope.certProperties.isRevoked) {
                            $scope.widgetState.printStatus = 'revoked';
                        } else {
                            $scope.widgetState.printStatus = 'signed';
                        }

                        $scope.pdfUrl = '/moduleapi/intyg/signed/' + $scope.cert.id + '/pdf';

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
            }
            loadCertificate();

            /*********************************************************************
             * Exposed scope interaction functions
             *********************************************************************/

            ManageCertificate.initSend($scope);
            $scope.send = function(cert) {
                ManageCertificate.send($scope, cert, 'TS', 'ts-bas.label.send', function() {
                        loadCertificate();
                    });
            };

            ManageCertificate.initMakulera($scope);
            $scope.makulera = function(cert) {
                var confirmationMessage = messageService.getProperty('ts-bas.label.makulera.confirmation', {
                    namn: cert.patient.fullstandigtNamn, personnummer: cert.patient.personid });

                ManageCertificate.makulera($scope, cert, confirmationMessage, function() {
                    loadCertificate();
                });
            };

            ManageCertificate.initCopyDialog($scope);
            $scope.copy = function(cert) {
                cert.intygType = 'ts-bas';
                ManageCertificate.copy($scope, cert);
            };

            $scope.print = function(cert) {

                if ($scope.certProperties.isRevoked) {
                    ManageCertView.printDraft(cert.id);
                } else {
                    document.pdfForm.submit();
                }
            };
        }]);
