define(
    [],
    function () {
        'use strict';

        return [
            '$scope',
            '$location',
            '$filter',
            '$anchorScroll',
            'fk7263.certificateService',
            '$routeParams',
            function ($scope, $location, $filter, $anchorScroll, certificateService, $routeParams) {
                $scope.cert = {};

                $scope.messages = [];
                $scope.isComplete = false;

                // init state
                $scope.widgetState = {
                    doneLoading: false,
                    hasError: false,
                    showComplete: false,
                    collapsedHeader: false
                };

                $scope.today = new Date();
                $scope.today.setHours(0, 0, 0, 0); // reset time to
                // increase comparison accuracy (using new Date() also sets time)

                $scope.toggleHeader = function () {
                    $scope.widgetState.collapsedHeader = !$scope.widgetState.collapsedHeader;
                };

                $scope.toggleShowComplete = function () {
                    $scope.widgetState.showComplete = !$scope.widgetState.showComplete;
                    if ($scope.widgetState.showComplete) {

                        var old = $location.hash();
                        $location.hash('top');
                        $anchorScroll();
                        // reset to old to keep any additional routing logic from kicking in
                        $location.hash(old);
                    }
                };

                $scope.form = {
                    'identity': {
                        'ID-kort': 'ID_KORT',
                        'Företagskort eller tjänstekort': 'FORETAG_ELLER_TJANSTEKORT',
                        'Körkort': 'KORKORT',
                        'Personlig kännedom': 'PERS_KANNEDOM',
                        'Försäkran enligt 18 kap. 4§': 'FORSAKRAN_KAP18',
                        'Pass': 'PASS'
                    },
                    'korkortd': false,
                    'behorighet': true
                };

                $scope.testerror = false;

                // Input limit handling
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

                // Watch Fält 4b -> Annat and update backend model when view changes.
                $scope.$watchCollection('[cert.otherData.baseradPaAnnat, basedOnState.check.annanReferens]',
                    function () {
                        if ($scope.cert.otherData !== undefined) {
                            if (!$scope.cert.otherData.baseradPaAnnat
                                || $scope.cert.otherData.baseradPaAnnat === ""
                                || !$scope.basedOnState.check.annanReferens) {
                                $scope.cert.annanReferensBeskrivning = null;
                                return;
                            }
                            $scope.cert.annanReferensBeskrivning = $scope.cert.otherData.baseradPaAnnat;
                        }
                    });

                // Watch Fält 10 -> Går ej att bedöma and update backend model when view changes.
                $scope.$watchCollection('[cert.otherData.prognosisClarification, cert.prognosis]',
                    function () {
                        if ($scope.cert.otherData !== undefined) {
                            if (!$scope.cert.otherData.prognosisClarification
                                || $scope.cert.otherData.prognosisClarification === ""
                                || $scope.cert.prognosis !== "UNKNOWN") {
                                $scope.cert.arbetsformagaPrognosGarInteAttBedomBeskrivning = null;
                                return;
                            }
                            $scope.cert.arbetsformagaPrognosGarInteAttBedomBeskrivning = $scope.cert.otherData.prognosisClarification;
                        }
                    });

                $scope.limitFieldLength = function (field) {
                    $scope.cert[field] = $scope.cert[field].substr(0, $scope.inputLimits[field]);
                };

                $scope.limitOtherField = function (field) {
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

                $scope.getTotalOvrigtLength = function () {
                    function getLengthOrZero(value) {
                        if (value === undefined) {
                            return 0;
                        } else {
                            return value.length;
                        }
                    }

                    var totalOvrigtLength = getLengthOrZero($scope.cert.kommentar);

                    if ($scope.cert.otherData !== undefined) {
                        totalOvrigtLength = getLengthOrZero($scope.cert.otherData.baseradPaAnnat),
                            +getLengthOrZero($scope.cert.otherData.workingHours25),
                            +getLengthOrZero($scope.cert.otherData.workingHours50),
                            +getLengthOrZero($scope.cert.otherData.workingHours75),
                            +getLengthOrZero($scope.cert.otherData.workingHours100),
                            +getLengthOrZero($scope.cert.otherData.prognosisClarification);
                    }
                    if ($scope.cert.otherData !== undefined) {
                        if ($scope.cert.otherData.rehabWhen instanceof Date) {
                            totalOvrigtLength += ($filter('date')
                            ($scope.cert.otherData.rehabWhen, 'yyyy-MM-dd')).length;
                        }
                    }
                    // NOTE: this date (rehabWhen) will probably
                    // need a label and therefore
                    // use more than the length of the date when
                    // merged with the other
                    // fields.
                    // Remember to add length for the label as well
                    // (probably applies to all
                    // in cert.otherData)
                    return totalOvrigtLength;
                };

                // Based on handling (4b)
                $scope.basedOnState = {
                    check: {
                        undersokningAvPatienten: false,
                        telefonkontaktMedPatienten: false,
                        journaluppgifter: false,
                        annanReferens: false
                    }
                };

                $scope.autoEnterDate = function (modelName) {
                    if ($scope.basedOnState.check[modelName]) {
                        if ($scope.cert[modelName] === "" || $scope.cert[modelName] === undefined) {
                            $scope.cert[modelName] = $filter('date')($scope.today, "yyyy-MM-dd");
                        }
                    } else {
                        $scope.cert[modelName] = "";
                    }
                };

                // Diagnose handling (2)
                $scope.diagnose_codes = [
                    {
                        value: "J44.0",
                        label: "J44.0 Kroniskt obstruktiv lungsjukdom med akut nedre luftvägsinfektion"
                    },
                    {
                        value: "K92.2",
                        label: "K92.2 Gastrointestinal blödning, ospecificerad"
                    }
                ];

                $scope.diagnoses = [
                    {
                        value: "Kroniskt obstruktiv lungsjukdom med akut nedre luftvägsinfektion",
                        label: "J44.0 Kroniskt obstruktiv lungsjukdom med akut nedre luftvägsinfektion"
                    },
                    {
                        value: "Gastrointestinal blödning, ospecificerad",
                        label: "K92.2 Gastrointestinal blödning, ospecificerad"
                    }
                ];

                // Arbetsförmåga handling (8b)
                $scope.workState = {
                    check25: false,
                    check50: false,
                    check75: false,
                    check100: false
                };

                function updateWorkStateDate(checked, model) {
                    if (model !== undefined) {
                        if (checked) {
                            if (!isDate(model.from)){
                                model.from = ($filter('date')($scope.today, 'yyyy-MM-dd'));
                            }
                            if (!isDate(model.tom)) {
                                model.tom = ($filter('date')($scope.today, 'yyyy-MM-dd'));
                            }
                        } else {
                            model.from = "";
                            model.tom = "";
                        }
                        $scope.updateTotalCertDays();
                    }
                }

                $scope.$watch('workState.check25', function (newVal, oldVal) {
                    updateWorkStateDate(newVal, $scope.cert.nedsattMed25);
                });

                $scope.$watch('workState.check50', function (newVal, oldVal) {
                    updateWorkStateDate(newVal, $scope.cert.nedsattMed50);
                });

                $scope.$watch('workState.check75', function (newVal, oldVal) {
                    updateWorkStateDate(newVal, $scope.cert.nedsattMed75);
                });

                $scope.$watch('workState.check100', function (newVal, oldVal) {
                    updateWorkStateDate(newVal, $scope.cert.nedsattMed100);
                });

                function isDate(date) {
                    return (date instanceof Date);
                }

                function getMinMaxDate(comparisonType, dates) {

                    var compareDate = false;
                    for (var i = 0; i < dates.length; i++) {
                        if (isDate(dates[i])) {
                            if (!compareDate // no valid date
                                // found yet
                                || (comparisonType === 'min' && dates[i] < compareDate) // looking
                                // for
                                // min
                                // date
                                || (comparisonType === 'max' && dates[i] > compareDate)) { // looking
                                // for
                                // max
                                // date
                                compareDate = dates[i];
                            }
                        }
                    }

                    return compareDate; // if no valid dates,
                    // compareDate is still
                    // false,
                    // otherwise contains the lowest/highest date
                    // sent
                    // depending on comparisonType
                }

                $scope.totalCertDays = false;
                $scope.updateTotalCertDays = function () {
                    var oneDay = 24 * 60 * 60 * 1000;
                    var startDates = [ $scope.cert.nedsattMed25.from, $scope.cert.nedsattMed50.from,
                        $scope.cert.nedsattMed75.from, $scope.cert.nedsattMed100.from ]
                    var endDates = [ $scope.cert.nedsattMed25.tom, $scope.cert.nedsattMed50.tom,
                        $scope.cert.nedsattMed75.tom, $scope.cert.nedsattMed100.tom ]
                    var minDate = getMinMaxDate('min', startDates);
                    var maxDate = getMinMaxDate('max', endDates);

                    if (!minDate || !maxDate) {
                        return $scope.totalCertDays = false;
                    } // return
                    // if
                    // there's
                    // no
                    // valid
                    // range
                    // span
                    // yet

                    $scope.totalCertDays = Math.round(Math.abs((minDate.getTime() - maxDate.getTime())
                        / (oneDay))) + 1;
                };

                // Rekommendationer 6a, 7, 11
                $scope.$watch('cert.rehabNow', function (newVal, oldVal) {
                    if ($scope.cert.otherData !== undefined) {
                        if (newVal === 'LATER' && $scope.cert.otherData.rehabWhen === '') {
                            $scope.cert.otherData.rehabWhen = $scope.today;
                        } else if (newVal === 'NOW') {
                            $scope.cert.otherData.rehabWhen = '';
                        }
                    }
                });

                $scope.$watch('cert.otherData.rehabWhen', function (newVal, oldVal) {
                    if (isDate(newVal)) {
                        $scope.cert.rehabNow = 'LATER';
                    }
                });

                $scope.cert = {};
                // Get the certificate draft from the server.
                // TODO: Hide the form until the draft has been loaded.
                certificateService.getDraft($routeParams.certificateId, function (data) {
                    $scope.cert = data.content;
                }, function (errorData) {
                    // TODO: Show error message.
                });

                /**
                 * Action to save the certificate draft to the server.
                 */
                $scope.save = function () {
                    certificateService.saveDraft($routeParams.certificateId, $scope.cert, function (data) {

                        $scope.certForm.$setPristine();

                        $scope.validationMessagesGrouped = {};
                        $scope.validationMessages = [];

                        if (data.status === 'COMPLETE') {
                            $scope.isComplete = true;
                        } else {
                            $scope.isComplete = false;
                            $scope.validationMessages = data.messages;

                            angular.forEach(data.messages, function (message) {
                                var field = message.field;
                                var parts = field.split(".");
                                var section;
                                if (parts.length > 0) {
                                    section = parts[0].toLowerCase();

                                    if ($scope.validationMessagesGrouped[section]) {
                                        $scope.validationMessagesGrouped[section].push(message);
                                    } else {
                                        $scope.validationMessagesGrouped[section] = [ message ];
                                    }
                                }
                            });
                        }
                    }, function (errorData) {
                        // TODO: Show error message.
                    });
                };

                /**
                 * Action to discard the certificate draft and return to WebCert again.
                 */
                $scope.discard = function () {
                    certificateService.discardDraft($routeParams.certificateId, function (data) {
                        // TODO: Redirect back to start page.
                    }, function (errorData) {
                        // TODO: Show error message.
                    });
                };
            } ];
    });
