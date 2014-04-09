define([], function() {
	'use strict';

	return [ '$scope','$filter', '$location', 'fk7263.certificateService', '$http', '$routeParams', '$log',
			function($scope, $filter, $location, certificateService, http, $routeParams, $log) {
				$scope.cert = {};

				$scope.doneLoading = false;

				$scope.send = function() {
					$location.path("/summary");
				};

				$scope.visibleStatuses = [ 'SENT' ];

				$scope.filterStatuses = function(statuses) {
					var result = [];
					if (!angular.isObject(statuses)) {
						return result;
					}
					for ( var i = 0; i < statuses.length; i++) {
						if ($scope.userVisibleStatusFilter(statuses[i])) {
							result.push(statuses[i]);
						}
					}
					return result;
				}

				$scope.userVisibleStatusFilter = function(status) {
					for ( var i = 0; i < $scope.visibleStatuses.length; i++) {
						if (status.type == $scope.visibleStatuses[i]) {
							return true;
						}
					}
					return false;
				}

				$scope.showStatusHistory = function() {
					$location.path("/statushistory");
				}

				$scope.backToViewCertificate = function() {
					$location.path("/view");
				}

				// expose calculated static link for pdf download
				$scope.downloadAsPdfLink = "/moduleapi/certificate/" + $routeParams.certificateId + "/pdf";

				certificateService.getCertificate($routeParams.certificateId, function(result) {
					$scope.doneLoading = true;
					if (result != null) {
						result.filteredStatuses = $scope.filterStatuses(result.status);
						// added calculated img src for certificate, beacuse for
						// some reason ng-src with "/img/{{cert.typ }}.png" would
						// evaluate first to "img/.png" before correct value = 404
						result.typ_image = "/img/fk7263.png";
						$scope.cert = result;
					} else {
						// show error view
						$location.path("/visafel/certnotfound");
					}
				}, function(error) {
					$log.debug("got error");
				});

				$scope.pagefocus = true;
			} ];
});
