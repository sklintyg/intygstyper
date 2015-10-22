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

            // fält 2 when new questions selected
            $scope.radioMedical = {
                checked : false
            };
            // fält 2 - when yes -> underlag,  format: [{"sortValue": 0, "id": "default", label: "Ange underlag..."},{}]

            /* we need a new model here to hold data for each row containing:
                -underlagsType(valtUnderlag string),
                -code for backend(will come at a later point from FK),
                -originalCreationDate(original creation date for cert),
                -userWillAttachData(bool, no actual attachment in form)

                model in this directive would be these data as an array of objects
                when onChange is triggers on each individual row element, scope model (underlagsupplements) get filled in

                same "setter function" should be called to sync with backend model, to set data data upon update and load event (with $watch)
               */

            $scope.valtUnderlag = 'default';

            // Holds all underlag base data in select
            $scope.underlagSelect = [];

            // this will be refactored to a service later ,also pristine and dirty will be added
            // init values on load, id: unique id made up of typ+counter, isSelected if user prev has selected something on this u.type
            $scope.additionalSupplements = {
                supplement0: { id: 'supplement0',
                    selectedType: 'default',
                    dateCreated: null,
                    willSupplyAttachment: false,
                    selectedOrder: 0,
                    isLatest: true },
            };



            // search and replace the model when supplement dropdown change
           $scope.onUnderlagChange = function(underlag) {
               if (underlag.selectedType !== 'default') {
                   // set supplement 1 to new underlag type
                   registerDateParsersForSupplementals($scope);
                   if(underlag) {
                       underlag.selectedType = underlag.selectedType;
                       underlag.dateCreated = underlag.dateCreated;
                       underlag.willSupplyAttachment = underlag.willSupplyAttachment;



                   } else {
                       throw("no prop found with id" + underlag.id);
                   }
/*
                   var willSupplyYESDirty = $scope.form2['willSupplyAttachmentRadioYes' + underlag.id].$dirty;
                   var willSupplyNODirty = $scope.form2['willSupplyAttachmentRadioNo' + underlag.id].$dirty;
                   console.log("Yes dirt?" + JSON.stringify(willSupplyYESDirty));
                   console.log("No dirt?" + JSON.stringify(willSupplyNODirty));

                   if(underlag.selectedType !== '' && underlag.dateCreated !== null &&
                       ( willSupplyYESDirty || willSupplyNODirty) ) {
                       underlag.isComplete = true;
                   } else {
                       underlag.isComplete = false;
                   }*/

               }
            }

            $scope.createUnderlag = function(currentSelectedNum) {
                // first check for completion of current row to set it as latest = False, there can only be one
                $scope.additionalSupplements['supplement' + currentSelectedNum].isLatest = false;
                var newNumber = currentSelectedNum + 1;
                var newId = 'supplement' + newNumber;
                $scope.additionalSupplements[newId] = {
                                                        id: newId,
                                                        selectedType: 'default',
                                                        dateCreated: null,
                                                        willSupplyAttachment: false,
                                                        selectedOrder: newNumber,
                                                        isLatest : true
                                                      };
                registerDateParsersForSupplementals($scope);
            }

            function onPageLoad(){
                $scope.underlagSelect = [
                    { "sortValue": 0, "id": "default", label: "Ange underlag eller utredning"},
                    { "sortValue": 1, "id": "neuropsykiatrisktUtlatande", label: "Neuropsykiatriskt utlåtande"},
                    { "sortValue": 2, "id": "underlagFranhabiliteringen", label: "Underlag från habiliteringen" },
                    { "sortValue": 3, "id": "underlagFranarbetsterapeut", label: "Underlag från arbetsterapeut" },
                    { "sortValue": 4, "id": "underlagFranfysioterapeut", label: "Underlag från fysioterapeut" },
                    { "sortValue": 5, "id": "underlagFranlogoped", label: "Underlag från logoped" },
                    { "sortValue": 6, "id": "underlagFranpsykolog", label: "Underlag från psykolog" },
                    { "sortValue": 7, "id": "underlagFranföretagshalsovard", label: "Underlag från företagshälsovård" },
                    { "sortValue": 8, "id": "utredningAvAnnanSpecialistklinik", label: "Utredning av annan specialistklinik"},
                    { "sortValue": 9, "id": "utredningFranVardinrattningUtomlands", label: "Utredning från vårdinrättning utomlands" },
                    { "sortValue": 10, "id": "ovrigt", label: "Övrigt" }
                ];
            };

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
                kannedomOmPatient: null,
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
                var baserasPaTypes = ['undersokningAvPatienten', 'telefonkontaktMedPatienten', 'journaluppgifter', 'kannedomOmPatient', ];
                addParsers(_$scope.form2, baserasPaTypes, _$scope.onChangeBaserasPaDate);
               // console.log("_$scope.form2" + JSON.stringify(_$scope.form2));
            }

            function addParsers(form2, attributes, fn) {
                var modelProperty;
                angular.forEach(attributes, function(type) {
                    modelProperty = this[type + 'Date'];
                    //console.log("modelprop from addParsers1" + JSON.stringify(modelProperty));
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
                            //console.log("iso" + isoValue);
                            return isoValue;

                        });
                    }
                }, form2);
            }


            /**
             * 2-extra. Update  datepickers when interacted with
             * @param supplemental
             */
            $scope.onChangeBaserasPaDateSupplemental = function(supplemental, $viewVal) {
                //console.log("supplemental in onChangeBaserasPaDateSupplemental: " + supplemental);
                if(utils.isValidString($viewVal)) {
                    $scope.additionalSupplements[supplemental].dateCreated = $viewVal;
                }
                //console.log("viewVal in onChangeBaserasPaDateSupplemental: " + $viewVal);
            }

            function registerDateParsersForSupplementals(_$scope, id) {
                // Register parse function for new row where datepicker resides
                // get active list of datepickes, with or without to register parsers on.
                var supplements = resolveActiveSupplements(id);

                //console.log("supplements active date pickers: "  + supplements); // incoming Tue Oct 13 2015 00:00:00 GMT+0200 (CEST)
                addParsersSupplemental(_$scope.form2, supplements, _$scope.onChangeBaserasPaDateSupplemental);
                //console.log("_$scope.form2" + JSON.stringify(_$scope.form2));
            }

            function resolveActiveSupplements(id) {
                var activeSupplementsToParse = [];
                if(!id) {
                    for (var key in $scope.additionalSupplements) {
                        if ($scope.additionalSupplements.hasOwnProperty(key)) {
                            var obj = $scope.additionalSupplements[key];
                            for (var prop in obj) {
                                if (obj.hasOwnProperty(prop)) {
                                    //console.log(prop + " = " + obj[prop]);
                                    if (prop === 'dateCreated') {
                                        var uniqueKey = key;
                                        activeSupplementsToParse.push(uniqueKey);
                                    }
                                }
                            }
                        }
                    }
                }
                activeSupplementsToParse.push(id);
                return activeSupplementsToParse;
            }

            function addParsersSupplemental(form2, attributes, fn) {
                var modelProperty;
                angular.forEach(attributes, function(type) {
                  //  console.log("attrs:" + JSON.stringify(attributes));
                    modelProperty = this[type + '-Date'];
                  //  console.log("modelprop after:" + JSON.stringify(modelProperty));
                    if (modelProperty) {
                    //    console.log("mp count");
                        // remove the datepickers default parser
                        modelProperty.$parsers.unshift(function(viewValue) {
                            fn(type, viewValue);

                            return viewValue;
                        });

                        modelProperty.$parsers.unshift(function(viewValue) {
                            var isoValue = dateUtils.convertDateToISOString(viewValue);
                          return isoValue;

                        });
                    }
                }, form2);
            }

            onPageLoad();

        }]);