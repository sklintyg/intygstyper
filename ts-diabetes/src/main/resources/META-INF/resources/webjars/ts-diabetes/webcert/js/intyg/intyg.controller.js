angular.module('ts-diabetes').controller('ts-diabetes.IntygController',
    [ '$location', '$log', '$rootScope', '$stateParams', '$scope', '$cookieStore', 'common.CertificateService',
        'common.ManageCertView', 'common.messageService', 'webcert.ManageCertificate','common.UserModel','common.IntygCopyRequestModel',
        'ts-diabetes.IntygController.ViewStateService',
        function($location, $log, $rootScope, $stateParams, $scope, $cookieStore, CertificateService, ManageCertView,
            messageService, ManageCertificate, UserModel, IntygCopyRequestModel, ViewState) {
            'use strict';

            /*********************************************************************
             * Page state
             *********************************************************************/

            ViewState.reset();
            $scope.viewState = ViewState;

            $scope.user = UserModel.userContext;
            $scope.cert = {};

            /*********************************************************************
             * Private support functions
             *********************************************************************/

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

            function loadCertificate() {
                CertificateService.getCertificate($stateParams.certificateId, ViewState.common.intyg.typ, function(result) {
                    ViewState.common.doneLoading = true;
                    if (result !== null && result !== '') {
                        $scope.cert = result.contents;

                        ViewState.intygAvser = createKorkortstypListString($scope.cert.intygAvser.korkortstyp);
                        ViewState.bedomning = createKorkortstypListString($scope.cert.bedomning.korkortstyp);

                        $rootScope.$emit('ts-diabetes.ViewCertCtrl.load', result);
                        ViewState.common.intyg.isSent = ManageCertView.isSentToTarget(result.statuses, 'TS');
                        ViewState.common.intyg.isRevoked = ManageCertView.isRevoked(result.statuses);
                        if(ViewState.common.intyg.isRevoked) {
                            ViewState.common.printStatus = 'revoked';
                        } else {
                            ViewState.common.printStatus = 'signed';
                        }

                        $scope.pdfUrl = '/moduleapi/intyg/ts-diabetes/' + $scope.cert.id + '/pdf';

                    } else {
                        ViewState.common.activeErrorMessageKey = 'common.error.data_not_found';
                    }
                }, function(error) {
                    ViewState.common.doneLoading = true;
                    $log.debug('got error' + error.message);
                    if (error.errorCode === 'DATA_NOT_FOUND') {
                        ViewState.common.activeErrorMessageKey = 'common.error.data_not_found';
                    } else {
                        ViewState.common.activeErrorMessageKey = 'common.error.data_not_found';
                    }
                });
            }

            /*********************************************************************
             * Exposed scope interaction functions
             *********************************************************************/

            /*********************************************************************
             * Page load
             *********************************************************************/
            loadCertificate();

            $scope.$on('loadCertificate', loadCertificate);
        }]);
