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
                collapsedHeader: false,
                hasInfoMissing: false
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

            function isInvalid(data) {
                return (data === undefined || data === null || data === '');
            }

            /**
             * Check if variable is undefined, null, NaN or an empty string = invalid
             * @param data
             * @returns {boolean}
             */
            function isValidString(data) {
                // data !== data from underscore.js: 'NaN' is the only value for which '===' is not reflexive.
                return (data !== undefined && data !== null && data === data && data !== '');
            }

            /**
             * Does supplied date look like an iso date XXXX-XX-XX (not a complete validation)?
             * @param date
             * @returns {*}
             */
            function isDate(date) {
                if (date === undefined || date === null) {
                    return false;
                }

                return moment(date, 'YYYY-MM-DD', true).isValid();
            }

            /**
             * Convert date to a moment date
             * @param date
             * @returns {*}
             */
            function toMoment(date) {
                if (date) {
                    return moment(date);
                } else {
                    return null;
                }
            }

            /**
             * Return value of companion date field in 8b date ranges
             * @param nedsattModelName
             * @param mainFromTom
             * @returns {b.$viewValue|*|locals.$viewValue|string|he.$viewValue|he.$setViewValue.$viewValue}
             */
            function getCompanionDateValue(nedsattModelName, mainFromTom) {
                return (mainFromTom === 'from') ? $scope.certForm[nedsattModelName+'tom'].$viewValue : $scope.certForm[nedsattModelName+'from'].$viewValue;
            }

            /**
             * Set if date named fieldName should be marked as invalid
             * @param fieldName
             * @param value
             */
            function setDateInvalidState(fieldName, value) {
                $scope.nedsattInvalid[fieldName] = value;
                $scope.certForm[fieldName].$setValidity(fieldName, value);
            }

            /**
             * Reset date invalid states to not invalid
             */
            function resetDateInvalidStates() {
                var groups = ['nedsattMed25from', 'nedsattMed25tom', 'nedsattMed50from', 'nedsattMed50tom', 'nedsattMed75from', 'nedsattMed75tom', 'nedsattMed100from', 'nedsattMed100tom'];
                for (var i = 0; i < groups.length; i++) {
                    setDateInvalidState(groups[i], false);
                }
            }

            /**
             * Convert date from string to a strict moment date
             * @param date
             * @returns {*}
             */
            function convertDateStrict(date) {

                if (typeof date === 'string' && date.length < 10) {
                    return null;
                }

                var momentDate = toMoment(date);
                if (momentDate !== null) {
                    // Format date strictly to 'YYYY-MM-DD'.
                    momentDate = moment(momentDate.format('YYYY-MM-DD'), 'YYYY-MM-DD', true).format('YYYY-MM-DD');
                    if (momentDate === 'invalid date') {
                        // We don't want to handle invalid dates at all
                        momentDate = null;
                    }
                }

                return momentDate;
            }


            /**
             * Create a date range object
             * @param groupName
             * @returns {{fromName: string, tomName: string}}
             */
            function createDateRangeGroup(groupName, strict, useModelValue) {
                var dateRangeGroup = {
                    fromName: groupName + 'from',
                    tomName: groupName + 'tom'
                };

                if (useModelValue) {
                    if ($scope.cert[groupName]) {
                        dateRangeGroup.nedsattFrom = $scope.cert[groupName].from;
                        dateRangeGroup.nedsattTom = $scope.cert[groupName].tom;
                    }
                } else {
                    dateRangeGroup.nedsattFrom = $scope.certForm[dateRangeGroup.fromName].$viewValue;
                    dateRangeGroup.nedsattTom = $scope.certForm[dateRangeGroup.tomName].$viewValue;
                }

                if (strict === undefined || strict === true) {
                    dateRangeGroup.nedsattFrom = convertDateStrict($scope.certForm[dateRangeGroup.fromName].$viewValue);
                    dateRangeGroup.nedsattTom = convertDateStrict($scope.certForm[dateRangeGroup.tomName].$viewValue);
                } else {
                    dateRangeGroup.nedsattFrom = $scope.certForm[dateRangeGroup.fromName].$viewValue;
                    dateRangeGroup.nedsattTom = $scope.certForm[dateRangeGroup.tomName].$viewValue;
                }

                /**
                 * Check if dateFieldName is marked as invalid
                 * @param group
                 * @returns {*}
                 */
                function isMarkedInvalid(dateFieldName) {
                    return $scope.nedsattInvalid[dateFieldName];
                }

                dateRangeGroup.isMarkedInvalid = function() {
                    return isMarkedInvalid(dateRangeGroup.fromName) || isMarkedInvalid(dateRangeGroup.tomName);
                };

                dateRangeGroup.hasValidDates = function() {
                    return (isValidString(dateRangeGroup.nedsattFrom) && isValidString(dateRangeGroup.nedsattTom));
                };

                dateRangeGroup.isValid = function() {
                    return (dateRangeGroup.hasValidDates() && !dateRangeGroup.isMarkedInvalid());
                };

                dateRangeGroup.isSame = function(otherGroup) {
                    return (dateRangeGroup.fromName === otherGroup.fromName);
                };

                return dateRangeGroup;
            }

            /**
             * Revalidate 8b dates
             */
            function validateDates(useModelValue) {
                resetDateInvalidStates();
                validateDateRangeOrder(useModelValue); // Set invalid if from dates are after tom dates
                validateDatePeriods(useModelValue); // Set invalid if date periods overlap
            }

            /**
             * Validate order of dates within a group
             */
            function validateDateRangeOrder(useModelValue) {

                // Update others still marked as invalid as well if they previously conflicted with the changed one
                var groups = ['nedsattMed25', 'nedsattMed50', 'nedsattMed75', 'nedsattMed100'];
                for (var i = 0; i < groups.length; i++) {
                    var dateGroup = createDateRangeGroup(groups[i], false, useModelValue);
                    if (dateGroup.isValid()) {
                        var momentFrom = toMoment(dateGroup.nedsattFrom);
                        var momentTom = toMoment(dateGroup.nedsattTom);
                        if(momentFrom.isValid() && momentTom.isValid() && toMoment(dateGroup.nedsattFrom).isAfter(toMoment(dateGroup.nedsattTom))) {
                            setDateInvalidState(dateGroup.fromName, true);
                            setDateInvalidState(dateGroup.tomName, true);
                        } else {
                            setDateInvalidState(dateGroup.fromName, false);
                            setDateInvalidState(dateGroup.tomName, false);
                        }
                    } else {
                        setDateInvalidState(dateGroup.fromName, false);
                        setDateInvalidState(dateGroup.tomName, false);
                    }
                }
            }

            /**
             * Mark overlapping dates as invalid comparing the provided dates
             * @param nedsattGroup1
             * @param nedsattGroup2
             */
            function markOverlappingDates(nedsattGroup1, nedsattGroup2) {

                if (nedsattGroup1.hasValidDates() && nedsattGroup2.hasValidDates()) {
                    if (toMoment(nedsattGroup1.nedsattFrom).isSame(nedsattGroup2.nedsattFrom, 'day')) {
                        setDateInvalidState(nedsattGroup1.fromName, true);
                        setDateInvalidState(nedsattGroup2.fromName, true);
                    }
                    if (toMoment(nedsattGroup1.nedsattTom).isSame(nedsattGroup2.nedsattFrom, 'day')) {
                        setDateInvalidState(nedsattGroup1.tomName, true);
                        setDateInvalidState(nedsattGroup2.fromName, true);
                    }
                    if (toMoment(nedsattGroup1.nedsattFrom).isSame(nedsattGroup2.nedsattTom, 'day')) {
                        setDateInvalidState(nedsattGroup1.fromName, true);
                        setDateInvalidState(nedsattGroup2.tomName, true);
                    }
                    if (toMoment(nedsattGroup1.nedsattTom).isSame(nedsattGroup2.nedsattTom, 'day')) {
                        setDateInvalidState(nedsattGroup1.tomName, true);
                        setDateInvalidState(nedsattGroup2.tomName, true);
                    }

                    if ((toMoment(nedsattGroup1.nedsattTom).isAfter(nedsattGroup2.nedsattFrom) &&
                        toMoment(nedsattGroup1.nedsattFrom).isBefore(nedsattGroup2.nedsattFrom)) || // first group overlaps in front
                        (toMoment(nedsattGroup1.nedsattFrom).isBefore(nedsattGroup2.nedsattTom) &&
                            toMoment(nedsattGroup1.nedsattTom).isAfter(nedsattGroup2.nedsattTom)) || // first group overlaps behind
                        (toMoment(nedsattGroup1.nedsattFrom).isBefore(nedsattGroup2.nedsattFrom) &&
                            toMoment(nedsattGroup1.nedsattTom).isAfter(nedsattGroup2.nedsattTom)) || // first group wraps second group
                        (toMoment(nedsattGroup1.nedsattFrom).isAfter(nedsattGroup2.nedsattFrom) &&
                            toMoment(nedsattGroup1.nedsattTom).isBefore(nedsattGroup2.nedsattTom))) { // second group wraps first group
                        setDateInvalidState(nedsattGroup1.fromName, true);
                        setDateInvalidState(nedsattGroup1.tomName, true);
                        setDateInvalidState(nedsattGroup2.fromName, true);
                        setDateInvalidState(nedsattGroup2.tomName, true);
                    }
                }
            }

            /**
             * Validate 8b date periods so they don't overlap or wrap in any way
             */
            function validateDatePeriods(useModelValue) {
                var groups = ['nedsattMed25', 'nedsattMed50', 'nedsattMed75', 'nedsattMed100'];

                // for every nedsatt group
                for (var i = 0; i < groups.length; i++) {

                    // where group is used, set and not already marked as invalid
                    var dateGroup = createDateRangeGroup(groups[i], false, useModelValue);

                    // check with all other period groups after nedsatt period if periods overlap
                    for (var j = i + 1; j < groups.length; j++) {

                        // dont check against unused dates and already invalid dates
                        var compareNedsattGroup = createDateRangeGroup(groups[j], false, useModelValue);
                        markOverlappingDates(dateGroup, compareNedsattGroup);
                    }
                }
            }

            /**
             * 8b: find earliest and latest dates (as moment objects) for arbetsförmåga
             * @returns {{minMoment: null, maxMoment: null}}
             */
            function findStartEndMoments(useModelValue) {
                var moments = {
                    minMoment: null,
                    maxMoment: null
                };
                var startMoments = [];
                var endMoments = [];

                var nedsattMedList = ['nedsattMed25', 'nedsattMed50', 'nedsattMed75', 'nedsattMed100'];

                angular.forEach(nedsattMedList, function(nedsattMed) {

                    function pushValidDate(list, dateValue) {
                        if ((typeof dateValue === 'string' && dateValue.length === 10) || dateValue instanceof Date) {
                            var momentDate = toMoment(dateValue);
                            if(momentDate !== null && momentDate.isValid()) {
                                var formattedDate = moment(momentDate.format('YYYY-MM-DD'), 'YYYY-MM-DD', true);
                                if (formattedDate.isValid()) {
                                    list.push(formattedDate);
                                }
                            }
                        }
                    }

                    var dateValue = null;

                    if(useModelValue) {
                        if($scope.cert[nedsattMed] && $scope.cert[nedsattMed].from) {
                            dateValue = $scope.cert[nedsattMed].from;
                        }
                    } else {
                        dateValue = $scope.certForm[nedsattMed+'from'].$viewValue;
                    }
                    pushValidDate(startMoments, dateValue);

                    dateValue = null;
                    if(useModelValue) {
                        if($scope.cert[nedsattMed] && $scope.cert[nedsattMed].tom) {
                            dateValue = $scope.cert[nedsattMed].tom;
                        }
                    } else {
                        dateValue = $scope.certForm[nedsattMed+'tom'].$viewValue;
                    }
                    pushValidDate(endMoments, dateValue);
                });

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
            function onArbetsformagaDatesUpdated(useModelValue) {
                $scope.updateTotalCertDays(useModelValue);

                var minMaxMoments = findStartEndMoments(useModelValue);
                checkArbetsformagaDatesRange(minMaxMoments.minMoment);
                checkArbetsformagaDatesPeriodLength(minMaxMoments.minMoment, minMaxMoments.maxMoment);
            }

            /**
             * 8b: Check that the earliest startdate in arbetsförmåga is no more than a week before today and no more than 6 months in the future
             * @type {boolean}
             */
            function checkArbetsformagaDatesRange(startMoment) {
                if (startMoment === null) {
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
                if (startMoment === null || endMoment === null) {
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

                // check if all info is available from HSA. If not, display the info message that someone needs to update it
                if ($scope.cert.vardperson && ($scope.cert.vardperson.postadress === undefined ||
                    $scope.cert.vardperson.postnummer === undefined ||
                    $scope.cert.vardperson.postort === undefined ||
                    $scope.cert.vardperson.telefonnummer === undefined ||
                    $scope.cert.vardperson.postadress === '' ||
                    $scope.cert.vardperson.postnummer === '' ||
                    $scope.cert.vardperson.postort === '' ||
                    $scope.cert.vardperson.telefonnummer === '')) {
                    $scope.widgetState.hasInfoMissing = true;
                } else {
                    $scope.widgetState.hasInfoMissing = false;
                }

                // Fält 4b
                // Register parse function for 4b date pickers
                var baserasPaTypes = ['undersokningAvPatienten', 'telefonkontaktMedPatienten', 'journaluppgifter', 'annanReferens'];
                angular.forEach(baserasPaTypes, function(type) {
                    this[type + 'Date'].$parsers.unshift(function() {
                        $scope.onChangeBaserasPaDate(type);
                    });
                }, $scope.certForm);

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
                var nedsattMedList = ['nedsattMed25', 'nedsattMed50', 'nedsattMed75', 'nedsattMed100'];
                angular.forEach(nedsattMedList, function(nedsattMed) {

                    function nedsattParser(viewValue) {

                        viewValue = convertDateStrict(viewValue);

                        var changedDateGroup = createDateRangeGroup(nedsattMed, false); // false = non-strict date conversion
                        if (!isValidString(changedDateGroup.nedsattFrom) && !isValidString(changedDateGroup.nedsattTom)) {
                            // uncheck check since both dates are undefined or empty
                            $scope.workState[nedsattMed] = false;

                        } else if (isValidString(changedDateGroup.nedsattFrom) || isValidString(changedDateGroup.nedsattTom)) {
                            // One of the dates is valid
                            $scope.workState[nedsattMed] = true; // Check nedsatt checkbox
                        }

                        if (viewValue === null) {
                            viewValue = undefined;
                        } else {
                            validateDates(false);
                            onArbetsformagaDatesUpdated(false);
                        }

                        return viewValue;
                    }

                    // Register parsers so we can follow changes in the date inputs
                    if ($scope.certForm[nedsattMed+'from'].$parsers.length > 1) {
                        $scope.certForm[nedsattMed+'from'].$parsers.shift();
                    }
                    $scope.certForm[nedsattMed+'from'].$parsers.unshift(nedsattParser);

                    if ($scope.certForm[nedsattMed+'from'].$formatters.length > 0) {
                        $scope.certForm[nedsattMed+'from'].$formatters.shift();
                    }
                    $scope.certForm[nedsattMed+'from'].$formatters.unshift(function(modelValue) {
                        validateDates(true);
                        onArbetsformagaDatesUpdated(true);
                        return modelValue;
                    });

                    if ($scope.certForm[nedsattMed+'tom'].$parsers.length > 1) {
                        $scope.certForm[nedsattMed+'tom'].$parsers.shift();
                    }
                    $scope.certForm[nedsattMed+'tom'].$parsers.unshift(nedsattParser);

                    if ($scope.certForm[nedsattMed+'tom'].$formatters.length > 0) {
                        $scope.certForm[nedsattMed+'tom'].$formatters.shift();
                    }
                    $scope.certForm[nedsattMed+'tom'].$formatters.unshift(function(modelValue) {
                        validateDates(true);
                        onArbetsformagaDatesUpdated(true);
                        return modelValue;
                    });

                    if ($scope.cert[nedsattMed]) {
                        $scope.workState[nedsattMed] = true;
                    }
                }, $scope.cert);

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
                    case "rehabiliteringAktuell":
                        $scope.form.rehab = 'JA';
                        break;
                    case "rehabiliteringEjAktuell":
                        $scope.form.rehab = 'NEJ';
                        break;
                    case "rehabiliteringGarInteAttBedoma":
                        $scope.form.rehab = 'GAREJ';
                        break;
                    }
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

                // Fält 4b. datum
                var baserasPaTypes = ['undersokningAvPatienten', 'telefonkontaktMedPatienten', 'journaluppgifter', 'annanReferens'];
                angular.forEach(baserasPaTypes, function(type) {
                    this[type] = $scope.certForm[type + 'Date'].$viewValue;
                }, $scope.cert);

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
                var nedsattMedList = ['nedsattMed25', 'nedsattMed50', 'nedsattMed75', 'nedsattMed100'];
                angular.forEach(nedsattMedList, function(nedsattMed) {

                    // convert dates to string from viewvalue (modelvalue is undefined for invalid dates from datepicker)
                    var from = $scope.certForm[nedsattMed+'from'].$viewValue;
                    var tom = $scope.certForm[nedsattMed+'tom'].$viewValue;
                    if (this[nedsattMed] === undefined && (isValidString(from) || isValidString(tom))) {

                        this[nedsattMed] = {};
                        if (isValidString(from)) {
                            this[nedsattMed].from = from;
                        }
                        if (isValidString(tom)) {
                            this[nedsattMed].tom = tom;
                        }
                    } else if (this[nedsattMed]) {
                        if (isValidString(from)) {
                            this[nedsattMed].from = from;
                        } else {
                            this[nedsattMed].from = undefined;
                        }
                        if (isValidString(tom)) {
                            this[nedsattMed].tom = tom;
                        } else {
                            this[nedsattMed].from = undefined;
                        }
                    }

                    if ($scope.workState[nedsattMed]) {
                        this[nedsattMed+'Beskrivning'] = $scope.form.ovrigt[nedsattMed+'Beskrivning'];
                    } else {
                        this[nedsattMed+'Beskrivning'] = null;
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
                        $scope.cert.arbetsformagaPrognosGarInteAttBedomaBeskrivning =
                        $scope.form.ovrigt.arbetsformagaPrognosGarInteAttBedomaBeskrivning;
                    break;
                }

                // Fält 7. Rehab radio conversions
                switch ($scope.form.rehab) {
                case 'JA':
                    $scope.cert.rehabilitering = "rehabiliteringAktuell";
                    break;
                case 'NEJ':
                    $scope.cert.rehabilitering = "rehabiliteringEjAktuell";
                    break;
                case 'GAREJ':
                    $scope.cert.rehabilitering = "rehabiliteringGarInteAttBedoma";
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
                if (isValidString($scope.certForm[baserasPaType + 'Date'].$viewValue)) {
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
            $scope.updateTotalCertDays = function(useModelValue) {
                var moments = findStartEndMoments(useModelValue);
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
                        var nedsatt = $scope.cert[nedsattModelName];
                        if (!nedsatt) {
                            nedsatt = $scope.cert[nedsattModelName] = {};
                        }

                        // Set from date
                        // find highest max date
                        var moments = findStartEndMoments();
                        if (!nedsatt.from || !isDate(nedsatt.from)) {

                            if (moments.maxMoment !== null && moments.maxMoment.isValid()) {
                                nedsatt.from = moments.maxMoment.add('days', 1).format('YYYY-MM-DD');
                            } else {
                                // if no max moment is available, use today
                                nedsatt.from = $filter('date')($scope.today, 'yyyy-MM-dd');
                            }

                        }

                        // Set tom date
                        if (nedsatt.from && (!nedsatt.tom || !isDate(nedsatt.tom))) {
                            nedsatt.tom = toMoment(nedsatt.from).add('days', 7).format('YYYY-MM-DD');
                        }
                    } else {

                        // Remove dates
                        delete $scope.cert[nedsattModelName];
                    }
                }
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