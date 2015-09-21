angular.module('ts-diabetes').controller('ts-diabetes.IntygController',
    [ '$location', '$log', '$rootScope', '$stateParams', '$scope', '$cookieStore',
        'common.IntygService', 'common.IntygProxy', 'common.messageService','common.UserModel','common.IntygCopyRequestModel',
        'ts-diabetes.IntygController.ViewStateService',
        function($location, $log, $rootScope, $stateParams, $scope, $cookieStore, IntygService, IntygProxy,
            messageService, UserModel, IntygCopyRequestModel, ViewState) {
            'use strict';

            /*********************************************************************
             * Page state
             *********************************************************************/

            ViewState.reset();
            $scope.viewState = ViewState;

            $scope.user = UserModel.user;

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
                IntygProxy.getIntyg($stateParams.certificateId, ViewState.common.intyg.type, function(result) {
                    ViewState.common.doneLoading = true;
                    if (result !== null && result !== '') {
                        ViewState.intygModel = result.contents;

                        ViewState.intygAvser = createKorkortstypListString(ViewState.intygModel.intygAvser.korkortstyp);
                        ViewState.bedomning = createKorkortstypListString(ViewState.intygModel.bedomning.korkortstyp);

                        $rootScope.$emit('ts-diabetes.ViewCertCtrl.load', result);
                        ViewState.common.intyg.isSent = IntygService.isSentToTarget(result.statuses, 'TS');
                        ViewState.common.intyg.isRevoked = IntygService.isRevoked(result.statuses);
                        if(ViewState.common.intyg.isRevoked) {
                            ViewState.common.printStatus = 'revoked';
                        } else {
                            ViewState.common.printStatus = 'signed';
                        }

                        $scope.pdfUrl = '/moduleapi/intyg/ts-diabetes/' + ViewState.intygModel.id + '/pdf';

                    } else {
                        ViewState.common.activeErrorMessageKey = 'common.error.data_not_found';
                    }
                }, function(error) {
                    ViewState.common.doneLoading = true;
                    ViewState.common.updateActiveError(error, $stateParams.signed);
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
