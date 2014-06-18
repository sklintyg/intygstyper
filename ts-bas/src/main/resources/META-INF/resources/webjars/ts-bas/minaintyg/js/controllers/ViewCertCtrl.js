define([
    'angular'
], function(angular) {
    'use strict';

    return [ '$scope', '$filter', '$location', '$routeParams', 'CertificateService', 'listCertService', 'dialogService',
        '$log', '$rootScope',
        function($scope, $filter, $location, $routeParams, certificateService, listCertService, dialogService, $log,
            $rootScope) {
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
                }, $scope.intygAvserList);

                for (var i = 0; i < $scope.intygAvserList.length; i++) {
                    if (i < $scope.intygAvserList.length - 1) {
                        $scope.intygAvser += $scope.intygAvserList[i].type + (', ');
                    }
                    else {
                        $scope.intygAvser += $scope.intygAvserList[i].type;
                    }
                }
            }, true);

            //Make a printable list of Befattningar (which as of yet consists of un-readable codes...)
            $scope.befattningar = '';
            $scope.updateBefattningar = function (befattningar) {
                var result = '';
                for (var i = 0; i < befattningar.length; i++) {
                    if (i < befattningar.length-1) {
                        result += befattningar[i] + (', ');
                    } else {
                        result += befattningar[i];
                    }
                }
                return result;
            };

            //Make a printable list of Specialiteter
            $scope.specialiteter = '';
            $scope.updateSpecialiteter = function (specialiteter) {
                var result = '';
                for (var i = 0; i < specialiteter.length; i++) {
                    if (i < specialiteter.length-1) {
                        result += specialiteter[i] + (', ');
                    } else {
                        result += specialiteter[i];
                    }
                }
                return result;
            };

            $scope.dialog = {
                acceptprogressdone: true,
                focus: false
            };

            // Archive dialog
            $scope.certToArchive = {};
            var archiveDialog;

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

            certificateService.getCertificate($routeParams.certificateId, function(result) {
                $scope.doneLoading = true;
                if (result !== null) {
                    $scope.cert = result.utlatande;
                    $scope.cert.status = $scope.filterStatuses(result.meta.statuses);
                    $rootScope.cert = $scope.cert;
                    if ($scope.cert.syn.synfaltsdefekter === true || $scope.cert.syn.nattblindhet === true ||
                        $scope.cert.syn.progressivogonsjukdom === true) {
                        $scope.achelptext = true;
                    }
                    $scope.befattningar = $scope.updateBefattningar($scope.cert.skapadAv.befattningar);
                    $scope.specialiteter = $scope.updateSpecialiteter($scope.cert.skapadAv.specialiteter);
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
