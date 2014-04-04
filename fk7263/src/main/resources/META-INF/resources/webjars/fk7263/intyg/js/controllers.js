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
            if (!angular.isObject(statuses)) {
                return result;
            }
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


        // expose calculated static link for pdf download
        $scope.downloadAsPdfLink = $scope.MODULE_CONFIG.MI_COMMON_API_CONTEXT_PATH + $scope.MODULE_CONFIG.CERT_ID_PARAMETER + "/pdf";

        certService.getCertificate($scope.MODULE_CONFIG.CERT_ID_PARAMETER, function(result) {
            $scope.doneLoading = true;
            if (result != null) {
                result.filteredStatuses = $scope.filterStatuses(result.status);
                // added calculated img src for certificate, beacuse for
                // some reason ng-src with "/img/{{cert.typ }}.png" would
                // evaluate first to "img/.png" before correct value = 404
                result.typ_image = "/img/fk7263.png";

                $scope.cert = result;
                $rootScope.cert = result;
            } else {
                // show error view
                $location.path("/visafel/certnotfound");
            }
        });

        $scope.pagefocus = true;
    } ]);

angular.module('controllers.fk7263').controller('SentCertWizardCtrl',
    [ '$scope', '$filter', '$location', '$rootScope', '$routeParams', 'certService', function SentCertWizardCtrl($scope, $filter, $location, $rootScope, $routeParams, certService) {
        $scope.sendingInProgress = false;
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
            $scope.sendingInProgress = true;
            certService.sendCertificate($scope.MODULE_CONFIG.CERT_ID_PARAMETER, $scope.selectedRecipientId, function(result) {
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
    } ]);

angular.module('controllers.fk7263').controller('ErrorCtrl', [ '$scope', '$routeParams', '$route', function SentCertWizardCtrl($scope, $routeParams, $route) {

    // set a default if no errorCode is given in routeparams
    $scope.errorCode = $routeParams.errorCode || "generic";
    $scope.backLink = $route.current.backLink || "#view";
    $scope.pagefocus = true;
} ]);
