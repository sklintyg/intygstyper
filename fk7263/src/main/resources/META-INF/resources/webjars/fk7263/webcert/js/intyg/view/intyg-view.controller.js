angular.module('fk7263').controller('fk7263.ViewCertCtrl',
    [ '$log', '$rootScope', '$stateParams', '$scope', '$cookieStore', 'common.CertificateService',
        'common.ManageCertView', 'common.messageService', 'webcert.ManageCertificate', 'common.UserModel',
        function($log, $rootScope, $stateParams, $scope, $cookieStore, CertificateService, ManageCertView,
            messageService, ManageCertificate, UserModel) {
            'use strict';

            var intygType = 'fk7263';

            // Check if the user used the special qa-link to get here.
            if ($stateParams.qaOnly) {
                $scope.isQaOnly = true;
            }

            // Page setup
            $scope.user = { lakare: UserModel.userContext.lakare };

            $scope.cert = {};
            $scope.cert.filledAlways = true;
            $scope.widgetState = {
                doneLoading: false,
                activeErrorMessageKey: null,
                showTemplate: true,
                printStatus: 'notloaded',
                newPatientId: false
            };

            $scope.certProperties = {
                isSent: false,
                isRevoked: false
            };

            /**
             * Private
             */
            function loadCertificate() {
                $log.debug('Loading certificate ' + $stateParams.certificateId);
                CertificateService.getCertificate($stateParams.certificateId, intygType, function(result) {
                    $scope.widgetState.doneLoading = true;
                    if (result !== null && result !== '') {
                        $scope.cert = result.contents;
                        
                        $scope.certProperties.isSent = ManageCertView.isSentToTarget(result.statuses, 'FK');
                        $scope.certProperties.isRevoked = ManageCertView.isRevoked(result.statuses);
                        if ($scope.certProperties.isRevoked) {
                            $scope.widgetState.printStatus = 'revoked';
                        } else {
                            $scope.widgetState.printStatus = 'signed';
                        }

                        $scope.pdfUrl = '/moduleapi/intyg/'+ intygType +'/' + $scope.cert.id + '/pdf';

                        $rootScope.$emit('fk7263.ViewCertCtrl.load', $scope.cert, $scope.certProperties);
                        $rootScope.$broadcast('intyg.loaded', $scope.cert);

                    } else {
                        if ($stateParams.signed) {
                            $scope.widgetState.activeErrorMessageKey = 'common.error.signed_but_not_ready';
                        } else {
                            $scope.widgetState.activeErrorMessageKey = 'fk7263.error.could_not_load_cert';
                        }
                    }
                    $scope.intygBackup.showBackupInfo = false;
                }, function(error) {
                    $scope.widgetState.doneLoading = true;
                    if (error.errorCode === 'DATA_NOT_FOUND') {
                        $scope.widgetState.activeErrorMessageKey = 'fk7263.error.data_not_found';
                    } else {
                        if ($stateParams.signed) {
                            $scope.widgetState.activeErrorMessageKey = 'common.error.signed_but_not_ready';
                        } else {
                            $scope.widgetState.activeErrorMessageKey = 'fk7263.error.could_not_load_cert';
                        }
                    }
                    $log.debug('Got error while loading cert');
                    $log.debug(error.message);
                    $scope.intygBackup.showBackupInfo = true;
                });
            }

            loadCertificate();

            /**
             * Event response from QACtrl which sends its intyg-info here in case intyg couldn't be loaded but QA info could.
             * @type {{}}
             */
            $scope.intygBackup = {intyg: null, showBackupInfo: false};
            var unbindFastEventFail = $rootScope.$on('fk7263.ViewCertCtrl.load.failed', function(event, intyg) {
                $scope.intygBackup.intyg = intyg;
            });
            $scope.$on('$destroy', unbindFastEventFail);

            $scope.$on('loadCertificate', loadCertificate);
        }]);