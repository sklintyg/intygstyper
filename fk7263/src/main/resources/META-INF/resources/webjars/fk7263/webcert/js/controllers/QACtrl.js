angular.module('fk7263').controller('fk7263.QACtrl',
    [ '$log', '$rootScope', '$routeParams', '$scope', '$timeout', '$window', '$filter', 'common.dialogService',
        'fk7263.fragaSvarService', 'common.fragaSvarCommonService', 'common.ManageCertView', 'common.statService',
        'common.User',
        function($log, $rootScope, $routeParams, $scope, $timeout, $window, $filter, dialogService, fragaSvarService,
            fragaSvarCommonService, ManageCertView, statService, User) {
            'use strict';

            // init state
            $scope.qaList = {};
            $scope.widgetState = {
                doneLoading: false,
                activeErrorMessageKey: null,
                newQuestionOpen: false,
                sentMessage: false,
                focusQuestion: false,
                showTemplate: true
            };

            var decorateSingleItem = function(qa) {
                if (qa.amne === 'PAMINNELSE') {
                    // RE-020 Påminnelser is never
                    // answerable
                    qa.answerDisabled = true;
                    qa.answerDisabledReason = undefined; // Påminnelser kan inte besvaras men det behöver vi inte säga
                } else if (qa.amne === 'KOMPLETTERING_AV_LAKARINTYG' && !User.userContext.lakare) {
                    // RE-005, RE-006
                    qa.answerDisabled = true;
                    qa.answerDisabledReason = 'Kompletteringar kan endast besvaras av läkare.';
                } else {
                    qa.answerDisabled = false;
                    qa.answerDisabledReason = undefined;
                }
                fragaSvarCommonService.decorateSingleItemMeasure(qa);
            };

            var decorateWithGUIParameters = function(list) {
                // answerDisabled
                // answerButtonToolTip
                angular.forEach(list, function(qa) {
                    decorateSingleItem(qa);
                });
            };

            // Request loading of QA's for this certificate
            fragaSvarService.getQAForCertificate($routeParams.certificateId, 'fk7263', function(result) {
                $log.debug('getQAForCertificate:success data:' + result);
                $scope.widgetState.doneLoading = true;
                $scope.widgetState.activeErrorMessageKey = null;
                decorateWithGUIParameters(result);
                $scope.qaList = result;

                // Tell viewcertctrl about the intyg in case cert load fails
                if (result.length > 0) {
                    $rootScope.$emit('fk7263.QACtrl.load', result[0].intygsReferens);
                }

            }, function(errorData) {
                // show error view
                $scope.widgetState.doneLoading = true;
                $scope.widgetState.activeErrorMessageKey = errorData.errorCode;
            });

            $scope.cert = {};
            $scope.certProperties = {
                isLoaded: false,
                isSent: false,
                isRevoked: false
            };

            var unbindFastEvent = $rootScope.$on('fk7263.ViewCertCtrl.load', function(event, revoked, statuses, cert) {
                $scope.cert = cert;
                $scope.certProperties.isLoaded = true;
                $scope.certProperties.isSent = ManageCertView.isSentToTarget(statuses, 'FK');
                $scope.certProperties.isRevoked = revoked;
            });
            $scope.$on('$destroy', unbindFastEvent);

            var certFailedEvent = $rootScope.$on('fk7263.ViewCertCtrl.fail', function() {
                $scope.certProperties.isLoaded = true;
                $scope.cert.grundData.skapadAv.vardenhet.enhetsid = '';
            });
            $scope.$on('$destroy', certFailedEvent);

            $scope.openIssuesFilter = function(qa) {
                return qa.status !== 'CLOSED';
            };

            $scope.closedIssuesFilter = function(qa) {
                return qa.status === 'CLOSED';
            };

            $scope.toggleQuestionForm = function() {
                $scope.widgetState.newQuestionOpen = !$scope.widgetState.newQuestionOpen;
                $scope.initQuestionForm();
                // hide sent message
                $scope.widgetState.sentMessage = false;
                $scope.widgetState.focusQuestion = true;
            };

            /**
             * Functions bound to individual fragasvar entities's
             */
            $scope.sendQuestion = function sendQuestion(newQuestion) {
                $log.debug('sendQuestion:' + newQuestion);
                newQuestion.updateInProgress = true; // trigger local spinner

                fragaSvarService.saveNewQuestion($routeParams.certificateId, 'fk7263', newQuestion,
                    function(result) {
                        $log.debug('Got saveNewQuestion result:' + result);
                        newQuestion.updateInProgress = false;
                        newQuestion.activeErrorMessageKey = null;
                        if (result !== null) {
                            decorateSingleItem(result);
                            // result is a new FragaSvar Instance: add it to our local repo
                            $scope.qaList.push(result);
                            $scope.activeQA = result.internReferens;
                            // close question form
                            $scope.toggleQuestionForm();
                            // show sent message
                            $scope.widgetState.sentMessage = true;
                            statService.refreshStat();
                        }
                    }, function(errorData) {
                        // show error view
                        newQuestion.updateInProgress = false;
                        newQuestion.activeErrorMessageKey = errorData.errorCode;
                    });
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

            $scope.sendAnswer = function sendAnswer(qa) {
                qa.updateInProgress = true; // trigger local spinner

                fragaSvarService.saveAnswer(qa, 'fk7263', function(result) {
                    $log.debug('Got saveAnswer result:' + result);
                    qa.updateInProgress = false;
                    qa.activeErrorMessageKey = null;
                    if (result !== null) {
                        decorateSingleItem(result);
                        // Create a proxyCopy
                        var proxyCopy = angular.copy(qa);
                        proxyCopy.proxyMessage = 'fk7263.fragasvar.answer.is.sent';
                        $scope.qaList.push(proxyCopy);
                        // update real item
                        angular.copy(result, qa);
                        $scope.activeQA = qa.internReferens;
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

            $scope.updateAllAsHandled = function(){
                angular.forEach($scope.qaList, function(qa) {
                    if(fragaSvarCommonService.isUnhandled(qa)){
                        $scope.updateAsHandled(qa);
                    }
                });
            };

            $scope.hasUnhandledQas = function(){
                if(!$scope.qaList || $scope.qaList.length === 0){
                    return false;
                }
                for (var i = 0, len = $scope.qaList.length; i < len; i++) {
                    var qa = $scope.qaList[i];
                    if(fragaSvarCommonService.isUnhandled(qa)){
                        return true;
                    }
                }
                return false;
            };

            $scope.updateAsHandled = function(qa) {
                $log.debug('updateAsHandled:' + qa);
                qa.updateHandledStateInProgress = true;

                fragaSvarService.closeAsHandled(qa.internReferens, 'fk7263', function(result) {
                    $log.debug('Got updateAsHandled result:' + result);
                    qa.activeErrorMessageKey = null;
                    qa.updateHandledStateInProgress = false;
                    if (result !== null) {
                        decorateSingleItem(result);
                        // Create a proxyCopy
                        var proxyCopy = angular.copy(qa);
                        proxyCopy.proxyMessage = 'fk7263.fragasvar.marked.as.hanterad';
                        $scope.qaList.push(proxyCopy);

                        angular.copy(result, qa);
                        $scope.activeQA = qa.internReferens;
                        statService.refreshStat();
                    }
                }, function(errorData) {
                    // show error view
                    qa.updateHandledStateInProgress = false;
                    qa.activeErrorMessageKey = errorData.errorCode;
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
                        decorateSingleItem(result);
                        // Create a proxyCopy
                        var proxyCopy = angular.copy(qa);
                        proxyCopy.proxyMessage = 'fk7263.fragasvar.marked.as.ohanterad';
                        $scope.qaList.push(proxyCopy);

                        angular.copy(result, qa);
                        $scope.activeQA = qa.internReferens;
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

            $scope.questionValidForSubmit = function(newQuestion) {
                var validToSend = newQuestion.chosenTopic.value && newQuestion.frageText &&
                    !newQuestion.updateInProgress;
                if (!validToSend) {
                    newQuestion.sendButtonToolTip =
                        'Du måste välja ett ämne och skriva en frågetext innan du kan skicka frågan';
                } else {
                    newQuestion.sendButtonToolTip = 'Skicka frågan';
                }
                return validToSend;
            };

            $scope.initQuestionForm = function() {
                // Topics are defined under RE-13
                $scope.newQuestion = {
                    topics: [
                        {
                            label: 'Välj ämne',
                            value: null
                        },
                        {
                            label: 'Arbetstidsförläggning',
                            value: 'ARBETSTIDSFORLAGGNING'
                        },
                        {
                            label: 'Avstämningsmöte',
                            value: 'AVSTAMNINGSMOTE'
                        },
                        {
                            label: 'Kontakt',
                            value: 'KONTAKT'
                        },
                        {
                            label: 'Övrigt',
                            value: 'OVRIGT'
                        }
                    ],
                    frageText: ''
                };
                $scope.newQuestion.chosenTopic = $scope.newQuestion.topics[0]; // 'Välj ämne' is default
            };
            $scope.initQuestionForm();

            // listeners - interscope communication
            var unbindMarkAllAsHandledEvent = $scope.$on('markAllAsHandledEvent', function($event) {
                $scope.updateAllAsHandled();
            });

            $scope.$on('$destroy', unbindMarkAllAsHandledEvent);

            var unbindHasUnhandledQasEvent = $scope.$on('hasUnhandledQasEvent', function($event, deferred) {
                deferred.resolve($scope.hasUnhandledQas());
            });

            $scope.$on('$destroy', unbindHasUnhandledQasEvent);

        }]);
