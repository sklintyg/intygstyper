angular.module('sjukersattning').controller('sjukersattning.EditCert.Form2Ctrl',
    ['$scope', '$log', 'sjukersattning.EditCertCtrl.ViewStateService', 'common.UtilsService',
        'common.DateUtilsService',
        function($scope, $log, viewState, utils, dateUtils) {
            'use strict';
            var model = viewState.intygModel;


            $scope.logOutModelConsole = function (){
                $log.info("Current Model" + JSON.stringify(model));
            }

            $scope.logOutViewState = function (){
                $log.info("Current Viewstate" + JSON.stringify(viewState));
            }

            $scope.model = model;

            $scope.viewState = viewState;

            // Fält 2. Based on handling
            $scope.basedOnState = {
                check: {
                    undersokningAvPatienten: false,
                    telefonkontaktMedPatienten: false,
                    journaluppgifter: false
                }
            };

            $scope.dates = {
                undersokningAvPatienten: null,
                journaluppgifter: null,
                telefonkontaktMedPatienten: null,
                //anhorigBeskrivningAvPatienten: null,
                kannedomOmPatient: null
            };


            // once we've doneLoading we can set the radion buttons to the model state.
            $scope.$watch('viewState.common.doneLoading', function(newVal, oldVal) {
                if(newVal === oldVal){
                    return;
                }
                if (newVal) {
                    registerDateParsersFor2($scope);
                    setBaserasPa();
                    // I really hate this but needs to be done because the datepicker doesn't accept non dates!!
                    transferModelToForm();
                }
            });

            function clearViewState() {
                $scope.basedOnState.check.undersokningAvPatienten = false;
                $scope.basedOnState.check.telefonkontaktMedPatienten = false;
                $scope.basedOnState.check.journaluppgifter = false;
            }

            function setBaserasPa() {
                $scope.basedOnState.check.undersokningAvPatienten = model.undersokningAvPatienten !== undefined;
                $scope.basedOnState.check.telefonkontaktMedPatienten = model.telefonkontaktMedPatienten !== undefined;
                $scope.basedOnState.check.journaluppgifter = model.journaluppgifter !== undefined;
            }

            function transferModelToForm() {
                $scope.dates.undersokningAvPatienten = $scope.model.undersokningAvPatienten;
                $scope.dates.telefonkontaktMedPatienten = $scope.model.telefonkontaktMedPatienten;
                $scope.dates.journaluppgifter = $scope.model.journaluppgifter;
                $scope.dates.kannedomOmPatient = $scope.model.kannedomOmPatient;
            }


            /**
             * 2. Toggle dates in the Based On date pickers when checkboxes are interacted with
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
             * 2. Update checkboxes when datepickers are interacted with
             * @param baserasPaType
             */
            $scope.onChangeBaserasPaDate = function(baserasPaType, $viewValue) {
                $scope.basedOnState.check[baserasPaType] = utils.isValidString($viewValue);
            };

            function registerDateParsersFor2(_$scope) {
                // Register parse function for 2 date pickers
                var baserasPaTypes = ['undersokningAvPatienten', 'telefonkontaktMedPatienten', 'journaluppgifter', 'kannedomOmPatient'];
                addParsers(_$scope.form2, baserasPaTypes, _$scope.onChangeBaserasPaDate);
            }

            function addParsers(form2, attributes, fn) {
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
                }, form2);
            }

        }]);