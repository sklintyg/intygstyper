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
    [ '$location', '$log', '$stateParams', '$scope', 'common.IntygService', 'common.moduleService', 'fk7263.ViewStateService',
        function($location, $log, $stateParams, $scope, IntygService, moduleService, ViewState) {
            'use strict';

            // Setup checkbox model

            if (ViewState.checkboxModel === undefined) {
                ViewState.checkboxModel = {
                    fields: {
                        'field1': {'id': 'smittskydd', 'mandatory': false, 'vald': true},
                        'field2': {'id': 'diagnos', 'mandatory': false, 'vald': true},
                        'field3': {'id': 'aktuelltSjukdomsforlopp', 'mandatory': false, 'vald': true},
                        'field4': {'id': 'funktionsnedsattning', 'mandatory': false, 'vald': true},
                        'field4b': {'id': 'intygetBaserasPa', 'mandatory': false, 'vald': true},
                        'field5': {'id': 'aktivitetsbegransning', 'mandatory': false, 'vald': true},
                        'field6a1': {'id': 'rekommendationerUtomForetagsHalsoVard', 'mandatory': false, 'vald': true},
                        'field6a2': {'id': 'rekommendationerForetagsHalsoVard', 'mandatory': true, 'vald': true},
                        'field6b': {'id': 'planeradBehandling', 'mandatory': false, 'vald': true},
                        'field7': {'id': 'rehabilitering', 'mandatory': false, 'vald': true},
                        'field8a1': {'id': 'arbetsFormagaRelativtUtomNuvarandeArbete', 'mandatory': false, 'vald': true},
                        'field8a2': {'id': 'arbetsFormagaRelativtNuvarandeArbete', 'mandatory': true, 'vald': true},
                        'field8b': {'id': 'bedomdArbetsFormaga', 'mandatory': true, 'vald': true},
                        'field9': {'id': 'arbetsFormaga', 'mandatory': false, 'vald': true},
                        'field10': {'id': 'prognos', 'mandatory': false, 'vald': true},
                        'field11': {'id': 'ressatt', 'mandatory': true, 'vald': true},
                        'field12': {'id': 'fkKontakt', 'mandatory': false, 'vald': true},
                        'field13': {'id': 'ovrigt', 'mandatory': false, 'vald': true}
                    }
                };
            }

            $scope.doneLoading = false;
            $scope.moduleService = moduleService;
            $scope.viewState = ViewState;
            $scope.visibleStatuses = [ 'SENT' ];


            // Load certificate

            function _userVisibleStatusFilter(status) {
                for (var i = 0; i < $scope.visibleStatuses.length; i++) {
                    if (status.type === $scope.visibleStatuses[i]) {
                        return true;
                    }
                }
                return false;
            }

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
            }

            if (ViewState.cert === undefined) {
                IntygService.getCertificate(ViewState.common.intygProperties.type, $stateParams.certificateId,
                    function(result) {
                        if (result !== null) {
                            ViewState.cert = result.utlatande;
                            ViewState.cert.filteredStatuses = _filterStatuses(result.meta.statuses);
                            $scope.doneLoading = true;
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


            // Show/hide fields

            function _isInvalid(value) {
                return !value;
            }

            $scope.showField1 = function _showField1()     {
                return !_isInvalid(ViewState.cert.avstangningSmittskydd);
            };
            $scope.showField2 = function _showField2()     {
                return !(_isInvalid(ViewState.cert.diagnosKod) && _isInvalid(ViewState.cert.diagnosBeskrivning));
            };
            $scope.showField3 = function _showField3()     {
                return !_isInvalid(ViewState.cert.sjukdomsforlopp);
            };
            $scope.showField4 = function _showField4()     {
                return !_isInvalid(ViewState.cert.funktionsnedsattning);
            };
            $scope.showField4b = function _showField4b()   {
                return !(_isInvalid(ViewState.cert.undersokningAvPatienten) && _isInvalid(ViewState.cert.telefonkontaktMedPatienten) &&
                         _isInvalid(ViewState.cert.journaluppgifter) && _isInvalid(ViewState.cert.annanReferens));
            };
            $scope.showField5 = function _showField5()     {
                return !_isInvalid(ViewState.cert.aktivitetsbegransning);
            };
            $scope.showField6a1 = function _showField6a1() {
                return !(_isInvalid(ViewState.cert.rekommendationKontaktArbetsformedlingen) && _isInvalid(ViewState.cert.rekommendationOvrigt));
            };
            $scope.showField6a2 = function _showField6a2() {
                return !_isInvalid(ViewState.cert.rekommendationKontaktForetagshalsovarden);
            };
            $scope.showField6b = function _showField6b()   {
                return !(_isInvalid(ViewState.cert.atgardInomSjukvarden) && _isInvalid(ViewState.cert.annanAtgard));
            };
            $scope.showField7 = function _showField7()     {
                return !_isInvalid(ViewState.cert.rehabilitering);
            };
            $scope.showField8a1 = function _showField8a1() {
                return !(_isInvalid(ViewState.cert.arbetsloshet) && _isInvalid(ViewState.cert.foraldrarledighet));
            };
            $scope.showField8a2 = function _showField8a2() {
                return !_isInvalid(ViewState.cert.nuvarandeArbetsuppgifter);
            };
            $scope.showField8b = function _showField8b()   {
                return !(_isInvalid(ViewState.cert.nedsattMed25) && _isInvalid(ViewState.cert.nedsattMed50) &&
                         _isInvalid(ViewState.cert.nedsattMed75) && _isInvalid(ViewState.cert.nedsattMed100));
            };
            $scope.showField9 = function _showField9()     {
                return !_isInvalid(ViewState.cert.arbetsformagaPrognos);
            };
            $scope.showField10 = function _showField10()   {
                return !_isInvalid(ViewState.cert.prognosBedomning);
            };
            $scope.showField11 = function _showField11()   {
                return !(_isInvalid(ViewState.cert.ressattTillArbeteAktuellt) && _isInvalid(ViewState.cert.ressattTillArbeteEjAktuellt));
            };
            $scope.showField12 = function _showField12()   {
                return !_isInvalid(ViewState.cert.kontaktMedFk);
            };
            $scope.showField13 = function _showField13()   {
                return !_isInvalid(ViewState.cert.kommentar);
            };


            // Navigation

            $scope.backToViewCertificate = function() {
                ViewState.checkboxModel = undefined;
                $location.path('/fk7263/view/' + $stateParams.certificateId);
            };

            $scope.confirmCertificateCustomization = function() {
                $location.path('/fk7263/customize/' + $stateParams.certificateId + '/summary');
            };

            $scope.pagefocus = true;
        }]);
