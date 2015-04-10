angular.module('ts-bas').controller('ts-bas.ViewCertCtrl',
    [ '$log', '$rootScope', '$stateParams', '$scope', '$cookieStore', 'common.CertificateService',
        'common.ManageCertView', 'common.messageService', 'webcert.ManageCertificate','common.User',
        'common.IntygCopyRequestModel',
        function($log, $rootScope, $stateParams, $scope, $cookieStore, CertificateService, ManageCertView,
            messageService, ManageCertificate, User, IntygCopyRequestModel) {
            'use strict';

            /*********************************************************************
             * Page state
             *********************************************************************/
            
            var intygType = 'ts-bas';

            $scope.user = { lakare: User.getUserContext().lakare };
            $scope.cert = {};
            $scope.widgetState = {
                doneLoading: false,
                activeErrorMessageKey: null,
                showTemplate: true,
                printStatus: 'notloaded'
            };

            $scope.view = {
                intygAvser: '',
                bedomning: ''
            };

            // expose calculated static link for pdf download
            $scope.downloadAsPdfLink = '/moduleapi/intyg/ts-bas/' + $stateParams.certificateId + '/pdf';

            // Decide if helptext related to field 1.a) - 1.c)
            $scope.achelptext = false;

            $scope.certProperties = {
                isSent: false,
                isRevoked: false
            };

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
                CertificateService.getCertificate($stateParams.certificateId, intygType, function(result) {
                    $scope.widgetState.doneLoading = true;
                    if (result !== null) {
                        $scope.cert = result.contents;
                        if ($scope.cert.syn.synfaltsdefekter === true || $scope.cert.syn.nattblindhet === true ||
                            $scope.cert.syn.progressivOgonsjukdom === true) {
                            $scope.achelptext = true;
                        }
                        $scope.view.intygAvser = createKorkortstypListString($scope.cert.intygAvser.korkortstyp);
                        $scope.view.bedomning = createKorkortstypListString($scope.cert.bedomning.korkortstyp);

                        $rootScope.$emit('ts-bas.ViewCertCtrl.load', result);
                        $scope.certProperties.isSent = ManageCertView.isSentToTarget(result.statuses, 'TS');
                        $scope.certProperties.isRevoked = ManageCertView.isRevoked(result.statuses);
                        if($scope.certProperties.isRevoked) {
                            $scope.widgetState.printStatus = 'revoked';
                        } else {
                            $scope.widgetState.printStatus = 'signed';
                        }

                        $scope.pdfUrl = '/moduleapi/intyg/ts-bas/' + $scope.cert.id + '/pdf';

                    } else {
                        $log.debug('Got error while loading cert - invalid data');
                        $scope.widgetState.activeErrorMessageKey = 'common.error.data_not_found';
                    }
                }, function(error) {
                    $scope.widgetState.doneLoading = true;
                    $log.debug('Got error while loading cert: ' + error.message);
                    if (error.errorCode === 'DATA_NOT_FOUND') {
                        $scope.widgetState.activeErrorMessageKey = 'common.error.data_not_found';
                    } else {
                        $scope.widgetState.activeErrorMessageKey = 'common.error.data_not_found';
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
