angular.module('fk7263').controller('fk7263.EditCert.Form4bCtrl',
    ['$log', 'fk7263.Domain.IntygModel', '$scope', 'fk7263.EditCertCtrl.ViewStateService', 'common.UtilsService', 'common.DateUtilsService',
        function($log, model, $scope, viewState, utils, dateUtils) {
            'use strict';
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

            // once we've doneLoading we can set the radion buttons to the model state.
            $scope.$watch('viewState.common.doneLoading', function(newVal) {
                if (newVal) {
                    registerDateParsersFor4b($scope);
                    setBaserasPa();
                    
                }
            });

            $scope.$watch('viewState.avstangningSmittskyddValue', function(newVal) {
                // only do this once the page is loaded and changes come from the gui!
                if(viewState.common.doneLoading && newVal) {
                    clearState();
                }
            });


            function clearState(){
                $scope.model.undersokningAvPatienten = undefined;
                $scope.model.telefonkontaktMedPatienten = undefined;
                $scope.model.journaluppgifter = undefined;
                $scope.model.annanReferens = undefined;
                $scope.model.annanReferensBeskrivning = undefined;
                $scope.basedOnState.check.undersokningAvPatienten = false;
                $scope.basedOnState.check.telefonkontaktMedPatienten = false;
                $scope.basedOnState.check.journaluppgifter = false;
                $scope.basedOnState.check.annanReferens = false;    
            }
            
            function setBaserasPa(){
                if(model.undersokningAvPatienten){
                    $scope.basedOnState.check.undersokningAvPatienten = true;
                } else {
                    $scope.basedOnState.check.undersokningAvPatienten = false;
                }
                if(model.telefonkontaktMedPatienten){
                    $scope.basedOnState.check.telefonkontaktMedPatienten = true;
                } else {
                    $scope.basedOnState.check.telefonkontaktMedPatienten = false;
                }
                if(model.journaluppgifter){
                    $scope.basedOnState.check.journaluppgifter = true;
                } else {
                    $scope.basedOnState.check.journaluppgifter = false;
                }
                if(model.annanReferens){
                    $scope.basedOnState.check.annanReferens = true;
                } else {
                    $scope.basedOnState.check.annanReferens = false;
                }
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
                    }
                } else {
                    // Clear date if check is unchecked
                    $scope.model[modelName] = '';
                }
            };

            /**
             * 4b. Update checkboxes when datepickers are interacted with
             * @param baserasPaType
             */
            $scope.onChangeBaserasPaDate = function(baserasPaType, $viewValue) {
                if (utils.isValidString($viewValue)) {
                    $scope.basedOnState.check[baserasPaType] = true;
                } else {
                    $scope.basedOnState.check[baserasPaType] = false;
                }
            };

            function registerDateParsersFor4b(_$scope) {
                // Register parse function for 4b date pickers
                var baserasPaTypes = ['undersokningAvPatienten', 'telefonkontaktMedPatienten', 'journaluppgifter',
                    'annanReferens'];
                addParsers(_$scope.form4b, baserasPaTypes, _$scope.onChangeBaserasPaDate );
            }

            function addParsers(model, attributes, fn){
                var modelProperty;
                angular.forEach(attributes, function(type) {
                    modelProperty = this[type + 'Date'];
                    if (modelProperty) {
                        modelProperty.$parsers.push(function(viewValue) {
                            viewValue = dateUtils.convertDateToISOString(viewValue);
                            return viewValue;
                        });
                        modelProperty.$parsers.push(function(viewValue) {
                            fn(type, viewValue);
                            return viewValue;
                        });
                    }
                }, model);
            }

        }]);