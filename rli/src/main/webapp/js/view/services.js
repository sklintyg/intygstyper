'use strict';

RLIApp.factory('certService', [ '$http', '$rootScope', function(http, rootScope) {

    var _selectedCertificate = null;

    function _getCertificate(id, callback) {
        http.get(rootScope.MODULE_CONFIG.MI_COMMON_API_CONTEXT_PATH + id).success(function(data) {
            console.log("got certificate data for id " + id);
            callback(data);
        }).error(function(data, status, headers, config) {
            console.log("error " + status);
            callback(null);
        });
    }

    // Return public API for our service
    return {
        getCertificate : _getCertificate
    }
} ]);
