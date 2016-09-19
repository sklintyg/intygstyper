/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

angular.module('fk7263').controller('fk7263.QACtrl',
    [ '$log', '$rootScope', '$stateParams', '$scope', '$timeout', '$window', '$filter', 'common.dialogService',
        'fk7263.fragaSvarProxy', 'common.fragaSvarCommonService', 'common.statService',
        'common.UserModel', 'fk7263.QACtrl.Helper', 'common.IntygViewStateService', 'common.ObjectHelper', 'common.pingService',
        function($log, $rootScope, $stateParams, $scope, $timeout, $window, $filter, dialogService, fragaSvarProxy,
            fragaSvarCommonService, statService, UserModel, qaHelper, CommonViewState, ObjectHelper, pingService) {
            'use strict';

            // init state
            $scope.qaList = [];
            $scope.widgetState = {
                doneLoading: false,
                activeErrorMessageKey: null,
                newQuestionOpen: false,
                sentMessage: false,
                focusQuestion: false,
                showTemplate: true
            };

            // Injecting the CommonViewState service so client-side only changes on the cert page (such as a send/revoke)
            // can trigger GUI updates in the Q&A view.
            $scope.viewState = CommonViewState;

            $scope.dismissSentMessage = function() {
                $scope.widgetState.sentMessage = false;
            };

            $scope.cert = {};
            $scope.certProperties = {
                isLoaded: false,
                isSent: false,
                isRevoked: false
            };

            var unbindFastEvent = $rootScope.$on('fk7263.ViewCertCtrl.load', function(event, cert, certProperties) {

                // IMPORTANT!! DON'T LET getQaForCertificate DEPEND ON THE INTYG LOAD EVENT (cert) in this case!
                // Messages needs to be loaded separately from the intyg as user should be able to see messages even if intyg didn't load.
                // Used when coming from Intyg page.
                $scope.cert = cert;
                $scope.certProperties.isLoaded = false;
                $scope.certProperties.isSent = false;
                $scope.certProperties.isRevoked = false;

                if(ObjectHelper.isDefined(cert) && ObjectHelper.isDefined(certProperties)) {

                    $scope.certProperties = certProperties;
                    $scope.certProperties.isLoaded = true;

                    var intygId = $stateParams.certificateId;
                    if(certProperties.forceUseProvidedIntyg) {
                        // Used for utkast page. In this case we must use the id from cert because $stateParams.certificateId is the id of the utkast, not the parentIntyg
                        intygId = cert.id;
                    }
                    qaHelper.fetchFragaSvar($scope, intygId, $scope.certProperties);

                } else if(ObjectHelper.isDefined($stateParams.certificateId)) {
                    qaHelper.fetchFragaSvar($scope, $stateParams.certificateId, null);
                }
            });
            $scope.$on('$destroy', unbindFastEvent);

            // ProxyMessage is set if question is handled and replaced by a blue context info box saying it is handled (or reopened) instead of the actual question showing.
            // Checking for proxyMessage therefore is a way to decide whether the question is shown or the message is (yes, its ugly. should be refactored)

            function isFragaOpen(qa) {
                return qa.proxyMessage === undefined && qa.status !== 'CLOSED';
            }

            function isFragaProxyMessageOpenShown(qa) {
                return qa.proxyMessage !== undefined && qa.messageStatus !== 'CLOSED' && qa.messageStatus !== 'HIDDEN';
            }

            function isFragaProxyMessageClosedShown(qa) {
                return qa.proxyMessage !== undefined && qa.messageStatus === 'CLOSED';
            }

            $scope.issueFilter = function(qa) {
                return true;
            };

            $scope.openIssuesFilter = function(qa) {
                return isFragaOpen(qa) || isFragaProxyMessageOpenShown(qa);
            };

            $scope.closedIssuesFilter = function(qa) {
                return !isFragaOpen(qa) || isFragaProxyMessageClosedShown(qa);
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
                $scope.widgetState.sentMessage = false;

                fragaSvarProxy.saveNewQuestion($stateParams.certificateId, 'fk7263', newQuestion,
                    function(result) {
                        $log.debug('Got saveNewQuestion result:' + result);
                        newQuestion.updateInProgress = false;
                        newQuestion.activeErrorMessageKey = null;
                        if (result !== null) {
                            fragaSvarCommonService.decorateSingleItem(result);
                            // result is a new FragaSvar Instance: add it to our local repo
                            $scope.qaList.push(result);
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

            // prolong session when user modifies svars-text
            $scope.$watch('newQuestion.frageText', function() {
                pingService.registerUserAction('fk7263-entering-newquestiontext');
            });

            // listeners - interscope communication
            var unbindmarkAnsweredAsHandledEvent = $scope.$on('markAnsweredAsHandledEvent', function($event, deferred, unhandledQas) {
                qaHelper.updateAnsweredAsHandled(deferred, unhandledQas, true);
            });

            $scope.$on('$destroy', unbindmarkAnsweredAsHandledEvent);

            var unbindHasUnhandledQasEvent = $scope.$on('hasUnhandledQasEvent', function($event, deferred) {
                deferred.resolve(fragaSvarCommonService.getUnhandledQas($scope.qaList));
            });

            $scope.$on('$destroy', unbindHasUnhandledQasEvent);

        }]);
