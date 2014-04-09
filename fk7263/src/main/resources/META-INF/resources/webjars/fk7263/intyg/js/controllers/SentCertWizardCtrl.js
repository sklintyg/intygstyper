define(
    [],
    function() {
    'use strict';
    return [ '$scope', '$filter', '$location', '$rootScope', '$routeParams', 'fk7263.certificateService', 
        function SentCertWizardCtrl($scope, $filter, $location, $rootScope, $routeParams, certService) {
            $scope.sendingInProgress = false;
            // Get active certificate from rootscope (passed from previous
            // controller)
            $scope.cert = $rootScope.cert;
            if (!angular.isObject($scope.cert)) {
                $location.path("/fel/certnotfound");
                return;
            }
            // expose calculated static link for pdf download
            $scope.downloadAsPdfLink = $scope.MODULE_CONFIG.MI_COMMON_API_CONTEXT_PATH + $routeParams.certificateId + "/pdf";

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
                $scope.sendingInProgress = true;
                certService.sendCertificate($routeParams.certificateId, $scope.selectedRecipientId, function(result) {
                    $scope.sendingInProgress = false;
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
                if (angular.isObject($scope.cert.status)) {
                    for ( var i = 0; i < $scope.cert.status.length; i++) {
                        if (($scope.cert.status[i].type === "SENT") && ($scope.cert.status[i].target === $scope.selectedRecipientId)) {
                            return true;
                        }
                    }
                }
                return false;
            }
            $scope.backToViewCertificate = function() {
                $location.path("/view");
            }

            $scope.pagefocus = true;
        } ];
    });

