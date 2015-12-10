/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

angular.module('fk7263').controller('fk7263.ViewCertCtrl',
    [ '$log', '$rootScope', '$stateParams', '$scope', 'common.IntygService','common.IntygProxy',
        'common.messageService', 'common.UserModel', 'fk7263.IntygController.ViewStateService',
        function($log, $rootScope, $stateParams, $scope, IntygService, IntygProxy,
            messageService, UserModel, ViewState) {
            'use strict';

            ViewState.reset();
            $scope.viewState = ViewState;

            // Check if the user used the special qa-link to get here.
            $scope.isQaOnly = UserModel.isUthopp();

            // Page setup
            $scope.user = UserModel;

            ViewState.intygModel.filledAlways = true;

            /**
             * Private
             */
            function loadIntyg() {
                $log.debug('Loading certificate ' + $stateParams.certificateId);
                IntygProxy.getIntyg($stateParams.certificateId, ViewState.common.intyg.type, function(result) {
                    ViewState.common.doneLoading = true;
                    if (result !== null && result !== '') {
                        ViewState.intygModel = result.contents;

                        ViewState.common.intyg.isSent = IntygService.isSentToTarget(result.statuses, 'FK');
                        ViewState.common.intyg.isRevoked = IntygService.isRevoked(result.statuses);
                        if (ViewState.common.intyg.isRevoked) {
                            ViewState.common.intyg.printStatus = 'revoked';
                        } else {
                            ViewState.common.intyg.printStatus = 'signed';
                        }
                        if(ViewState.intygModel !== undefined && ViewState.intygModel.grundData !== undefined){
                            ViewState.enhetsId = ViewState.intygModel.grundData.skapadAv.vardenhet.enhetsid;
                        }

                        $scope.pdfUrl = '/moduleapi/intyg/'+ ViewState.common.intyg.type +'/' + ViewState.intygModel.id + '/pdf';

                        $rootScope.$emit('fk7263.ViewCertCtrl.load', ViewState.intygModel, ViewState.common.intyg);
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
            var unbindFastEventFail = $rootScope.$on('fk7263.ViewCertCtrl.load.failed', function(event, intyg) {
                $scope.intygBackup.intyg = intyg;
            });
            $scope.$on('$destroy', unbindFastEventFail);

            $scope.$on('loadCertificate', loadIntyg);
        }]);
