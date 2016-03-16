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

angular.module('fk7263').factory('fk7263.fragaSvarProxy',
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
         * answer komplettering with a new intyg (basically do a copy with a 'komplettering' relation to this intyg)
         */
        function _answerWithIntyg(fragaSvar, intygsTyp, intygCopyRequest, onSuccess, onError) {
            $log.debug('_answerWithIntyg: fragaSvarId:' + fragaSvar.internReferens + ' intygsTyp: ' + intygsTyp);

            var restPath = '/api/intyg/' + intygsTyp + '/' + intygCopyRequest.intygId + '/komplettera';
            var payload = {};
            payload.patientPersonnummer = intygCopyRequest.patientPersonnummer;
            if (intygCopyRequest.nyttPatientPersonnummer) {
                payload.nyttPatientPersonnummer = intygCopyRequest.nyttPatientPersonnummer;
            }

            $http.post(restPath, payload).success(function(data) {
                $log.debug('got data:' + data.intygsUtkastId);
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
            angular.forEach(qas, function(qa/*, key*/) {
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
            answerWithIntyg: _answerWithIntyg,
            saveNewQuestion: _saveNewQuestion,
            closeAsHandled: _closeAsHandled,
            closeAllAsHandled: _closeAllAsHandled,
            openAsUnhandled: _openAsUnhandled
        };
    });
