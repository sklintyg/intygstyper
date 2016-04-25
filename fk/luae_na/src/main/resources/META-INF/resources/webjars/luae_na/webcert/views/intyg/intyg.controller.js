angular.module('luae_na').controller('luae_na.ViewCertCtrl',
    [ '$log', '$rootScope', '$stateParams', '$scope', 'common.IntygService','common.IntygProxy',
        'common.messageService', 'common.UserModel', 'luae_na.IntygController.ViewStateService',
        'luae_na.FormFactory', 'common.dynamicLabelService',
        function($log, $rootScope, $stateParams, $scope, IntygService, IntygProxy,
            messageService, UserModel, ViewState, formFactory, DynamicLabelService) {
            'use strict';

            ViewState.reset();
            $scope.viewState = ViewState;

            // Check if the user used the special qa-link to get here.
            if ($stateParams.qaOnly) {
                $scope.isQaOnly = true;
            }

            // Page setup
            $scope.user = UserModel;

            ViewState.intygModel = {};
            ViewState.intygModel.filledAlways = true;

            $scope.intygFields = formFactory.getFormFields();
            // Remove vardenhet group, uses custom layout
            $scope.intygFields.pop();

            /**
             * Private
             */
            function loadIntyg() {
                $log.debug('Loading certificate ' + $stateParams.certificateId);
                IntygProxy.getIntyg($stateParams.certificateId, ViewState.common.intyg.type, function(result) {
                    ViewState.common.doneLoading = true;
                    if (result !== null && result !== '') {
                        ViewState.intygModel = result.contents;

                        DynamicLabelService.updateDynamicLabels(ViewState.common.intyg.type, ViewState.intygModel);

                        ViewState.common.intyg.isSent = IntygService.isSentToTarget(result.statuses, 'FKASSA');
                        ViewState.common.intyg.isRevoked = IntygService.isRevoked(result.statuses);
                        if (ViewState.common.intyg.isRevoked) {
                            ViewState.common.intyg.printStatus = 'revoked';
                        } else {
                            ViewState.common.intyg.printStatus = 'signed';
                        }

                        $scope.pdfUrl = '/moduleapi/intyg/'+ ViewState.common.intyg.type +'/' + ViewState.intygModel.id + '/pdf';

                        $rootScope.$emit('ViewCertCtrl.load', ViewState.intygModel, ViewState.common.intyg);
                        $rootScope.$broadcast('intyg.loaded', ViewState.intygModel);

                    } else {
                        if ($stateParams.signed) {
                            ViewState.common.activeErrorMessageKey = 'common.error.sign.not_ready_yet';
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
            var unbindFastEventFail = $rootScope.$on('luae_na.ViewCertCtrl.load.failed', function(event, intyg) {
                $scope.intygBackup.intyg = intyg;
            });
            $scope.$on('$destroy', unbindFastEventFail);

            $scope.$on('loadCertificate', loadIntyg);
        }]);
