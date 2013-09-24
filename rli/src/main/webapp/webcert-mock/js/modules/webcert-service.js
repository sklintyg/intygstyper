'use strict';
/**
 * Mock service that gives modules access to the webcert module services.
 */
angular.module('services.webcertService', []);
angular.module('services.webcertService').factory('webcertService', [ '$http', '$rootScope','$log', function(http, rootScope, $log) {

    // createDraft
    function _createDraft(type, callback) {
        http.post(rootScope.MODULE_CONFIG.MODULE_CONTEXT_PATH + '/api/draft', type, { headers: {'Content-Type': 'text/plain'} }).success(function(data) {
            $log.debug("created new draft of type " + type);
            callback(data);
        }).error(function(data, status, headers, config) {
            $log.debug("error " + status);
            callback(null);
        });
    }

    // getDraft
    function _getDraft(id, callback) {
        http.get(rootScope.MODULE_CONFIG.MODULE_CONTEXT_PATH + '/api/draft/' + id).success(function(data) {
            $log.debug("got draft data for id " + id);
            callback(data);
        }).error(function(data, status, headers, config) {
            $log.debug("error " + status);
            callback(null);
        });
    }
    
    function _getDraftList(callback) {
    	http.get(rootScope.MODULE_CONFIG.MODULE_CONTEXT_PATH + '/api/draft/list').success(function(data) {
            $log.debug("got draft list data ");
            callback(data);
        }).error(function(data, status, headers, config) {
            $log.debug("error " + status);
            callback(null);
        });
    }

    // saveDraft
    function _saveDraft(id, model, callback) {
        http.put(rootScope.MODULE_CONFIG.MODULE_CONTEXT_PATH + '/api/draft/' + id, model).success(function(data) {
            $log.debug("saved draft data for id " + id);
            callback(data);
        }).error(function(data, status, headers, config) {
            $log.debug("error " + status);
            callback(null);
        });
    }

    // deleteDraft
    function _deleteDraft(id, callback) {
        http.delete(rootScope.MODULE_CONFIG.MODULE_CONTEXT_PATH + '/api/draft/' + id).success(function(data) {
            $log.debug("deleted draft data for id " + id);
            callback(data);
        }).error(function(data, status, headers, config) {
            $log.debug("error " + status);
            callback(null);
        });
    }

    // Return public API for our service
    return {
        createDraft : _createDraft,
        getDraft : _getDraft,
        getDraftList : _getDraftList,
        saveDraft : _saveDraft,
        deleteDraft : _deleteDraft
    }
} ]);