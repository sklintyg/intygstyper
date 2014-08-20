define([
    'angular'
], function(angular) {
    'use strict';

    return ['$scope', '$filter', '$location', '$rootScope', '$routeParams', 'sendCertService', 'listCertService',
        '$log',
        function SentCertWizardCtrl($scope, $filter, $location, $rootScope, $routeParams, sendCertService,
            listCertService, $log) {
            $scope.sendingInProgress = false;
            // Get active certificate from rootscope (passed from previous
            // controller)
            $scope.cert = $rootScope.cert;
            if (!angular.isObject($scope.cert)) {
                $location.path('/ts-bas/fel/certnotfound');
                return;
            }
            // expose calculated static link for pdf download
            $scope.downloadAsPdfLink = '/moduleapi/certificate/' + $scope.cert.id + '/pdf';

            // Initialize recipient handling, default to TS
            $scope.selectedRecipientId = 'ts';
            // set selected recipeintID in rootscope to preserve state between
            // controller instance invocations
            $rootScope.selectedRecipientId = $scope.selectedRecipientId;

            $scope.recipientList = [];
            sendCertService.getRecipients('ts-bas', function(result) {
                if (result !== null && result.length > 0) {
                    for (var i = 0; i < result.length; ++i) {
                        $scope.recipientList[i] = {
                            'id': result[i].id,
                            'recipientName': result[i].name
                        }
                    }
                } else {
                    // show error view
                    $location.path('/ts-bas/fel/failedreceiverecipients');
                }
            }, function(result) {
                // show error view
                $location.path('/ts-bas/fel/failedreceiverecipients');
            });

            $scope.getRecipientName = function(id) {
                for (var i = 0; i < $scope.recipientList.length; i++) {
                    var recipient = $scope.recipientList[i];
                    if (recipient.id === id) {
                        return recipient.recipientName;
                    }
                }
            };

            $scope.confirmRecipientSelection = function() {
                // now we have a recipient selected, set the selection in
                // rootscope
                $log.debug('rootscope got: ' + $rootScope.selectedRecipientId);
                $rootScope.selectedRecipientId = $scope.selectedRecipientId;
                $location.path('/ts-bas/summary');
            };

            $scope.confirmAndSend = function() {
                $scope.sendingInProgress = true;
                sendCertService.sendCertificate($scope.cert.id, $scope.selectedRecipientId, function(result) {
                    $scope.sendingInProgress = false;
                    if (result !== null && result.resultCode === 'sent') {
                        listCertService.emptyCache();
                        $location.path('/ts-bas/sent');
                    } else {
                        // show error view
                        $location.path('/ts-bas/fel/couldnotsend');
                    }
                });
            };

            $scope.alreadySentToRecipient = function() {
                // check if selected recipient exists with SENT status in cert history
                if (angular.isObject($scope.cert.status)) {
                    for (var i = 0; i < $scope.cert.status.length; i++) {
                        if (($scope.cert.status[i].type === 'SENT') &&
                            ($scope.cert.status[i].target === $scope.selectedRecipientId)) {
                            return true;
                        }
                    }
                }
                return false;
            };

            $scope.backToViewCertificate = function() {
                $location.path('/ts-bas/view/' + $scope.cert.id);
            };

            $scope.pagefocus = true;
        }
    ];
});
