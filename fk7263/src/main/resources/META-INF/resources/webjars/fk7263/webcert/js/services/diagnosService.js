angular.module('fk7263').factory('fk7263.diagnosService',
    function($http, $log, $window) {
        'use strict';

        /*
         * get diagnosis by code
         */
        function _getByCode(code, onSuccess, onError) {
            $window.doneLoading = false;
            $log.debug('_searchByCode: codeFragment:' + code);
            var restPath = '/moduleapi/diagnos/kod';
            $http.post(restPath, code.toUpperCase()).success(function(response) {
                if (response && response.resultat === 'OK') {
                    onSuccess(response);
                }
                else {
                    onError(response);
                }
                $window.doneLoading = true;
            }).error(function(response, status) {
                $log.error('error ' + status);
                onError(response);
                $window.doneLoading = true;
            });
        }

        /*
         * Search diagnosis by code
         */
        function _searchByCode(codeFragment) {
            $log.debug('_searchByCode: codeFragment:' + codeFragment);
            var restPath = '/moduleapi/diagnos/kod/sok';
            var data = {
                codeFragment: codeFragment.toUpperCase(),
                nbrOfResults: 10
            };
            return $http.post(restPath, data);
        }

        /*
         * Search diagnosis by description
         */
        function _searchByDescription(searchString) {
            $log.debug('_searchByDescription: ' + searchString);
            var restPath = '/moduleapi/diagnos/beskrivning/sok';
            var data = {
                descriptionSearchString: searchString,
                nbrOfResults: 10
            };
            return $http.post(restPath, data);
        }

        // Return public API for the service
        return {
            getByCode: _getByCode,
            searchByCode: _searchByCode,
            searchByDescription: _searchByDescription
        };
    });

