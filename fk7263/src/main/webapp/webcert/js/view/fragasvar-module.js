/**
 * Fragasvar Module - services and controllers related to FragaSvar
 * functionality in webcert.
 */
angular.module('wc.fragasvarmodule', []);
angular.module('wc.fragasvarmodule').factory('fragaSvarService', [ '$http', '$log', function($http, $log) {

    /*
     * Load questions and answers data for a certificate
     */
    function _getQAForCertificate(id, callback) {
        $log.debug("_getQAForCertificate");
        var restPath = '/moduleapi/fragasvar/' + id;
        $http.get(restPath).success(function(data) {
            $log.debug("got data:" + data);
            callback(data);
        }).error(function(data, status, headers, config) {
            $log.error("error " + status);
            // Let calling code handle the error of no data response
            callback(null);
        });
    }

    /*
     * save new answer to a question
     */

    function _saveAnswer(fragaSvar, callback) {
        $log.debug("_saveAnswer");
       
        var restPath = '/moduleapi/fragasvar/' + fragaSvar.internReferens + "/answer";
        $http.put(restPath, fragaSvar.svarsText).success(function(data) {
            $log.debug("got data:" + data);
            callback(data);
        }).error(function(data, status, headers, config) {
            $log.error("error " + status);
            // Let calling code handle the error of no data response
            callback(null);
        });
    }
    // Return public API for the service
    return {
        getQAForCertificate : _getQAForCertificate,
        saveAnswer : _saveAnswer
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

    $scope.openIssuesFilter = function(qa) {
        return qa.status != "CLOSED";
    };

    $scope.closedIssuesFilter = function(qa) {
        return qa.status === "CLOSED";
    };

    $scope.sendAnswer = function sendAnswer(qa) {
        $log.debug("saveAnswer:" + qa);
        var qaActive = qa;
        fragaSvarService.saveAnswer(qa, function(result) {
            $log.debug("Got saveAnswer result:" + result);
            $scope.widgetState.doneLoading = true;
            if (result != null) {
                angular.copy(result,qa);
            } else {
                // show error view
                $scope.widgetState.hasError = true;
            }
        });
        // fake success:
        //qa.status = "ANSWERED";
        //qa.svarSkickadDatum = new Date();
    }

} ]);
