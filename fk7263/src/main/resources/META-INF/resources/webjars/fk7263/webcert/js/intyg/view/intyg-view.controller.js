angular.module('fk7263').controller('fk7263.ViewCertCtrl',
    [ '$log', '$rootScope', '$stateParams', '$scope', '$cookieStore', 'common.CertificateService',
        'common.ManageCertView', 'common.messageService', 'webcert.ManageCertificate', 'common.UserModel',
        'fk7263.IntygController.ViewStateService',
        function($log, $rootScope, $stateParams, $scope, $cookieStore, CertificateService, ManageCertView,
            messageService, ManageCertificate, UserModel, ViewState) {
            'use strict';

            ViewState.reset();
            $scope.viewState = ViewState;

            // Check if the user used the special qa-link to get here.
            if ($stateParams.qaOnly) {
                $scope.isQaOnly = true;
            }

            // Page setup
            $scope.user = { lakare: UserModel.userContext.lakare };

            $scope.cert = {};
            $scope.cert.filledAlways = true;

            /**
             * Private
             */
            function loadCertificate() {
                $log.debug('Loading certificate ' + $stateParams.certificateId);
                CertificateService.getCertificate($stateParams.certificateId, ViewState.common.intyg.typ, function(result) {
                    ViewState.common.doneLoading = true;
                    if (result !== null && result !== '') {
                        $scope.cert = result.contents;

                        ViewState.common.intyg.isSent = ManageCertView.isSentToTarget(result.statuses, 'FK');
                        ViewState.common.intyg.isRevoked = ManageCertView.isRevoked(result.statuses);
                        if (ViewState.common.intyg.isRevoked) {
                            ViewState.common.intyg.printStatus = 'revoked';
                        } else {
                            ViewState.common.intyg.printStatus = 'signed';
                        }

                        $scope.pdfUrl = '/moduleapi/intyg/'+ ViewState.common.intyg.typ +'/' + $scope.cert.id + '/pdf';

                        $rootScope.$emit('fk7263.ViewCertCtrl.load', $scope.cert, ViewState.common.intyg);
                        $rootScope.$broadcast('intyg.loaded', $scope.cert);

                    } else {
                        if ($stateParams.signed) {
                            ViewState.common.activeErrorMessageKey = 'common.error.signed_but_not_ready';
                        } else {
                            ViewState.common.activeErrorMessageKey = 'fk7263.error.could_not_load_cert';
                        }
                    }
                    $scope.intygBackup.showBackupInfo = false;
                }, function(error) {
                    ViewState.common.doneLoading = true;
                    if (error.errorCode === 'DATA_NOT_FOUND') {
                        ViewState.common.activeErrorMessageKey = 'fk7263.error.data_not_found';
                    } else {
                        if ($stateParams.signed) {
                            ViewState.common.activeErrorMessageKey = 'common.error.signed_but_not_ready';
                        } else {
                            ViewState.common.activeErrorMessageKey = 'fk7263.error.could_not_load_cert';
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
