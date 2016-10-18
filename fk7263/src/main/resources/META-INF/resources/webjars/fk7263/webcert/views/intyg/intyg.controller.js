/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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
    [ '$log', '$rootScope', '$stateParams', '$scope', '$state',
        'common.IntygProxy', 'common.messageService', 'common.UserModel', 'common.ObjectHelper', 'fk7263.IntygController.ViewStateService',
        function($log, $rootScope, $stateParams, $scope, $state,
            IntygProxy, messageService, UserModel, ObjectHelper, ViewState) {
            'use strict';

            ViewState.reset();
            ViewState.intygModel = {};
            ViewState.intygModel.filledAlways = true;

            $scope.viewState = ViewState;
            $scope.user = UserModel;

            // Check if the user used the special qa-link to get here.
            $scope.isQaOnly = UserModel.isUthopp();


            $scope.gotoRelatedIntyg = function(intyg) {
                if (intyg.status === 'SIGNED') {
                    $state.go('webcert.intyg.fk.fk7263', {certificateId: intyg.intygsId});
                }
                else {
                    $state.go('fk7263-edit', {certificateId: intyg.intygsId});
                }
            };

            /**
             * Private
             */

            function loadIntyg() {
                $log.debug('Loading certificate ' + $stateParams.certificateId);

                var sjf = ObjectHelper.isDefined($stateParams.sjf) ? $stateParams.sjf : false;

                IntygProxy.getIntyg($stateParams.certificateId, ViewState.common.intygProperties.type, sjf, function(result) {
                    ViewState.common.doneLoading = true;
                    if (result !== null && result !== '') {
                        ViewState.intygModel = result.contents;
                        ViewState.relations = result.relations;

                        if(ViewState.intygModel !== undefined && ViewState.intygModel.grundData !== undefined){
                            ViewState.enhetsId = ViewState.intygModel.grundData.skapadAv.vardenhet.enhetsid;
                        }

                        ViewState.common.updateIntygProperties(result);

                        $scope.pdfUrl = '/moduleapi/intyg/'+ ViewState.common.intygProperties.type +'/' + ViewState.intygModel.id + '/pdf';

                        $rootScope.$emit('ViewCertCtrl.load', ViewState.intygModel, ViewState.common.intygProperties);
                        $rootScope.$broadcast('intyg.loaded', ViewState.intygModel);

                    } else {
                        $rootScope.$emit('ViewCertCtrl.load', null, null);

                        if ($stateParams.signed) {
                            ViewState.common.activeErrorMessageKey = 'common.error.sign.not_ready_yet';
                        } else {
                            ViewState.common.activeErrorMessageKey = 'common.error.could_not_load_cert';
                        }
                    }
                    $scope.intygBackup.showBackupInfo = false;
                }, function(error) {
                    $rootScope.$emit('ViewCertCtrl.load', null, null);
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
