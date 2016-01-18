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

angular.module('ts-bas').controller('ts-bas.IntygController',
    [ '$log', '$rootScope', '$stateParams', '$scope',
        'common.IntygService', 'common.IntygProxy', 'common.messageService','common.User',
        'ts-bas.IntygController.ViewStateService',
        function($log, $rootScope, $stateParams, $scope, IntygService, IntygProxy,
            messageService, User, ViewState) {
            'use strict';

            /*********************************************************************
             * Page state
             *********************************************************************/

            ViewState.reset();
            $scope.viewState = ViewState;

            $scope.user = { lakare: User.getUser().lakare };

            // expose calculated static link for pdf download
            $scope.downloadAsPdfLink = '/moduleapi/intyg/ts-bas/' + $stateParams.certificateId + '/pdf';

            // Decide if helptext related to field 1.a) - 1.c)
            $scope.achelptext = false;

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
                IntygProxy.getIntyg($stateParams.certificateId, ViewState.common.intyg.type, function(result) {
                    ViewState.common.doneLoading = true;
                    if (result !== null) {
                        ViewState.intygModel = result.contents;
                        if (ViewState.intygModel.syn.synfaltsdefekter === true || ViewState.intygModel.syn.nattblindhet === true ||
                            ViewState.intygModel.syn.progressivOgonsjukdom === true) {
                            $scope.achelptext = true;
                        }
                        ViewState.intygAvser = createKorkortstypListString(ViewState.intygModel.intygAvser.korkortstyp);
                        ViewState.bedomning = createKorkortstypListString(ViewState.intygModel.bedomning.korkortstyp);

                        $rootScope.$emit('ts-bas.ViewCertCtrl.load', result);
                        ViewState.common.intyg.isSent = IntygService.isSentToTarget(result.statuses, 'TS');
                        ViewState.common.intyg.isRevoked = IntygService.isRevoked(result.statuses);
                        if(ViewState.common.intyg.isRevoked) {
                            ViewState.common.printStatus = 'revoked';
                        } else {
                            ViewState.common.printStatus = 'signed';
                        }

                        $scope.pdfUrl = '/moduleapi/intyg/ts-bas/' + ViewState.intygModel.id + '/pdf';

                    } else {
                        $log.debug('Got error while loading cert - invalid data');
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
            loadCertificate();

            $scope.$on('loadCertificate', loadCertificate);
        }]);
