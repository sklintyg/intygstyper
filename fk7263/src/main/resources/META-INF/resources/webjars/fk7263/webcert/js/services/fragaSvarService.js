angular.module('fk7263').factory('fk7263.fragaSvarService',
    function($http, $log) {
        'use strict';

        /*
         * Load questions and answers data for a certificate
         */
        function _getQAForCertificate(id, onSuccess, onError) {
            $log.debug('_getQAForCertificate');
            var restPath = '/moduleapi/fragasvar/' + id;
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
        function _saveAnswer(fragaSvar, onSuccess, onError) {
            $log.debug('_saveAnswer');

            var restPath = '/moduleapi/fragasvar/' + fragaSvar.internReferens + '/answer';
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
        function _closeAsHandled(fragaSvar, onSuccess, onError) {
            $log.debug('_closeAsHandled');

            var restPath = '/moduleapi/fragasvar/close/' + fragaSvar.internReferens;
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
         * update the handled status to unhandled ('ANSWERED or PENDING_EXTERNAL_ACTION depending if the question has an
         * answer set or not') of a QuestionAnswer
         */
        function _openAsUnhandled(fragaSvar, onSuccess, onError) {
            $log.debug('_openAsUnhandled');

            var restPath = '/moduleapi/fragasvar/open/' + fragaSvar.internReferens;
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
        function _saveNewQuestion(certId, question, onSuccess, onError) {
            $log.debug('_saveNewQuestion');
            var payload = {};
            payload.amne = question.chosenTopic.value;
            payload.frageText = question.frageText;

            var restPath = '/moduleapi/fragasvar/' + certId;
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
            openAsUnhandled: _openAsUnhandled
        };
    });
