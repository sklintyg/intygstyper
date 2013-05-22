'use strict';

RLIApp.factory('certService', [ '$http', function($http) {

    var _selectedCertificate = null;

    function _getCertificate(callback,id) {
        $http.get('/moduleapi/certificate/' + id).success(function(data) {
            console.log("got certificate");
            callback(data);
        }).error(function(data, status, headers, config) {
            console.log("error " + status);
        });
    }

    // Return public API for our service
    return {
        getCertificate : _getCertificate
    }
} ]);
