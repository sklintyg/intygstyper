angular.module('luse').factory('sjukersattning.fragaSvarService',
    function($http, $log) {
        'use strict';

        /*
         * Load questions and answers data for a certificate
         */
        function _getQAForCertificate(intygsId, intygsTyp, onSuccess, onError) {
            $log.debug('_getQAForCertificate: intygsId:' + intygsId + ' intygsTyp: ' + intygsTyp);
            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + intygsId;
            $http.get(restPath).success(function(data) {
                $log.debug('got data:' + data);
                onSuccess(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                // Let calling code handle the error of no data response
                onError(data);
            });
        }

        /*
         * save new answer to a question
         */
        function _saveAnswer(fragaSvar, intygsTyp, onSuccess, onError) {
            $log.debug('_saveAnswer: fragaSvarId:' + fragaSvar.internReferens + ' intygsTyp: ' + intygsTyp);

            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + fragaSvar.internReferens + '/besvara';
            $http.put(restPath, fragaSvar.svarsText).success(function(data) {
                $log.debug('got data:' + data);
                onSuccess(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                // Let calling code handle the error of no data response
                onError(data);
            });
        }

        /*
         * update the handled status to handled ('Closed') of a QuestionAnswer
         */
        function _closeAsHandled(fragaSvarId, intygsTyp, onSuccess, onError) {
            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + fragaSvarId + '/stang';
            $http.get(restPath).success(function(data) {
                $log.debug('got data:' + data);
                onSuccess(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                // Let calling code handle the error of no data response
                onError(data);
            });
        }

        /*
         * update the handled status to handled ('Closed') of a QuestionAnswer
         */
        function _closeAllAsHandled(qas, onSuccess, onError) {
            var restPath = '/moduleapi/fragasvar/stang';
            var fs = [];
            angular.forEach(qas, function(qa, key) {
                this.push({ intygsTyp : qa.intygsReferens.intygsTyp, fragaSvarId:qa.internReferens });
            }, fs);

            $http.put(restPath, fs).success(function(data) {
                $log.debug('got data:' + data);
                onSuccess(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                // Let calling code handle the error of no data response
                onError(data);
            });
        }

        /*
         * update the handled status to unhandled ('ANSWERED or PENDING_EXTERNAL_ACTION depending if the question has an
         * answer set or not') of a QuestionAnswer
         */
        function _openAsUnhandled(fragaSvarId, intygsTyp, onSuccess, onError) {
            $log.debug('_openAsUnhandled: fragaSvarId:' + fragaSvarId + ' intygsTyp: ' + intygsTyp);

            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + fragaSvarId + '/oppna';
            $http.get(restPath).success(function(data) {
                $log.debug('got data:' + data);
                onSuccess(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                // Let calling code handle the error of no data response
                onError(data);
            });
        }

        /*
         * save new question
         */
        function _saveNewQuestion(intygsId, intygsTyp, question, onSuccess, onError) {
            $log.debug('_saveNewQuestion: intygsId:' + intygsId + ' intygsTyp: ' + intygsTyp);
            var payload = {};
            payload.amne = question.chosenTopic.value;
            payload.frageText = question.frageText;

            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + intygsId;
            $http.post(restPath, payload).success(function(data) {
                $log.debug('got callback data:' + data);
                onSuccess(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                // Let calling code handle the error of no data response
                onError(data);
            });
        }


        // Return public API for the service
        return {
            getQAForCertificate: _getQAForCertificate,
            saveAnswer: _saveAnswer,
            saveNewQuestion: _saveNewQuestion,
            closeAsHandled: _closeAsHandled,
            closeAllAsHandled: _closeAllAsHandled,
            openAsUnhandled: _openAsUnhandled
        };
    });
