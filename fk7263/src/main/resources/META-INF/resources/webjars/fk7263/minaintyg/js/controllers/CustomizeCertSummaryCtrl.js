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
    ['$window', '$location', '$log', '$rootScope', '$stateParams', '$scope', 'common.IntygService', 'common.messageService', 'fk7263.ViewStateService',
        function($window, $location, $log, $rootScope, $stateParams, $scope, IntygService, messageService, ViewState) {
            'use strict';

            // Setup default checkbox model in case of refresh

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
            }

            $scope.doneLoading = false;
            $scope.downloadSuccess = false;
            $scope.messageService = messageService;
            $scope.viewState = ViewState;
            $scope.visibleStatuses = [ 'SENT' ];

            $scope.field5Tooltip = messageService.getProperty("fk7263.customize.message.limitation");

            $scope.downloadAsPdfLink = '/moduleapi/certificate/' + 'fk7263' + '/' + $stateParams.certificateId + '/pdf/arbetsgivarutskrift';


            // Show/hide fields

            var _fields = ViewState.checkboxModel.fields;

            function _isNull(object) { return object == null; }
            function _isEmpty(object) { return object == null || object.length < 1; }

            function _showField(field, isIncludedField) {
                if (isIncludedField && field.vald) {
                    return true;
                }
                if (!(isIncludedField || field.vald)) {
                    return true;
                }
                return false;
            }

            $scope.showField1 = function _showField1(isIncludedField) {
                if (_isNull(ViewState.cert.avstangningSmittskydd)) {
                    return false;
                }
                return _showField(_fields.field1, isIncludedField);
            };

            $scope.showField2 = function _showField2(isIncludedField) {
                if (_isEmpty(ViewState.cert.diagnosKod) && _isEmpty(ViewState.cert.diagnosBeskrivning)) {
                    return false;
                }
                return _showField(_fields.field2, isIncludedField);
            };

            $scope.showField3 = function _showField3(isIncludedField) {
                if (_isEmpty(ViewState.cert.sjukdomsforlopp)) {
                    return false;
                }
                return _showField(_fields.field3, isIncludedField);
            };

            $scope.showField4 = function _showField4(isIncludedField) {
                if (_isEmpty(ViewState.cert.funktionsnedsattning)) {
                    return false;
                }
                return _showField(_fields.field4, isIncludedField);
            };

            $scope.showField4b = function _showField4b(isIncludedField)  {
                if (_isNull(ViewState.cert.undersokningAvPatienten) || _isNull(ViewState.cert.telefonkontaktMedPatienten) ||
                    _isNull(ViewState.cert.journaluppgifter) || _isNull(ViewState.cert.annanReferens)) {
                    return false;
                }
                return _showField(_fields.field4b, isIncludedField);
            };

            $scope.showField5 = function _showField5(isIncludedField) {
                if (_isEmpty(ViewState.cert.aktivitetsbegransning)) {
                    return false;
                }
                return _showField(_fields.field5, isIncludedField);
            };

            $scope.showField6a1 = function _showField6a1(isIncludedField) {
                if (_isNull(ViewState.cert.rekommendationKontaktArbetsformedlingen) || _isEmpty(ViewState.cert.rekommendationOvrigt)) {
                    return false;
                }
                return _showField(_fields.field6a_1, isIncludedField);
            };

            $scope.showField6a2 = function _showField6a2(isIncludedField) {
                if (_isNull(ViewState.cert.rekommendationKontaktForetagshalsovarden)) {
                    return false;
                }
                return _showField(_fields.field6a_2, isIncludedField);
            };

            $scope.showField6b = function _showField6b(isIncludedField)  {
                if (_isEmpty(ViewState.cert.atgardInomSjukvarden) || _isEmpty(ViewState.cert.annanAtgard)) {
                    return false;
                };
                return _showField(_fields.field6b, isIncludedField);
            };

            $scope.showField7 = function _showField7(isIncludedField)   {
                if (_isNull(ViewState.cert.rehabilitering)) {
                    return false;
                }
                return _showField(_fields.field7, isIncludedField);
            };

            $scope.showField8a1 = function _showField8a1(isIncludedField) {
                if (_isNull(ViewState.cert.arbetsloshet)  || _isNull(ViewState.cert.foraldrarledighets)) {
                    return false;
                }
                return _showField(_fields.field8a_1, isIncludedField);
            };

            $scope.showField8a2 = function _showField8a2(isIncludedField) {
                if (_isEmpty(ViewState.cert.nuvarandeArbetsuppgifter)) {
                    return false;
                }
                return _showField(_fields.field8a_2, isIncludedField);
            };

            $scope.showField8b = function _showField8b(isIncludedField)  {
                if (_isNull(ViewState.cert.nedsattMed25) || _isNull(ViewState.cert.nedsattMed50) ||
                    _isNull(ViewState.cert.nedsattMed75) || _isNull(ViewState.cert.nedsattMed100)) {
                    return false;
                }
                return _showField(_fields.field8b, isIncludedField);
            };

            $scope.showField9 = function _showField9(isIncludedField)   {
                if (_isEmpty(ViewState.cert.arbetsformagaPrognos)) {
                    return false;
                }
                return _showField(_fields.field9, isIncludedField);
            };

            $scope.showField10 = function _showField10(isIncludedField)   {
                if (_isNull(ViewState.cert.prognosBedomning)) {
                    return false;
                }
                return _showField(_fields.field10, isIncludedField);
            };

            $scope.showField11 = function _showField11(isIncludedField)  {
                if (_isNull(ViewState.cert.ressattTillArbeteAktuellt) || _isNull(ViewState.cert.ressattTillArbeteEjAktuellt)) {
                    return false;
                }
                return _showField(_fields.field11, isIncludedField);
            };

            $scope.showField12 = function _showField12(isIncludedField)  {
                if (_isNull(ViewState.cert.kontaktMedFk)) {
                    return false;
                }
                return _showField(_fields.field12, isIncludedField);
            };

            $scope.showField13 = function _showField13(isIncludedField)  {
                if (_isEmpty(ViewState.cert.kommentar)) {
                    return false;
                }
                return _showField(_fields.field13, isIncludedField);
            };


            // Navigation

            $scope.backToCustomizeCertificate = function() {
                $location.path('/fk7263/customize/' + $stateParams.certificateId);
            };

            $scope.backToInbox = function() {
                $location.path('/web/start/#/');
            };


            // Submit user selected fields to PDF generator

            function _addInput(name, item) {
                return '<input type="hidden" name="' + name + '" value="' + item + '" />';
            };

            function _getSelectedFields(fields) {
                var selectedFields = [];
                if (!angular.isObject(fields)) {
                    return selectedFields;
                }
                for (var key in fields) {
                    var field = fields[key];
                    if (!field.mandatory && field.vald) {
                        // Only add non-mandatory fields if they are selected
                        selectedFields.push(field.id);
                    }
                }
                return selectedFields;
            }

            $scope.submit = function() {
                var inputs = '';
                var fields = _getSelectedFields($scope.viewState.checkboxModel.fields);

                angular.forEach(fields, function(item) {
                    inputs += _addInput('selectedOptionalFields', item);
                });

                //send request
                $window.jQuery('<form action="' + $scope.downloadAsPdfLink + '" target="_blank" method="post">' + inputs + '</form>')
                    .appendTo('body').submit().remove();

                $scope.downloadSuccess = true;
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
