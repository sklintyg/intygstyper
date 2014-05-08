define([
    'angular'
], function(angular) {
    'use strict';

    var moduleName = 'wc.ManageCertView';

    /**
     * Common certificate management methods between certificate modules
     */
    var ManageCertView = angular.module(moduleName, []);

    ManageCertView.factory('ManageCertView', [ '$http', '$log', '$location', '$routeParams', 'wcDialogService', 'CertificateService', 'statService',
        function($http, $log, $location, $routeParams, wcDialogService, CertificateService, statService) {

            //var member;

            /*
             * Discard a certificate draft
             */
            function _discard($scope) {

                var bodyText = 'Är du säker på att du vill radera utkastet? Intyget kommer tas bort och kan inte längre återskapas i Webcert.';
                $scope.dialog = {
                    acceptprogressdone: false,
                    errormessageid: 'Error',
                    showerror: false
                };

                var draftDeleteDialog = {};
                draftDeleteDialog = wcDialogService.showDialog($scope, {
                    dialogId: 'confirm-draft-delete',
                    titleId: 'label.confirmaddress',
                    bodyText: bodyText,
                    button1id: 'confirm-draft-delete-button',

                    button1click: function() {
                        $log.debug('delete draft ');
                        $scope.dialog.acceptprogressdone = false;
                        CertificateService.discardDraft($routeParams.certificateId, function() {
                            $scope.dialog.acceptprogressdone = true;
                            statService.refreshStat(); // Update statistics to reflect change
                            $location.path('/unsigned');
                            draftDeleteDialog.close();
                        }, function(error) {
                            $scope.dialog.acceptprogressdone = true;
                            if (error.errorCode === 'DATA_NOT_FOUND') { // Godtagbart, intyget var redan borta.
                                statService.refreshStat(); // Update statistics to reflect change
                                $location.path('/unsigned');
                            } else {
                                $scope.dialog.showerror = true;
                                if (error === '') {
                                    $scope.dialog.errormessageid = 'common.error.cantconnect';
                                } else {
                                    $scope.dialog.errormessageid = ('error.message.' + error.errorCode).toLowerCase();
                                }
                            }
                        });
                    },
                    button1text: 'common.delete',
                    button2text: 'common.cancel',
                    autoClose: false
                });
            }

            // Return public API for the service
            return {
                discard: _discard
            };
        }
    ]);

    return moduleName;
});
