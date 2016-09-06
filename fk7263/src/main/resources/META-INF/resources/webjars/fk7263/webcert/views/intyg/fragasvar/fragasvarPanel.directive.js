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

/**
 * Created by BESA on 2015-03-05.
 */

/**
 * qaPanel directive. Common directive for both unhandled and handled questions/answers
 */
angular.module('fk7263').directive('qaPanel',
    ['$window', '$log', '$timeout', '$state', '$stateParams', '$q',
        'common.UserModel', 'common.fragaSvarCommonService', 'fk7263.fragaSvarProxy',
        'common.statService', 'common.dialogService', 'common.ObjectHelper', 'common.IntygCopyRequestModel', 'common.FocusElementService', 'common.pingService',
        function($window, $log, $timeout, $state, $stateParams, $q,
            UserModel, fragaSvarCommonService, fragaSvarProxy, statService, dialogService, ObjectHelper,
            IntygCopyRequestModel, focusElement, pingService) {
            'use strict';

            function _hasKompletteringUtkastRelation(relations) {
                if (typeof relations === 'undefined' || relations === null) {
                    return false;
                }
                for (var a = 0; a < relations.length; a++) {
                    var relation = relations[a];
                    if (relation.kod === 'KOMPLT') {
                        return true;
                    }
                }
                return false;
            }

            return {
                restrict: 'A',
                transclude: true,
                replace: true,
                templateUrl: '/web/webjars/fk7263/webcert/views/intyg/fragasvar/fragasvarPanel.directive.html',
                scope: {
                    panelId: '@',
                    qa: '=',
                    qaList: '=',
                    intyg: '=',
                    intygProperties: '='
                },
                controller: function($scope, $element, $attrs) {

                    $scope.$watch('qa.svarsText', function() {
                        pingService.registerUserAction('fk7263-entering-svarstext');
                    });

                    $scope.showAnswerField = false;
                    var kompletteringDialog = null;

                    $scope.showHandledButtons = function() {
                        return !$scope.intygProperties.isRevoked &&
                            !$scope.intygProperties.kompletteringOnly &&
                            // Enforce business rule FS-011, from FK + answer should remain closed
                            ($scope.qa.frageStallare === 'WC' || !$scope.qa.svarsText);
                    };

                    $scope.handledFunction = function(newState) {
                        if (arguments.length) {
                            if (newState) {
                                $scope.updateAsHandled($scope.qa);
                            }
                            else {
                                $scope.updateAsUnHandled($scope.qa);
                            }
                        }
                        else {
                            return $scope.qa.status === 'CLOSED';
                        }
                    };

                    $scope.showNotHandledButtons = function(qa, intygProperties) {
                        return !intygProperties.kompletteringOnly && qa.status !== 'CLOSED' && qa.amne !== 'PAMINNELSE';
                    };

                    $scope.hasStatus = function(qa, status) {
                        return qa.status === status;
                    };

                    $scope.isAllowedToAnswerAtAll = function(qa, intygProperties) {
                        return intygProperties.kompletteringOnly ||
                            (qa.amne !== 'KOMPLETTERING_AV_LAKARINTYG') ||
                            (qa.amne === 'KOMPLETTERING_AV_LAKARINTYG' &&
                            ($scope.showAnswerField || (qa.svarsText && qa.status === 'CLOSED')));
                    };

                    $scope.isAllowedToAnswer = function(qa, intygProperties) {
                        return $scope.isAllowedToAnswerFraga(qa, intygProperties) || $scope.showAnswerField;
                    };

                    $scope.isAllowedToAnswerFraga = function(qa, intygProperties) {
                        return !qa.answerDisabled && !intygProperties.isRevoked;
                    };

                    $scope.isNotAllowedToAnswer = function(qa) {
                        return qa.answerDisabled && qa.answerDisabledReason && !$scope.hasStatus(qa, 'CLOSED');
                    };

                    $scope.isKompletteringAllowed = function(qa, intygProperties) {
                        return !intygProperties.kompletteringOnly && $scope.isAllowedToAnswerFraga(qa, intygProperties) &&
                            qa.amne === 'KOMPLETTERING_AV_LAKARINTYG';
                    };

                    $scope.isNotAllowedToKomplettera = function(qa, intygProperties) {
                        return $scope.isAllowedToAnswerFraga(qa, intygProperties) && $scope.showAnswerField;
                    };

                    $scope.sendAnswer = function sendAnswer(qa) {
                        qa.updateInProgress = true; // trigger local spinner

                        fragaSvarProxy.saveAnswer(qa, 'fk7263', function(result) {
                            $log.debug('Got saveAnswer result:' + result);
                            qa.updateInProgress = false;
                            qa.activeErrorMessageKey = null;
                            if (result !== null) {
                                fragaSvarCommonService.decorateSingleItem(result);
                                //addListMessage($scope.qaList, qa, 'fk7263.fragasvar.answer.is.sent');

                                // update real item
                                angular.copy(result, qa);
                                statService.refreshStat();
                            }
                        }, function(errorData) {
                            // show error view
                            qa.updateInProgress = false;
                            qa.activeErrorMessageKey = errorData.errorCode;
                        });
                    };

                    $scope.abortAnswer = function() {
                        $scope.showAnswerField = false;
                    };

                    $scope.openKompletteringsUtkast = function(intygsTyp) {
                        var latestKomplRelation;
                        for (var a = 0; a < $scope.intygProperties.relations.length; a++) {
                            var relation =  $scope.intygProperties.relations[a];
                            if (relation.kod === 'KOMPLT') {
                                if (typeof latestKomplRelation === 'undefined') {
                                    latestKomplRelation = relation;
                                } else if (relation.date > latestKomplRelation.date) {
                                    latestKomplRelation = relation;
                                }
                            }
                        }
                        if (typeof latestKomplRelation !== 'undefined') {
                            $state.go(intygsTyp + '-edit', {
                                certificateId: latestKomplRelation.intygsId
                            });
                        }
                    };

                    $scope.svaraMedFortsattPaIntygsutkast = _hasKompletteringUtkastRelation($scope.intygProperties.relations);

                    $scope.openKompletteringDialog = function(qa, cert) {

                        var kompletteringDialogModel = {
                            qa: qa,
                            uthopp: UserModel.isUthopp(),
                            svaraMedFortsattPaIntygsutkast: $scope.svaraMedFortsattPaIntygsutkast
                        };

                        kompletteringDialog = dialogService.showDialog({
                            dialogId: 'komplettering-dialog',
                            titleId: 'fk7263.fragasvar.komplettering.dialogtitle',
                            templateUrl: '/app/partials/komplettering-dialog.html',
                            model: kompletteringDialogModel,
                            button1click: function(modalInstance) {
                                $scope.answerWithIntyg(qa, cert).then(function(result) {

                                    function goToDraft(type, intygId) {
                                        $state.go(type + '-edit', {
                                            certificateId: intygId
                                        });
                                    }

                                    modalInstance.close();
                                    goToDraft(cert.typ, result.intygsUtkastId);

                                }, function(result) {
                                    // something went wrong. leave dialog open to show error message
                                });
                            },
                            button1id: 'button1answerintyg-dialog',
                            button2click: function(modalInstance) {
                                qa.activeDialogErrorMessageKey = null;
                                $scope.showAnswerField = true;
                                modalInstance.close();
                                focusElement('answerText-' + qa.internReferens);
                            },
                            button2id: 'button2answermessage-dialog',
                            button3click: function(modalInstance) {
                                modalInstance.close();
                                $scope.openKompletteringsUtkast(cert.typ);
                            },
                            button3id:  'button3gotoutkast-dialog',
                            autoClose: false,
                            size: 'lg'
                        }).result.then(function() {
                            kompletteringDialog = null; // Dialog closed
                        }, function() {
                            kompletteringDialog = null; // Dialog dismissed
                        });

                    };

                    $scope.answerWithIntyg = function(qa, intyg) {

                        qa.activeDialogErrorMessageKey = null;
                        if (!ObjectHelper.isDefined(intyg)) {
                            qa.activeDialogErrorMessageKey = 'fk7263.error.komplettera-no-intyg';
                            return;
                        }

                        var deferred = $q.defer();

                        qa.updateInProgress = true; // trigger local spinner
                        fragaSvarProxy.answerWithIntyg(qa, intyg.typ,
                          IntygCopyRequestModel.build({
                            intygId: intyg.id,
                            intygType: intyg.typ,
                            patientPersonnummer: intyg.grundData.patient.personId,
                            nyttPatientPersonnummer: $stateParams.patientId 
                          }), function(result) {
                          
                                qa.updateInProgress = false;
                                qa.activeDialogErrorMessageKey = null;
                                statService.refreshStat();
                                deferred.resolve(result);

                            }, function(errorData) {
                                // show error view
                                qa.updateInProgress = false;

                                if(errorData.message === null || errorData.errorCode === -1) {
                                    qa.activeDialogErrorMessageKey = 'common.error.cantconnect';
                                } else{
                                    qa.activeDialogErrorMessageKey = 'fk7263.error' + errorData.errorCode;
                                }

                                deferred.reject(errorData);
                            });

                        return deferred.promise;
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
                                    dialogService.showErrorMessageDialog(
                                        'Kunde inte markera/avmarkera frågan som vidarebefordrad. ' +
                                        'Försök gärna igen för att se om felet är tillfälligt. Annars kan du kontakta supporten');
                                }
                            });
                    };

                    $scope.updateAnsweredAsHandled = function(deferred, unhandledQas) {
                        if (unhandledQas === undefined || unhandledQas.length === 0) {
                            return;
                        }
                        fragaSvarProxy.closeAllAsHandled(unhandledQas,
                            function(qas) {
                                if (qas) {
                                    angular.forEach(qas, function(qa) { //unused parameter , key
                                        fragaSvarCommonService.decorateSingleItem(qa);
                                        //addListMessage(qas, qa, 'fk7263.fragasvar.marked.as.hanterad'); // TODOOOOOOOO TEST !!!!!!!!!!
                                    });
                                    statService.refreshStat();
                                }
                                $window.doneLoading = true;
                                if (deferred) {
                                    deferred.resolve();
                                }
                            }, function() { // unused parameter: errorData
                                // show error view
                                $window.doneLoading = true;
                                if (deferred) {
                                    deferred.resolve();
                                }
                            });
                    };

                    $scope.hasUnhandledQas = function() {
                        if (!$scope.qaList || $scope.qaList.length === 0) {
                            return false;
                        }
                        for (var i = 0, len = $scope.qaList.length; i < len; i++) {
                            var qa = $scope.qaList[i];
                            var isUnhandled = fragaSvarCommonService.isUnhandled(qa);
                            var fromFk = fragaSvarCommonService.fromFk(qa);
                            if (qa.status === 'ANSWERED' || (isUnhandled && fromFk)) {
                                return true;
                            }
                        }
                        return false;
                    };

                    $scope.updateAsHandled = function(qa, deferred) {
                        $log.debug('updateAsHandled:' + qa);
                        qa.updateHandledStateInProgress = true;

                        fragaSvarProxy.closeAsHandled(qa.internReferens, 'fk7263', function(result) {
                            qa.activeErrorMessageKey = null;
                            qa.updateHandledStateInProgress = false;
                            if (result !== null) {
                                fragaSvarCommonService.decorateSingleItem(result);
                                //addListMessage($scope.qaList, qa, 'fk7263.fragasvar.marked.as.hanterad');

                                angular.copy(result, qa);
                                statService.refreshStat();
                            }
                            $window.doneLoading = true;
                            if (deferred) {
                                deferred.resolve();
                            }
                        }, function(errorData) {
                            // show error view
                            qa.updateHandledStateInProgress = false;
                            qa.activeErrorMessageKey = errorData.errorCode;
                            $window.doneLoading = true;
                            if (deferred) {
                                deferred.resolve();
                            }
                        });
                    };

                    $scope.updateAsUnHandled = function(qa) {
                        $log.debug('updateAsUnHandled:' + qa);
                        qa.updateHandledStateInProgress = true; // trigger local

                        fragaSvarProxy.openAsUnhandled(qa.internReferens, 'fk7263', function(result) {
                            $log.debug('Got openAsUnhandled result:' + result);
                            qa.activeErrorMessageKey = null;
                            qa.updateHandledStateInProgress = false;

                            if (result !== null) {
                                fragaSvarCommonService.decorateSingleItem(result);
                                //addListMessage($scope.qaList, qa, 'fk7263.fragasvar.marked.as.ohanterad');

                                angular.copy(result, qa);
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
                        var link = fragaSvarCommonService.buildMailToLink(qa);
                        if (link !== 'error') {
                            $window.location = link;
                        }
                    };

                    $scope.dismissProxy = function(qa) {
                        if (qa === undefined) {
                            $scope.widgetState.sentMessage = false;
                            return;
                        }
                        for (var i = 0; i < $scope.qaList.length; i++) {
                            if (qa.proxyMessage !== undefined && $scope.qaList[i].proxyMessage !== undefined &&
                                qa.internReferens === $scope.qaList[i].internReferens) {
                                $scope.qaList.splice(i, 1);
                                return;
                            }
                        }
                    };

                    // listeners - interscope communication
                    var unbindmarkAnsweredAsHandledEvent = $scope.$on('markAnsweredAsHandledEvent',
                        function($event, deferred, unhandledQas) {
                            $scope.updateAnsweredAsHandled(deferred, unhandledQas);
                        });
                    $scope.$on('$destroy', unbindmarkAnsweredAsHandledEvent);

                }
            };
        }]);
