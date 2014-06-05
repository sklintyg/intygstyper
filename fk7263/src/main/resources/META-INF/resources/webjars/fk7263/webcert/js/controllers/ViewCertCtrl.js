define([
    'angular',
    'webjars/common/webcert/js/services/CertificateService',
    'webjars/common/webcert/js/services/ManageCertView'
], function(angular, CertificateService, ManageCertView) {
    'use strict';

    var ManageCertificate = require('services/ManageCertificate');

    var moduleName = 'fk7263.ViewCertCtrl';
    angular.module(moduleName, [ManageCertView, ManageCertificate]).
        controller(moduleName, ['$log', '$rootScope', '$routeParams', '$scope', '$cookieStore', CertificateService, ManageCertView, ManageCertificate,
            function($log, $rootScope, $routeParams, $scope, $cookieStore, CertificateService, ManageCertView, ManageCertificate) {

                // Copy dialog setup
                var COPY_DIALOG_COOKIE = 'wc.dontShowCopyDialog';
                var copyDialog = {
                    isOpen: false
                };
                $scope.dialog = {
                    acceptprogressdone: true,
                    focus: false,
                    errormessageid: 'error.failedtocopyintyg',
                    showerror: false,
                    dontShowCopyInfo: $cookieStore.get(COPY_DIALOG_COOKIE)
                };

                // Page setup
                $scope.cert = {};
                $scope.cert.filledAlways = true;
                $scope.widgetState = {
                    doneLoading: false,
                    activeErrorMessageKey: null,
                    showTemplate: true
                };

                $scope.certProperties = {
                    isSent: false,
                    isRevoked: false
                };

                $log.debug('Loading certificate ' + $routeParams.certificateId);
                CertificateService.getCertificate($routeParams.certificateId, function(result) {
                    $scope.widgetState.doneLoading = true;
                    if (result !== null && result !== '') {
                        $scope.cert = result.contents;
                        $rootScope.$emit(moduleName + '.load', result.metaData);
                        $scope.certProperties.isSent = ManageCertView.isSentToTarget(result.metaData.statuses, 'FK');
                        $scope.certProperties.isRevoked = ManageCertView.isRevoked(result.metaData.statuses);

                        $scope.pdfUrl = '/moduleapi/intyg/signed/' + $scope.cert.id + '/pdf';
                    } else {
                        $scope.widgetState.activeErrorMessageKey = 'error.could_not_load_cert';
                    }
                }, function(error) {
                    $scope.widgetState.doneLoading = true;
                    if (error.errorCode === 'DATA_NOT_FOUND') {
                        $scope.widgetState.activeErrorMessageKey = 'error.data_not_found';
                    } else {
                        $scope.widgetState.activeErrorMessageKey = 'error.could_not_load_cert';
                    }
                    $log.debug('Got error while loading cert');
                    $log.debug(error.message);
                });

                /**
                 * Private
                 */


                /**
                 * Exposed functions
                 * @param cert
                 */
                ManageCertificate.initSend($scope);
                $scope.send = function(cert) {
                    ManageCertificate.send($scope, cert, 'fk7263.label.send');
                };

                $scope.copy = function(cert) {
                    copyDialog = ManageCertificate.copy($scope, cert, copyDialog, COPY_DIALOG_COOKIE);
                };
            }
        ]);

    return moduleName;
});
