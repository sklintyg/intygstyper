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

angular.module('fk7263').controller('fk7263.CustomizeCertCtrl',
    [ '$location', '$log', '$rootScope', '$stateParams', '$scope', 'common.IntygService', 'common.messageService', 'fk7263.ViewStateService',
        function($location, $log, $rootScope, $stateParams, $scope, IntygService, messageService, ViewState) {
            'use strict';

            // Setup checkbox model

            if (ViewState.checkboxModel == undefined) {
                ViewState.checkboxModel = {
                    fields : {
                        'field1'    : { 'id': 'smittskydd',                          'mandatory':false, 'vald':true },
                        'field2'    : { 'id': 'diagnos',                             'mandatory':false, 'vald':true },
                        'field3'    : { 'id': 'aktuelltSjukdomsforlopp',             'mandatory':false, 'vald':true },
                        'field4'    : { 'id': 'funktionsnedsattning',                'mandatory':false, 'vald':true },
                        'field4b'   : { 'id': 'intygetBaserasPa',                    'mandatory':false, 'vald':true },
                        'field5'    : { 'id': 'aktivitetsbegransning',               'mandatory':false, 'vald':true },
                        'field6a_1' : { 'id': 'rekommendationerUtomForetagsHalsoVard', 'mandatory':false, 'vald':true },
                        'field6a_2' : { 'id': 'rekommendationerForetagsHalsoVard',   'mandatory':true,  'vald':true },
                        'field6b'   : { 'id': 'planeradBehandling',                  'mandatory':false, 'vald':true },
                        'field7'    : { 'id': 'rehabilitering',                      'mandatory':false, 'vald':true },
                        'field8a'   : { 'id': 'arbetsFormagaRelatvt',                'mandatory':true,  'vald':true },
                        'field8a_1'   : { 'id': 'arbetsFormagaRelativtUtomNuvarandeArbete', 'mandatory':false,  'vald':true },
                        'field8a_2'   : { 'id': 'arbetsFormagaRelativtNuvarandeArbete',     'mandatory':true,  'vald':true },
                        'field8b'   : { 'id': 'bedomdArbetsFormaga',                 'mandatory':true,  'vald':true },
                        'field9'    : { 'id': 'arbetsFormaga',                       'mandatory':false, 'vald':true },
                        'field10'   : { 'id': 'prognos',                             'mandatory':false, 'vald':true },
                        'field11'   : { 'id': 'ressatt',                             'mandatory':true,  'vald':true },
                        'field12'   : { 'id': 'fkKontakt',                           'mandatory':false, 'vald':true },
                        'field13'   : { 'id': 'ovrigt',                              'mandatory':false, 'vald':true }
                    }
                };

                ViewState.checkboxModel.allItemsSelected = true;
            }

            $scope.viewState = ViewState;
            $scope.doneLoading = false;
            $scope.messageService = messageService;
            $scope.visibleStatuses = [ 'SENT' ];


            // Watch all option fields and update the selectAll checkbox if necessary

            $scope.$watch("viewState.checkboxModel.fields", function() {
                var isAllItemsSelected = true;
                for (var key in ViewState.checkboxModel.fields) {
                    var field = ViewState.checkboxModel.fields[key];
                    if (field.vald == false) {
                        isAllItemsSelected = false;
                        break;
                    }
                }
                ViewState.checkboxModel.allItemsSelected = isAllItemsSelected;
            }, true);

            // Fired when the selectAll checkbox is changed

            $scope.selectAll = function () {
                for (var key in ViewState.checkboxModel.fields) {
                    var field = ViewState.checkboxModel.fields[key];
                    if (field.mandatory == false) {
                        field.vald = ViewState.checkboxModel.allItemsSelected;
                    }
                }
            };


            // Navigation

            $scope.backToViewCertificate = function() {
                ViewState.checkboxModel = undefined;
                $location.path('/fk7263/view/' + $stateParams.certificateId);
            };

            $scope.confirmCertificateCustomization = function() {
                $location.path('/fk7263/customize/' + $stateParams.certificateId + '/summary');
            };


            // Load certificate

            function _userVisibleStatusFilter(status) {
                for (var i = 0; i < $scope.visibleStatuses.length; i++) {
                    if (status.type === $scope.visibleStatuses[i]) {
                        return true;
                    }
                }
                return false;
            };

            function _filterStatuses(statuses) {
                var result = [];
                if (!angular.isObject(statuses)) {
                    return result;
                }
                for (var i = 0; i < statuses.length; i++) {
                    if (_userVisibleStatusFilter(statuses[i])) {
                        result.push(statuses[i]);
                    }
                }
                return result;
            };

            if (ViewState.cert == undefined) {
                IntygService.getCertificate(ViewState.common.intygProperties.type, $stateParams.certificateId,
                    function(result) {
                        $scope.doneLoading = true;
                        if (result !== null) {
                            ViewState.cert = result.utlatande;
                            ViewState.cert.filteredStatuses = _filterStatuses(result.meta.statuses);
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
