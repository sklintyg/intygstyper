/**
 * Fragasvar Module - services and controllers related to FragaSvar
 * functionality in webcert.
 */
angular.module('wc.fragasvarmodule', []);
angular.module('wc.fragasvarmodule').factory('fragaSvarService', [ '$http', '$log', '$dialog', function($http, $log, $dialog) {

    /*
     * Load questions and answers data for a certificate
     */
    function _getQAForCertificate(id, onSuccess, onError) {
        $log.debug("_getQAForCertificate");
        var restPath = '/moduleapi/fragasvar/' + id;
        $http.get(restPath).success(function(data) {
            $log.debug("got data:" + data);
            onSuccess(data);
        }).error(function(data, status, headers, config) {
            $log.error("error " + status);
            // Let calling code handle the error of no data response
            onError(data);
        });
    }

    /*
     * save new answer to a question
     */
    function _saveAnswer(fragaSvar, onSuccess, onError) {
        $log.debug("_saveAnswer");

        var restPath = '/moduleapi/fragasvar/' + fragaSvar.internReferens + "/answer";
        $http.put(restPath, fragaSvar.svarsText).success(function(data) {
            $log.debug("got data:" + data);
            onSuccess(data);
        }).error(function(data, status, headers, config) {
            $log.error("error " + status);
            // Let calling code handle the error of no data response
            onError(data);
        });
    }
    /*
     * update the handled status to handled ("Closed") of a QuestionAnswer
     */
    function _closeAsHandled(fragaSvar, onSuccess, onError) {
        $log.debug("_closeAsHandled");

        var restPath = '/moduleapi/fragasvar/close/' + fragaSvar.internReferens;
        $http.get(restPath).success(function(data) {
            $log.debug("got data:" + data);
            onSuccess(data);
        }).error(function(data, status, headers, config) {
            $log.error("error " + status);
            // Let calling code handle the error of no data response
            onError(data);
        });
    }
    /*
     * update the handled status to unhandled ("ANSWERED or
     * PENDING_EXTERNAL_ACTION depending if the question has an answer set or
     * not") of a QuestionAnswer
     */
    function _openAsUnhandled(fragaSvar, onSuccess, onError) {
        $log.debug("_openAsUnhandled");

        var restPath = '/moduleapi/fragasvar/open/' + fragaSvar.internReferens;
        $http.get(restPath).success(function(data) {
            $log.debug("got data:" + data);
            onSuccess(data);
        }).error(function(data, status, headers, config) {
            $log.error("error " + status);
            // Let calling code handle the error of no data response
            onError(data);
        });
    }

    /*
     * save new question
     */
    function _saveNewQuestion(certId, question, onSuccess, onError) {
        $log.debug("_saveNewQuestion");
        var payload = {};
        payload.amne = question.chosenTopic.value;
        payload.frageText = question.frageText;

        var restPath = "/moduleapi/fragasvar/" + certId;
        $http.post(restPath, payload).success(function(data) {
            $log.debug("got callback data:" + data);
            onSuccess(data);
        }).error(function(data, status, headers, config) {
            $log.error("error " + status);
            // Let calling code handle the error of no data response
            onError(data);
        });
    }
    
    /*
     * Toggle vidarebefordrad state
     */
    function _setVidareBefordradState(id, isVidareBefordrad, callback) {
        $log.debug("_setVidareBefordradState");
        var restPath = '/moduleapi/fragasvar/' + id + "/setDispatchState";
        $http.put(restPath, isVidareBefordrad.toString()).success(function(data) {
            $log.debug("got data:" + data);
            callback(data);
        }).error(function(data, status, headers, config) {
            $log.error("error " + status);
            // Let calling code handle the error of no data response
            callback(null);
        });
    }
    
    //Todo: this functionality also exists in wc, maybe refactor to common resource (wc-util)
    function _showErrorMessageDialog(message, callback) {
        var msgbox = $dialog.messageBox("Ett fel inträffade", message, [ {
            label : 'OK',
            result : true
        } ]);

        msgbox.open().then(function(result) {
            if (callback) {
                callback(result)
            }
        });
    }
    // Return public API for the service
    return {
        getQAForCertificate : _getQAForCertificate,
        saveAnswer : _saveAnswer,
        saveNewQuestion : _saveNewQuestion,
        closeAsHandled : _closeAsHandled,
        openAsUnhandled : _openAsUnhandled,
        setVidareBefordradState : _setVidareBefordradState,
        showErrorMessageDialog: _showErrorMessageDialog
    }
} ]);

/**
 * QACtrl - Controller for logic related to viewing a Fraga/Svar for a
 * certificate
 * 
 */
