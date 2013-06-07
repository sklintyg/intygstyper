'use strict';

/* Controllers */
angular.module('controllers.fk7263', []);
angular.module('controllers.fk7263').controller('ViewCertCtrl',
        [ '$scope', '$filter', '$location', '$rootScope', 'certService', function ViewCertCtrl($scope, $filter, $location, $rootScope, certService) {
            $scope.cert = {};

            $scope.doneLoading = false;

            $scope.send = function() {
                // $scope.shouldBeOpen = true;

                $location.path("/recipients");
            };

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
                    $location.path("/fel");
                }
            });
        } ]);

angular.module('controllers.fk7263').controller('SentCertWizardCtrl',
        [ '$scope', '$filter', '$location', '$rootScope', 'certService', function SentCertWizardCtrl($scope, $filter, $location, $rootScope, certService) {
            // Get active certificate from rootscope (passed from previous
            // controller)
            $scope.cert = $rootScope.cert;
            // expose calculated static link for pdf download
            $scope.downloadAsPdfLink = $scope.MODULE_CONFIG.MI_COMMON_API_CONTEXT_PATH + $scope.MODULE_CONFIG.CERT_ID_PARAMETER + "/pdf";

            // Initialize recipient handling
            // TODO: get recipientlist from serverside config?
            $scope.selectedRecipient = "fk";
            $scope.recipientList = [ {
                id : "fk",
                recipientName : "Försäkringskassan"
            }, {
                id : "other",
                recipientName : "Annan mottagare"
            } ];

            $scope.getSelectedRecipient = function (id) {
                angular.forEach($scope.recipientList, function(recipient) {
                    if (recipient.id == id) {
                        return recipient;
                    }
                });
            }

            $scope.confirmRecipientSelection = function() {
                // now we have a recipient selected
                $rootScope.selectedRecipient = $scope.getSelectedRecipient($scope.selectedRecipient);
                $location.path("/summary");
            }

            $scope.confirmAndSend = function() {
                // now we have a recipient selected
                $rootScope.selectedRecipient = {
                    id : $scope.selectedRecipient,
                    recipientName : getRecipientName($scope.selectedRecipient)
                }
                $location.path("/sent");
            }

        } ]);
