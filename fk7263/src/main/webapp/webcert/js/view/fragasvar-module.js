/**
 * Fragasvar Module - services and controllers related to FragaSvar
 * functionality in webcert.
 */
angular.module('wc.fragasvarmodule', []);
angular.module('wc.fragasvarmodule').factory('fragaSvarService', [ '$http', '$log', function($http, $log) {

    /*
     * Load questions and answers data for a certificate
     */
    function _getQAForCertificate(id, callback) {
        $log.debug("_getQAForCertificate");
        var restPath = '/moduleapi/fragasvar/' + id;
        $http.get(restPath).success(function(data) {
            $log.debug("got data:" + data);
            callback(data);
        }).error(function(data, status, headers, config) {
            $log.error("error " + status);
            // Let calling code handle the error of no data response
            callback(null);
        });
    }

    /*
     * save new answer to a question
     */
    function _saveAnswer(fragaSvar, callback) {
        $log.debug("_saveAnswer");

        var restPath = '/moduleapi/fragasvar/' + fragaSvar.internReferens + "/answer";
        $http.put(restPath, fragaSvar.svarsText).success(function(data) {
            $log.debug("got data:" + data);
            callback(data);
        }).error(function(data, status, headers, config) {
                $log.error("error " + status);
                // Let calling code handle the error of no data response
                callback(null);
            });
    }
    /*
     * update the handled status to handled ("Closed") of a QuestionAnswer
     */
    function _closeAsHandled(fragaSvar, callback) {
        $log.debug("_closeAsHandled");

        var restPath = '/moduleapi/fragasvar/close/' + fragaSvar.internReferens;
        $http.get(restPath).success(function(data) {
            $log.debug("got data:" + data);
            callback(data);
        }).error(function(data, status, headers, config) {
                $log.error("error " + status);
                // Let calling code handle the error of no data response
                callback(null);
            });
    }
    /*
     * update the handled status to unhandled ("ANSWERED or PENDING_EXTERNAL_ACTION depending if the question has an answer set or not") of a QuestionAnswer
     */
    function _openAsUnhandled(fragaSvar, callback) {
        $log.debug("_openAsUnhandled");

        var restPath = '/moduleapi/fragasvar/open/' + fragaSvar.internReferens;
        $http.get(restPath).success(function(data) {
            $log.debug("got data:" + data);
            callback(data);
        }).error(function(data, status, headers, config) {
                $log.error("error " + status);
                // Let calling code handle the error of no data response
                callback(null);
            });
    }

    /*
     * save new question
     */
    function _saveNewQuestion(certId, question, callback) {
        $log.debug("_saveNewQuestion");
        var payload = {};
        payload.amne = question.chosenTopic.value;
        payload.frageText = question.frageText;

        var restPath = "/moduleapi/fragasvar/" + certId;
        $http.post(restPath, payload).success(function(data) {
            $log.debug("got callback data:" + data);
            callback(data);
        }).error(function(data, status, headers, config) {
            $log.error("error " + status);
            // Let calling code handle the error of no data response
            callback(null);
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
angular.module('wc.fragasvarmodule').controller('QACtrl',
        [ '$scope', '$rootScope', '$log', '$timeout', 'fragaSvarService', function CreateCertCtrl($scope, $rootScope, $log, $timeout, fragaSvarService) {

            // init state
            $scope.qaList = {};
            $scope.widgetState = {
                doneLoading : false,
                hasError : false,
                newQuestionOpen : false
            }
            // Request loading of QA's for this certificate
            fragaSvarService.getQAForCertificate($scope.MODULE_CONFIG.CERT_ID_PARAMETER, function(result) {
                $log.debug("Got getQAForCertificate data:" + result);
                $scope.widgetState.doneLoading = true;
                if (result != null) {
                    $scope.decorateWithGUIParameters(result);
                    $scope.qaList = result;
                } else {
                    // show error view
                    $scope.widgetState.hasError = true;
                }
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

            $scope.sendAnswer = function sendAnswer(qa) {
                $log.debug("saveAnswer:" + qa);
                var qaActive = qa;
                fragaSvarService.saveAnswer(qa, function(result) {
                    $log.debug("Got saveAnswer result:" + result);
                    $scope.widgetState.doneLoading = true;
                    if (result != null) {
                        angular.copy(result, qa);
                    } else {
                        // show error view
                        $scope.widgetState.hasError = true;
                    }
                });
            }

            $scope.updateAsHandled = function(qa){
                $log.debug("updateAsHandled:" + qa);
                fragaSvarService.closeAsHandled(qa, function(result) {
                    $log.debug("Got saveAnswer result:" + result);
                    $scope.widgetState.doneLoading = true;
                    if (result != null) {
                        angular.copy(result, qa);
                    } else {
                        // show error view
                        $scope.widgetState.hasError = true;
                    }
                });

            }
            $scope.updateAsUnHandled = function(qa){
                $log.debug("updateAsUnHandled:" + qa);
                fragaSvarService.openAsUnhandled(qa, function(result) {
                    $log.debug("Got saveAnswer result:" + result);
                    $scope.widgetState.doneLoading = true;
                    if (result != null) {
                        angular.copy(result, qa);
                    } else {
                        // show error view
                        $scope.widgetState.hasError = true;
                    }
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

            $scope.sendQuestion = function sendQuestion(newQuestion) {
                $log.debug("sendQuestion:" + newQuestion);

                fragaSvarService.saveNewQuestion($scope.MODULE_CONFIG.CERT_ID_PARAMETER, newQuestion, function(result) {
                    $log.debug("Got saveNewQuestion result:" + result);
                    if (result != null) {
                        // result is a new FragaSvar Instance: add it to our
                        // local repo
                        $scope.qaList.push(result);
                        // close question form
                        $scope.toggleQuestionForm();

                    } else {
                        // show error view
                        $scope.widgetState.hasError = true;
                    }
                });
            }

        } ]);
