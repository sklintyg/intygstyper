define(['angular'], function(angular) {
    'use strict';

    return [ '$scope', '$filter', '$location', '$stateParams', 'CertificateService', 'listCertService', 'dialogService', '$log', '$rootScope',
            function($scope, $filter, $location, $stateParams, certificateService, listCertService, dialogService, $log, $rootScope) {
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
                    $location.path('/rli/recipients');
                };

                $scope.opts = {
                    backdropFade: true,
                    dialogFade: true
                };

                $scope.intygAvser = '';
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

                var dialogRequestModel = {
                    acceptprogressdone: true,
                    focus: false
                };


                // Archive dialog
                $scope.certToArchive = {};
                var archiveDialog;

                $scope.openArchiveDialog = function(cert) {
                    $scope.certToArchive = cert;
                    dialogRequestModel.focus = true;
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
                        autoClose: false,
                        dialogRequestModel: dialogRequestModel
                    });
                };

                // expose calculated static link for pdf download
                $scope.downloadAsPdfLink = '/moduleapi/certificate/' + $stateParams.certificateId + '/pdf';

                // Decide if helptext related to field 1.a) - 1.c)
                $scope.achelptext = false;

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
                };

                $scope.visibleStatuses = [ 'SENT' ];

                $scope.userVisibleStatusFilter = function(status) {
                    for ( var i = 0; i < $scope.visibleStatuses.length; i++) {
                        if (status.type === $scope.visibleStatuses[i]) {
                            return true;
                        }
                    }
                    return false;
                };

                certificateService.getCertificate($stateParams.certificateId, function(result) {
                    $scope.doneLoading = true;
                    if (result !== null) {
                        $scope.cert = result.utlatande;
                        $scope.cert.status = $scope.filterStatuses(result.meta.statuses);
                        $rootScope.cert = $scope.cert;
                        if ($scope.cert.syn.synfaltsdefekter === true || $scope.cert.syn.nattblindhet === true ||
                                $scope.cert.syn.progressivogonsjukdom === true) {
                            $scope.achelptext = true;
                        }
                    } else {
                        // show error view
                        $location.path('/fel');
                    }
                }, function(error) {
                    $log.debug(error);
                });
            }
        ];
});
