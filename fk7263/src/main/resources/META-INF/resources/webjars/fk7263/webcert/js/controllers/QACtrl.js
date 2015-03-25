angular.module('fk7263').controller('fk7263.QACtrl',
    [ '$log', '$rootScope', '$routeParams', '$scope', '$timeout', '$window', '$filter', 'common.dialogService',
        'fk7263.fragaSvarService', 'common.fragaSvarCommonService', 'common.ManageCertView', 'common.statService',
        'common.UserModel',
        function($log, $rootScope, $routeParams, $scope, $timeout, $window, $filter, dialogService, fragaSvarService,
            fragaSvarCommonService, ManageCertView, statService, UserModel) {
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

            var decorateWithGUIParameters = function(list) {
                // answerDisabled
                // answerButtonToolTip
                angular.forEach(list, function(qa) {
                    fragaSvarCommonService.decorateSingleItem(qa);
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

            var unbindFastEvent = $rootScope.$on('fk7263.ViewCertCtrl.load', function(event, cert, certProperties) {
                $scope.cert = cert;
                $scope.certProperties.isLoaded = true;
                $scope.certProperties.isSent = certProperties.isSent;
                $scope.certProperties.isRevoked = certProperties.isRevoked;
            });
            $scope.$on('$destroy', unbindFastEvent);

            $scope.openIssuesFilter = function(qa) {
                return (qa.proxyMessage === undefined && qa.status !== 'CLOSED') ||
                    (qa.proxyMessage !== undefined && qa.messageStatus !== 'CLOSED' && qa.messageStatus !== 'HIDDEN');

            };

            $scope.closedIssuesFilter = function(qa) {
                return (qa.proxyMessage === undefined && qa.status === 'CLOSED') ||
                    (qa.proxyMessage !== undefined && qa.messageStatus === 'CLOSED');
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
            $scope.sendQuestion = function (newQuestion) {
                $log.debug('sendQuestion:' + newQuestion);
                newQuestion.updateInProgress = true; // trigger local spinner

                fragaSvarService.saveNewQuestion($routeParams.certificateId, 'fk7263', newQuestion,
                    function(result) {
                        $log.debug('Got saveNewQuestion result:' + result);
                        newQuestion.updateInProgress = false;
                        newQuestion.activeErrorMessageKey = null;
                        if (result !== null) {
                            fragaSvarCommonService.decorateSingleItem(result);
                            // result is a new FragaSvar Instance: add it to our local repo
                            $scope.qaList.push(result);
                            //$scope.activeQA = result.internReferens;
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

            var unbindHasUnhandledQasEvent = $scope.$on('hasUnhandledQasEvent', function($event, deferred) {
                deferred.resolve(fragaSvarCommonService.getUnhandledQas($scope.qaList));
            });

            $scope.$on('$destroy', unbindHasUnhandledQasEvent);

        }]);
