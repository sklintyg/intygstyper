define([
    'angular'
], function(angular) {
    'use strict';

    var moduleName = 'wc.ManageCertView';

    /**
     * Common certificate management methods between certificate modules
     */
    var ManageCertView = angular.module(moduleName, []);

    ManageCertView.factory('ManageCertView',
        [ '$http', '$log', '$location', '$route', '$routeParams', '$timeout', 'wcDialogService', 'CertificateService',
            'statService',
            function($http, $log, $location, $route, $routeParams, $timeout, wcDialogService, CertificateService,
                statService) {

                //var member;

                /**
                 * Save draft to webcert
                 * @param $scope
                 * @private
                 */

                function _save($scope) {
                    CertificateService.saveDraft($routeParams.certificateId, $scope.cert,
                        function(data) {

                            $scope.certForm.$setPristine();

                            $scope.validationMessagesGrouped = {};
                            $scope.validationMessages = [];

                            if (data.status === 'COMPLETE') {
                                $scope.isComplete = true;
                            } else {
                                $scope.isComplete = false;
                                $scope.validationMessages = data.messages;

                                angular.forEach(data.messages, function(message) {
                                    var field = message.field;
                                    var parts = field.split('.');
                                    var section;
                                    if (parts.length > 0) {
                                        section = parts[0].toLowerCase();

                                        if ($scope.validationMessagesGrouped[section]) {
                                            $scope.validationMessagesGrouped[section].push(message);
                                        } else {
                                            $scope.validationMessagesGrouped[section] = [message];
                                        }
                                    }
                                });
                            }
                        }, function() {
                            // Show error message if save fails
                        }
                    );
                }


                /**
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
                                        $scope.dialog.errormessageid =
                                            ('error.message.' + error.errorCode).toLowerCase();
                                    }
                                }
                            });
                        },
                        button1text: 'common.delete',
                        button2text: 'common.cancel',
                        autoClose: false
                    });
                }

                var _confirmSign = function(certificateId, $scope, onSuccess) {
                    $log.debug('sign draft ');
                    $scope.dialog.acceptprogressdone = false;
                    $scope.dialog.showerror = false;

                    CertificateService.signDraft(certificateId, function(data) {

                        (function checkStatus() {
                            CertificateService.getSignStatus(data.id, function(data) {
                                if ('BEARBETAR' === data.status) {
                                    $scope._timer = $timeout(checkStatus, 1000);
                                } else if ('SIGNERAD' === data.status) {
                                    $scope.dialog.acceptprogressdone = true;
                                    onSuccess();
                                } else {
                                    $scope.dialog.acceptprogressdone = true;
                                    $scope.dialog.showerror = true;
                                    $scope.dialog.errormessageid = 'common.error.signerror';
                                }
                            });
                        })();

                    }, function(error) {
                        $scope.dialog.acceptprogressdone = true;
                        $scope.dialog.showerror = true;
                        if (error.errorCode === 'DATA_NOT_FOUND') {
                            $scope.dialog.errormessageid = 'common.error.certificatenotfound';
                        } else if (error.errorCode === 'INVALID_STATE') {
                            $scope.dialog.errormessageid = 'common.error.certificateinvalid';
                        } else if (error === '') {
                            $scope.dialog.errormessageid = 'common.error.cantconnect';
                        } else {
                            $scope.dialog.errormessageid = ('error.message.' + error.errorCode).toLowerCase();
                        }
                    });
                };

                function _sign($scope) {
                    var bodyText = 'Är du säker på att du vill signera intyget?';
                    var dialog = wcDialogService.showDialog($scope, {
                        dialogId: 'confirm-sign',
                        titleId: 'label.confirmsign',
                        bodyText: bodyText,
                        autoClose: false,
                        button1id: 'confirm-signera-utkast-button',

                        button1click: function() {
                            _confirmSign($routeParams.certificateId, $scope, function() {
                                dialog.close();
                                $route.reload();
                                statService.refreshStat();
                            });
                        },
                        button1text: 'common.sign',
                        button2click: function() {
                            if ($scope._timer) {
                                $timeout.cancel($scope._timer);
                            }
                            $scope.dialog.acceptprogressdone = true;
                        },
                        button2text: 'common.cancel'
                    });
                }

                // Return public API for the service
                return {
                    save: _save,
                    discard: _discard,
                    sign: _sign,

                    __test__: {
                        confirmSign: _confirmSign
                    }
                };
            }
        ]);

    return moduleName;
});
