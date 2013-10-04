'use strict';

/* 
 * WC View Certificate Services 
 */
angular.module('wc.fk7263.services', []);
angular.module('wc.fk7263.services').factory('viewCertificateService', [ '$http', '$log', function($http, $log) {

    
    /* 
     * Load certificate details
     */

    function _getCertificate(id, callback) {
        $log.debug("_getCertificates id:" + id);
        var restPath = '/moduleapi/intyg/' + id;
        $http.get(restPath).success(function(data) {
            $log.debug("_getCertificates data:" + data);
            callback(data);
        }).error(function(data, status, headers, config) {
            $log.error("error " + status);
            //Let calling code handle the error of no data response 
            callback(null);
        });
    }
    
   
    
    // Return public API for the service
    return {
        getCertificate : _getCertificate
    }
} ]);