angular.module('wc.fragasvarmodule').controller('QACtrl',
        [ '$scope', '$rootScope', '$log', '$timeout', 'fragaSvarService', function CreateCertCtrl($scope, $rootScope, $log, $timeout, fragaSvarService) {

            // init state
            $scope.qaList = {};
            $scope.widgetState = {
                doneLoading : false,
                activeErrorMessageKey : null,
                newQuestionOpen : false
            }

            // Request loading of QA's for this certificate
            fragaSvarService.getQAForCertificate($scope.MODULE_CONFIG.CERT_ID_PARAMETER, function(result) {
                $log.debug("getQAForCertificate:success data:" + result);
                $scope.widgetState.doneLoading = true;
                $scope.widgetState.activeErrorMessageKey = null;
                $scope.decorateWithGUIParameters(result);
                $scope.qaList = result;

            }, function(errorData) {
                // show error view
                $scope.widgetState.doneLoading = true;
                $scope.widgetState.activeErrorMessageKey = errorData.errorCode;
            });

            $scope.decorateWithGUIParameters = function(list) {
                // answerDisabled
                // answerButtonToolTip
                angular.forEach(list, function(qa, key) {
                    if (qa.amne == "PAMINNELSE") {
                        // RE-020 Påminnelser is never answerable
                        qa.answerDisabled = true;
                        qa.answerButtonToolTip = "Påminnelser kan inte besvaras";
                    } else if (qa.amne == "KOMPLETTERING_AV_LAKARINTYG" && !$rootScope.WC_CONTEXT.lakare) {
                        // RE-005, RE-006
                        qa.answerDisabled = true;
                        qa.answerButtonToolTip = "Kompletteringar kan endast besvaras av läkare";
                    } else {
                        qa.answerDisabled = false;
                        qa.answerButtonToolTip = "Skicka svaret";
                    }

                });
            }
            $scope.openIssuesFilter = function(qa) {
                return qa.status != "CLOSED";
            };

            $scope.closedIssuesFilter = function(qa) {
                return qa.status === "CLOSED";
            };
            $scope.newQuestionOpen = false;

            $scope.toggleQuestionForm = function() {
                $scope.widgetState.newQuestionOpen = !$scope.widgetState.newQuestionOpen;
                $scope.initQuestionForm();
            }

            /**
             * Functions bound to individual fragasvar entities's
             * 
             */
            $scope.sendQuestion = function sendQuestion(newQuestion) {
                $log.debug("sendQuestion:" + newQuestion);
                newQuestion.doneLoading = false; // trigger local spinner
                fragaSvarService.saveNewQuestion($scope.MODULE_CONFIG.CERT_ID_PARAMETER, newQuestion, function(result) {
                    $log.debug("Got saveNewQuestion result:" + result);
                    newQuestion.doneLoading = true;
                    newQuestion.activeErrorMessageKey = null;
                    if (result != null) {
                        // result is a new FragaSvar Instance: add it to our
                        // local repo
                        $scope.qaList.push(result);
                        // close question form
                        $scope.toggleQuestionForm();

                    }
                }, function(errorData) {
                    // show error view
                    newQuestion.doneLoading = true;
                    newQuestion.activeErrorMessageKey = errorData.errorCode;
                });
            }

            $scope.sendAnswer = function sendAnswer(qa) {
                qa.doneLoading = false; // trigger local spinner
                fragaSvarService.saveAnswer(qa, function(result) {
                    $log.debug("Got saveAnswer result:" + result);
                    qa.doneLoading = true;
                    qa.activeErrorMessageKey = null;
                    if (result != null) {
                        angular.copy(result, qa);
                    }
                }, function(errorData) {
                    // show error view
                    qa.doneLoading = true;
                    qa.activeErrorMessageKey = errorData.errorCode;
                });

            }
            $scope.onVidareBefordradChange = function(qa) {
                qa.updateInProgress = true;
                $timeout(
                        function() { // wrap in timeout to
                                        // simulate
                            // latency -
                            fragaSvarService
                                    .setVidareBefordradState(
                                            qa.internReferens,
                                            qa.vidarebefordrad,
                                            function(result) {
                                                qa.updateInProgress = false;

                                                if (result != null) {
                                                    qa.vidarebefordrad = result.vidarebefordrad;
                                                } else {
                                                    qa.vidarebefordrad = !qa.vidarebefordrad;
                                                    fragaSvarService
                                                            .showErrorMessageDialog("Kunde inte markera/avmarkera frågan som vidarebefordrad. Försök gärna igen för att se om felet är tillfälligt. Annars kan du kontakta supporten");
                                                }
                                            });
                        }, 1000);
            }
            
            $scope.updateAsHandled = function(qa) {
                $log.debug("updateAsHandled:" + qa);
                qa.doneLoading = false; // trigger local spinner
                fragaSvarService.closeAsHandled(qa, function(result) {
                    $log.debug("Got updateAsHandled result:" + result);
                    qa.activeErrorMessageKey = null;
                    qa.doneLoading = true;
                    if (result != null) {
                        angular.copy(result, qa);
                    }
                }, function(errorData) {
                    // show error view
                    qa.doneLoading = true;
                    qa.activeErrorMessageKey = errorData.errorCode;
                });

            }
            $scope.updateAsUnHandled = function(qa) {
                $log.debug("updateAsUnHandled:" + qa);
                qa.doneLoading = false; // trigger local spinner
                fragaSvarService.openAsUnhandled(qa, function(result) {
                    $log.debug("Got openAsUnhandled result:" + result);
                    qa.activeErrorMessageKey = null;
                    qa.doneLoading = true;
                    if (result != null) {
                        angular.copy(result, qa);
                    }
                }, function(errorData) {
                    // show error view
                    qa.doneLoading = true;
                    qa.activeErrorMessageKey = errorData.errorCode;
                });

            }

            $scope.initQuestionForm = function() {
                // Topics are defined under RE-13
                $scope.newQuestion = {
                    topics : [ {
                        label : 'Arbetstidsförläggning',
                        value : 'ARBETSTIDSFORLAGGNING'
                    }, {
                        label : 'Avstämningsmöte',
                        value : 'AVSTAMNINGSMOTE'
                    }, {
                        label : 'Kontakt',
                        value : 'KONTAKT'
                    }, {
                        label : 'Övrigt',
                        value : 'OVRIGT'
                    } ],
                    frageText : ""
                };
                $scope.newQuestion.chosenTopic = $scope.newQuestion.topics[3]; // 'Övrigt'
                // is
                // default
            }
            $scope.initQuestionForm();

        } ]);
