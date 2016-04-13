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
                $log.debug('called fmb:');
                $log.debug(response);

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

