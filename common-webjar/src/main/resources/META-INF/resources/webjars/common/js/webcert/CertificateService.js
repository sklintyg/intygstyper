define(['angular'], function(angular) {
    'use strict';

    var moduleName = 'wc.CertificateService';

    /**
     * Common certificate management methods between certificate modules
     */
    var CertificateService = angular.module(moduleName, []);

    CertificateService.factory('CertificateService', [ '$http', '$log',
        function($http, $log) {

            function _handleError(callback, error) {
                if (callback) {
                    callback(error);
                } else {
                    $log.error(error);
                }
            }

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

            function _getSigneringshash(intygId, onSuccess, onError) {
                $log.debug('_getSigneringshash, intygId: ' + intygId);
                var restPath = '/moduleapi/intyg/signeringshash/' + intygId;
                $http.post(restPath).
                    success(function(data) {
                        onSuccess(data);
                    }).
                    error(function(error) {
                        _handleError(onError, error);
                    });
            }

            function _getSigneringsstatus(ticketId, onSuccess, onError) {
                $log.debug('_getSigneringsstatus, ticketId: ' + ticketId);
                var restPath = '/moduleapi/intyg/signeringsstatus/' + ticketId;
                $http.get(restPath).
                    success(function(data) {
                        onSuccess(data);
                    }).
                    error(function(error) {
                        _handleError(onError, error);
                    });
            }

            function _signeraUtkast(intygId, onSuccess, onError) {
                $log.debug('_signeraUtkast, intygId:' + intygId);
                var restPath = '/moduleapi/intyg/signera/server/' + intygId;
                $http.post(restPath).
                    success(function(data) {
                        onSuccess(data);
                    }).
                    error(function(error) {
                        _handleError(onError, error);
                    });
            }

            function _signeraUtkastWithSignatur(ticketId, signatur, onSuccess, onError) {
                $log.debug('_signeraUtkastWithSignatur, ticketId: ' + ticketId);
                var restPath = '/moduleapi/intyg/signera/klient/' + ticketId;
                $http.post(restPath, { signatur: signatur }).
                    success(function(data) {
                        onSuccess(data);
                    }).
                    error(function(error) {
                        _handleError(onError, error);
                    });
            }

            // Return public API for the service
            return {
                getCertificate: _getCertificate,
                getDraft: _getDraft,
                saveDraft: _saveDraft,
                discardDraft: _discardDraft,
                getSigneringshash: _getSigneringshash,
                getSigneringsstatus: _getSigneringsstatus,
                signeraUtkast: _signeraUtkast,
                signeraUtkastWithSignatur: _signeraUtkastWithSignatur
            };
        }
    ]);

    return moduleName;
});
