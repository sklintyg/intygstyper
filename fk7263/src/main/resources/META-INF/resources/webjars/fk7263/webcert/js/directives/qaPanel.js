/**
 * Created by BESA on 2015-03-05.
 */

/**
 * qaPanel directive. Common directive for both unhandled and handled questions/answers
 */
angular.module('fk7263').directive('qaPanel',
    [ '$window', '$log', '$timeout', 'common.User', 'common.fragaSvarCommonService', 'fk7263.fragaSvarService',
        'common.statService', 'common.dialogService',
        function($window, $log, $timeout, User, fragaSvarCommonService, fragaSvarService, statService, dialogService) {
            'use strict';

            return {
                restrict: 'A',
                transclude: true,
                replace: true,
                templateUrl: '/web/webjars/fk7263/webcert/js/directives/qaPanel.html',
                scope: {
                    panelId: '@',
                    qa: '=',
                    qaList: '=',
                    cert: '=',
                    certProperties: '='
                },
                controller: function($scope, $element, $attrs) {

                    $scope.handledPanel = $attrs.type === 'handled';

                    $scope.sendAnswer = function sendAnswer(qa) {
                        qa.updateInProgress = true; // trigger local spinner

                        fragaSvarService.saveAnswer(qa, 'fk7263', function(result) {
                            $log.debug('Got saveAnswer result:' + result);
                            qa.updateInProgress = false;
                            qa.activeErrorMessageKey = null;
                            if (result !== null) {
                                fragaSvarCommonService.decorateSingleItem(result);
                                // Create a proxyCopy
                                var proxyCopy = angular.copy(qa);
                                proxyCopy.proxyMessage = 'fk7263.fragasvar.answer.is.sent';
                                $scope.qaList.push(proxyCopy);
                                // update real item
                                angular.copy(result, qa);
                                //$scope.activeQA = qa.internReferens;
                                statService.refreshStat();
                            }
                        }, function(errorData) {
                            // show error view
                            qa.updateInProgress = false;
                            qa.activeErrorMessageKey = errorData.errorCode;
                        });
                    };

                    $scope.onVidareBefordradChange = function(qa) {
                        qa.forwardInProgress = true;

                        fragaSvarCommonService.setVidareBefordradState(qa.internReferens, 'fk7263', qa.vidarebefordrad,
                            function(result) {
                                qa.forwardInProgress = false;

                                if (result !== null) {
                                    qa.vidarebefordrad = result.vidarebefordrad;
                                } else {
                                    qa.vidarebefordrad = !qa.vidarebefordrad;
                                    dialogService.showErrorMessageDialog('Kunde inte markera/avmarkera frågan som vidarebefordrad. ' +
                                        'Försök gärna igen för att se om felet är tillfälligt. Annars kan du kontakta supporten');
                                }
                            });
                    };

                    $scope.updateAnsweredAsHandled = function(deferred, unhandledQas){
                        if(unhandledQas === undefined || unhandledQas.length === 0 ){
                            return;
                        }
                        fragaSvarService.closeAllAsHandled(unhandledQas,
                            function(qas){
                                if(qas) {
                                    angular.forEach(qas, function(qa, key) {
                                        fragaSvarCommonService.decorateSingleItem(qa);
                                        qa.proxyMessage = 'fk7263.fragasvar.marked.as.hanterad';
                                    });
                                    statService.refreshStat();
                                }
                                $window.doneLoading = true;
                                if(deferred) {
                                    deferred.resolve();
                                }
                            },function(errorData) {
                                // show error view
                                $window.doneLoading = true;
                                if(deferred) {
                                    deferred.resolve();
                                }
                            });
                    };

                    $scope.hasUnhandledQas = function(){
                        if(!$scope.qaList || $scope.qaList.length === 0){
                            return false;
                        }
                        for (var i = 0, len = $scope.qaList.length; i < len; i++) {
                            var qa = $scope.qaList[i];
                            var isUnhandled = fragaSvarCommonService.isUnhandled(qa);
                            var fromFk = fragaSvarCommonService.fromFk(qa);
                            if(qa.status === 'ANSWERED' || (isUnhandled && fromFk)){
                                return true;
                            }
                        }
                        return false;
                    };

                    $scope.updateAsHandled = function(qa, deferred) {
                        $log.debug('updateAsHandled:' + qa);
                        qa.updateHandledStateInProgress = true;

                        fragaSvarService.closeAsHandled(qa.internReferens, 'fk7263', function(result) {
                            qa.activeErrorMessageKey = null;
                            qa.updateHandledStateInProgress = false;
                            if (result !== null) {
                                fragaSvarCommonService.decorateSingleItem(result);
                                // Create a proxyCopy
                                var proxyCopy = angular.copy(qa);
                                proxyCopy.proxyMessage = 'fk7263.fragasvar.marked.as.hanterad';
                                $scope.qaList.push(proxyCopy);

                                angular.copy(result, qa);
                                //$scope.activeQA = qa.internReferens;
                                statService.refreshStat();
                            }
                            $window.doneLoading = true;
                            if(deferred) {
                                deferred.resolve();
                            }
                        }, function(errorData) {
                            // show error view
                            qa.updateHandledStateInProgress = false;
                            qa.activeErrorMessageKey = errorData.errorCode;
                            $window.doneLoading = true;
                            if(deferred) {
                                deferred.resolve();
                            }
                        });
                    };

                    $scope.updateAsUnHandled = function(qa) {
                        $log.debug('updateAsUnHandled:' + qa);
                        qa.updateHandledStateInProgress = true; // trigger local

                        fragaSvarService.openAsUnhandled(qa.internReferens, 'fk7263', function(result) {
                            $log.debug('Got openAsUnhandled result:' + result);
                            qa.activeErrorMessageKey = null;
                            qa.updateHandledStateInProgress = false;

                            if (result !== null) {
                                fragaSvarCommonService.decorateSingleItem(result);
                                // Create a proxyCopy
                                var proxyCopy = angular.copy(qa);
                                proxyCopy.proxyMessage = 'fk7263.fragasvar.marked.as.ohanterad';
                                $scope.qaList.push(proxyCopy);

                                angular.copy(result, qa);
                                //$scope.activeQA = qa.internReferens;
                                statService.refreshStat();
                            }
                        }, function(errorData) {
                            // show error view
                            qa.updateHandledStateInProgress = false;
                            qa.activeErrorMessageKey = errorData.errorCode;
                        });
                    };

                    // Handle vidarebefordra dialog
                    $scope.openMailDialog = function(qa) {
                        // use timeout so that external mail client has a chance to start before showing dialog
                        $timeout(function() {
                            fragaSvarCommonService.handleVidareBefodradToggle(qa, $scope.onVidareBefordradChange);
                        }, 1000);
                        // Launch mail client
                        $window.location = fragaSvarCommonService.buildMailToLink(qa);
                    };

                    $scope.dismissProxy = function(qa) {
                        if (qa === undefined) {
                            $scope.widgetState.sentMessage = false;
                            return;
                        }
                        for (var i = 0; i < $scope.qaList.length; i++) {
                            if (qa === $scope.qaList[i]) {
                                $scope.qaList.splice(i, 1);
                                return;
                            }
                        }
                    };

                }
            };
        }]);
