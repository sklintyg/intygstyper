'use strict';

/* Controllers */
var controllers = angular.module('wc.fk7263.controllers', []);

/*
 * EditCertCtrl - Controller for logic related to editing a certificate
 */
controllers.controller('EditCertCtrl', [ '$scope', '$filter', '$location', '$rootScope', '$timeout', '$anchorScroll', 'certificateService',
    function ($scope, $filter, $location, $rootScope, $timeout, $anchorScroll, certificateService) {

        // init state
        $scope.widgetState = {
            doneLoading : false,
            hasError : false,
            showComplete : false,
            collapsedHeader : false
        };
        $scope.widgetState.doneLoading = true;
        $scope.today = new Date();
        $scope.today.setHours(0, 0, 0, 0); // reset time to
        // increase
        // comparison
        // accuracy (using new Date() also sets
        // time)
        $scope.messages = [];
        $scope.isComplete = false;

        $scope.toggleHeader = function () {
            $scope.widgetState.collapsedHeader = !$scope.widgetState.collapsedHeader;
        };

        $scope.toggleShowComplete = function () {
            $scope.widgetState.showComplete = !$scope.widgetState.showComplete;
            if ($scope.widgetState.showComplete) {

                var old = $location.hash();
                $location.hash('top');
                $anchorScroll();
                //reset to old to keep any additional routing logic from kicking in
                $location.hash(old);
            }
        };

        $scope.helpTooltip = "Ange vad uppgifterna i intyget baseras på. Flera alternativ kan väljas.<br><br> <a href='#'>Öppna hjälpen</a>";

        // Input limit handling
        $scope.inputLimits = {
            diagnosBeskrivning : 100,
            sjukdomsforlopp : 270,
            funktionsnedsattning : 560,
            aktivitetsbegransning : 1100,
            nuvarandeArbetsuppgifter : 120,
            arbetsformagaPrognos : 600,
            atgardInomSjukvarden : 66,
            annanAtgard : 66,
            ovrigt : 420
            // 420 = combined field 13 (and dependencies) limit
        };

        $scope.limitFieldLength = function (field) {
            $scope.cert[field] = $scope.cert[field].substr(0, $scope.inputLimits[field]);
        };


        $scope.limitOtherField = function (field) {
            function limitOvrigtLength (val) {
                var totalOvrigtLength = $scope.getTotalOvrigtLength();
                if (totalOvrigtLength > $scope.inputLimits.ovrigt) {
                    // Remove characters over limit from current field
                    return val.substr(0, val.length - (totalOvrigtLength - $scope.inputLimits.ovrigt));
                }
                return val;
            }
            $scope.cert[field] = limitOvrigtLength($scope.cert[field])
        };

        $scope.getTotalOvrigtLength = function () {
            function getLengthOrZero(value) {
                if (value == undefined) {
                    return 0;
                } else {
                    return value.length;
                }
            };
            var totalOvrigtLength = getLengthOrZero($scope.cert.kommentar);

            if ($scope.cert.otherData != undefined) {
                totalOvrigtLength = getLengthOrZero($scope.cert.otherData.baseradPaAnnat),
                    +getLengthOrZero($scope.cert.otherData.workingHours25),
                    +getLengthOrZero($scope.cert.otherData.workingHours50),
                    +getLengthOrZero($scope.cert.otherData.workingHours75),
                    +getLengthOrZero($scope.cert.otherData.workingHours100),
                    +getLengthOrZero($scope.cert.otherData.prognosisClarification);
            }
            if ($scope.cert.otherData != undefined) {
                if ($scope.cert.otherData.rehabWhen instanceof Date) {
                    totalOvrigtLength += ($filter('date')($scope.cert.otherData.rehabWhen, 'yyyy-MM-dd')).length;
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
            check : {
                undersokningAvPatienten : false,
                telefonkontaktMedPatienten : false,
                journaluppgifter : false,
                annanReferens : false
            }
        };

        $scope.autoEnterDate = function (modelName) {
            if ($scope.basedOnState.check[modelName]) {
                if ($scope.cert[modelName] == "" || $scope.cert[modelName] == undefined) {
                    $scope.cert[modelName] = $filter('date')($scope.today, "yyyy-MM-dd");
                }
            } else {
                $scope.cert[modelName] = "";
            }
        };


        // Diagnose handling (2)
        $scope.diagnose_codes = [
            {
                value : "J44.0",
                label : "J44.0 Kroniskt obstruktiv lungsjukdom med akut nedre luftvägsinfektion"
            },
            {
                value : "K92.2",
                label : "K92.2 Gastrointestinal blödning, ospecificerad"
            }
        ];

        $scope.diagnoses = [
            {
                value : "Kroniskt obstruktiv lungsjukdom med akut nedre luftvägsinfektion",
                label : "J44.0 Kroniskt obstruktiv lungsjukdom med akut nedre luftvägsinfektion"
            },
            {
                value : "Gastrointestinal blödning, ospecificerad",
                label : "K92.2 Gastrointestinal blödning, ospecificerad"
            }
        ];

        // Arbetsförmåga handling (8b)
        $scope.workState = {
            check25 : false,
            check50 : false,
            check75 : false,
            check100 : false
        };

        function updateWorkStateDate (checked, model) {
            if (model != undefined) {
	        	if (checked) {
	                if (!isDate(model.start))
	                    model.start = $scope.today;
	                if (!isDate(model.end))
	                    model.end = $scope.today;
	            } else {
	                model.start = "";
	                model.end = "";
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

        function isDate (date) {
            return (date instanceof Date);
        }

        function getMinMaxDate (comparisonType, dates) {

            var compareDate = false;
            for (var i = 0; i < dates.length; i++) {
                if (isDate(dates[i])) {
                    if (!compareDate // no valid date
                        // found yet
                        || (comparisonType == 'min' && dates[i] < compareDate) // looking
                        // for
                        // min
                        // date
                        || (comparisonType == 'max' && dates[i] > compareDate)) { // looking
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
            var startDates = [ $scope.cert.nedsattMed25.start, $scope.cert.nedsattMed50.start, $scope.cert.nedsattMed75.start, $scope.cert.nedsattMed100.start ]
            var endDates = [ $scope.cert.nedsattMed25.end, $scope.cert.nedsattMed50.end, $scope.cert.nedsattMed75.end, $scope.cert.nedsattMed100.end ]
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

            $scope.totalCertDays = Math.round(Math.abs((minDate.getTime() - maxDate.getTime()) / (oneDay))) + 1;
        };

        // Rekommendationer 6a, 7, 11
        $scope.$watch('cert.rehabNow', function (newVal, oldVal) {
        	if ($scope.cert.otherData != undefined) {
	            if (newVal == 'LATER' && $scope.cert.otherData.rehabWhen == '') {
	                $scope.cert.otherData.rehabWhen = $scope.today;
	            } else if (newVal == 'NOW') {
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

	    certificateService.getDraft($scope.MODULE_CONFIG.CERT_ID_PARAMETER,
	        function (data) {
	            $scope.cert = data.content;
	            // TODO: Set the cert from the data.
	        }, function (errorData) {
	            // TODO: Show error message.
	        });

        /**
         * Action to save the certificate draft to the server.
         */
        $scope.save = function () {
            certificateService.saveDraft($scope.MODULE_CONFIG.CERT_ID_PARAMETER, $scope.cert,
                function (data) {
                    // TODO: Convert validation messages.
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
	                                $scope.validationMessagesGrouped[section] = [message];
	                            }
	                        }
	                    });
	                }
                },
                function (errorData) {
                    // TODO: Show error message.
                });
        };

        /**
         * Action to discard the certificate draft and return to WebCert again.
         */
        $scope.discard = function () {
            certificateService.discardDraft($scope.MODULE_CONFIG.CERT_ID_PARAMETER,
                function (data) {
                    // TODO: Redirect back to WebCert.
                },
                function (errorData) {
                    // TODO: Show error message.
                });
        };

    }]);

/*
 * ViewCertCtrl - Controller for logic related to viewing a certificate
 */
controllers.controller('ViewCertCtrl', [ '$scope', '$log', '$timeout', 'certificateService',
    function ($scope, $log, $timeout, certificateService) {

        // init state
        $scope.widgetState = {
            doneLoading : false,
            activeErrorMessageKey : null
        };
        $scope.certProperties = {
            sentToFK : false
        };
        $scope.cert = {};
        $scope.cert.filledAlways = true;

        var isSentToFK = function (statusArr) {
            if (statusArr) {
                for (var i = 0; i < statusArr.length; i++) {
                    if (statusArr[i].target === 'FK' && statusArr[i].type === 'SENT') {
                        return true;
                    }
                }
            }
            return false;
        };
        var isRevoked = function (statusArr) {
            if (statusArr) {
                for (var i = 0; i < statusArr.length; i++) {
                    if (statusArr[i].type === 'CANCELLED') {
                        return true;
                    }
                }
            }
            return false;
        };

        // Load certificate json
        certificateService.getCertificate($scope.MODULE_CONFIG.CERT_ID_PARAMETER, function (result) {
            $log.debug('Got getCertificate data:' + result);
            $scope.widgetState.doneLoading = true;
            if (result != null && result != '') {
                $scope.cert = result.certificateContent;

                $scope.certProperties.sentToFK = isSentToFK(result.certificateContentMeta.statuses);
                $scope.certProperties.isRevoked = isRevoked(result.certificateContentMeta.statuses);
            } else {
              $scope.widgetState.activeErrorMessageKey = 'error.could_not_load_cert';
            }
        }, function (errorData) {
            // show error view
            $scope.widgetState.doneLoading = true;
            if (errorData && errorData.errorCode === 'AUTHORIZATION_PROBLEM') {
                $scope.widgetState.activeErrorMessageKey = 'error.could_not_load_cert_not_auth';
            } else if (errorData && errorData.errorCode) {
                $scope.widgetState.activeErrorMessageKey = 'error.' + errorData.errorCode;
            } else {
                $scope.widgetState.activeErrorMessageKey = 'error.could_not_load_cert';
            }
        });
    }]);
