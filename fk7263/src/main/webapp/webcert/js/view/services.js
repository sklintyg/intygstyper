'use strict';

/* 
 * WC View Certificate Services 
 */
angular.module('services.fk7263.wc', []);
angular.module('services.fk7263.wc').factory('viewCertificateService', [ '$http', '$log', function($http, $log) {

    
    /* 
     * Load certificate details
     */

    function _getCertificate(id, callback) {
        $log.debug("_getCertificates id:" + id);
        //var restPath = '/api/certificates/' + dataType;
        var restPath = '/m/fk7263/webcert/views/utlatande_internal.json';// + id;
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


//************************maybe rector to separate FragaSvar module*********************************************/
angular.module('services.fk7263.wc').factory('fragaSvarService', [ '$http', '$log', function($http, $log) {

    
    /* 
     * Load questions and answers data for a certificate
     */

    function _getQAForCertificate(id, callback) {
        $log.debug("_getQA");
        var restPath = '/moduleapi/intyg/' + id + "/fragasvar";
        $http.get(restPath).success(function(data) {
            $log.debug("got data:" + data);
            callback(data);
        }).error(function(data, status, headers, config) {
            $log.error("error " + status);
            //Let calling code handle the error of no data response 
            callback(null);
        });
    }
    
    // Return public API for the service
    return {
        getQAForCertificate : _getQAForCertificate
    }
} ]);
