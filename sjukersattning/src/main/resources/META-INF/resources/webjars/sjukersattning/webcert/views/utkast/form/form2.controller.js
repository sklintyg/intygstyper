angular.module('sjukersattning').controller('sjukersattning.EditCert.Form2Ctrl',
    ['$scope', '$log', 'sjukersattning.EditCertCtrl.ViewStateService', 'common.UtilsService', '$filter',
        'common.DateUtilsService',
        function($scope, $log, viewState, utils, $filter, dateUtils ) {
            'use strict';
            var model = viewState.intygModel;

            $scope.model = model;
            $scope.viewState = viewState;


            $scope.viewModel = {
                radioMedicalChecked: false,
                underlagCompleted: [], // hold what row is selected
                initialUnderlag: [{ 'typ': 0, 'datum': null, 'bilaga': false }]
            };

            function onPageLoad(){
                $scope.underlagSelect = viewState.underlagOptions;
                if(model.underlag.length < 0) {
                    // if we have underlag with id other then 0 (default is 0) from server, show them
                    $scope.viewModel.radioMedicalChecked = true; // test this
                }
            }

            // Fält 1. Based on handling
            $scope.basedOnState = {
                check: {
                    undersokningAvPatienten: false,
                    telefonkontaktMedPatienten: false,
                    journaluppgifter: false,
                    kannedomOmPatient: false
                }
            };

            $scope.dates = {
                undersokningAvPatienten: null,
                journaluppgifter: null,
                telefonkontaktMedPatienten: null,
                kannedomOmPatient: null
            };

            // once we've doneLoading we can set the radion buttons to the model state.
            $scope.$watch('viewState.common.doneLoading', function(newVal, oldVal) {
                if(newVal === oldVal){
                    return;
                }
                if (newVal) {
                    registerDateParsersFor2($scope);
                    registerDateParsersForSupplementals($scope);
                    setBaserasPa();
                    // I really hate this but needs to be done because the datepicker doesn't accept non dates!!
                    transferModelToForm();
                }
            });

            function clearViewState() {
                $scope.basedOnState.check.undersokningAvPatienten = false;
                $scope.basedOnState.check.telefonkontaktMedPatienten = false;
                $scope.basedOnState.check.journaluppgifter = false;
                $scope.basedOnState.check.kannedomOmPatient = false;
            }

            function setBaserasPa() {
                $scope.basedOnState.check.undersokningAvPatienten = model.undersokningAvPatienten !== undefined;
                $scope.basedOnState.check.telefonkontaktMedPatienten = model.telefonkontaktMedPatienten !== undefined;
                $scope.basedOnState.check.journaluppgifter = model.journaluppgifter !== undefined;
                $scope.basedOnState.check.kannedomOmPatient = model.kannedomOmPatient !== undefined;

            }

            function transferModelToForm() {
                $scope.dates.undersokningAvPatienten = model.undersokningAvPatienten;
                $scope.dates.telefonkontaktMedPatienten = model.telefonkontaktMedPatienten;
                $scope.dates.journaluppgifter = model.journaluppgifter;
                $scope.dates.kannedomOmPatient = model.kannedomOmPatient;

            }


            /**
             * 2. Toggle dates in the Based On date pickers when checkboxes are interacted with
             * @param modelName
             */
            $scope.toggleBaseradPaDate = function(modelName) {

                // Set todays date when a baserat pa field is checked
                if ($scope.basedOnState.check[modelName]) {
                    if (!model[modelName] || model[modelName] === '') {
                        model[modelName] = dateUtils.todayAsYYYYMMDD();
                        $scope.dates[modelName] = model[modelName];
                    }
                } else {
                    // Clear date if check is unchecked
                    model[modelName] = undefined;
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
                var baserasPaTypes = ['undersokningAvPatienten', 'telefonkontaktMedPatienten', 'journaluppgifter', 'kannedomOmPatient' ];
                addParsers(_$scope, baserasPaTypes, _$scope.onChangeBaserasPaDate);
            }

            function addParsers(form2, attributes, fn) {
                var modelProperty;
                angular.forEach(attributes, function(type) {
                    modelProperty = this[type + 'Date'];
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

            /**
             * 2-Update datepickers on supplementals when interacted with
             * @param supplemental
             */
            $scope.onChangeBaserasPaDateSupplemental = function(supplemental, $viewVal) {
                if(utils.isValidString($viewVal)) {
                    $scope.viewModel.initialUnderlag[supplemental].datum = $viewVal;
                }
            };

            function registerDateParsersForSupplementals(_$scope, id) {
                // Register parse function for new row where datepicker resides
                // get active list of datepickes, with or without to register parsers on.
                var supplements = resolveActiveSupplements(id);
                 //console.log('supplements active date pickers: '  + supplements); // incoming Tue Oct 13 2015 00:00:00 GMT+0200 (CEST)
                addParsersSupplemental(_$scope, supplements, _$scope.onChangeBaserasPaDateSupplemental);
            }

            function resolveActiveSupplements(id) {
                var activeSupplementsToParse = [];
                if(!id) {
                    angular.forEach($scope.viewModel.initialUnderlag, function(value, key){

                        if( utils.isDefined(value.datum) ) {
                            activeSupplementsToParse.push(value.typ);
                        }
                    });
                }
                activeSupplementsToParse.push(id);
              //  console.log(activeSupplementsToParse);
                return activeSupplementsToParse;
            }

            function addParsersSupplemental(form2, attributes, fn) {
                var modelProperty;
                angular.forEach(attributes, function(type) {
                  //  console.log('attrs:' + JSON.stringify(attributes));
                    modelProperty = this[type + '-Date'];
                  //  console.log('modelprop after:' + JSON.stringify(modelProperty));
                    if (modelProperty) {
                    //    console.log('mp count');
                        // remove the datepickers default parser
                        modelProperty.$parsers.unshift(function(viewValue) {
                            fn(type, viewValue);

                            return viewValue;
                        });

                        modelProperty.$parsers.unshift(function(viewValue) {
                            var isoValue = dateUtils.convertDateToISOString(viewValue);
                            $scope.viewModel.initialUnderlag[type].datum = isoValue;
                          return isoValue;

                        });
                    }
                }, form2);
            }

            // Holds all underlag base data in select
            $scope.underlagSelect = [];
            // search and replace the model when supplement dropdown change
            $scope.onUnderlagChange = function(underlag, index) {
                if(underlag.typ !== 0 && underlag.datum !== null) {

                    registerDateParsersForSupplementals($scope);
                    if(underlag.typ !==0 && underlag.datum !== null &&
                        (underlag.bilaga !== undefined || null) ) {

                        if( $scope.viewModel.underlagCompleted.indexOf(index) === -1 ) {
                            $scope.viewModel.underlagCompleted.push(index);// set currently manipulated underlag
                        }
                        console.log('viewmodel underlag: ', $scope.viewModel.initialUnderlag);
                        // add duplicate check? match for existing?

                        var current = model.underlag[underlag.typ];

                        if(current !== undefined && current.typ === underlag.typ &&
                            current.datum === underlag.datum && current.bilaga === underlag.bilaga){ // if existing
                            current.typ = underlag.typ;
                            current.datum = dateUtils.convertDateToISOString(underlag.datum);
                            current.bilaga = underlag.bilaga;
                        } else { // if new
                            underlag.datum = dateUtils.convertDateToISOString(underlag.datum);
                            model.underlag.push( underlag );
                        }
                        console.log('model underlag: ' + JSON.stringify($scope.model.underlag));
                    }
                } else {
                    // reset
                    underlag.datum = null;
                    underlag.bilaga = false;
                }
            }

            $scope.createUnderlag = function() {
                $scope.viewModel.initialUnderlag.push({ typ: 0, datum: null, bilaga: false }); // we set this first to allow be validations
                registerDateParsersForSupplementals($scope);
            }

            $scope.removeUnderlag = function(typ, index){
                if(index === 0) { // hide when first is removed
                    resetUnderlag();
                    $scope.viewModel.radioMedicalChecked = false;
                } else if(index === 1) { // if 0, we delete the last unpopulated

                    if(typ !== 0 ) {
                        deleteUnderlag(typ, index);
                    } else{
                        $scope.viewModel.initialUnderlag.pop();
                    }

                } else {
                    if(typ !== 0) {
                        deleteUnderlag(typ, index);
                    } else{
                        $scope.viewModel.initialUnderlag.pop();
                    }
                }
            }

            function deleteUnderlag(typ){

            //    var find  = $scope.viewModel.initialUnderlag.indexOf(typ);

                var index = $scope.viewModel.initialUnderlag.map(function(obj, index) {
                    if(obj.typ === typ) {
                        return index;
                    }
                }).filter(isFinite)

                console.log('find: ' , index);

                $scope.viewModel.initialUnderlag.splice(index[index.length - 1], 1);
                $scope.model.underlag.splice(index[index.length - 1], 1);

                console.log( 'm: ' + $scope.model.underlag + ' vm: ' + $scope.viewModel.initialUnderlag );
            }

            function resetUnderlag(){
                model.underlag = [];
            }


            onPageLoad();

        }]);