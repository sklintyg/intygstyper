angular.module('fk7263').controller('fk7263.ViewCertCtrl',
    [ '$log', '$rootScope', '$stateParams', '$scope', '$cookieStore', 'common.IntygService',
        'common.messageService', 'common.UserModel', 'fk7263.IntygController.ViewStateService',
        function($log, $rootScope, $stateParams, $scope, $cookieStore, IntygService,
            messageService, UserModel, ViewState) {
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
            function loadIntyg() {
                $log.debug('Loading certificate ' + $stateParams.certificateId);
                IntygService.getIntyg($stateParams.certificateId, ViewState.common.intyg.type, function(result) {
                    ViewState.common.doneLoading = true;
                    if (result !== null && result !== '') {
                        $scope.cert = result.contents;

                        ViewState.common.intyg.isSent = IntygService.isSentToTarget(result.statuses, 'FK');
                        ViewState.common.intyg.isRevoked = IntygService.isRevoked(result.statuses);
                        if (ViewState.common.intyg.isRevoked) {
                            ViewState.common.intyg.printStatus = 'revoked';
                        } else {
                            ViewState.common.intyg.printStatus = 'signed';
                        }

                        $scope.pdfUrl = '/moduleapi/intyg/'+ ViewState.common.intyg.type +'/' + $scope.cert.id + '/pdf';

                        $rootScope.$emit('fk7263.ViewCertCtrl.load', $scope.cert, ViewState.common.intyg);
                        $rootScope.$broadcast('intyg.loaded', $scope.cert);

                    } else {
                        if ($stateParams.signed) {
                            ViewState.common.activeErrorMessageKey = 'common.error.signed_but_not_ready';
                        } else {
                            ViewState.common.activeErrorMessageKey = 'common.error.could_not_load_cert';
                        }
                    }
                    $scope.intygBackup.showBackupInfo = false;
                }, function(error) {
                    ViewState.common.doneLoading = true;
                    ViewState.common.updateActiveError(error, $stateParams.signed);
                    $scope.intygBackup.showBackupInfo = true;
                });
            }

            loadIntyg();

            /**
             * Event response from QACtrl which sends its intyg-info here in case intyg couldn't be loaded but QA info could.
             * @type {{}}
             */
            $scope.intygBackup = {intyg: null, showBackupInfo: false};
            var unbindFastEventFail = $rootScope.$on('fk7263.ViewCertCtrl.load.failed', function(event, intyg) {
                $scope.intygBackup.intyg = intyg;
            });
            $scope.$on('$destroy', unbindFastEventFail);

            $scope.$on('loadCertificate', loadIntyg);
        }]);
