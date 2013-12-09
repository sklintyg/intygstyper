/*
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
'use strict';
/**
 * Mock service that gives modules access to the webcert module services.
 */
angular.module('services.webcertService', []);
angular.module('services.webcertService').factory('webcertService', [ '$http', '$rootScope','$log', function(http, rootScope, $log) {

    // getDraftList
    function _getDraftList(callback) {
        http.get(rootScope.MODULE_CONFIG.MODULE_CONTEXT_PATH + '/api/draft').success(function(data) {
            $log.debug("got draft list data");
            callback(data);
        }).error(function(data, status, headers, config) {
            $log.debug("error " + status);
            callback(null);
        });
    }

    // createDraft
    function _createDraft(type, callback) {
        http.post(rootScope.MODULE_CONFIG.MODULE_CONTEXT_PATH + '/api/draft', type).success(function(data) {
            $log.debug("created new draft of type " + type.certificateType);
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
        http.delete(rootScope.MODULE_CONFIG.MODULE_CONTEXT_PATH + '/api/draft/' + id ).success(function(data) {
            $log.debug("deleted draft data for id " + id);
            callback(data);
        }).error(function(data, status, headers, config) {
            $log.debug("error " + status);
            callback(null);
        });
    }

    // Return public API for our service
    return {
        getDraftList : _getDraftList,
        createDraft : _createDraft,
        getDraft : _getDraft,
        saveDraft : _saveDraft,
        deleteDraft : _deleteDraft
    }
} ]);