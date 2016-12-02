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
    ['$window', '$location', '$log', '$stateParams', '$scope','common.dialogService', 'common.IntygService', 'common.messageService', 'common.moduleService', 'fk7263.ViewStateService',
        function($window, $location, $log, $stateParams, $scope, dialogService, IntygService, messageService, moduleService, ViewState) {
            'use strict';

            // Setup default checkbox model in case of refresh

            if (ViewState.checkboxModel === undefined) {
                ViewState.checkboxModel = {
                    fields : {
                        'field1'    : { 'id': 'smittskydd',                          'mandatory':false, 'vald':true },
                        'field2'    : { 'id': 'diagnos',                             'mandatory':false, 'vald':true },
                        'field3'    : { 'id': 'aktuelltSjukdomsforlopp',             'mandatory':false, 'vald':true },
                        'field4'    : { 'id': 'funktionsnedsattning',                'mandatory':false, 'vald':true },
                        'field4b'   : { 'id': 'intygetBaserasPa',                    'mandatory':false, 'vald':true },
                        'field5'    : { 'id': 'aktivitetsbegransning',               'mandatory':false, 'vald':true },
                        'field6a1'  : { 'id': 'rekommendationerUtomForetagsHalsoVard', 'mandatory':false, 'vald':true },
                        'field6a2'  : { 'id': 'rekommendationerForetagsHalsoVard',   'mandatory':true,  'vald':true },
                        'field6b'   : { 'id': 'planeradBehandling',                  'mandatory':false, 'vald':true },
                        'field7'    : { 'id': 'rehabilitering',                      'mandatory':false, 'vald':true },
                        'field8a1'  : { 'id': 'arbetsFormagaRelativtUtomNuvarandeArbete', 'mandatory':false,  'vald':true },
                        'field8a2'  : { 'id': 'arbetsFormagaRelativtNuvarandeArbete',     'mandatory':true,  'vald':true },
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
            $scope.moduleService = moduleService;
            $scope.viewState = ViewState;
            $scope.visibleStatuses = [ 'SENT' ];

            $scope.field5Tooltip = messageService.getProperty('fk7263.customize.message.limitation');

            $scope.downloadAsPdfLink = '/moduleapi/certificate/' + 'fk7263' + '/' + $stateParams.certificateId + '/pdf/arbetsgivarutskrift';


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
                        $scope.doneLoading = true;
                        if (result !== null) {
                            ViewState.cert = result.utlatande;
                            ViewState.cert.filteredStatuses = _filterStatuses(result.meta.statuses);
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

            var _fields = ViewState.checkboxModel.fields;

            function _XOR(a, b) {
                // exclusive or implementation
                return ( a || b ) && !( a && b );
            }

            function _isInvalid(value) {
                return !value;
            }

            function _showField(field, isIncludedField) {
                // if only one value is true, then hide field
                if (_XOR(isIncludedField, field.vald)) {
                    return false;
                }
                // if values are both true or both false, then show field
                return true;
            }

            $scope.showAllFields = function _showAllFields() {
                var returnValue = true;
                angular.forEach(_fields, function(field, key) {
                    if (!field.vald) {
                        returnValue = false;
                    }
                });
                return returnValue;
            };

            $scope.showField1 = function _showField1(isIncludedField) {
                if (_isInvalid(ViewState.cert.avstangningSmittskydd)) {
                    return false;
                }
                return _showField(_fields.field1, isIncludedField);
            };

            $scope.showField2 = function _showField2(isIncludedField) {
                if (_isInvalid(ViewState.cert.diagnosKod) && _isInvalid(ViewState.cert.diagnosBeskrivning)) {
                    return false;
                }
                return _showField(_fields.field2, isIncludedField);
            };

            $scope.showField3 = function _showField3(isIncludedField) {
                if (_isInvalid(ViewState.cert.sjukdomsforlopp)) {
                    return false;
                }
                return _showField(_fields.field3, isIncludedField);
            };

            $scope.showField4 = function _showField4(isIncludedField) {
                if (_isInvalid(ViewState.cert.funktionsnedsattning)) {
                    return false;
                }
                return _showField(_fields.field4, isIncludedField);
            };

            $scope.showField4b = function _showField4b(isIncludedField)  {
                if (_isInvalid(ViewState.cert.undersokningAvPatienten) && _isInvalid(ViewState.cert.telefonkontaktMedPatienten) &&
                    _isInvalid(ViewState.cert.journaluppgifter) && _isInvalid(ViewState.cert.annanReferens)) {
                    return false;
                }
                return _showField(_fields.field4b, isIncludedField);
            };

            $scope.showField5 = function _showField5(isIncludedField) {
                if (_isInvalid(ViewState.cert.aktivitetsbegransning)) {
                    return false;
                }
                return _showField(_fields.field5, isIncludedField);
            };

            $scope.showField6a1 = function _showField6a1(isIncludedField) {
                if (_isInvalid(ViewState.cert.rekommendationKontaktArbetsformedlingen) && _isInvalid(ViewState.cert.rekommendationOvrigt)) {
                    return false;
                }
                return _showField(_fields.field6a1, isIncludedField);
            };

            $scope.showField6a2 = function _showField6a2(isIncludedField) {
                if (_isInvalid(ViewState.cert.rekommendationKontaktForetagshalsovarden)) {
                    return false;
                }
                return _showField(_fields.field6a2, isIncludedField);
            };

            $scope.showField6b = function _showField6b(isIncludedField)  {
                if (_isInvalid(ViewState.cert.atgardInomSjukvarden) && _isInvalid(ViewState.cert.annanAtgard)) {
                    return false;
                }
                return _showField(_fields.field6b, isIncludedField);
            };

            $scope.showField7 = function _showField7(isIncludedField)   {
                if (_isInvalid(ViewState.cert.rehabilitering)) {
                    return false;
                }
                return _showField(_fields.field7, isIncludedField);
            };

            $scope.showField8a1 = function _showField8a1(isIncludedField) {
                if (_isInvalid(ViewState.cert.arbetsloshet) && _isInvalid(ViewState.cert.foraldrarledighet)) {
                    return false;
                }
                return _showField(_fields.field8a1, isIncludedField);
            };

            $scope.showField8a2 = function _showField8a2(isIncludedField) {
                if (_isInvalid(ViewState.cert.nuvarandeArbetsuppgifter)) {
                    return false;
                }
                return _showField(_fields.field8a2, isIncludedField);
            };

            $scope.showField8b = function _showField8b(isIncludedField)  {
                if (_isInvalid(ViewState.cert.nedsattMed25) && _isInvalid(ViewState.cert.nedsattMed50) &&
                    _isInvalid(ViewState.cert.nedsattMed75) && _isInvalid(ViewState.cert.nedsattMed100)) {
                    return false;
                }
                return _showField(_fields.field8b, isIncludedField);
            };

            $scope.showField9 = function _showField9(isIncludedField)   {
                if (_isInvalid(ViewState.cert.arbetsformagaPrognos)) {
                    return false;
                }
                return _showField(_fields.field9, isIncludedField);
            };

            $scope.showField10 = function _showField10(isIncludedField)   {
                if (_isInvalid(ViewState.cert.prognosBedomning)) {
                    return false;
                }
                return _showField(_fields.field10, isIncludedField);
            };

            $scope.showField11 = function _showField11(isIncludedField)  {
                if (_isInvalid(ViewState.cert.ressattTillArbeteAktuellt) && _isInvalid(ViewState.cert.ressattTillArbeteEjAktuellt)) {
                    return false;
                }
                return _showField(_fields.field11, isIncludedField);
            };

            $scope.showField12 = function _showField12(isIncludedField)  {
                if (_isInvalid(ViewState.cert.kontaktMedFk)) {
                    return false;
                }
                return _showField(_fields.field12, isIncludedField);
            };

            $scope.showField13 = function _showField13(isIncludedField)  {
                if (_isInvalid(ViewState.cert.kommentar)) {
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


            $scope.leaveSummaryPage = null;
            $scope.toState = null;

            $scope.onLeaveSummaryPage = function() {
                $scope.leaveSummaryPage = true;
                $location.path($scope.toState.url);
            };

            function _onStateChangeStart(event, toState, toParams, fromState, fromParams, options) {
                // continue with event when leaveSummaryPage variable is set
                if ($scope.leaveSummaryPage) {
                    return;
                }

                // continue with event when navigation is back to step 1
                if (toParams.certificateId === fromParams.certificateId) {
                    return;
                }

                // prevent this event and display modal dialog
                event.preventDefault();
                $scope.toState = toState;
                _showDialog();
            }

            $scope.$on('$stateChangeStart', _onStateChangeStart);

            function _showDialog() {
                dialogService.showDialog($scope, {
                    dialogId: 'fk7263-customize-summary-dialog',
                    templateUrl: '/web/webjars/fk7263/minaintyg/views/customize-cert-summary-leave.html',
                    autoClose: true
                });
            }

            // Submit user selected fields to PDF generator

            function _addInput(name, item) {
                return '<input type="hidden" name="' + name + '" value="' + item + '" />';
            }

            function _getSelectedFields(fields) {
                var selectedFields = [];
                angular.forEach(fields, function(field, key) {
                    if (!field.mandatory && field.vald) {
                        // Only add non-mandatory fields if they are selected
                        selectedFields.push(field.id);
                    }
                });

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


            $scope.pagefocus = true;
        }]);
