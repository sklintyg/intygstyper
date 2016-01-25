angular.module('sjukersattning').controller('sjukersattning.EditCert.FormUnderlagCtrl',
    ['$scope', '$log', 'sjukersattning.EditCertCtrl.ViewStateService', 'common.UtilsService', '$filter',
        'common.DateUtilsService',
        function($scope, $log, viewState, utils, $filter, dateUtils ) {
            'use strict';
            var model = viewState.intygModel;

            $scope.model = model;
            $scope.viewState = viewState;

            $scope.grundForMUState = {
                dates: {},
                check:{},
                labelID: ['KV_FKMU_0001.1.RBK','KV_FKMU_0001.3.RBK','KV_FKMU_0001.4.RBK','KV_FKMU_0001.5.RBK'],
                fieldNames: ['undersokningAvPatienten', 'journaluppgifter', 'anhorigsBeskrivningAvPatienten', 'annatGrundForMU' ]
            };
            $scope.grundForMUState.fieldNames.forEach(function(grundForMUdate) {
                $scope.grundForMUState.check[grundForMUdate] = false;
                $scope.grundForMUState.dates[grundForMUdate] = null;
            });

            // once we've doneLoading we can set the radion buttons to the model state.
            $scope.$watch('viewState.common.doneLoading', function(newVal, oldVal) {
                if(newVal === oldVal){
                    return;
                }
                if (newVal) {
                    //registerDateParsersFor2($scope);
                    setGrundForMU();
                    // I really hate this but needs to be done because the datepicker doesn't accept non dates!!
                    transferModelToForm();
                    viewState.updateUnderlagOptions();
                }
            });

            function clearViewState() {
                $scope.grundForMUState.fieldNames.forEach(function(fieldName) {
                    $scope.grundForMUState.check[fieldName] = false;
                });
            }

            function setGrundForMU() {
                $scope.grundForMUState.fieldNames.forEach(function(fieldName) {
                    $scope.grundForMUState.check[fieldName] = model[fieldName] !== undefined;
                });
            }

            function transferModelToForm() {
                $scope.grundForMUState.fieldNames.forEach(function(fieldName) {
                    $scope.grundForMUState.dates[fieldName] = model[fieldName];
                });
            }


            /**
             * 2. Toggle dates in the Based On date pickers when checkboxes are interacted with
             * @param modelName
             */
            $scope.toggleBaseradPaDate = function(modelName) {

                // Set todays date when a baserat pa field is checked
                if ($scope.grundForMUState.check[modelName]) {
                    if (!model[modelName] || model[modelName] === '') {
                        model[modelName] = dateUtils.todayAsYYYYMMDD();
                        $scope.grundForMUState.dates[modelName] = model[modelName];
                    }
                } else {
                    // Clear date if check is unchecked
                    model[modelName] = undefined;
                    $scope.grundForMUState.dates[modelName] = '';
                }
            };

            /**
             * 2. Update checkboxes when datepickers are interacted with
             * @param baserasPaType
             */
            $scope.onChangeGrundForMUDate = function(baserasPaType, $viewValue) {
                $scope.grundForMUState.check[baserasPaType] = utils.isValidString($viewValue);
            };

            function registerDateParsersFor2(_$scope) {
                // Register parse function for 2 date pickers
                addParsers(_$scope, $scope.grundForMUState.fieldNames, _$scope.onChangeGrundForMUDate);
            }

            function addParsers(form2, attributes, fn) {
                var modelProperty;
                angular.forEach(attributes, function(type) {
                    modelProperty = this.form2[type + 'Date'];
                    //console.log('modelprop from addParsers1' + JSON.stringify(modelProperty));
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
                            //console.log('iso' + isoValue);
                            return isoValue;
                        });
                    }
                }, form2);
            }

            $scope.onUnderlagFinnsChanged = function() {
                if (model.underlagFinns) {
                    if (model.underlag.length === 0) {
                        $scope.createUnderlag();
                    }
                }
                else {
                    model.underlag = [];
                }
            };

            $scope.createUnderlag = function() {
                model.underlag.push({ typ: null, datum: null, hamtasFran: null });
                $scope.form2.$setDirty();
            };

            $scope.removeUnderlag = function(index) {
                model.underlag.splice(index, 1);
                $scope.form2.$setDirty();
            };
        }]);