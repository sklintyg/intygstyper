define(['angular'], function(angular) {
    'use strict';

    var moduleName = 'mi.CertificateService';

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
                var restPath = '/moduleapi/certificate/' + id;
                $http.get(restPath).success(function(data) {
                    $log.debug('_getCertificate data:' + data);
                    onSuccess(data);
                }).error(function(data, status) {
                    $log.error('error ' + status);
                    onError(data);
                });
            }


            // Return public API for the service
            return {
                getCertificate: _getCertificate
            };
        }
    ]);

    return moduleName;
});
