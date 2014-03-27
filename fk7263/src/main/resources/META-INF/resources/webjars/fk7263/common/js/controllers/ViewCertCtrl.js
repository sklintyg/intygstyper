define([
], function () {
    'use strict';

    return ['$scope', '$filter', '$location', 'fk7263.certificateService', '$http', '$routeParams',
        function ($scope, $filter, $location, certService, http, $routeParams) {

	        // init state
	        $scope.widgetState = {
	            doneLoading : false,
	            activeErrorMessageKey : null
	        };
	        $scope.certProperties = {
	            sentToFK : false
	        };
          
	        $scope.cert = {};
            $scope.doneLoading = false;
            $scope.shouldBeOpen = false;
            $scope.cert.filledAlways = true;

            var isSentToFK = function (statusArr) {
                if (statusArr) {
                    for (var i = 0; i < statusArr.length; i++) {
                        if (statusArr[i].target === 'FK' && statusArr[i].type === 'SENT') {
                            return true;
                        }
                    }
                }
                return false;
            };
            
            var isRevoked = function (statusArr) {
                if (statusArr) {
                    for (var i = 0; i < statusArr.length; i++) {
                        if (statusArr[i].type === 'CANCELLED') {
                            return true;
                        }
                    }
                }
                return false;
            };
            
            $scope.open = function () {
                $scope.shouldBeOpen = true;
            };

            $scope.close = function () {
                $scope.closeMsg = 'I was closed at: ' + new Date();
                $scope.shouldBeOpen = false;
            };

            $scope.opts = {
                backdropFade : true,
                dialogFade : true
            };

            console.log("Loading certificate " + $routeParams.certificateId);

            certService.getCertificate($routeParams.certificateId, function (result) {
            	$scope.doneLoading = true;
                if (result != null && result != '') {
                    $scope.cert = result.certificateContent;

                    $scope.certProperties.sentToFK = isSentToFK(result.certificateContentMeta.statuses);
                    $scope.certProperties.isRevoked = isRevoked(result.certificateContentMeta.statuses);
                } else {
                  $scope.widgetState.activeErrorMessageKey = 'error.could_not_load_cert';
                }
            });
        }];
});
