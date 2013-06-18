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
            
            $scope.visibleStatuses = ['SENT','CANCELLED'];
            
            $scope.userVisibleStatusFilter = function(status) {
                return ($scope.visibleStatuses.indexOf(status.type) !== -1);
            };

            $scope.showStatusHistory = function() {
                $location.path("/statushistory");
            }
            
            $scope.backToViewCertificate = function() {
                $location.path("/view");
            }
            $scope.smittskydd = function() {
                if (angular.isObject($scope.cert.aktiviteter)) {
                    // collect all smittskydd actitivies
                    var smittskyddActivities = $scope.cert.aktiviteter.filter(function(aktivitet) {
                        return aktivitet.aktivitetskod == "AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA";
                    }).length;

                    if (smittskyddActivities > 0) {
                        return "yes";
                    } else {
                        return "no";
                    }
                }
            }
            // expose calculated static link for pdf download
            $scope.downloadAsPdfLink = $scope.MODULE_CONFIG.MI_COMMON_API_CONTEXT_PATH + $scope.MODULE_CONFIG.CERT_ID_PARAMETER + "/pdf";

            console.log("Loading certificate " + $scope.MODULE_CONFIG.CERT_ID_PARAMETER);
            certService.getCertificate($scope.MODULE_CONFIG.CERT_ID_PARAMETER, function(result) {
                $scope.doneLoading = true;
                if (result != null) {
                    $scope.cert = result;
                    $rootScope.cert = result;
                } else {
                    // show error view
                    $location.path("/fel/certnotfound");
                }
            });
        } ]);

angular.module('controllers.fk7263').controller('SentCertWizardCtrl',
        [ '$scope', '$filter', '$location', '$rootScope', '$routeParams', 'certService', function SentCertWizardCtrl($scope, $filter, $location, $rootScope, $routeParams, certService) {
            $scope.doneLoading = true;
            // Get active certificate from rootscope (passed from previous
            // controller)
            $scope.cert = $rootScope.cert;
            if (!angular.isObject($scope.cert)) {
                console.log("No certificate in rootScope");
                $location.path("/fel/certnotfound");
                return;
            }
            // expose calculated static link for pdf download
            $scope.downloadAsPdfLink = $scope.MODULE_CONFIG.MI_COMMON_API_CONTEXT_PATH + $scope.MODULE_CONFIG.CERT_ID_PARAMETER + "/pdf";

            // Initialize recipient handling, default to FK
            $scope.selectedRecipientId = $rootScope.selectedRecipientId || "FK";
            //set selected recipeintID in rootscope to preserve state between controller instance invocations
            $rootScope.selectedRecipientId = $scope.selectedRecipientId;
            $scope.recipientList = [ {
                "id" : "FK",
                "recipientName" : "Försäkringskassan"
            }];

            $scope.getRecipientName = function (id) {
                for(var i=0;i<$scope.recipientList.length;i++) {
                    var recipient = $scope.recipientList[i];
                    if (recipient.id == id) {
                        return recipient.recipientName;
                    }
                };
            }

            $scope.confirmRecipientSelection = function() {
                // now we have a recipient selected, set the selection in rootscope
                $rootScope.selectedRecipientId = $scope.selectedRecipientId;
                $location.path("/summary");
            }

            $scope.confirmAndSend = function() {
                $scope.doneLoading = false;
                console.log("Sending certificate " + $scope.MODULE_CONFIG.CERT_ID_PARAMETER);
                certService.sendCertificate($scope.MODULE_CONFIG.CERT_ID_PARAMETER, $scope.selectedRecipientId, function(result) {
                    $scope.doneLoading = true;
                    if (result != null && result.resultCode == "sent") {
                        console.log("sent successfully");
                        $location.path("/sent");
                    } else {
                        // show error view
                        $location.path("/fel/couldnotsend");
                    }
                });
                
            }
            
            $scope.backToViewCertificate = function() {
                $location.path("/view");
            }

        } ]);

angular.module('controllers.fk7263').controller('ErrorCtrl',
        [ '$scope', '$routeParams', function SentCertWizardCtrl($scope, $routeParams) {

            //set a default if no errorCode is given in routeparams
            $scope.errorCode = $routeParams.errorCode || "generic";
            

        } ]);
