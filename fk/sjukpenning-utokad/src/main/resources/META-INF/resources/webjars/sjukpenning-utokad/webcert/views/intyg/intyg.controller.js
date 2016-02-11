angular.module('lisu').controller('sjukpenning-utokad.ViewCertCtrl',
    [ '$log', '$rootScope', '$stateParams', '$scope', 'common.IntygService','common.IntygProxy',
        'common.messageService', 'common.UserModel', 'sjukpenning-utokad.IntygController.ViewStateService',
        'sjukpenning-utokad.FormFactory', 'common.dynamicLabelService',
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

            $scope.intygFields = formFactory.formFields;

            /**
             * Private
             */
            function loadIntyg() {
                $log.debug('Loading certificate ' + $stateParams.certificateId);
                IntygProxy.getIntyg($stateParams.certificateId, ViewState.common.intyg.type, function(result) {
                    ViewState.common.doneLoading = true;
                    if (result !== null && result !== '') {
                        ViewState.intygModel = result.contents;

                        var version = '0.9';
                        DynamicLabelService.updateDynamicLabels(ViewState.common.intyg.type, ViewState.intygModel);

                        ViewState.common.intyg.isSent = IntygService.isSentToTarget(result.statuses, 'FK');
                        ViewState.common.intyg.isRevoked = IntygService.isRevoked(result.statuses);
                        if (ViewState.common.intyg.isRevoked) {
                            ViewState.common.intyg.printStatus = 'revoked';
                        } else {
                            ViewState.common.intyg.printStatus = 'signed';
                        }

                        $scope.pdfUrl = '/moduleapi/intyg/'+ ViewState.common.intyg.type +'/' + ViewState.intygModel.id + '/pdf';

                        $rootScope.$emit('sjukpenning-utokad.ViewCertCtrl.load', ViewState.intygModel, ViewState.common.intyg);
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
            var unbindFastEventFail = $rootScope.$on('sjukpenning-utokad.ViewCertCtrl.load.failed', function(event, intyg) {
                $scope.intygBackup.intyg = intyg;
            });
            $scope.$on('$destroy', unbindFastEventFail);

            $scope.$on('loadCertificate', loadIntyg);

            formFactory.buildTillaggsFragor($scope.formFields, ViewState.intygModel, 10);
            $scope.$on('dynamicLabels.updated', function () {
                formFactory.buildTillaggsFragor($scope.formFields, ViewState.intygModel, 10);
            });

        }]);
