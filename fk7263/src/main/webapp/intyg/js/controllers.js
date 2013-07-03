'use strict';

/* Controllers */
angular.module('controllers.fk7263', []);
angular.module('controllers.fk7263').controller('ViewCertCtrl',
        [ '$scope', '$filter', '$location', '$rootScope', 'certService', function ViewCertCtrl($scope, $filter, $location, $rootScope, certService) {
            $scope.cert = {};
            
            $scope.doneLoading = false;

            $scope.send = function() {
                $location.path("/summary");
            };

            $scope.visibleStatuses = [ 'SENT' ];

            $scope.filterStatuses = function(statuses) {
                var result = [];
                for ( var i = 0; i < statuses.length; i++) {
                    if ($scope.userVisibleStatusFilter(statuses[i])) {
                        result.push(statuses[i]);
                    }
                }
                return result;
            }

            $scope.userVisibleStatusFilter = function(status) {
                for ( var i = 0; i < $scope.visibleStatuses.length; i++) {
                    if (status.type == $scope.visibleStatuses[i]) {
                        return true;
                    }
                }
                return false;
            }

            $scope.showStatusHistory = function() {
                $location.path("/statushistory");
            }

            $scope.backToViewCertificate = function() {
                $location.path("/view");
            }

            // Convenience methods to simplify template markup
            $scope.smittskydd = function() {
                if (angular.isObject($scope.cert.aktiviteter)) {
                    // check if some activity has the trigger aktivitetskod
                    for ( var i = 0; i < $scope.cert.aktiviteter.length; i++) {
                        if ($scope.cert.aktiviteter[i].aktivitetskod == "AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA") {
                            return "yes";
                        }
                    }
                }
                // in all other cases
                return "no";
            }

            // expose calculated static link for pdf download
            $scope.downloadAsPdfLink = $scope.MODULE_CONFIG.MI_COMMON_API_CONTEXT_PATH + $scope.MODULE_CONFIG.CERT_ID_PARAMETER + "/pdf";

            certService.getCertificate($scope.MODULE_CONFIG.CERT_ID_PARAMETER, function(result) {
                $scope.doneLoading = true;
                if (result != null) {
                    result.filteredStatuses = $scope.filterStatuses(result.status);
                    // added calculated img src for certificate, beacuse for
                    // some reason ng-src with "/img/{{cert.typ }}.png" would
                    // evaluate first to "img/.png" before correct value = 404
                    result.typ_image = "/img/" + result.typ + ".png";

	                // setup which fields contain values. those which do not will get an unfilled style to make them less visually significant
	                // TODO: Implement cases for when fields are filled or not. empty strings? a certain value?
	                result.filledFields = {};
	                result.filledFields.diagnosis = true; //example: result.bedomtTillstand.beskrivning != '' || result.bedomtTillstand.tillstandskod != '';
	                result.filledFields.progressofdesease = true;
	                result.filledFields.disabilities = true;
	                result.filledFields.basedon = true;
	                result.filledFields.limitation = true;
	                result.filledFields.recommendations = true;
	                result.filledFields.plannedtreatment = true;
	                result.filledFields.workrehab = true;
	                result.filledFields.patientworkcapacity = true;
	                result.filledFields.patientworkcapacityjudgement = true;
	                result.filledFields.prognosis = true;
	                result.filledFields.othertransport = true;
	                result.filledFields.fkcontact = true;

                    $scope.cert = result;
                    $rootScope.cert = result;
                } else {
                    // show error view
                    $location.path("/fel/certnotfound");
                }
            });

            $scope.pagefocus = true;
        } ]);

angular.module('controllers.fk7263').controller('SentCertWizardCtrl',
        [ '$scope', '$filter', '$location', '$rootScope', '$routeParams', 'certService', function SentCertWizardCtrl($scope, $filter, $location, $rootScope, $routeParams, certService) {
            $scope.doneLoading = true;
            // Get active certificate from rootscope (passed from previous
            // controller)
            $scope.cert = $rootScope.cert;
            if (!angular.isObject($scope.cert)) {
                $location.path("/fel/certnotfound");
                return;
            }
            // expose calculated static link for pdf download
            $scope.downloadAsPdfLink = $scope.MODULE_CONFIG.MI_COMMON_API_CONTEXT_PATH + $scope.MODULE_CONFIG.CERT_ID_PARAMETER + "/pdf";

            // Initialize recipient handling, default to FK
            $scope.selectedRecipientId = $rootScope.selectedRecipientId || "FK";
            // set selected recipeintID in rootscope to preserve state between
            // controller instance invocations
            $rootScope.selectedRecipientId = $scope.selectedRecipientId;
            $scope.recipientList = [ {
                "id" : "FK",
                "recipientName" : "Försäkringskassan"
            } ];

            $scope.getRecipientName = function(id) {
                for ( var i = 0; i < $scope.recipientList.length; i++) {
                    var recipient = $scope.recipientList[i];
                    if (recipient.id == id) {
                        return recipient.recipientName;
                    }
                }
                ;
            }

            $scope.confirmRecipientSelection = function() {
                // now we have a recipient selected, set the selection in
                // rootscope
                $rootScope.selectedRecipientId = $scope.selectedRecipientId;
                $location.path("/summary");
            }

            $scope.confirmAndSend = function() {
                $scope.doneLoading = false;
                certService.sendCertificate($scope.MODULE_CONFIG.CERT_ID_PARAMETER, $scope.selectedRecipientId, function(result) {
                    $scope.doneLoading = true;
                    if (result != null && result.resultCode == "sent") {
                        $location.path("/sent");
                    } else {
                        // show error view
                        $location.path("/fel/couldnotsend");
                    }
                });

            }

            $scope.alreadySentToRecipient = function() {
                // check if selected recipient exists with SENT status in cert
                // history
                for ( var i = 0; i < $scope.cert.status.length; i++) {
                    if (($scope.cert.status[i].type === "SENT") && ($scope.cert.status[i].target === $scope.selectedRecipientId)) {
                        return true;
                    }
                }
                return false;
            }
            $scope.backToViewCertificate = function() {
                $location.path("/view");
            }

            $scope.pagefocus = true;
        } ]);

angular.module('controllers.fk7263').controller('ErrorCtrl', [ '$scope', '$routeParams', function SentCertWizardCtrl($scope, $routeParams) {

    // set a default if no errorCode is given in routeparams
    $scope.errorCode = $routeParams.errorCode || "generic";
    $scope.pagefocus = true;
} ]);
