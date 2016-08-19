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

angular.module('fk7263').controller('fk7263.CustomizeCertSummaryCtrl',
    [ '$location', '$log', '$rootScope', '$stateParams', '$scope', 'common.IntygService', 'common.messageService', 'fk7263.ViewStateService',
        function($location, $log, $rootScope, $stateParams, $scope, IntygService, messageService, ViewState) {
            'use strict';

            // Setup default checkbox model in case of refresh

            if (ViewState.checkboxModel == undefined) {
                ViewState.checkboxModel = {
                    fields : {
                        'field1'    : { 'id': '', 'mandatory':false, 'vald':true },
                        'field2'    : { 'id': '', 'mandatory':false, 'vald':true },
                        'field3'    : { 'id': '', 'mandatory':false, 'vald':true },
                        'field4'    : { 'id': '', 'mandatory':false, 'vald':true },
                        'field4b'   : { 'id': '', 'mandatory':false, 'vald':true },
                        'field5'    : { 'id': '', 'mandatory':false, 'vald':true },
                        'field6a_1' : { 'id': '', 'mandatory':false, 'vald':true },
                        'field6a_2' : { 'id': '', 'mandatory':true,  'vald':true },
                        'field6b'   : { 'id': '', 'mandatory':false, 'vald':true },
                        'field7'    : { 'id': '', 'mandatory':false, 'vald':true },
                        'field8a'   : { 'id': '', 'mandatory':true,  'vald':true },
                        'field8b'   : { 'id': '', 'mandatory':true,  'vald':true },
                        'field9'    : { 'id': '', 'mandatory':false, 'vald':true },
                        'field10'   : { 'id': '', 'mandatory':false, 'vald':true },
                        'field11'   : { 'id': '', 'mandatory':true,  'vald':true },
                        'field12'   : { 'id': '', 'mandatory':false, 'vald':true },
                        'field13'   : { 'id': '', 'mandatory':false, 'vald':true }
                    }
                };

                ViewState.checkboxModel.allItemsSelected = true;
            }

            $scope.viewState = ViewState;
            $scope.messageService = messageService;
            $scope.visibleStatuses = [ 'SENT' ];
            $scope.doneLoading = false;
            $scope.acceptProgressDone = true;
            $scope.downloadSuccess = false;


            $scope.field5Tooltip = messageService.getProperty("fk7263.customize.message.limitation");

            $scope.downloadAsPdfLink = '/moduleapi/certificate/' + 'fk7263' + '/' + $stateParams.certificateId + '/pdf';

            $scope.submitPdf = function() {
                $scope.acceptProgressDone = false;

                document.getElementById("view-as-pdf").submit(); // Form submission
                $scope.downloadSuccess = true;
            };

            $scope.backToCustomizeCertificate = function() {
                $location.path('/fk7263/customize/' + $stateParams.certificateId);
            };

            $scope.backToInbox = function() {
                $location.path('/web/start/#/');
            };


            // Load certificate

            $scope.filterStatuses = function(statuses) {
                var result = [];
                if (!angular.isObject(statuses)) {
                    return result;
                }
                for (var i = 0; i < statuses.length; i++) {
                    if ($scope.userVisibleStatusFilter(statuses[i])) {
                        result.push(statuses[i]);
                    }
                }
                return result;
            };

            $scope.userVisibleStatusFilter = function(status) {
                for (var i = 0; i < $scope.visibleStatuses.length; i++) {
                    if (status.type === $scope.visibleStatuses[i]) {
                        return true;
                    }
                }
                return false;
            };

            if (ViewState.cert == undefined) {
                IntygService.getCertificate(ViewState.common.intygProperties.type, $stateParams.certificateId,
                    function(result) {
                        $scope.doneLoading = true;
                        if (result !== null) {
                            ViewState.cert = result.utlatande;
                            ViewState.cert.filteredStatuses = $scope.filterStatuses(result.meta.statuses);
                            $rootScope.cert = ViewState.cert;
                        } else {
                            // show error view
                            $location.path('/visafel/certnotfound');
                        }
                    }, function() {
                        $log.debug('got error');
                    });
            } else {
                $scope.doneLoading = true;
            }

            $scope.pagefocus = true;
        }]);
