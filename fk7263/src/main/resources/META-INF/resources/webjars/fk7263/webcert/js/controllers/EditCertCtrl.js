angular.module('fk7263').controller('fk7263.EditCertCtrl',
    [ '$anchorScroll', '$filter', '$location', '$scope', '$log', 'common.CertificateService',
        'common.ManageCertView', 'common.User',
        function($anchorScroll, $filter, $location, $scope, $log, CertificateService, ManageCertView, User) {
            'use strict';

            /**********************************************************************************
             * Default state
             **********************************************************************************/

                // Page states
            $scope.user = User;
            $scope.today = new Date();
            $scope.today.setHours(0, 0, 0, 0); // reset time to increase comparison accuracy (using new Date() also sets time)
            $scope.widgetState = {
                doneLoading: false,
                activeErrorMessageKey: null,
                hasError: false,
                showComplete: false,
                collapsedHeader: false
            };

            // Intyg state
            $scope.cert = {};
            $scope.hasSavedThisSession = false;
            $scope.messages = [];
            $scope.isComplete = false;
            $scope.isSigned = false;

            // Keeps track of in-form interactions which is converted to internal model on save,
            // and converted from internal model on load
            $scope.form = {
                arbete: true,
                prognos: 'YES',
                rehab: 'NEJ',
                ressattTillArbeteAktuellt: false,
                ovrigt: {
                    'annanReferensBeskrivning': null,
                    'nedsattMed25Beskrivning': null,
                    'nedsattMed50Beskrivning': null,
                    'nedsattMed75Beskrivning': null,
                    'arbetsformagaPrognosGarInteAttBedomaBeskrivning': null
                }
            };

            // Fält 2. Diagnose handling Typeahead is implemented in a future story
            $scope.diagnoseCodes = [
                /*                {
                 value: 'J44.0',
                 label: 'J44.0 Kroniskt obstruktiv lungsjukdom med akut nedre luftvägsinfektion'
                 },
                 {
                 value: 'K92.2',
                 label: 'K92.2 Gastrointestinal blödning, ospecificerad'
                 }*/
            ];

            $scope.diagnoses = [
                /*                {
                 value: 'Kroniskt obstruktiv lungsjukdom med akut nedre luftvägsinfektion',
                 label: 'J44.0 Kroniskt obstruktiv lungsjukdom med akut nedre luftvägsinfektion'
                 },
                 {
                 value: 'Gastrointestinal blödning, ospecificerad',
                 label: 'K92.2 Gastrointestinal blödning, ospecificerad'
                 }*/
            ];

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
            $scope.datesOutOfRange = false;
            $scope.datesPeriodTooLong = false;
            $scope.totalCertDays = false;

            // 8b. Arbetsförmåga checks
            $scope.workState = {
                nedsattMed25: false,
                nedsattMed50: false,
                nedsattMed75: false,
                nedsattMed100: false
            };

            // 8b. Arbetsförmåga date field invalid states. Keeps track of which nedsatt date fields that are invalid from onChange checks
            $scope.nedsattInvalid = {
                nedsattMed25from: false,
                nedsattMed25tom: false,
                nedsattMed50from: false,
                nedsattMed50tom: false,
                nedsattMed75from: false,
                nedsattMed75tom: false,
                nedsattMed100from: false,
                nedsattMed100tom: false
            };

            // Text input limits for different fields
            $scope.inputLimits = {
                diagnosBeskrivning: 100,
                sjukdomsforlopp: 270,
                funktionsnedsattning: 560,
                aktivitetsbegransning: 1100,
                nuvarandeArbetsuppgifter: 120,
                arbetsformagaPrognos: 600,
                atgardInomSjukvarden: 66,
                annanAtgard: 66,
                ovrigt: 420
                // 420 = combined field 13 (and dependencies) limit
            };

            /***************************************************************************
             * Private controller support functions
             ***************************************************************************/

            /**
             * Does supplied date look like an iso date XXXX-XX-XX (not a complete validation)?
             * @param date
             * @returns {*}
             */
            function isDate(date) {
                return moment(date).isValid();
            }

            function toMoment(date) {
                if (date) {
                    return moment(date);
                } else {
                    return null;
                }
            }


            /**
             * 8b: find earliest and latest dates (as moment objects) for arbetsförmåga
             * @returns {{minMoment: null, maxMoment: null}}
             */
            function findStartEndMoments() {
                var moments = {
                    minMoment: null,
                    maxMoment: null
                };
                var startMoments = [];
                var endMoments = [];

                if ($scope.cert.nedsattMed25) {
                    if ($scope.cert.nedsattMed25.from) {
                        startMoments.push(toMoment($scope.cert.nedsattMed25.from));
                    }
                    if ($scope.cert.nedsattMed25.tom) {
                        endMoments.push(toMoment($scope.cert.nedsattMed25.tom));
                    }
                }
                if ($scope.cert.nedsattMed50) {
                    if ($scope.cert.nedsattMed50.from) {
                        startMoments.push(toMoment($scope.cert.nedsattMed50.from));
                    }
                    if ($scope.cert.nedsattMed50.tom) {
                        endMoments.push(toMoment($scope.cert.nedsattMed50.tom));
                    }
                }
                if ($scope.cert.nedsattMed75) {
                    if ($scope.cert.nedsattMed75.from) {
                        startMoments.push(toMoment($scope.cert.nedsattMed75.from));
                    }
                    if ($scope.cert.nedsattMed75.tom) {
                        endMoments.push(toMoment($scope.cert.nedsattMed75.tom));
                    }
                }
                if ($scope.cert.nedsattMed100) {
                    if ($scope.cert.nedsattMed100.from) {
                        startMoments.push(toMoment($scope.cert.nedsattMed100.from));
                    }
                    if ($scope.cert.nedsattMed100.tom) {
                        endMoments.push(toMoment($scope.cert.nedsattMed100.tom));
                    }
                }

                if (startMoments.length > 0) {
                    moments.minMoment = moment.min(startMoments);
                }
                if (endMoments.length > 0) {
                    moments.maxMoment = moment.max(endMoments);
                }

                return moments;
            }

            /**
             * 8b: Called when checks or dates for Arbetsförmåga are changed. Update dependency controls here
             */
            function onArbetsformagaDatesUpdated() {
                $scope.updateTotalCertDays();

                var rangeMoments = findStartEndMoments();
                checkArbetsformagaDatesRange(rangeMoments.minMoment);

                var periodMoments = findStartEndMoments();
                checkArbetsformagaDatesPeriodLength(periodMoments.minMoment, periodMoments.maxMoment);
            }

            /**
             * 8b: Check that the earliest startdate in arbetsförmåga is no more than a week before today and no more than 6 months in the future
             * @type {boolean}
             */
            function checkArbetsformagaDatesRange(startMoment) {
                if (!startMoment) {
                    $scope.datesOutOfRange = false;
                    return;
                }

                var now = moment();
                var olderThanAWeek = startMoment.isBefore(now.subtract('days', 7));
                var moreThanSixMonthsInFuture = startMoment.isAfter(now.add('months', 6));
                $scope.datesOutOfRange = (olderThanAWeek || moreThanSixMonthsInFuture);
            }

            /**
             * 8b: Check that the period between the earliest startdate and the latest end date is no more than 6 months in the future
             * @type {boolean}
             */
            function checkArbetsformagaDatesPeriodLength(startMoment, endMoment) {
                if (!startMoment || !endMoment) {
                    $scope.datesPeriodTooLong = false;
                    return;
                }

                $scope.datesPeriodTooLong = (Math.abs(startMoment.diff(endMoment, 'months')) >= 6);
            }

            /**
             * Convert internal model to form temporary data bindings
             * @param $scope
             */
            function convertCertToForm($scope) {

                // Fält 4b. AnnanReferensBeskrivning
                $scope.basedOnState.check.undersokningAvPatienten = $scope.cert.undersokningAvPatienten !== undefined;
                $scope.basedOnState.check.telefonkontaktMedPatienten =
                    $scope.cert.telefonkontaktMedPatienten !== undefined;
                $scope.basedOnState.check.journaluppgifter = $scope.cert.journaluppgifter !== undefined;
                if ($scope.cert.annanReferensBeskrivning !== undefined) {
                    $scope.form.ovrigt.annanReferensBeskrivning = $scope.cert.annanReferensBeskrivning;
                    $scope.basedOnState.check.annanReferens = true;
                } else {
                    $scope.basedOnState.check.annanReferens = false;
                }

                // Fält 8b. nedsattMedXXBeskrivning
                if ($scope.cert.nedsattMed25) {
                    $scope.workState.nedsattMed25 = true;
                }
                if ($scope.cert.nedsattMed50) {
                    $scope.workState.nedsattMed50 = true;
                }
                if ($scope.cert.nedsattMed75) {
                    $scope.workState.nedsattMed75 = true;
                }
                if ($scope.cert.nedsattMed100) {
                    $scope.workState.nedsattMed100 = true;
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
                if ($scope.cert.arbetsformataPrognosJa) {
                    $scope.form.prognos = 'YES';
                } else if ($scope.cert.arbetsformataPrognosJaDelvis) {
                    $scope.form.prognos = 'PARTLY';
                } else if ($scope.cert.arbetsformataPrognosNej) {
                    $scope.form.prognos = 'NO';
                } else if ($scope.cert.arbetsformataPrognosGarInteAttBedoma) {
                    $scope.form.prognos = 'UNKNOWN';
                    $scope.form.ovrigt.arbetsformagaPrognosGarInteAttBedomaBeskrivning =
                        $scope.cert.arbetsformagaPrognosGarInteAttBedomaBeskrivning;
                }

                // Fält 7. Rehab radio conversions
                if ($scope.cert.rehabiliteringAktuell) {
                    $scope.form.rehab = 'JA';
                } else if ($scope.cert.rehabiliteringEjAktuell) {
                    $scope.form.rehab = 'NEJ';
                } else if ($scope.cert.rehabiliteringGarInteAttBedoma) {
                    $scope.form.rehab = 'GAREJ';
                }

                // Fält 6a.
                if ($scope.cert.rekommendationOvrigt !== undefined) {
                    $scope.form.rekommendationOvrigt = $scope.cert.rekommendationOvrigt;
                }

                // Fält 11. Ressätt till arbete
                $scope.form.ressattTillArbeteAktuellt = $scope.cert.ressattTillArbeteAktuellt;
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
                    $scope.cert.ressattTillArbeteAktuellt = false;
                    $scope.cert.ressattTillArbeteEjAktuellt = false;
                    $scope.cert.rekommendationKontaktArbetsformedlingen = false;
                    $scope.cert.rekommendationKontaktForetagshalsovarden = false;
                    $scope.form.rehab = 'NEJ';
                    $scope.cert.rekommendationOvrigtCheck = false;
                    $scope.cert.rekommendationOvrigt = undefined;

                } else {

                    // Fält 8a.
                    if ($scope.cert.nuvarandeArbete && $scope.form.nuvarandeArbetsuppgifter) {
                        $scope.cert.nuvarandeArbetsuppgifter = $scope.form.nuvarandeArbetsuppgifter;
                    } else {
                        $scope.cert.nuvarandeArbetsuppgifter = null;
                    }
                }

                // Fält 4b. AnnanReferensBeskrivning
                if ($scope.basedOnState.check.annanReferens) {
                    $scope.cert.annanReferensBeskrivning = $scope.form.ovrigt.annanReferensBeskrivning;
                } else {
                    $scope.cert.annanReferensBeskrivning = null;
                }

                // Fält 6a.
                if ($scope.cert.rekommendationOvrigtCheck) {
                    $scope.cert.rekommendationOvrigt = $scope.form.rekommendationOvrigt;
                } else {
                    $scope.cert.rekommendationOvrigt = null;
                }

                // Fält 8b.
                if ($scope.workState.nedsattMed25) {
                    $scope.cert.nedsattMed25Beskrivning = $scope.form.ovrigt.nedsattMed25Beskrivning;
                } else {
                    $scope.cert.nedsattMed25Beskrivning = null;
                }
                if ($scope.workState.nedsattMed50) {
                    $scope.cert.nedsattMed50Beskrivning = $scope.form.ovrigt.nedsattMed50Beskrivning;
                } else {
                    $scope.cert.nedsattMed50Beskrivning = null;
                }
                if ($scope.workState.nedsattMed75) {
                    $scope.cert.nedsattMed75Beskrivning = $scope.form.ovrigt.nedsattMed75Beskrivning;
                } else {
                    $scope.cert.nedsattMed75Beskrivning = null;
                }

                // Fält 10. Går ej att bedöma and update backend model when view changes.
                $scope.cert.arbetsformataPrognosJa = false;
                $scope.cert.arbetsformataPrognosJaDelvis = false;
                $scope.cert.arbetsformataPrognosNej = false;
                $scope.cert.arbetsformataPrognosGarInteAttBedoma = false;
                $scope.cert.arbetsformagaPrognosGarInteAttBedomaBeskrivning = null;
                switch ($scope.form.prognos) {
                case 'YES':
                    $scope.cert.arbetsformataPrognosJa = true;
                    break;
                case 'PARTLY':
                    $scope.cert.arbetsformataPrognosJaDelvis = true;
                    break;
                case 'NO':
                    $scope.cert.arbetsformataPrognosNej = true;
                    break;
                case 'UNKNOWN':
                    $scope.cert.arbetsformataPrognosGarInteAttBedoma = true;
                    $scope.cert.arbetsformagaPrognosGarInteAttBedomaBeskrivning =
                        $scope.form.ovrigt.arbetsformagaPrognosGarInteAttBedomaBeskrivning;
                    break;
                }

                // Fält 7. Rehab radio conversions
                $scope.cert.rehabiliteringAktuell = false;
                $scope.cert.rehabiliteringEjAktuell = false;
                $scope.cert.rehabiliteringGarInteAttBedoma = false;

                switch ($scope.form.rehab) {
                case 'JA':
                    $scope.cert.rehabiliteringAktuell = true;
                    break;
                case 'NEJ':
                    $scope.cert.rehabiliteringEjAktuell = true;
                    break;
                case 'GAREJ':
                    $scope.cert.rehabiliteringGarInteAttBedoma = true;
                    break;
                }

                // Fält 11. Ressätt till arbete
                if ($scope.form.ressattTillArbeteAktuellt) {
                    $scope.cert.ressattTillArbeteAktuellt = true;
                    $scope.cert.ressattTillArbeteEjAktuellt = false;
                } else {
                    $scope.cert.ressattTillArbeteAktuellt = false;
                    $scope.cert.ressattTillArbeteEjAktuellt = true;
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
                if ($scope.cert[baserasPaType] && $scope.cert[baserasPaType] !== '') {
                    $scope.basedOnState.check[baserasPaType] = true;
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
                function getLengthOrZero(value) {
                    if (!value) {
                        return 0;
                    } else {
                        return value.length;
                    }
                }

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

            /**
             * Calculate total days between the earliest and the latest dates supplied in the 8b controls
             * @type {boolean}
             */
            $scope.updateTotalCertDays = function() {
                var moments = findStartEndMoments();
                if (!moments.minMoment || !moments.maxMoment) {
                    // return if there's no valid range span yet
                    $scope.totalCertDays = false;
                    return $scope.totalCertDays;
                }

                $scope.totalCertDays = moments.maxMoment.diff(moments.minMoment, 'days') + 1;
            };

            /**
             * Update arbetsformaga dates when checkbox is updated
             * @param nedsattModelName
             */
            $scope.onChangeWorkStateCheck = function(nedsattModelName) {
                if ($scope.cert !== undefined) {
                    if ($scope.workState[nedsattModelName]) {

                        // Set suggested dates
                        var workstate = $scope.cert[nedsattModelName];
                        if (!workstate) {
                            workstate = $scope.cert[nedsattModelName] = {};
                        }

                        // Set from date
                        var moments = findStartEndMoments();
                        if (!workstate.from || !isDate(workstate.from)) {

                            // find highest max date
                            if (!moments.maxMoment) {
                                // if no max moment is available, use today
                                workstate.from = $filter('date')($scope.today, 'yyyy-MM-dd');
                            } else {
                                workstate.from = moments.maxMoment.add('days', 1).format('YYYY-MM-DD');
                            }
                        }

                        // Set tom date
                        if (!workstate.tom || !isDate(workstate.tom)) {
                            workstate.tom = toMoment(workstate.from).add('days', 7).format('YYYY-MM-DD');
                        }
                    } else {

                        // Remove dates
                        delete $scope.cert[nedsattModelName];
                    }

                    onArbetsformagaDatesUpdated();
                }
            };

            /**
             * Set checkbox and non-selected date for arbetsformaga % when a date is changed
             * @param nedsattModelName
             * @param fromTom
             */
            $scope.onChangeNedsattMed = function(nedsattModelName, fromTom) {
                var i;

                // Bail out if model hasn't been loaded yet
                var nedsattModel = $scope.cert[nedsattModelName];
                if (nedsattModel === undefined) {
                    return;
                }

                var dateField = $scope.cert[nedsattModelName][fromTom];
                if (dateField === undefined) {
                    return;
                }

                // if a date has been set
                if (dateField && dateField !== '') {

                    // Check checkbox
                    $scope.workState[nedsattModelName] = true;

                    // If non-changed date for same % is still invalid, set that as well
                    if (fromTom === 'from' && !isDate(nedsattModel.tom)) {
                        nedsattModel.tom = nedsattModel.from;
                    } else if (fromTom === 'tom' && !isDate(nedsattModel.from)) {
                        nedsattModel.from = nedsattModel.tom;
                    }

                    // Set invalid if from dates are after tom dates
                    var groups = ['nedsattMed25', 'nedsattMed50', 'nedsattMed75', 'nedsattMed100'];
                    for (i = 0; i < groups.length; i++) {
                        if ($scope.cert[groups[i]] && $scope.cert[groups[i]].from && $scope.cert[groups[i]].tom) {
                            if (toMoment($scope.cert[groups[i]].from).isAfter(toMoment($scope.cert[groups[i]].tom))) {
                                $scope.nedsattInvalid[groups[i] + 'from'] = true;
                                $scope.nedsattInvalid[groups[i] + 'tom'] = true;
                            } else {
                                $scope.nedsattInvalid[groups[i] + 'from'] = false;
                                $scope.nedsattInvalid[groups[i] + 'tom'] = false;
                            }
                        } else {
                            $scope.nedsattInvalid[groups[i] + 'from'] = false;
                            $scope.nedsattInvalid[groups[i] + 'tom'] = false;
                        }
                    }

                    // Set invalid if date periods overlap
                    for (i = 0; i < groups.length; i++) {
                        // for every nedsatt group
                        var nedsatt = $scope.cert[groups[i]];

                        // where group is used, set and not already marked as invalid
                        if (nedsatt && nedsatt.from && nedsatt.tom &&
                            !($scope.nedsattInvalid[groups[i] + 'from'] && $scope.nedsattInvalid[groups[i] + 'tom'])) {

                            // check with all other period groups after nedsatt period if periods overlap
                            for (var j = i + 1; j < groups.length; j++) {
                                var nedsattCompare = $scope.cert[groups[j]];

                                // dont check against unused dates and already invalid dates
                                if (nedsattCompare && nedsattCompare.from && nedsattCompare.tom &&
                                    !($scope.nedsattInvalid[groups[j] + 'from'] &&
                                        $scope.nedsattInvalid[groups[j] + 'tom'])) {

                                    if (toMoment(nedsatt.from).isSame(nedsattCompare.from)) {
                                        $scope.nedsattInvalid[groups[i] + 'from'] = true;
                                        $scope.nedsattInvalid[groups[j] + 'from'] = true;
                                    }
                                    if (toMoment(nedsatt.tom).isSame(nedsattCompare.from)) {
                                        $scope.nedsattInvalid[groups[i] + 'tom'] = true;
                                        $scope.nedsattInvalid[groups[j] + 'from'] = true;
                                    }
                                    if (toMoment(nedsatt.from).isSame(nedsattCompare.tom)) {
                                        $scope.nedsattInvalid[groups[i] + 'from'] = true;
                                        $scope.nedsattInvalid[groups[j] + 'tom'] = true;
                                    }
                                    if (toMoment(nedsatt.tom).isSame(nedsattCompare.tom)) {
                                        $scope.nedsattInvalid[groups[i] + 'tom'] = true;
                                        $scope.nedsattInvalid[groups[j] + 'tom'] = true;
                                    }

                                    if ((toMoment(nedsatt.tom).isAfter(nedsattCompare.from) &&
                                        toMoment(nedsatt.from).isBefore(nedsattCompare.from)) || // first group overlaps in front
                                        (toMoment(nedsatt.from).isBefore(nedsattCompare.tom) &&
                                            toMoment(nedsatt.tom).isAfter(nedsattCompare.tom)) || // first group overlaps behind
                                        (toMoment(nedsatt.from).isBefore(nedsattCompare.from) &&
                                            toMoment(nedsatt.tom).isAfter(nedsattCompare.tom)) || // first group wraps second group
                                        (toMoment(nedsatt.from).isAfter(nedsattCompare.from) &&
                                            toMoment(nedsatt.tom).isBefore(nedsattCompare.tom))) { // second group wraps first group
                                        $scope.nedsattInvalid[groups[i] + 'from'] = true;
                                        $scope.nedsattInvalid[groups[i] + 'tom'] = true;
                                        $scope.nedsattInvalid[groups[j] + 'from'] = true;
                                        $scope.nedsattInvalid[groups[j] + 'tom'] = true;
                                    }
                                }
                            }
                        }
                    }
                }

                onArbetsformagaDatesUpdated();
            };

            /****************************************************************************
             * Exposed interaction functions to view
             ****************************************************************************/

            /**
             * Toggle header part ("Dölj meny"-knapp)
             */
            $scope.toggleHeader = function() {
                $scope.widgetState.collapsedHeader = !$scope.widgetState.collapsedHeader;
            };

            /**
             * Toggle "Visa vad som behöver kompletteras
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
            $scope.save = function() {
                $scope.hasSavedThisSession = true;
                convertFormToCert($scope);
                ManageCertView.save($scope);
            };

            /**
             * Action to discard the certificate draft and return to WebCert again.
             */
            $scope.discard = function() {
                ManageCertView.discard($scope);
            };

            /**
             * Action to sign the certificate draft and return to Webcert again.
             */
            $scope.sign = function() {
                ManageCertView.signera($scope, 'fk7263');
            };

            /**
             * Print draft
             */
            $scope.print = function() {
                ManageCertView.printDraft($scope.cert.id);
            };

            /**************************************************************************
             * Load certificate and setup form
             **************************************************************************/

                // Get the certificate draft from the server.
            ManageCertView.load($scope, function(cert) {
                // Decorate intygspecific default data
                $scope.cert = cert;
                convertCertToForm($scope);
            });

        }]);