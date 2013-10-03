/**
 * Fragasvar Module - services and controllers related to FragaSvar functionality in webcert.
 */
angular.module('wc.fragasvarmodule', []);
angular.module('wc.fragasvarmodule').factory('fragaSvarService', [ '$http', '$log', function($http, $log) {

    
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


/**
 * QACtrl - Controller for logic related to viewing a Fraga/Svar for a
 * certificate
 * 
 */
angular.module('wc.fragasvarmodule').controller('QACtrl', [ '$scope', '$log', '$timeout', 'fragaSvarService', function CreateCertCtrl($scope, $log, $timeout, fragaSvarService) {

    // init state
    $scope.qaList = {};
    $scope.widgetState = {
        doneLoading : false,
        hasError : false
    }
    // Request loading of QA's for this certificate
    $timeout(function() { // wrap in timeout to simulate latency - remove soon
        fragaSvarService.getQAForCertificate($scope.MODULE_CONFIG.CERT_ID_PARAMETER, function(result) {
            $log.debug("Got getQAForCertificate data:" + result);
            $scope.widgetState.doneLoading = true;
            if (result != null) {
                $scope.qaList = result;
            } else {
                // show error view
                $scope.widgetState.hasError = true;
            }
        });
    }, 500);

} ]);
