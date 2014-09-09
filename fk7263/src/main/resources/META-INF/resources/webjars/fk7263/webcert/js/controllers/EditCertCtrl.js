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
                identity: {
                    'ID-kort': 'ID_KORT',
                    'Företagskort eller tjänstekort': 'FORETAG_ELLER_TJANSTEKORT',
                    'Körkort': 'KORKORT',
                    'Personlig kännedom': 'PERS_KANNEDOM',
                    'Försäkran enligt 18 kap. 4§': 'FORSAKRAN_KAP18',
                    'Pass': 'PASS'
                },
                korkortd: false,
                behorighet: true,
                arbete: true,
                prognosis: 'YES',
                rehab: 'NEJ',
                rekommendationOvrigtCheck: false,
                ovrigt: {
                    'annanReferensBeskrivning' : null,
                    'nedsattMed25Beskrivning' : null,
                    'nedsattMed50Beskrivning' : null,
                    'nedsattMed75Beskrivning' : null,
                    'arbetsformagaPrognosGarInteAttBedomaBeskrivning' : null,
                    'rehabNow' : 'NOW',
                    'rehabWhen' : null
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
            var ISODATE_REGEXP = /^\d{4}-\d{2}-\d{2}$/;
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
                nedsattMed25from: false, nedsattMed25tom: false,
                nedsattMed50from: false, nedsattMed50tom: false,
                nedsattMed75from: false, nedsattMed75tom: false,
                nedsattMed100from: false, nedsattMed100tom: false
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
             * Set a default value to listed properties on an object
             * @param list
             * @param propertyNames
             * @param defaultValue
             */
            function setPropertyDefaults(list, propertyNames, defaultValue) {
                for (var i = 0; i < propertyNames.length; i++) {
                    if (list[propertyNames[i]] === undefined) {
                        list[propertyNames[i]] = defaultValue;
                    }
                }
            }

            /**
             * 8b: Called when checks or dates for Arbetsförmåga are changed. Update dependency controls here
             */
            function onArbetsformagaDatesUpdated() {
                $scope.updateTotalCertDays();

                var rangeDates = findStartEndDates();
                checkArbetsformagaDatesRange(rangeDates.minDate);

                var periodDates = findStartEndDates();
                checkArbetsformagaDatesPeriodLength(periodDates.minDate, periodDates.maxDate);
            }

            /**
             * 8b: Check that the earliest startdate in arbetsförmåga is no more than a week before today and no more than 6 months in the future
             * @type {boolean}
             */
            function checkArbetsformagaDatesRange(startDate) {
                if (!startDate) {
                    $scope.datesOutOfRange = false;
                    return;
                }

                var olderThanAWeek = moment(startDate).isBefore(moment().subtract('days', 7));
                var moreThanSixMonthsInFuture = moment(startDate).isAfter(moment().add('months', 6));
                $scope.datesOutOfRange = (olderThanAWeek || moreThanSixMonthsInFuture);
            }

            /**
             * 8b: Check that the period between the earliest startdate and the latest end date is no more than 6 months in the future
             * @type {boolean}
             */
            function checkArbetsformagaDatesPeriodLength(startDate, endDate) {
                if (!startDate || !endDate) {
                    $scope.datesPeriodTooLong = false;
                    return;
                }

                $scope.datesPeriodTooLong = (Math.abs(moment(startDate).diff(endDate, 'months')) >= 6);
            }

            /**
             * 8b: find earliest and latest dates for arbetsförmåga
             * @returns {{minDate: null, maxDate: null}}
             */
            function findStartEndDates() {
                var dates = {
                    minDate: null,
                    maxDate: null
                };
                var startDates = [];
                var endDates = [];

                if ($scope.cert.nedsattMed25) {
                    startDates.push($scope.cert.nedsattMed25.from);
                    endDates.push($scope.cert.nedsattMed25.tom);
                }
                if ($scope.cert.nedsattMed50) {
                    startDates.push($scope.cert.nedsattMed50.from);
                    endDates.push($scope.cert.nedsattMed50.tom);
                }
                if ($scope.cert.nedsattMed75) {
                    startDates.push($scope.cert.nedsattMed75.from);
                    endDates.push($scope.cert.nedsattMed75.tom);
                }
                if ($scope.cert.nedsattMed100) {
                    startDates.push($scope.cert.nedsattMed100.from);
                    endDates.push($scope.cert.nedsattMed100.tom);
                }

                dates.minDate = getMinMaxDate('min', startDates);
                dates.maxDate = getMinMaxDate('max', endDates);
                return dates;
            }

            /**
             * Does supplied date look like an iso date XXXX-XX-XX (not a complete validation)?
             * @param date
             * @returns {*}
             */
            function isDate(date) {
                var validDateFormat = ISODATE_REGEXP.test(date);
                return validDateFormat;
            }

            /**
             * Get earliest or latest date in a list of dates
             * @param comparisonType
             * @param dates
             * @returns {boolean}
             */
            function getMinMaxDate(comparisonType, dates) {

                var compareDate = false;
                for (var i = 0; i < dates.length; i++) {
                    if (isDate(dates[i])) {
                        if (!compareDate || // no valid date found yet
                            (comparisonType === 'min' && dates[i] < compareDate) || // looking for min date
                            (comparisonType === 'max' && dates[i] > compareDate)) { // looking for max date
                            compareDate = dates[i];
                        }
                    }
                }

                // if no valid dates, compareDate is still false, otherwise contains the lowest/highest date
                // sent depending on comparisonType
                return compareDate;
            }

            /**
             * Convert a date into time ms since 1970-01-01
             * @param date
             * @returns {number}
             */
            function convertDateToTime(date) {
                var splitDate = date.split('-');
                var time = (new Date(splitDate[0], splitDate[1], splitDate[2])).getTime();
                return time;
            }

            /**
             * Convert internal model to form temporary data bindings
             * @param $scope
             */
            function convertCertToForm($scope) {

                // Fält 4b. AnnanReferensBeskrivning
                if ($scope.cert.undersokningAvPatienten !== undefined) {
                    $scope.basedOnState.check.undersokningAvPatienten = true;
                } else {
                    $scope.basedOnState.check.undersokningAvPatienten = false;
                }
                if ($scope.cert.telefonkontaktMedPatienten !== undefined) {
                    $scope.basedOnState.check.telefonkontaktMedPatienten = true;
                } else {
                    $scope.basedOnState.check.telefonkontaktMedPatienten = false;
                }
                if ($scope.cert.journaluppgifter !== undefined) {
                    $scope.basedOnState.check.journaluppgifter = true;
                } else {
                    $scope.basedOnState.check.journaluppgifter = false;
                }
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
                } else if ($scope.cert.nedsattMed50Beskrivning !== undefined) {
                    $scope.form.ovrigt.cert.nedsattMed50Beskrivning = $scope.cert.nedsattMed50Beskrivning;
                } else if ($scope.cert.nedsattMed75Beskrivning !== undefined) {
                    $scope.form.ovrigt.cert.nedsattMed75Beskrivning = $scope.cert.nedsattMed75Beskrivning;
                }

                // Fält 8a. Set nuvarande arbete default value
                if ($scope.cert.nuvarandeArbetsuppgifter !== undefined ||
                    (!$scope.cert.arbetsloshet && !$scope.cert.foraldrarledighet)) {
                    $scope.form.arbete = true;
                } else {
                    $scope.form.arbete = false;
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
                    $scope.form.ovrigt.arbetsformagaPrognosGarInteAttBedomaBeskrivning = $scope.cert.arbetsformagaPrognosGarInteAttBedomaBeskrivning;
                }

                // Fält 11. Rehab radio conversions
                if ($scope.cert.rehabiliteringAktuell) {
                    $scope.form.rehab = 'JA';
                } else if ($scope.cert.rehabiliteringEjAktuell) {
                    $scope.form.rehab = 'NEJ';
                } else if ($scope.cert.rehabiliteringGarInteAttBedoma) {
                    $scope.form.rehab = 'GAREJ';
                }

                // TODO? omgående/senare form.ovrigt.rehabNow
                // datum senare form.ovrigt.rehabWhen
            }

            /**
             * Convert form temporary bindings to internal model
             * @param $scope
             */
            function convertFormToCert($scope) {

                // Fält 1. Smittskydd. Vid sparning: ta bort data på alla fält som döljs när smittskydd är icheckat.
                if ($scope.cert.avstangningSmittskydd) {
                    $scope.cert.undersokningAvPatienten = null;
                    $scope.cert.telefonkontaktMedPatienten = null;
                    $scope.cert.journaluppgifter = null;
                    $scope.cert.annanReferens = null;
                    $scope.form.ovrigt.annanReferensBeskrivning = null;
                    $scope.cert.annanReferensBeskrivning = null;

                    $scope.cert.diagnosKod = null;
                    $scope.cert.diagnosKod2 = null;
                    $scope.cert.diagnosKod3 = null;

                    $scope.cert.diagnosBeskrivning1 = null;
                    $scope.cert.diagnosBeskrivning2 = null;
                    $scope.$scope.cert.diagnosBeskrivning3 = null;
                    $scope.cert.diagnosBeskrivning = null;
                    $scope.cert.samsjuklighet = false;

                    $scope.cert.sjukdomsforlopp = null;
                    $scope.cert.funktionsnedsattning = null;
                    $scope.cert.aktivitetsbegransning = null;

                    $scope.form.rehabNow = 'NOW';
                    $scope.form.rehabWhen = null;

                    $scope.form.arbete = false;
                    $scope.cert.nuvarandeArbetsuppgifter = null;
                    $scope.cert.arbetsloshet = false;
                    $scope.cert.foraldrarledighet = false;

                    $scope.cert.atgardInomSjukvarden = null;
                    $scope.cert.annanAtgard = null;
                    $scope.cert.ressattTillArbeteAktuellt = false;
                    $scope.cert.rekommendationKontaktArbetsformedlingen = false;
                    $scope.cert.rekommendationKontaktForetagshalsovarden = false;
                    $scope.form.rekommendationOvrigtCheck = false;
                    $scope.cert.rekommendationOvrigt = null;
                }

                // Fält 4b. AnnanReferensBeskrivning
                if ($scope.basedOnState.check.annanReferens) {
                    $scope.cert.annanReferensBeskrivning = $scope.form.ovrigt.annanReferensBeskrivning;
                } else {
                    $scope.cert.annanReferensBeskrivning = null;
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
                switch($scope.form.prognos) {
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

                // Fält 11. Rehab radio conversions
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

                // TODO? omgående/senare form.ovrigt.rehabNow
                // datum senare form.ovrigt.rehabWhen
            }

            /*************************************************************************
             * Ng-change and watches updating behaviour in form (try to get rid of these or at least make them consistent)
             *************************************************************************/

            $scope.autoEnterDate = function(modelName) {

                // Set todays date when a baserat pa field is checked
                if ($scope.basedOnState.check[modelName]) {
                    if ($scope.cert[modelName] === undefined || $scope.cert[modelName] === '') {
                        $scope.cert[modelName] = $filter('date')($scope.today, 'yyyy-MM-dd');
                    }
                } else {

                    // Clear date if check is unchecked
                    $scope.cert[modelName] = '';
                }
            };

            $scope.onChangeBaserasPaDate = function(baserasPaType) {
                if ($scope.cert[baserasPaType] !== undefined && isDate($scope.cert[baserasPaType])) {
                    $scope.basedOnState.check[baserasPaType] = true;
                }
            };

            $scope.limitFieldLength = function(field) {
                $scope.cert[field] = $scope.cert[field].substr(0, $scope.inputLimits[field]);
            };

            $scope.limitOtherField = function(field) {
                function limitOvrigtLength(val) {
                    var totalOvrigtLength = $scope.getTotalOvrigtLength();
                    if (totalOvrigtLength > $scope.inputLimits.ovrigt) {
                        // Remove characters over limit from current field
                        return val.substr(0, val.length - (totalOvrigtLength - $scope.inputLimits.ovrigt));
                    }
                    return val;
                }

                $scope.cert[field] = limitOvrigtLength($scope.cert[field]);
            };

            // Calculate total length of all fields ending up in Övrigt in the external model
            $scope.getTotalOvrigtLength = function() {
                function getLengthOrZero(value) {
                    if (value === undefined || value === null) {
                        return 0;
                    } else {
                        return value.length;
                    }
                }

                var totalOvrigtLength = getLengthOrZero($scope.cert.kommentar);

                if ($scope.form.ovrigt !== undefined) {
                    totalOvrigtLength = getLengthOrZero($scope.form.ovrigt.annanReferensBeskrivning) +
                        getLengthOrZero($scope.form.ovrigt.nedsattMed25Beskrivning) +
                        getLengthOrZero($scope.form.ovrigt.nedsattMed50Beskrivning) +
                        getLengthOrZero($scope.form.ovrigt.nedsattMed75Beskrivning) +
                        getLengthOrZero($scope.form.ovrigt.arbetsformagaPrognosGarInteAttBedomaBeskrivning);
                }
                if ($scope.form.ovrigt !== undefined) {
                    if ($scope.form.ovrigt.rehabWhen instanceof Date) {
                        totalOvrigtLength += ($filter('date')
                        ($scope.form.ovrigt.rehabWhen, 'yyyy-MM-dd')).length;
                    }
                }

                return totalOvrigtLength;
            };

            /**
             * Calculate total days between the earliest and the latest dates supplied in the 8b controls
             * @type {boolean}
             */
            $scope.updateTotalCertDays = function() {
                var oneDay = 24 * 60 * 60 * 1000;
                var dates = findStartEndDates();
                if (!dates.minDate || !dates.maxDate) {
                    // return if there's no valid range span yet
                    $scope.totalCertDays = false;
                    return $scope.totalCertDays;
                }

                dates.minDate = convertDateToTime(dates.minDate);
                dates.maxDate = convertDateToTime(dates.maxDate);
                $scope.totalCertDays = Math.round(Math.abs((dates.minDate - dates.maxDate) / (oneDay))) + 1;
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
                        var dates = findStartEndDates();
                        if (!workstate.from || !isDate(workstate.from)) {

                            // find highest max date
                            if (!dates.maxDate) {
                                // if no maxdate is available, use today
                                var today = ($filter('date')($scope.today, 'yyyy-MM-dd'));
                                workstate.from = today;
                            } else {
                                workstate.from = moment(dates.maxDate).add('days', 1).format('YYYY-MM-DD');
                            }
                        }

                        // Set tom date
                        if (!workstate.tom || !isDate(workstate.tom)) {
                            workstate.tom = moment(workstate.from).add('days', 7).format('YYYY-MM-DD');
                        }
                    } else {

                        // Remove dates
                        delete $scope.cert[nedsattModelName];
                    }

                    onArbetsformagaDatesUpdated();
                }
            };

            /**
             * Watches Rekommendationer 6a, 7, 11
             */
            $scope.$watch('form.ovrigt.rehabNow', function(newVal) {
                if ($scope.form.ovrigt !== undefined) {
                    if (newVal === 'LATER' && $scope.form.ovrigt.rehabWhen === '') {
                        $scope.form.ovrigt.rehabWhen = $scope.today;
                    } else if (newVal === 'NOW') {
                        $scope.form.ovrigt.rehabWhen = '';
                    }
                }
            });
            $scope.$watch('form.ovrigt.rehabWhen', function(newVal) {
                if (isDate(newVal)) {
                    $scope.form.ovrigt.rehabNow = 'LATER';
                }
            });

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

                // if a valid date has been set
                if (dateField !== undefined && isDate(dateField)) {

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
                            if (moment($scope.cert[groups[i]].from).isAfter(moment($scope.cert[groups[i]].tom))) {
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

                                    if (moment(nedsatt.from).isSame(nedsattCompare.from)) {
                                        $scope.nedsattInvalid[groups[i] + 'from'] = true;
                                        $scope.nedsattInvalid[groups[j] + 'from'] = true;
                                    }
                                    if (moment(nedsatt.tom).isSame(nedsattCompare.from)) {
                                        $scope.nedsattInvalid[groups[i] + 'tom'] = true;
                                        $scope.nedsattInvalid[groups[j] + 'from'] = true;
                                    }
                                    if (moment(nedsatt.from).isSame(nedsattCompare.tom)) {
                                        $scope.nedsattInvalid[groups[i] + 'from'] = true;
                                        $scope.nedsattInvalid[groups[j] + 'tom'] = true;
                                    }
                                    if (moment(nedsatt.tom).isSame(nedsattCompare.tom)) {
                                        $scope.nedsattInvalid[groups[i] + 'tom'] = true;
                                        $scope.nedsattInvalid[groups[j] + 'tom'] = true;
                                    }

                                    if ((moment(nedsatt.tom).isAfter(nedsattCompare.from) &&
                                        moment(nedsatt.from).isBefore(nedsattCompare.from)) || // first group overlaps in front
                                        (moment(nedsatt.from).isBefore(nedsattCompare.tom) &&
                                            moment(nedsatt.tom).isAfter(nedsattCompare.tom)) || // first group overlaps behind
                                        (moment(nedsatt.from).isBefore(nedsattCompare.from) &&
                                            moment(nedsatt.tom).isAfter(nedsattCompare.tom)) || // first group wraps second group
                                        (moment(nedsatt.from).isAfter(nedsattCompare.from) &&
                                            moment(nedsatt.tom).isBefore(nedsattCompare.tom))) { // second group wraps first group
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
