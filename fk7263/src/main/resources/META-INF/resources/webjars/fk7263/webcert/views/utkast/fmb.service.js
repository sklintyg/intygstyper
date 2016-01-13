angular.module('fk7263').factory('fk7263.fmbService', ['$http' , '$q', '$log',
    function($http, $q, $log) {
        'use strict';

        /*
         * get diagnosis by code
         */
        function _getFMBHelpTextsByCode(diagnosisCode) {
            var deferred = $q.defer(),
                restPath = '/api/fmb/' + diagnosisCode.toUpperCase();

            $http.get(restPath).success(function(response) {
                deferred.resolve(response);
            }).error(function(response, status) {
                $log.error('error ' + status);
                deferred.reject(status);
            });

            return deferred.promise;
        }


        // Return public API for the service
        return {
            getFMBHelpTextsByCode: _getFMBHelpTextsByCode
        };
    }]);

