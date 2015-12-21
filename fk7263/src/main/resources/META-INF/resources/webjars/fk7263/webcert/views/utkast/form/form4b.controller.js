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

angular.module('fk7263').controller('fk7263.EditCert.Form4bCtrl',
    ['$scope', '$log', 'fk7263.EditCertCtrl.ViewStateService', 'common.UtilsService',
        'common.DateUtilsService',
        function($scope, $log, viewState, utils, dateUtils) {
            'use strict';
            var model = viewState.intygModel;
            $scope.model = model;

            $scope.viewState = viewState;

            // Fält 4b. Based on handling
            $scope.basedOnState = {
                check: {
                    undersokningAvPatienten: false,
                    telefonkontaktMedPatienten: false,
                    journaluppgifter: false,
                    annanReferens: false
                }
            };

            $scope.dates = {
                undersokningAvPatienten: null,
                telefonkontaktMedPatienten: null,
                journaluppgifter: null,
                annanReferens: null
            };

            // once we've doneLoading we can set the radion buttons to the model state.
            $log.debug('--- start doneloading 4b ---');
            var doneLoading = false;
            $scope.$watch(function() {

                if(viewState.common.doneLoading) {
                    if (angular.isObject($scope.form4b.undersokningAvPatientenDate) &&
                        angular.isObject($scope.form4b.telefonkontaktMedPatientenDate) &&
                        angular.isObject($scope.form4b.journaluppgifterDate) &&
                        angular.isObject($scope.form4b.annanReferensDate)) {
                        return true;
                    }
                    $log.debug('--- form4b control not loaded yet ---');
                    $log.debug('1:');
                    $log.debug($scope.form4b.undersokningAvPatientenDate);
                    $log.debug('2:');
                    $log.debug($scope.form4b.telefonkontaktMedPatientenDate);
                    $log.debug('3:');
                    $log.debug($scope.form4b.journaluppgifterDate);
                    $log.debug('4:');
                    $log.debug($scope.form4b.annanReferensDate);
                }
                return false;

            }, function(newVal) {
                if (doneLoading) {
                    return;
                }
                if (newVal) {
                    doneLoading = true;
                    registerDateParsersFor4b($scope);
                    setBaserasPa();
                    // I really hate this but needs to be done because the datepicker doesn't accept non dates!!
                    transferModelToForm();
                }
            });

            $scope.$watch('viewState.avstangningSmittskyddValue', function(newVal, oldVal) {
                if(newVal === oldVal){
                    return;
                }
                // only do this once the page is loaded and changes come from the gui!
                if (viewState.common.doneLoading && newVal) {
                    model.updateToAttic('form4b');
                    model.clear('form4b');
                    clearViewState();
                } else {
                    model.restoreFromAttic('form4b');
                    transferModelToForm();
                    setBaserasPa();
                }
            });


            function clearViewState() {
                $scope.basedOnState.check.undersokningAvPatienten = false;
                $scope.basedOnState.check.telefonkontaktMedPatienten = false;
                $scope.basedOnState.check.journaluppgifter = false;
                $scope.basedOnState.check.annanReferens = false;
            }

            function setBaserasPa() {
                $scope.basedOnState.check.undersokningAvPatienten = model.undersokningAvPatienten !== undefined;
                $scope.basedOnState.check.telefonkontaktMedPatienten = model.telefonkontaktMedPatienten !== undefined;
                $scope.basedOnState.check.journaluppgifter = model.journaluppgifter !== undefined;
                $scope.basedOnState.check.annanReferens = model.annanReferens !== undefined;
            }

            function transferModelToForm() {
                $scope.dates.undersokningAvPatienten = $scope.model.undersokningAvPatienten;
                $scope.dates.telefonkontaktMedPatienten = $scope.model.telefonkontaktMedPatienten;
                $scope.dates.journaluppgifter = $scope.model.journaluppgifter;
                $scope.dates.annanReferens = $scope.model.annanReferens;
            }


            /**
             * 4b. Toggle dates in the Based On date pickers when checkboxes are interacted with
             * @param modelName
             */
            $scope.toggleBaseradPaDate = function(modelName) {

                // Set todays date when a baserat pa field is checked
                if ($scope.basedOnState.check[modelName]) {
                    if (!$scope.model[modelName] || $scope.model[modelName] === '') {
                        $scope.model[modelName] = dateUtils.todayAsYYYYMMDD();
                        $scope.dates[modelName] = $scope.model[modelName];
                    }
                } else {
                    // Clear date if check is unchecked
                    $scope.model[modelName] = undefined;
                    $scope.dates[modelName] = '';
                }
            };

            /**
             * 4b. Update checkboxes when datepickers are interacted with
             * @param baserasPaType
             */
            $scope.onChangeBaserasPaDate = function(baserasPaType, $viewValue) {
                $scope.basedOnState.check[baserasPaType] = utils.isValidString($viewValue);
            };

            function registerDateParsersFor4b(_$scope) {
                // Register parse function for 4b date pickers
                var baserasPaTypes = ['undersokningAvPatienten', 'telefonkontaktMedPatienten', 'journaluppgifter',
                    'annanReferens'];
                addParsers(_$scope.form4b, baserasPaTypes, _$scope.onChangeBaserasPaDate);
            }

            function addParsers(form4b, attributes, fn) {
                var modelProperty;
                angular.forEach(attributes, function(type) {
                    modelProperty = this[type + 'Date'];
                    if (modelProperty) {
                        // remove the datepickers default parser
                        modelProperty.$parsers.unshift(function(viewValue) {
                            fn(type, viewValue);
                            return viewValue;
                        });

                        modelProperty.$parsers.unshift(function(viewValue) {
                            // always set the model value

                            var isoValue = dateUtils.convertDateToISOString(viewValue);
                            model[type] = isoValue;
                            return isoValue;
                        });
                    }
                }, form4b);
            }

        }]);
