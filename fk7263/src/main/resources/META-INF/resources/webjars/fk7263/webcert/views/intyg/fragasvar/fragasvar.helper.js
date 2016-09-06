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

angular.module('fk7263').service('fk7263.QACtrl.Helper',
    ['$log', '$timeout', '$rootScope', 'fk7263.fragaSvarProxy', 'common.fragaSvarCommonService', '$window', 'common.statService', 'common.ObjectHelper',
        function ($log, $timeout, $rootScope, fragaSvarProxy, fragaSvarCommonService, $window, statService, ObjectHelper) {
            'use strict';

            function delayFindMessageAndAct(timeout, qaList, message, onFound) {
                $timeout(function () {
                    var i;
                    for (i = 0; i < qaList.length; i++) {
                        if (qaList[i].id === message.id && qaList[i].proxyMessage !== undefined) {
                            onFound(i);
                            break;
                        }
                    }
                }, timeout);

                $log.debug('Message not found:' + message.id);
            }

            function addListMessage(qaList, qa, messageId) {
                var messageProxy = {};
                messageProxy.proxyMessage = messageId;
                messageProxy.id = qa.id;
                messageProxy.senasteHandelseDatum = qa.senasteHandelseDatum;
                messageProxy.messageStatus = qa.status;
                qaList.push(messageProxy);

                delayFindMessageAndAct(5000, qaList, messageProxy, function (index) {
                    qaList[index].messageStatus = 'HIDDEN';
                    delayFindMessageAndAct(2000, qaList, messageProxy, function (index) {
                        qaList.splice(index, 1);
                    });
                });
            }

            this.updateAnsweredAsHandled = function (deferred, unhandledQas, fromHandledDialog) {
                if (unhandledQas === undefined || unhandledQas.length === 0) {
                    return;
                }
                fragaSvarProxy.closeAllAsHandled(unhandledQas,
                    function (qas) {
                        if (qas) {
                            angular.forEach(qas, function (qa) { //unused parameter , key
                                fragaSvarCommonService.decorateSingleItem(qa);
                                if (fromHandledDialog) {
                                    qa.proxyMessage = 'fk7263.fragasvar.marked.as.hanterad';
                                } else {
                                    addListMessage(qas, qa, 'fk7263.fragasvar.marked.as.hanterad'); // TODOOOOOOOO TEST !!!!!!!!!!
                                }
                            });
                            statService.refreshStat();
                        }
                        $window.doneLoading = true;
                        if (deferred) {
                            deferred.resolve();
                        }
                    }, function () { // unused parameter: errorData
                        // show error view
                        $window.doneLoading = true;
                        if (deferred) {
                            deferred.resolve();
                        }
                    });
            };

            function decorateWithGUIParameters(list) {
                // answerDisabled
                // answerButtonToolTip
                angular.forEach(list, function (qa) {
                    fragaSvarCommonService.decorateSingleItem(qa);
                });
            }

            function filterKompletteringar(qaList, widgetState, certProperties) {

                var isAnyKompletteringarNotHandled = false;

                // Filter out the komplettering the utkast was based on and only that one.
                var filteredList = qaList.filter(function(qa) {

                    var isKompletteringFraga = qa.amne === 'KOMPLETTERING_AV_LAKARINTYG';

                    // Check if this komplettering isn't handled. Used to show sign if there are no more unhandled kompletteringar
                    if(!isAnyKompletteringarNotHandled){
                        isAnyKompletteringarNotHandled = (isKompletteringFraga && qa.status !== 'CLOSED');
                    }

                    // Filter out the komplettering the utkast was based on and only that one.
                    return isKompletteringFraga && Number(qa.internReferens) === Number(certProperties.meddelandeId);
                });

                // If there aren't any kompletteringar that aren't handled already, we can show the sign that all kompletteringar are handled.
                widgetState.showAllKompletteringarHandled = !isAnyKompletteringarNotHandled;
                return filteredList;
            }

            this.filterKompletteringar = filterKompletteringar;

            this.fetchFragaSvar = function (scope, intygId, certProperties) {
                // Request loading of QA's for this certificate
                fragaSvarProxy.getQAForCertificate(intygId, 'fk7263', function (result) {
                    $log.debug('getQAForCertificate:success data:' + result);
                    scope.widgetState.doneLoading = true;
                    scope.widgetState.activeErrorMessageKey = null;

                    scope.widgetState.showAllKompletteringarHandled = false;

                    decorateWithGUIParameters(result);
                    if(ObjectHelper.isDefined(certProperties) && certProperties.kompletteringOnly){
                        // If kompletteringsmode, only show kompletteringsissues
                        result = filterKompletteringar(result, scope.widgetState, certProperties);
                    }

                    scope.qaList = result;

                    // Tell viewcertctrl about the intyg in case intyg load fails
                    if (result.length > 0) {
                        $rootScope.$emit('fk7263.QACtrl.load', result[0].intygsReferens);
                    }

                }, function (errorData) {
                    // show error view
                    scope.widgetState.doneLoading = true;
                    scope.widgetState.activeErrorMessageKey = errorData.errorCode;
                });
            };

        }]);
