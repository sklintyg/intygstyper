define(['angular'], function(angular) {
    'use strict';

    var moduleName = 'wc.CertificateService';

    /**
     * Common certificate management methods between certificate modules
     */
    var CertificateService = angular.module(moduleName, []);

    CertificateService.factory('CertificateService', [ '$http', '$log',
        function($http, $log) {

            /*
             * Load certificate details from the server.
             */
            function _getCertificate(id, onSuccess, onError) {
                $log.debug('_getCertificate id:' + id);
                var restPath = '/moduleapi/intyg/signed/' + id;
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
                $http['delete'](restPath).
                    success(function(data) {
                        $log.debug('_discardDraft data: ' + data);
                        onSuccess(data);
                    }).
                    error(function(data, status) {
                        $log.error('error ' + status);
                        onError(data);
                    });
            }

            function _signDraft(id, onSuccess, onError) {
                $log.debug('_signDraft id:' + id);
                var restPath = '/moduleapi/intyg/signera/server/' + id;
                $http.post(restPath).
                    success(function(data) {
                        $log.debug('_signDraft data: ' + data);
                        onSuccess(data);
                    }).
                    error(function(data, status) {
                        $log.error('error ' + status);
                        onError(data);
                    });
            }

            function _getSignStatus(biljettId, onSuccess, onError) {
                $log.debug('_getSignStatus biljettId: ' + biljettId);
                var restPath = '/moduleapi/intyg/signera/status/' + biljettId;
                $http.get(restPath).
                    success(function(data) {
                        $log.debug('_getSignStatus status: ' + data.status);
                        onSuccess(data);
                    }).
                    error(function (data, status) {
                        $log.error('error ' + status);
                        onError(data);
                    });
            }

            // Return public API for the service
            return {
                getCertificate: _getCertificate,
                getDraft: _getDraft,
                saveDraft: _saveDraft,
                discardDraft: _discardDraft,
                signDraft: _signDraft,
                getSignStatus : _getSignStatus
            };
        }
    ]);

    return moduleName;
});
