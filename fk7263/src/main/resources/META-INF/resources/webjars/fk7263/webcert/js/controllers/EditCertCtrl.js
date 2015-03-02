angular.module('fk7263').controller('fk7263.EditCertCtrl',
    ['$rootScope', '$anchorScroll', '$filter', '$location', '$scope', '$log', '$timeout', '$routeParams',
        'common.CertificateService', 'common.ManageCertView', 'common.User', 'common.wcFocus',
        'common.intygNotifyService', 'fk7263.diagnosService', 'common.DateUtilsService', 'common.UtilsService','fk7263.EditCertCtrl.DateRangeGroupsService',
        function($rootScope, $anchorScroll, $filter, $location, $scope, $log, $timeout, $routeParams,
            CertificateService, ManageCertView, User, wcFocus, intygNotifyService, diagnosService, dateUtils, utils, DateRangeGroupsService ) {
            'use strict';

            /**************************************************************************
             * Private vars
             */
            var _dateRangeGroups;

            /**********************************************************************************
             * Default state
             **********************************************************************************/
            // the below state needs to be moved to models xxxx
                // Page states
            $scope.user = User;
            $scope.today = new Date();
            $scope.today.setHours(0, 0, 0, 0); // reset time to increase comparison accuracy (using new Date() also sets time)
            $scope.focusFirstInput = false;
            $scope.widgetState = {
                doneLoading: false,
                activeErrorMessageKey: null,
                saveErrorMessageKey: null,
                hasError: false,
                showComplete: false,
                collapsedHeader: false,
                hasInfoMissing: false,
                vidarebefordraInProgress: false,
                hospName: $routeParams.hospName,
                deleted: false
            };

            // Intyg state
            $scope.cert = {};
            $scope.hasSavedThisSession = false;
            $scope.messages = [];
            $scope.isComplete = false;
            $scope.isSigned = false;
            $scope.certMeta = {
                intygId: null,
                intygType: 'fk7263',
                vidarebefordrad: false
            };

            // Keeps track of in-form interactions which is converted to internal model on save,
            // and converted from internal model on load
            $scope.form = {
                arbete: true,
                prognos: 'YES',
                rehab: 'NEJ',
                ressattTillArbeteAktuellt: undefined,
                ovrigt: {
                    'annanReferensBeskrivning': null,
                    'nedsattMed25Beskrivning': null,
                    'nedsattMed50Beskrivning': null,
                    'nedsattMed75Beskrivning': null,
                    'arbetsformagaPrognosGarInteAttBedomaBeskrivning': null
                }
            };

            $scope.stash = {
                cert : { kommentar : ''}
            }

            // Fält 4b. Based on handling
            $scope.basedOnState = {
                check: {
                    undersokningAvPatienten: false,
                    telefonkontaktMedPatienten: false,
                    journaluppgifter: false,
                    annanReferens: false
                }
            };

            // 8b. Arbetsförmåga date management
            $scope.field8b = {
                nedsattMed25 : null,
                nedsattMed50 : null,
                nedsattMed75 : null,
                nedsattMed100 : null,
                onChangeWorkStateCheck : function(nedsattModelName) {
                    if(_dateRangeGroups){
                        _dateRangeGroups.onChangeWorkStateCheck(nedsattModelName);
                    }
                }
            };

            $scope.datesOutOfRange = false;
            $scope.datesPeriodTooLong = false;
            $scope.totalCertDays = false;

            // Text input limits for different fields
            $scope.inputLimits = {
                diagnosBeskrivning: 220, // combined field 2 diagnoses (and förtydligande)
                sjukdomsforlopp: 270,
                funktionsnedsattning: 450,
                aktivitetsbegransning: 1100,
                nuvarandeArbetsuppgifter: 120,
                arbetsformagaPrognos: 600,
                atgardInomSjukvarden: 66,
                annanAtgard: 66,
                ovrigt: 360 // = combined field 13 (and dependencies that end up in field 13) limit
            };

            /***************************************************************************
             * Model property watches
             ***************************************************************************/


            /***************************************************************************
             * Private controller support functions
             ***************************************************************************/

            // -- start registerDateParsers

            /**
             * Register date parsers
             * @param $scope
             */
            function registerDateParsers(_$scope) {
                registerDateParsersFor4b(_$scope);
                registerDateParsersFor8b(_$scope);
            }


            // 4b ---------------------------------------------

            function registerDateParsersFor4b(_$scope) {
                // Register parse function for 4b date pickers
                var baserasPaTypes = ['undersokningAvPatienten', 'telefonkontaktMedPatienten', 'journaluppgifter',
                    'annanReferens'];
                addParser(_$scope.certForm, baserasPaTypes, _$scope.onChangeBaserasPaDate );
                addDateToISOParser(_$scope.certForm, baserasPaTypes);
            }

            function addParser(model, attributes, fn){
                var modelProperty;
                angular.forEach(attributes, function(type) {
                    modelProperty = this[type + 'Date'];
                    if (modelProperty) {
                        modelProperty.$parsers.push(function(viewValue) {
                            fn(type);
                            return viewValue;
                        });
                    }
                }, model);
            }

            function addDateToISOParser(model, attributes){
                angular.forEach(attributes, function(type) {
                    var modelProperty = this[type + 'Date'];
                    if (modelProperty) {
                        modelProperty.$parsers.push(function(viewValue) {
                            viewValue = dateUtils.convertDateToISOString(viewValue);
                            return viewValue;
                        });
                    }
                }, model);
            }

            // 4b ---------------------------------------------

            function registerDateParsersFor8b(_$scope) {
                if(_dateRangeGroups === undefined){
                    _dateRangeGroups = DateRangeGroupsService.build(_$scope);
                }
                _dateRangeGroups.validateDatesWithCert(_$scope.cert);
            }

            /**
             * Convert internal model to form temporary data bindings
             * @param $scope
             */
            function convertCertToForm($scope) {

                // check if all info is available from HSA. If not, display the info message that someone needs to update it
                if (!$scope.cert.grundData || !$scope.cert.grundData.skapadAv ||
                    !$scope.cert.grundData.skapadAv.vardenhet ||
                    $scope.cert.grundData.skapadAv.vardenhet.postadress === undefined ||
                    $scope.cert.grundData.skapadAv.vardenhet.postnummer === undefined ||
                    $scope.cert.grundData.skapadAv.vardenhet.postort === undefined ||
                    $scope.cert.grundData.skapadAv.vardenhet.telefonnummer === undefined ||
                    $scope.cert.grundData.skapadAv.vardenhet.postadress === '' ||
                    $scope.cert.grundData.skapadAv.vardenhet.postnummer === '' ||
                    $scope.cert.grundData.skapadAv.vardenhet.postort === '' ||
                    $scope.cert.grundData.skapadAv.vardenhet.telefonnummer === '') {
                    $scope.widgetState.hasInfoMissing = true;
                } else {
                    $scope.widgetState.hasInfoMissing = false;
                }

                // Fält 2. diagnos
                if ($scope.cert.diagnosKodsystem1) {
                    $scope.form.diagnosKodverk = $scope.cert.diagnosKodsystem1;
                }
                else {
                    $scope.form.diagnosKodverk = 'ICD_10_SE';
                }

                // Fält 4b. AnnanReferensBeskrivning
                $scope.basedOnState.check.undersokningAvPatienten = $scope.cert.undersokningAvPatienten !== undefined;
                $scope.basedOnState.check.telefonkontaktMedPatienten =
                    $scope.cert.telefonkontaktMedPatienten !== undefined;
                $scope.basedOnState.check.journaluppgifter = $scope.cert.journaluppgifter !== undefined;
                if ($scope.cert.annanReferens !== undefined) {
                    $scope.form.ovrigt.annanReferensBeskrivning = $scope.cert.annanReferensBeskrivning;
                    $scope.basedOnState.check.annanReferens = true;
                } else {
                    $scope.basedOnState.check.annanReferens = false;
                }

                if ($scope.cert.nedsattMed25Beskrivning !== undefined) {
                    $scope.form.ovrigt.nedsattMed25Beskrivning = $scope.cert.nedsattMed25Beskrivning;
                }
                if ($scope.cert.nedsattMed50Beskrivning !== undefined) {
                    $scope.form.ovrigt.nedsattMed50Beskrivning = $scope.cert.nedsattMed50Beskrivning;
                }
                if ($scope.cert.nedsattMed75Beskrivning !== undefined) {
                    $scope.form.ovrigt.nedsattMed75Beskrivning = $scope.cert.nedsattMed75Beskrivning;
                }

                // Fält 8a. Set nuvarande arbete default value
                if ($scope.cert.nuvarandeArbetsuppgifter !== undefined) {
                    $scope.form.nuvarandeArbetsuppgifter = $scope.cert.nuvarandeArbetsuppgifter;
                }

                // Fält 10. Går ej att bedöma and update backend model when view changes.
                if ($scope.cert.prognosBedomning !== undefined) {
                    switch ($scope.cert.prognosBedomning) {
                    case 'arbetsformagaPrognosJa':
                        $scope.form.prognos = 'YES';
                        break;
                    case 'arbetsformagaPrognosJaDelvis':
                        $scope.form.prognos = 'PARTLY';
                        break;
                    case 'arbetsformagaPrognosNej':
                        $scope.form.prognos = 'NO';
                        break;
                    case 'arbetsformagaPrognosGarInteAttBedoma':
                        $scope.form.prognos = 'UNKNOWN';
                        $scope.form.ovrigt.arbetsformagaPrognosGarInteAttBedomaBeskrivning =
                            $scope.cert.arbetsformagaPrognosGarInteAttBedomaBeskrivning;
                        break;
                    }
                }
                // Fält 7. Rehab radio conversions
                if ($scope.cert.rehabilitering !== undefined) {
                    switch ($scope.cert.rehabilitering) {
                    case 'rehabiliteringAktuell':
                        $scope.form.rehab = 'JA';
                        break;
                    case 'rehabiliteringEjAktuell':
                        $scope.form.rehab = 'NEJ';
                        break;
                    case 'rehabiliteringGarInteAttBedoma':
                        $scope.form.rehab = 'GAREJ';
                        break;
                    }
                }

                // Fält 6a.
                if ($scope.cert.rekommendationOvrigt !== undefined) {
                    $scope.form.rekommendationOvrigt = $scope.cert.rekommendationOvrigt;
                }

                // Fält 11. Ressätt till arbete
                $scope.form.ressattTillArbeteAktuellt = undefined;
                if ($scope.cert.ressattTillArbeteAktuellt) {
                    $scope.form.ressattTillArbeteAktuellt = 'JA';
                }
                if ($scope.cert.ressattTillArbeteEjAktuellt) {
                    $scope.form.ressattTillArbeteAktuellt = 'NEJ';
                }
            }

            /**
             * Convert form temporary bindings to internal model
             * @param $scope
             */
            function convertFormToCert($scope) {

                // Fält 1. Smittskydd. Vid sparning: ta bort data på alla fält före 8b som döljs när smittskydd är icheckat.
                if ($scope.cert.avstangningSmittskydd) {

                    // 4b. Baserat på
                    $scope.cert.undersokningAvPatienten = undefined;
                    $scope.cert.telefonkontaktMedPatienten = undefined;
                    $scope.cert.journaluppgifter = undefined;
                    $scope.cert.annanReferens = undefined;
                    $scope.form.ovrigt.annanReferensBeskrivning = undefined;
                    $scope.cert.annanReferensBeskrivning = undefined;
                    $scope.basedOnState.check.undersokningAvPatienten = false;
                    $scope.basedOnState.check.telefonkontaktMedPatienten = false;
                    $scope.basedOnState.check.journaluppgifter = false;
                    $scope.basedOnState.check.annanReferens = false;

                    // 2. Diagnos
                    $scope.cert.diagnosKodverk1 = undefined;
                    $scope.cert.diagnosKodverk2 = undefined;
                    $scope.cert.diagnosKodverk3 = undefined;
                    $scope.cert.diagnosKod = undefined;
                    $scope.cert.diagnosKod2 = undefined;
                    $scope.cert.diagnosKod3 = undefined;
                    $scope.cert.diagnosBeskrivning1 = undefined;
                    $scope.cert.diagnosBeskrivning2 = undefined;
                    $scope.cert.diagnosBeskrivning3 = undefined;
                    $scope.cert.diagnosBeskrivning = undefined;
                    $scope.cert.samsjuklighet = false;

                    // 3,4,5
                    $scope.cert.sjukdomsforlopp = undefined;
                    $scope.cert.funktionsnedsattning = undefined;
                    $scope.cert.aktivitetsbegransning = undefined;

                    // 8a
                    $scope.cert.nuvarandeArbete = false;
                    $scope.cert.nuvarandeArbetsuppgifter = undefined;
                    $scope.cert.arbetsloshet = false;
                    $scope.cert.foraldrarledighet = false;

                    // 6b åtgärder
                    $scope.cert.atgardInomSjukvarden = undefined;
                    $scope.cert.annanAtgard = undefined;

                    // 6a, 7, 11
                    $scope.cert.rekommendationKontaktArbetsformedlingen = false;
                    $scope.cert.rekommendationKontaktForetagshalsovarden = false;
                    $scope.form.rehab = undefined;
                    $scope.cert.rekommendationOvrigtCheck = false;
                    $scope.cert.rekommendationOvrigt = undefined;

                } else {

                    // Fält 4b. datum
                    var baserasPaTypes = ['undersokningAvPatienten', 'telefonkontaktMedPatienten', 'journaluppgifter',
                        'annanReferens'];
                    angular.forEach(baserasPaTypes, function(type) {
                        this[type] = dateUtils.convertDateToISOString($scope.certForm[type + 'Date'].$viewValue);
                    }, $scope.cert);

                    // Fält 2. diagnos
                    $scope.cert.diagnosKodsystem1 = $scope.form.diagnosKodverk;
                    $scope.cert.diagnosKodsystem2 = $scope.form.diagnosKodverk;
                    $scope.cert.diagnosKodsystem3 = $scope.form.diagnosKodverk;

                    // Fält 4b. AnnanReferensBeskrivning
                    if ($scope.basedOnState.check.annanReferens) {
                        $scope.cert.annanReferensBeskrivning = $scope.form.ovrigt.annanReferensBeskrivning;
                    } else {
                        $scope.cert.annanReferensBeskrivning = null;
                    }

                    // Fält 8a.
                    if ($scope.cert.nuvarandeArbete && $scope.form.nuvarandeArbetsuppgifter) {
                        $scope.cert.nuvarandeArbetsuppgifter = $scope.form.nuvarandeArbetsuppgifter;
                    } else {
                        $scope.cert.nuvarandeArbetsuppgifter = null;
                    }

                    // Fält 6a.
                    if ($scope.cert.rekommendationOvrigtCheck) {
                        $scope.cert.rekommendationOvrigt = $scope.form.rekommendationOvrigt;
                    } else {
                        $scope.cert.rekommendationOvrigt = null;
                    }

                    // Fält 7. Rehab radio conversions
                    switch ($scope.form.rehab) {
                    case 'JA':
                        $scope.cert.rehabilitering = 'rehabiliteringAktuell';
                        break;
                    case 'NEJ':
                        $scope.cert.rehabilitering = 'rehabiliteringEjAktuell';
                        break;
                    case 'GAREJ':
                        $scope.cert.rehabilitering = 'rehabiliteringGarInteAttBedoma';
                        break;
                    }

                }

                // Fält 8b.
                var nedsattMedList = ['nedsattMed25', 'nedsattMed50', 'nedsattMed75', 'nedsattMed100'];
                angular.forEach(nedsattMedList, function(nedsattMed) {

                    // convert dates to string from viewvalue (modelvalue is undefined for invalid dates from datepicker)
                    var from = dateUtils.convertDateToISOString($scope.certForm[nedsattMed + 'from'].$viewValue);
                    var tom = dateUtils.convertDateToISOString($scope.certForm[nedsattMed + 'tom'].$viewValue);
                    if (this[nedsattMed] === undefined && (utils.isValidString(from) || utils.isValidString(tom))) {

                        this[nedsattMed] = {};
                        if (utils.isValidString(from)) {
                            this[nedsattMed].from = from;
                        }
                        if (utils.isValidString(tom)) {
                            this[nedsattMed].tom = tom;
                        }
                    } else if (this[nedsattMed]) {
                        if (utils.isValidString(from)) {
                            this[nedsattMed].from = from;
                        } else {
                            this[nedsattMed].from = undefined;
                        }
                        if (utils.isValidString(tom)) {
                            this[nedsattMed].tom = tom;
                        } else {
                            this[nedsattMed].tom = undefined;
                        }

                        if(this[nedsattMed].from === undefined && this[nedsattMed].tom === undefined) {
                            delete this[nedsattMed];
                        }
                    }

                    if ($scope.field8b[nedsattMed].workState) {
                        this[nedsattMed + 'Beskrivning'] = $scope.form.ovrigt[nedsattMed + 'Beskrivning'];
                    } else {
                        this[nedsattMed + 'Beskrivning'] = null;
                    }
                }, $scope.cert);

                // Fält 10. Går ej att bedöma and update backend model when view changes.
                $scope.cert.arbetsformagaPrognosGarInteAttBedomaBeskrivning = null;
                switch ($scope.form.prognos) {
                case 'YES':
                    $scope.cert.prognosBedomning = 'arbetsformagaPrognosJa';
                    break;
                case 'PARTLY':
                    $scope.cert.prognosBedomning = 'arbetsformagaPrognosJaDelvis';
                    break;
                case 'NO':
                    $scope.cert.prognosBedomning = 'arbetsformagaPrognosNej';
                    break;
                case 'UNKNOWN':
                    $scope.cert.prognosBedomning = 'arbetsformagaPrognosGarInteAttBedoma';
                    $scope.cert.arbetsformagaPrognosGarInteAttBedomaBeskrivning =
                        $scope.form.ovrigt.arbetsformagaPrognosGarInteAttBedomaBeskrivning;
                    break;
                }

                // Fält 11. Ressätt till arbete
                $scope.cert.ressattTillArbeteAktuellt = false;
                $scope.cert.ressattTillArbeteEjAktuellt = false;
                if ($scope.form.ressattTillArbeteAktuellt === 'JA') {
                    $scope.cert.ressattTillArbeteAktuellt = true;
                    $scope.cert.ressattTillArbeteEjAktuellt = false;
                } else {
                    $scope.cert.ressattTillArbeteAktuellt = false;
                    $scope.cert.ressattTillArbeteEjAktuellt = true;
                }

            }

            function getLengthOrZero(value) {
                if (typeof (value) !== 'string') {
                    return 0;
                } else {
                    return value.length;
                }
            }

            /*************************************************************************
             * Ng-change and watches updating behaviour in form (try to get rid of these or at least make them consistent)
             *************************************************************************/

            /**
             * 4b. Toggle dates in the Based On date pickers when checkboxes are interacted with
             * @param modelName
             */
            $scope.toggleBaseradPaDate = function(modelName) {

                // Set todays date when a baserat pa field is checked
                if ($scope.basedOnState.check[modelName]) {
                    if (!$scope.cert[modelName] || $scope.cert[modelName] === '') {
                        $scope.cert[modelName] = $filter('date')($scope.today, 'yyyy-MM-dd');
                    }
                } else {

                    // Clear date if check is unchecked
                    $scope.cert[modelName] = '';
                }
            };

            /**
             * 4b. Update checkboxes when datepickers are interacted with
             * @param baserasPaType
             */
            $scope.onChangeBaserasPaDate = function(baserasPaType) {
                if (utils.isValidString($scope.certForm[baserasPaType + 'Date'].$viewValue)) {
                    $scope.basedOnState.check[baserasPaType] = true;
                } else {
                    $scope.basedOnState.check[baserasPaType] = false;
                }
            };

            /**
             * Truncate intyg field to length of inputLimit
             * @param field
             */
            $scope.limitFieldLength = function(field) {
                $scope.cert[field] = $scope.cert[field].substr(0, $scope.inputLimits[field]);
            };

            /**
             * Limit length of field dependent on field 2 in the external model
             * @param field
             */
            $scope.limitDiagnosBeskrivningField = function(field) {
                function limitDiagnoseLength(val) {
                    var totalLength = $scope.getTotalDiagnosBeskrivningLength();
                    if (totalLength > $scope.inputLimits.diagnosBeskrivning) {
                        // Remove characters over limit from current field
                        return val.substr(0, val.length - (totalLength - $scope.inputLimits.diagnosBeskrivning));
                    }
                    return val;
                }

                if ($scope.cert[field]) {
                    $scope.cert[field] = limitDiagnoseLength($scope.cert[field]);
                }
            };

            /**
             * Calculate total length of all fields ending up in diagnosBeskrivning in the external model
             * @returns {*}
             */
            $scope.getTotalDiagnosBeskrivningLength = function() {
                var totalLength = getLengthOrZero($scope.cert.diagnosBeskrivning) +
                        getLengthOrZero($scope.cert.diagnosKod2) +
                        getLengthOrZero($scope.cert.diagnosKod3) +
                        getLengthOrZero($scope.cert.diagnosBeskrivning1) +
                        getLengthOrZero($scope.cert.diagnosBeskrivning2) +
                        getLengthOrZero($scope.cert.diagnosBeskrivning3);
                return totalLength;
            };

            /**
             * Limit length of field dependent on field 13 in the external model
             * @param field
             */
            $scope.limitOtherField = function(field) {
                function limitOvrigtLength(val) {
                    var totalOvrigtLength = $scope.getTotalOvrigtLength();
                    if (totalOvrigtLength > $scope.inputLimits.ovrigt) {
                        // Remove characters over limit from current field
                        return val.substr(0, val.length - (totalOvrigtLength - $scope.inputLimits.ovrigt));
                    }
                    return val;
                }

                if ($scope.form.ovrigt[field]) {
                    $scope.form.ovrigt[field] = limitOvrigtLength($scope.form.ovrigt[field]);
                } else if ($scope.cert[field]) {
                    $scope.cert[field] = limitOvrigtLength($scope.cert[field]);
                }
            };

            /**
             * Calculate total length of all fields ending up in Övrigt in the external model
             * @returns {*}
             */
            $scope.getTotalOvrigtLength = function() {

                var totalOvrigtLength = getLengthOrZero($scope.cert.kommentar);

                if ($scope.form.ovrigt !== undefined) {
                    totalOvrigtLength += getLengthOrZero($scope.form.ovrigt.annanReferensBeskrivning) +
                    getLengthOrZero($scope.form.ovrigt.nedsattMed25Beskrivning) +
                    getLengthOrZero($scope.form.ovrigt.nedsattMed50Beskrivning) +
                    getLengthOrZero($scope.form.ovrigt.nedsattMed75Beskrivning) +
                    getLengthOrZero($scope.form.ovrigt.arbetsformagaPrognosGarInteAttBedomaBeskrivning);
                }

                return totalOvrigtLength;
            };

            $scope.$watch('cert.avstangningSmittskydd', function(newVal) {

                // Remove defaults not applicable when smittskydd is active
                if (newVal === true) {
                    $scope.form.prognos = undefined;
                    $scope.form.rehab = undefined;

                    // turn off resor till och from jobbet
                    // Fält 11. Ressätt till arbete
                    $scope.cert.ressattTillArbeteAktuellt = false;
                    $scope.form.ressattTillArbeteAktuellt = undefined;

                }
            });

            /**
             *  Remove choices related to diagnoskoder when the choice changes to make sure
             */
            $scope.onChangeKodverk = function() {
                $scope.cert.diagnosKod = '';
                $scope.cert.diagnosBeskrivning1 = '';
                $scope.cert.diagnosKod2 = '';
                $scope.cert.diagnosBeskrivning2 = '';
                $scope.cert.diagnosKod3 = '';
                $scope.cert.diagnosBeskrivning3 = '';
            };

            /**
             *
             * @param codeSystem
             * @param val
             * @returns {*}
             */
            $scope.searchDiagnoseByDescription = function(codeSystem, val) {
                return diagnosService.searchByDescription(codeSystem, val)
                    .then(function(response) {
                        if (response && response.data && response.data.resultat === 'OK') {
                            return response.data.diagnoser.map(function(item) {
                                return {
                                    value: item.kod,
                                    beskrivning: item.beskrivning,
                                    label: item.kod + ' | ' + item.beskrivning
                                };
                            });
                        }
                        else {
                            return [];
                        }
                    }, function(response) {
                        $log.debug('Error searching diagnose code');
                        $log.debug(response);
                        return [];
                    });
            };
            $scope.getDiagnoseCodes = function(codeSystem, val) {
                return diagnosService.searchByCode(codeSystem, val)
                    .then(function(response) {
                        if (response && response.data && response.data.resultat === 'OK') {
                            return response.data.diagnoser.map(function(item) {
                                return {
                                    value: item.kod,
                                    beskrivning: item.beskrivning,
                                    label: item.kod + ' | ' + item.beskrivning
                                };
                            });
                        }
                        else {
                            return [];
                        }
                    }, function(response) {
                        $log.debug('Error searching diagnose code');
                        $log.debug(response);
                        return [];
                    });
            };

            /****************************************************************************
             * Exposed interaction functions to view
             ****************************************************************************/

            /**
             * Toggle header part ('Dölj meny'-knapp)
             */
            $scope.toggleHeader = function() {
                $scope.widgetState.collapsedHeader = !$scope.widgetState.collapsedHeader;
            };

            /**
             * Toggle 'Visa vad som behöver kompletteras'
             */
            $scope.toggleShowComplete = function() {
                $scope.widgetState.showComplete = !$scope.widgetState.showComplete;
                if ($scope.widgetState.showComplete) {
                    $scope.save();
                    var old = $location.hash();
                    $location.hash('top');
                    $anchorScroll();
                    // reset to old to keep any additional routing logic from kicking in
                    $location.hash(old);
                }
            };

            /**
             * Action to save the certificate draft to the server.
             */
            $scope.save = function(autoSave) {
                $scope.hasSavedThisSession = true;
                convertFormToCert($scope);
                return ManageCertView.save($scope, $scope.certMeta.intygType, autoSave);
            };

            /**
             * Action to discard the certificate draft and return to WebCert again.
             */
            $scope.discard = function() {
                ManageCertView.discard($scope, $scope.certMeta.intygType);
            };

            /**
             * Action to sign the certificate draft and return to Webcert again.
             */
            $scope.sign = function() {
                ManageCertView.signera($scope, $scope.certMeta.intygType);
            };

            /**
             * Print draft
             */
            $scope.print = function() {
                ManageCertView.printDraft( $scope.cert.id, $scope.certMeta.intygType );
            };

            /**
             * User selects a diagnose code
             */
            $scope.onDiagnoseCode1Select = function($item) {
                $scope.cert.diagnosBeskrivning1 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning1');
            };
            $scope.onDiagnoseCode2Select = function($item) {
                $scope.cert.diagnosBeskrivning2 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning2');
            };
            $scope.onDiagnoseCode3Select = function($item) {
                $scope.cert.diagnosBeskrivning3 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning3');
            };

            /**
             * User selects a diagnose description
             */
            $scope.onDiagnoseDescription1Select = function($item) {
                $scope.cert.diagnosKod = $item.value;
                $scope.cert.diagnosBeskrivning1 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning1');
            };
            $scope.onDiagnoseDescription2Select = function($item) {
                $scope.cert.diagnosKod2 = $item.value;
                $scope.cert.diagnosBeskrivning2 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning2');
            };
            $scope.onDiagnoseDescription3Select = function($item) {
                $scope.cert.diagnosKod3 = $item.value;
                $scope.cert.diagnosBeskrivning3 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning3');
            };

            /**
             * Handle vidarebefordra dialog
             */
            $scope.openMailDialog = function() {
                intygNotifyService.forwardIntyg($scope.certMeta, $scope.widgetState);
            };

            $scope.onVidarebefordradChange = function() {
                intygNotifyService.onForwardedChange($scope.certMeta, $scope.widgetState);
            };

            /**************************************************************************
             * Load certificate and setup form / Constructor ...
             **************************************************************************/

                // Get the certificate draft from the server.
            ManageCertView.load($scope, $scope.certMeta.intygType, function(cert) {
                // check that the certs status is not signed
                if($scope.isSigned){
                    // just change straight to the intyg
                    $location.url('/intyg/' + $scope.certMeta.intygType + '/' + $scope.certMeta.intygId);
                }

                // Decorate intygspecific default data
                $scope.cert = cert;

                convertCertToForm($scope);
                registerDateParsers($scope);
                _dateRangeGroups.validateDatesWithCert($scope.cert);

                $timeout(function() {
                    wcFocus('firstInput');
                    $rootScope.$broadcast('intyg.loaded', $scope.cert);
                }, 10);
            });

            $rootScope.$on('intyg.deleted', function() {
                $scope.widgetState.deleted = true;
                $scope.widgetState.activeErrorMessageKey = 'error';
                $scope.cert = undefined;
            });

        }]);