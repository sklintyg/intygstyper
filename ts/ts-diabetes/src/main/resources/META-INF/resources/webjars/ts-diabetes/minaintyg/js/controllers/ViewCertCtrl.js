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

angular.module('ts-diabetes').controller('ts-diabetes.ViewCertCtrl',
    [ '$location', '$log', '$rootScope', '$stateParams', '$scope', 'common.IntygListService',
        'common.IntygService', 'common.dialogService', 'common.moduleService',
        function($location, $log, $rootScope, $stateParams, $scope, IntygListService, IntygService,
            dialogService, moduleService) {
            'use strict';

            $scope.cert = {};
            $rootScope.cert = {};

            $scope.moduleService = moduleService;

            $scope.doneLoading = false;
            $scope.shouldBeOpen = false;

            $scope.open = function() {
                $scope.shouldBeOpen = true;
            };

            $scope.close = function() {
                $scope.closeMsg = 'I was closed at: ' + new Date();
                $scope.shouldBeOpen = false;
            };

            $scope.send = function() {
                $location.path('/ts-diabetes/recipients').search({ module: 'ts-diabetes', defaultRecipient: 'TS'});
            };

            $scope.opts = {
                backdropFade: true,
                dialogFade: true
            };

            $scope.view = {
                intygAvser: '',
                bedomning: ''
            };

            function createKorkortstypListString(list) {

                var tempList = [];
                angular.forEach(list, function(key) {
                    if (key.selected) {
                        this.push(key);
                    }
                }, tempList);

                var resultString = '';
                for (var i = 0; i < tempList.length; i++) {
                    if (i < tempList.length - 1) {
                        resultString += tempList[i].type + (', ');
                    } else {
                        resultString += tempList[i].type;
                    }
                }

                return resultString;
            }

            //Make a printable list of Befattningar (which as of yet consists of un-readable codes...)
            $scope.befattningar = '';
            $scope.updateBefattningar = function(befattningar) {
                var result = '';
                if (befattningar !== undefined) {
                    for (var i = 0; i < befattningar.length; i++) {
                        if (i < befattningar.length - 1) {
                            result += befattningar[i] + (', ');
                        } else {
                            result += befattningar[i];
                        }
                    }
                }
                return result;
            };

            //Make a printable list of Specialiteter
            $scope.specialiteter = '';
            $scope.updateSpecialiteter = function(specialiteter) {
                var result = '';
                if (specialiteter !== undefined) {
                    for (var i = 0; i < specialiteter.length; i++) {
                        if (i < specialiteter.length - 1) {
                            result += specialiteter[i] + (', ');
                        } else {
                            result += specialiteter[i];
                        }
                    }
                }
                return result;
            };

            $scope.dialog = {
                acceptprogressdone: true,
                focus: false
            };

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
                        $location.path('/fel/couldnotarchivecert');
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

            // expose calculated static link for pdf download
            $scope.downloadAsPdfLink = '/moduleapi/certificate/' + 'ts-diabetes' + '/' + $stateParams.certificateId + '/pdf';

            // Decide if helptext related to field 1.a) - 1.c)
            $scope.achelptext = false;

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

            $scope.visibleStatuses = [ 'SENT' ];

            $scope.userVisibleStatusFilter = function(status) {
                for (var i = 0; i < $scope.visibleStatuses.length; i++) {
                    if (status.type === $scope.visibleStatuses[i]) {
                        return true;
                    }
                }
                return false;
            };

            IntygService.getCertificate('ts-diabetes', $stateParams.certificateId, function(result) {
                $scope.doneLoading = true;
                if (result !== null) {
                    $scope.cert = result.utlatande;
                    $scope.cert.status = $scope.filterStatuses(result.meta.statuses);
                    $rootScope.cert = $scope.cert;

                    $scope.view.befattningar = $scope.updateBefattningar($scope.cert.grundData.skapadAv.befattningar);
                    $scope.view.specialiteter = $scope.updateSpecialiteter($scope.cert.grundData.skapadAv.specialiteter);

                    $scope.view.intygAvser = createKorkortstypListString($scope.cert.intygAvser.korkortstyp);
                    $scope.view.bedomning = createKorkortstypListString($scope.cert.bedomning.korkortstyp);

                } else {
                    // show error view
                    $location.path('/ts-diabetes/visafel/certnotfound');
                }
            }, function(error) {
                $log.debug('got error' + error);
                $location.path('/ts-diabetes/visafel/certnotfound');
            });
        }]);
