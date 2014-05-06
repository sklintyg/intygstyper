define([], function() {
    'use strict';

    return [ '$scope', '$filter', '$location', '$routeParams', 'ts-bas.certificateService', 'listCertService', 'dialogService', '$log', '$rootScope',
            function($scope, $filter, $location, $routeParams, certificateService, listCertService, dialogService, $log, $rootScope) {
                $scope.cert = {};
                $rootScope.cert = {};

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
                    $location.path('/ts-bas/recipients');
                };

                $scope.opts = {
                    backdropFade: true,
                    dialogFade: true
                };

                $scope.intygAvser = "";
                $scope.intygAvserList = [];

                $scope.$watch('cert.intygAvser.korkortstyp', function() {
                    if (!$scope.cert || !$scope.cert.intygAvser || !$scope.cert.intygAvser.korkortstyp) {
                        return;
                    }
                    angular.forEach($scope.cert.intygAvser.korkortstyp, function(key) {
                        if (key.selected) {
                            this.push(key);
                        }
                    }, $scope.intygAvserList );
                    
                    for (var i = 0; i < $scope.intygAvserList.length; i++) {
                        if (i < $scope.intygAvserList.length-1) {
                                $scope.intygAvser += $scope.intygAvserList[i].type + (', ');
                        }
                        else {
                            $scope.intygAvser += $scope.intygAvserList[i].type;
                        }
                    }
                }, true);

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
                $scope.downloadAsPdfLink = '/moduleapi/certificate/' + $routeParams.certificateId + '/pdf';

                // Decide if helptext related to field 1.a) - 1.c)
                $scope.achelptext = false;

                certificateService.getCertificate($routeParams.certificateId, function(result) {
                    $scope.doneLoading = true;
                    if (result !== null) {
                        $scope.cert = result;
                        $rootScope.cert = result;
                        if (result.syn.synfaltsdefekter === true || result.syn.nattblindhet === true || result.syn.progressivogonsjukdom === true) {
                            $scope.achelptext = true;
                        }
                    } else {
                        // show error view
                        $location.path('/fel');
                    }
                }, function(error) {
                    $log.debug(error);
                });
            } ];
});
