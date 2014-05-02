define([], function() {
    'use strict';

    return ['$http', '$log',
        function($http, $log) {

            /*
             * Load certificate details from the server.
             */
            function _getCertificate(id, onSuccess, onError) {
                $log.debug('_getCertificate id:' + id);
                var restPath = '/moduleapi/certificate/' + id;
                $http.get(restPath).success(function(data) {
                    $log.debug('_getCertificate data:' + data);
                    onSuccess(data);
                }).error(function(data, status) {
                    $log.error('error ' + status);
                    onError(data);
                });
            }

            /**
             * Get a certificate draft with the specified id from the server.
             */
            function _getDraft(id, onSuccess, onError) {
                $log.debug('_getDraft id: ' + id);
                var restPath = '/moduleapi/intyg/draft/' + id;
                $http.get(restPath).
                    success(function(data) {
                        $log.debug('_getDraft data: ' + data);
                        onSuccess(data);
                    }).
                    error(function(data, status) {
                        $log.error('error ' + status);
                        onError(data);
                    });
            }

            /**
             * Saves a certificate draft to the server.
             */
            function _saveDraft(id, cert, onSuccess, onError) {
                $log.debug('_saveDraft id: ' + id);
                var restPath = '/moduleapi/intyg/draft/' + id;
                $http.put(restPath, cert).
                    success(function(data) {
                        $log.debug('_saveDraft data: ' + data);
                        onSuccess(data);
                    }).
                    error(function(data, status) {
                        $log.error('error ' + status);
                        onError(data);
                    });
            }

            /**
             * Discards a certificate draft and removes it from the server.
             */
            function _discardDraft(id, onSuccess, onError) {
                $log.debug('_discardDraft id: ' + id);
                var restPath = '/moduleapi/intyg/draft/' + id;
                $http.remove(restPath).
                    success(function(data) {
                        $log.debug('_discardDraft data: ' + data);
                        onSuccess(data);
                    }).
                    error(function(data, status) {
                        $log.error('error ' + status);
                        onError(data);
                    });
            }

            // Return public API for the service
            return {
                getCertificate: _getCertificate,
                getDraft: _getDraft,
                saveDraft: _saveDraft,
                discardDraft: _discardDraft
            };
        }
    ];
});
