#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
define([
], function () {
    'use strict';

    return ['${symbol_dollar}http', '${symbol_dollar}log',
        function (${symbol_dollar}http, ${symbol_dollar}log) {

            /**
             * Get a certificate draft with the specified id from the server.
             */
            function _getDraft (id, onSuccess, onError) {
                ${symbol_dollar}log.debug('_getDraft id: ' + id);
                var restPath = '/moduleapi/intyg/draft/' + id;
                ${symbol_dollar}http.get(restPath).
                    success(function (data) {
                        ${symbol_dollar}log.debug('_getDraft data: ' + data);
                        onSuccess(data);
                    }).
                    error(function (data, status) {
                        ${symbol_dollar}log.error('error ' + status);
                        onError(data);
                    });
            }

            /**
             * Saves a certificate draft to the server.
             */
            function _saveDraft (id, cert, onSuccess, onError) {
                ${symbol_dollar}log.debug('_saveDraft id: ' + id);
                var restPath = '/moduleapi/intyg/draft/' + id;
                ${symbol_dollar}http.put(restPath, cert).
                    success(function (data) {
                        ${symbol_dollar}log.debug('_saveDraft data: ' + data);
                        onSuccess(data);
                    }).
                    error(function (data, status) {
                        ${symbol_dollar}log.error('error ' + status);
                        onError(data);
                    });
            }

            /**
             * Discards a certificate draft and removes it from the server.
             */
            function _discardDraft (id, onSuccess, onError) {
                ${symbol_dollar}log.debug('_discardDraft id: ' + id);
                var restPath = '/moduleapi/intyg/draft/' + id;
                ${symbol_dollar}http.delete(restPath).
                    success(function (data) {
                        ${symbol_dollar}log.debug('_discardDraft data: ' + data);
                        onSuccess(data);
                    }).
                    error(function (data, status) {
                        ${symbol_dollar}log.error('error ' + status);
                        onError(data);
                    });
            }

            // Return public API for the service
            return {
                getDraft : _getDraft,
                saveDraft : _saveDraft,
                discardDraft : _discardDraft
            };
        }];
});
