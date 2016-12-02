/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

angular.module('fk7263').controller('fk7263.ViewCertCtrl',
    [ '$location', '$log', '$rootScope', '$stateParams', '$scope', 'common.IntygListService',
        'common.IntygService', 'common.dialogService', 'common.messageService', 'common.moduleService', 'fk7263.ViewStateService',
        function($location, $log, $rootScope, $stateParams, $scope, IntygListService,
            IntygService, dialogService, messageService, moduleService, ViewState) {
            'use strict';

            $rootScope.cert = {};

            ViewState.reset();
            ViewState.cert = {};
            $scope.viewState = ViewState;

            $scope.messageService = messageService;
            $scope.moduleService = moduleService;

            $scope.doneLoading = false;
            $scope.visibleStatuses = [ 'SENT' ];
            $scope.dialog = {
                acceptprogressdone: true,
                focus: false
            };

            // Navigation

            $scope.send = function() {
                $location.path('/fk7263/recipients').search({ module: 'fk7263', defaultRecipient: 'FK'});
            };

            $scope.showStatusHistory = function() {
                $location.path('/fk7263/statushistory');
            };

            $scope.backToViewCertificate = function() {
                $location.path('/fk7263/view/' + $stateParams.certificateId);
            };

            $scope.customizeCertificate = function() {
                $location.path('/fk7263/customize/' + $stateParams.certificateId);
            };

            // expose calculated static link for pdf download
            $scope.downloadAsPdfLink = '/moduleapi/certificate/' + 'fk7263' + '/' + $stateParams.certificateId + '/pdf';


            // Archive dialog
            var archiveDialog = {};

            $scope.archiveSelected = function() {
                var item = $scope.cert;
                $log.debug('archive ' + item.id);
                $scope.dialog.acceptprogressdone = false;
                IntygListService.archiveCertificate(item, function(fromServer, oldItem) {
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


            // Load certificate

            $scope.userVisibleStatusFilter = function(status) {
                for (var i = 0; i < $scope.visibleStatuses.length; i++) {
                    if (status.type === $scope.visibleStatuses[i]) {
                        return true;
                    }
                }
                return false;
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

            IntygService.getCertificate('fk7263', $stateParams.certificateId, function(result) {
                $scope.doneLoading = true;
                if (result !== null) {
                    ViewState.cert = result.utlatande;
                    ViewState.cert.filteredStatuses = $scope.filterStatuses(result.meta.statuses);
                    $rootScope.cert = ViewState.cert;
                } else {
                    // show error view
                    $location.path('/fk7263/visafel/certnotfound');
                }
            }, function() {
                $log.debug('got error');
                $location.path('/fk7263/visafel/certnotfound');
            });

            $scope.pagefocus = true;
        }]);
