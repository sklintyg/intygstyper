angular.module('fk7263').controller('fk7263.ViewCertCtrl',
    [ '$location', '$log', '$rootScope', '$stateParams', '$scope', 'minaintyg.listCertService',
        'common.certificateService', 'common.dialogService', 'common.messageService',
        function($location, $log, $rootScope, $stateParams, $scope, listCertService, certificateService, dialogService,
            messageService) {
            'use strict';

            $scope.cert = {};
            $rootScope.cert = {};

            $scope.messageService = messageService;

            $scope.doneLoading = false;

            $scope.send = function() {
                $location.path('/fk7263/recipients').search({ module: 'fk7263', defaultRecipient: 'FK'});
            };

            $scope.visibleStatuses = [ 'SENT' ];

            $scope.dialog = {
                acceptprogressdone: true,
                focus: false
            };

            var archiveDialog = {};

            $scope.archiveSelected = function() {
                var item = $scope.cert;
                $log.debug('archive ' + item.id);
                $scope.dialog.acceptprogressdone = false;
                listCertService.archiveCertificate(item, function(fromServer, oldItem) {
                    $log.debug('statusUpdate callback:' + fromServer);
                    if (fromServer !== null) {
                        // Better way to update the object?
                        oldItem.archived = fromServer.archived;
                        oldItem.status = fromServer.status;
                        oldItem.selected = false;
                        archiveDialog.close();
                        $scope.dialog.acceptprogressdone = true;
                        $location.path('#/start');
                    } else {
                        // show error view
                        $location.path('/fk7263/fel/couldnotarchivecert');
                    }
                });
            };

            // Archive dialog
            $scope.certToArchive = {};

            $scope.openArchiveDialog = function(cert) {
                $scope.certToArchive = cert;
                $scope.dialog.focus = true;
                archiveDialog = dialogService.showDialog($scope, {
                    dialogId: 'archive-confirmation-dialog',
                    titleId: 'inbox.archivemodal.header',
                    bodyTextId: 'inbox.archivemodal.text',
                    button1click: function() {
                        $log.debug('archive');
                        $scope.archiveSelected();
                    },
                    button1id: 'archive-button',
                    button1text: 'button.archive',
                    autoClose: false
                });
            };

            $scope.filterStatuses = function(statuses) {
                var result = [];
                if (!angular.isObject(statuses)) {
                    return result;
                }
                for (var i = 0; i < statuses.length; i++) {
                    if ($scope.userVisibleStatusFilter(statuses[i])) {
                        result.push(statuses[i]);
                    }
                }
                return result;
            };

            $scope.userVisibleStatusFilter = function(status) {
                for (var i = 0; i < $scope.visibleStatuses.length; i++) {
                    if (status.type === $scope.visibleStatuses[i]) {
                        return true;
                    }
                }
                return false;
            };

            $scope.showStatusHistory = function() {
                $location.path('/fk7263/statushistory');
            };

            $scope.backToViewCertificate = function() {
                $location.path('/fk7263/view/' + $stateParams.certificateId);
            };

            // expose calculated static link for pdf download
            $scope.downloadAsPdfLink = '/moduleapi/certificate/' + $stateParams.certificateId + '/pdf';

            certificateService.getCertificate($stateParams.certificateId, function(result) {
                $scope.doneLoading = true;
                if (result !== null) {
                    $scope.cert = result.utlatande;
                    $scope.cert.filteredStatuses = $scope.filterStatuses(result.meta.statuses);
                    $rootScope.cert = $scope.cert;
                } else {
                    // show error view
                    $location.path('/visafel/certnotfound');
                }
            }, function() {
                $log.debug('got error');
            });

            $scope.pagefocus = true;
        }]);
