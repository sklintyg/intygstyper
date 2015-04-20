angular.module('ts-bas').controller('ts-bas.IntygController',
    [ '$log', '$rootScope', '$stateParams', '$scope', '$cookieStore', 'common.CertificateService',
        'common.ManageCertView', 'common.messageService', 'webcert.ManageCertificate','common.User',
        'ts-bas.IntygController.ViewStateService',
        function($log, $rootScope, $stateParams, $scope, $cookieStore, CertificateService, ManageCertView,
            messageService, ManageCertificate, User, ViewState) {
            'use strict';

            /*********************************************************************
             * Page state
             *********************************************************************/
            
            ViewState.reset();
            $scope.viewState = ViewState;

            $scope.user = { lakare: User.getUserContext().lakare };
            $scope.cert = {};

            // expose calculated static link for pdf download
            $scope.downloadAsPdfLink = '/moduleapi/intyg/ts-bas/' + $stateParams.certificateId + '/pdf';

            // Decide if helptext related to field 1.a) - 1.c)
            $scope.achelptext = false;

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
                CertificateService.getCertificate($stateParams.certificateId, ViewState.common.intyg.type, function(result) {
                    ViewState.common.doneLoading = true;
                    if (result !== null) {
                        $scope.cert = result.contents;
                        if ($scope.cert.syn.synfaltsdefekter === true || $scope.cert.syn.nattblindhet === true ||
                            $scope.cert.syn.progressivOgonsjukdom === true) {
                            $scope.achelptext = true;
                        }
                        ViewState.intygAvser = createKorkortstypListString($scope.cert.intygAvser.korkortstyp);
                        ViewState.bedomning = createKorkortstypListString($scope.cert.bedomning.korkortstyp);

                        $rootScope.$emit('ts-bas.ViewCertCtrl.load', result);
                        ViewState.common.intyg.isSent = ManageCertView.isSentToTarget(result.statuses, 'TS');
                        ViewState.common.intyg.isRevoked = ManageCertView.isRevoked(result.statuses);
                        if(ViewState.common.intyg.isRevoked) {
                            ViewState.common.printStatus = 'revoked';
                        } else {
                            ViewState.common.printStatus = 'signed';
                        }

                        $scope.pdfUrl = '/moduleapi/intyg/ts-bas/' + $scope.cert.id + '/pdf';

                    } else {
                        $log.debug('Got error while loading cert - invalid data');
                        ViewState.common.activeErrorMessageKey = 'common.error.data_not_found';
                    }
                }, function(error) {
                    ViewState.common.doneLoading = true;
                    $log.debug('Got error while loading cert: ' + error.message);
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
