define([
], function () {
    'use strict';

    return ['$scope', '$filter', '$location', 'fk7263.certificateService', '$http', '$routeParams',
        function ($scope, $filter, $location, certificateService, http, $routeParams) {    
	        // init state
	        $scope.widgetState = {
	            doneLoading : false,
	            activeErrorMessageKey : null
	        };
	        $scope.certProperties = {
	            sentToFK : undefined
	        };
          
	        $scope.cert = {};
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

            certificateService.getCertificate($routeParams.certificateId, function (result){
            	$scope.widgetState.doneLoading = true;
                if (result != null && result != '') {
                    $scope.cert = result.contents;

                    $scope.certProperties.sentToFK = isSentToFK(result.metaData.statuses);
                    $scope.certProperties.isRevoked = isRevoked(result.metaData.statuses);
                } else {
                  $scope.widgetState.activeErrorMessageKey = 'error.could_not_load_cert';
                }
            }, function (error) {
                $scope.widgetState.doneLoading = true;
                $scope.widgetState.activeErrorMessageKey = 'error.data_not_found';
            	console.log("Got error while loading cert");
            	console.log(error.data);
            });
        }];
});
