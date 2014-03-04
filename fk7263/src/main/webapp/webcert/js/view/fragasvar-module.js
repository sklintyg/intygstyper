/**
 * Fragasvar Module - specific services and controllers related to FragaSvar
 * functionality in fk7263 module. 
 * (Common Fragasvar functionality is contained in wc-common-fragasvar-module in WC app)
 */
angular.module('wc.fragasvarmodule', []);
angular.module('wc.fragasvarmodule').factory('fragaSvarService', [ '$http', '$log', function($http, $log) {

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


    // Return public API for the service
    return {
        getQAForCertificate : _getQAForCertificate,
        saveAnswer : _saveAnswer,
        saveNewQuestion : _saveNewQuestion,
        closeAsHandled : _closeAsHandled,
        openAsUnhandled : _openAsUnhandled
    }
} ]);

/**
 * QACtrl - Controller for logic related to viewing a Fraga/Svar for a
 * certificate
 * 
 */
angular
        .module('wc.fragasvarmodule')
        .controller(
                'QACtrl',
                [
                        '$scope',
                        '$rootScope',
                        '$log',
                        '$timeout',
                        '$window',
                        'fragaSvarService',
                        'fragaSvarCommonService',
                        'wcDialogService',
                      'User',
                        function CreateCertCtrl($scope, $rootScope, $log, $timeout, $window, fragaSvarService, fragaSvarCommonService, wcDialogService, User) {

                            // init state
                            $scope.qaList = {};
                            $scope.widgetState = {
                                doneLoading : false,
                                activeErrorMessageKey : null,
                                newQuestionOpen : false,
                                sentMessage : false,
                                focusQuestion : false
                            }

                            // Request loading of QA's for this certificate
                            fragaSvarService.getQAForCertificate($scope.MODULE_CONFIG.CERT_ID_PARAMETER, function(result) {
                                $log.debug("getQAForCertificate:success data:" + result);
                                $scope.widgetState.doneLoading = true;
                                $scope.widgetState.activeErrorMessageKey = null;
                                decorateWithGUIParameters(result);
                                $scope.qaList = result;

                            }, function(errorData) {
                                // show error view
                                $scope.widgetState.doneLoading = true;
                                $scope.widgetState.activeErrorMessageKey = errorData.errorCode;
                            });

                           var decorateWithGUIParameters = function(list) {
                                // answerDisabled
                                // answerButtonToolTip
                                angular.forEach(list, function(qa, key) {
                                    decorateSingleItem(qa);
                                });
                            }

                            var decorateSingleItem = function(qa) {
                                if (qa.amne == "PAMINNELSE") {
                                    // RE-020 Påminnelser is never
                                    // answerable
                                    qa.answerDisabled = true;
                                    qa.answerDisabledReason = undefined; // Påminnelser kan inte besvaras men det behöver vi inte säga
                                } else if (qa.amne == "KOMPLETTERING_AV_LAKARINTYG" && !User.userContext.lakare) {
                                    // RE-005, RE-006
                                    qa.answerDisabled = true;
                                    qa.answerDisabledReason = "Kompletteringar kan endast besvaras av läkare.";
                                } else {
                                    qa.answerDisabled = false;
                                    qa.answerDisabledReason = undefined;
                                }
                                fragaSvarCommonService.decorateSingleItemMeasure(qa);
                            }
                            $scope.openIssuesFilter = function(qa) {
                                return qa.status != "CLOSED";
                            };

                            $scope.closedIssuesFilter = function(qa) {
                                return qa.status === "CLOSED";
                            };

                            $scope.toggleQuestionForm = function() {
                                $scope.widgetState.newQuestionOpen = !$scope.widgetState.newQuestionOpen;
                                $scope.initQuestionForm();
                                // hide sent message
                                $scope.widgetState.sentMessage = false;
                              $scope.widgetState.focusQuestion = true;
                            }

                            /**
                             * Functions bound to individual fragasvar
                             * entities's
                             * 
                             */
                            $scope.sendQuestion = function sendQuestion(newQuestion) {
                                $log.debug("sendQuestion:" + newQuestion);
                                newQuestion.updateInProgress = true; // trigger
                                // local
                                // spinner
                     
                                    fragaSvarService.saveNewQuestion($scope.MODULE_CONFIG.CERT_ID_PARAMETER, newQuestion, function(result) {
                                        $log.debug("Got saveNewQuestion result:" + result);
                                        newQuestion.updateInProgress = false;
                                        newQuestion.activeErrorMessageKey = null;
                                        if (result != null) {
                                            decorateSingleItem(result);
                                            // result is a new FragaSvar
                                            // Instance:
                                            // add it to our
                                            // local repo
                                            $scope.qaList.push(result);
                                            $scope.activeQA = result.internReferens;
                                            // close question form
                                            $scope.toggleQuestionForm();
                                            // show sent message
                                            $scope.widgetState.sentMessage = true;
                                        }
                                    }, function(errorData) {
                                        // show error view
                                        newQuestion.updateInProgress = false;
                                        newQuestion.activeErrorMessageKey = errorData.errorCode;
                                    });
                            }

                            $scope.dismissProxy = function(qa) {
                            	
                            		if(qa == undefined){
                            			$scope.widgetState.sentMessage = false;
                            			return;
                            		}
                            	
                                for ( var i = 0; i < $scope.qaList.length; i++) {
                                    if (qa === $scope.qaList[i]) {
                                        $scope.qaList.splice(i, 1);
                                        return;
                                    }
                                }

                            }

                            $scope.sendAnswer = function sendAnswer(qa) {
                                qa.updateInProgress = true; // trigger local
                                // spinner
               
                                    fragaSvarService.saveAnswer(qa, function(result) {
                                        $log.debug("Got saveAnswer result:" + result);
                                        qa.updateInProgress = false;
                                        qa.activeErrorMessageKey = null;
                                        if (result != null) {
                                            decorateSingleItem(result);
                                            // Create a proxyCopy
                                            var proxyCopy = angular.copy(qa);
                                            proxyCopy.proxyMessage = "fragasvar.answer.is.sent";
                                            $scope.qaList.push(proxyCopy);
                                            // update real item
                                            angular.copy(result, qa);
                                            $scope.activeQA = qa.internReferens;

                                        }
                                    }, function(errorData) {
                                        // show error view
                                        qa.updateInProgress = false;
                                        qa.activeErrorMessageKey = errorData.errorCode;
                                    });
                            }
                            $scope.onVidareBefordradChange = function(qa) {
                                qa.forwardInProgress = true;
                   
                                            fragaSvarCommonService
                                                    .setVidareBefordradState(
                                                            qa.internReferens,
                                                            qa.vidarebefordrad,
                                                            function(result) {
                                                                qa.forwardInProgress = false;

                                                                if (result != null) {
                                                                    qa.vidarebefordrad = result.vidarebefordrad;
                                                                } else {
                                                                    qa.vidarebefordrad = !qa.vidarebefordrad;
                                                                    wcDialogService
                                                                            .showErrorMessageDialog("Kunde inte markera/avmarkera frågan som vidarebefordrad. Försök gärna igen för att se om felet är tillfälligt. Annars kan du kontakta supporten");
                                                                }
                                                            });
                            }

                            $scope.updateAsHandled = function(qa) {
                                $log.debug("updateAsHandled:" + qa);
                                qa.updateHandledStateInProgress = true;
             
                                    fragaSvarService.closeAsHandled(qa, function(result) {
                                        $log.debug("Got updateAsHandled result:" + result);
                                        qa.activeErrorMessageKey = null;
                                        qa.updateHandledStateInProgress = false;
                                        if (result != null) {
                                            decorateSingleItem(result);
                                            // Create a proxyCopy
                                            var proxyCopy = angular.copy(qa);
                                            proxyCopy.proxyMessage = "fragasvar.marked.as.hanterad";
                                            $scope.qaList.push(proxyCopy);

                                            angular.copy(result, qa);
                                            $scope.activeQA = qa.internReferens;
                                        }
                                    }, function(errorData) {
                                        // show error view
                                        qa.updateHandledStateInProgress = false;
                                        qa.activeErrorMessageKey = errorData.errorCode;
                                    });
                            }
                            $scope.updateAsUnHandled = function(qa) {
                                $log.debug("updateAsUnHandled:" + qa);
                                qa.updateHandledStateInProgress = true; // trigger
                                // local
      
                                    fragaSvarService.openAsUnhandled(qa, function(result) {
                                        $log.debug("Got openAsUnhandled result:" + result);
                                        qa.activeErrorMessageKey = null;
                                        qa.updateHandledStateInProgress = false;
                                        
                                        if (result != null) {
                                            decorateSingleItem(result);
                                         // Create a proxyCopy
                                            var proxyCopy = angular.copy(qa);
                                            proxyCopy.proxyMessage = "fragasvar.marked.as.ohanterad";
                                            $scope.qaList.push(proxyCopy);

                                            angular.copy(result, qa);
                                            $scope.activeQA = qa.internReferens;
                                        }
                                    }, function(errorData) {
                                        // show error view
                                        qa.updateHandledStateInProgress = false;
                                        qa.activeErrorMessageKey = errorData.errorCode;
                                    });
                            }

                            // Handle vidarebefordra dialog
                            $scope.openMailDialog = function(qa) {
                                //use timeout so that external mail client has a chance to start before showing dialog
                                $timeout(function() {
                                    fragaSvarCommonService.handleVidareBefodradToggle(qa, $scope.onVidareBefordradChange);
                                }, 1000);
                                //Launch mail client
                                $window.location = fragaSvarCommonService.buildMailToLink(qa);
                            }
                            
                            $scope.questionValidForSubmit = function(newQuestion) {
                                var validToSend= newQuestion.chosenTopic.value && newQuestion.frageText && !newQuestion.updateInProgress;
                                if (!validToSend) {
                                    newQuestion.sendButtonToolTip = "Du måste välja ett ämne och skriva en frågetext innan du kan skicka frågan";
                                } else {
                                    newQuestion.sendButtonToolTip = "Skicka frågan";
                                }
                                return validToSend;
                            }
                            
                            $scope.initQuestionForm = function() {
                                // Topics are defined under RE-13
                                $scope.newQuestion = {
                                    topics : [ {
                                        label : 'Välj ämne',
                                        value : null
                                    },{
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
                                $scope.newQuestion.chosenTopic = $scope.newQuestion.topics[0]; // 'Välj ämne'
                                // is
                                // default
                            }
                            $scope.initQuestionForm();

                        } ]);
