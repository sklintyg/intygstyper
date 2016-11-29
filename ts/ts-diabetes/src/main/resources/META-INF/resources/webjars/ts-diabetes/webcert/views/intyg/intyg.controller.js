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

angular.module('ts-diabetes').controller('ts-diabetes.IntygController',
    [ '$location', '$log', '$rootScope', '$stateParams', '$scope',
        'common.IntygProxy', 'common.messageService','common.UserModel','common.IntygCopyRequestModel',
        'ts-diabetes.IntygController.ViewStateService', 'common.dynamicLabelService',
        function($location, $log, $rootScope, $stateParams, $scope, IntygProxy,
            messageService, UserModel, IntygCopyRequestModel, ViewState, DynamicLabelService) {
            'use strict';

            /*********************************************************************
             * Page state
             *********************************************************************/

            ViewState.reset();
            $scope.viewState = ViewState;

            $scope.user = UserModel.user;

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

            function loadIntyg() {
                $log.debug('Loading intyg ' + $stateParams.certificateId);
                IntygProxy.getIntyg($stateParams.certificateId, ViewState.common.intygProperties.type, false, function(result) {
                    ViewState.common.doneLoading = true;
                    if (result !== null && result !== '') {
                        ViewState.intygModel = result.contents;
                        ViewState.relations = result.relations;

                        DynamicLabelService.updateDynamicLabels(ViewState.common.intygProperties.type, ViewState.intygModel.textVersion);

                        ViewState.intygAvser = createKorkortstypListString(ViewState.intygModel.intygAvser.korkortstyp);
                        ViewState.bedomning = createKorkortstypListString(ViewState.intygModel.bedomning.korkortstyp);

                        if(ViewState.intygModel !== undefined && ViewState.intygModel.grundData !== undefined){
                            ViewState.enhetsId = ViewState.intygModel.grundData.skapadAv.vardenhet.enhetsid;
                        }

                        ViewState.common.updateIntygProperties(result);

                        $scope.pdfUrl = '/moduleapi/intyg/ts-diabetes/' + ViewState.intygModel.id + '/pdf';

                        $rootScope.$emit('ts-diabetes.ViewCertCtrl.load', result);

                    } else {
                        ViewState.common.activeErrorMessageKey = 'common.error.data_not_found';
                    }
                }, function(error) {
                    ViewState.common.doneLoading = true;
                    ViewState.common.updateActiveError(error, $stateParams.signed);
                });
            }

            /*********************************************************************
             * Exposed scope interaction functions
             *********************************************************************/

            /*********************************************************************
             * Page load
             *********************************************************************/
            loadIntyg();

            $scope.$on('loadCertificate', loadIntyg);
        }]);
