define(['angular'], function(angular) {
    'use strict';

    return ['$scope', '$filter', '$location', 'CertificateService', '$http', '$routeParams', '$log',
        function($scope, $filter, $location, CertificateService, http, $routeParams, $log) {

        $scope.cert = {};

        $scope.widgetState = {
            doneLoading: false,
            showTemplate: true
        };
        $scope.shouldBeOpen = false;

        $scope.intygAvser = '';
        $scope.intygAvserList = [];

        $scope.$watch('cert.intygAvser.korkortstyp', function() {
            if (!$scope.cert || !$scope.cert.intygAvser || !$scope.cert.intygAvser.korkortstyp) {
                return;
            }
            angular.forEach($scope.cert.intygAvser.korkortstyp, function(key) {
                if (key.selected) {
                    this.push(key);
                }
            }, $scope.intygAvserList );

            for (var i = 0; i < $scope.intygAvserList.length; i++) {
                if (i < $scope.intygAvserList.length-1) {
                    $scope.intygAvser += $scope.intygAvserList[i].type + (', ');
                }
                else {
                    $scope.intygAvser += $scope.intygAvserList[i].type;
                }
            }
        }, true);

        // expose calculated static link for pdf download
        $scope.downloadAsPdfLink = '/moduleapi/certificate/' + $routeParams.certificateId + '/pdf';

        // Decide if helptext related to field 1.a) - 1.c)
        $scope.achelptext = false;

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
        };

        $scope.visibleStatuses = [ 'SENT' ];

        $scope.userVisibleStatusFilter = function(status) {
            for ( var i = 0; i < $scope.visibleStatuses.length; i++) {
                if (status.type === $scope.visibleStatuses[i]) {
                    return true;
                }
            }
            return false;
        };

        CertificateService.getCertificate($routeParams.certificateId, function(result) {
            $scope.widgetState.doneLoading = true;
            if (result !== null) {
                $scope.cert = result.contents;
                $scope.cert.status = $scope.filterStatuses(result.metaData.statuses);
                if ($scope.cert.syn.synfaltsdefekter === true || $scope.cert.syn.nattblindhet === true ||
                    $scope.cert.syn.progressivOgonsjukdom === true) {
                    $scope.achelptext = true;
                }
            } else {
                $scope.widgetState.activeErrorMessageKey = 'error.could_not_load_cert';
            }
        }, function(error) {
            $scope.widgetState.doneLoading = true;
            if (error.errorCode === 'DATA_NOT_FOUND') {
                $scope.widgetState.activeErrorMessageKey = 'error.data_not_found';
            } else {
                $scope.widgetState.activeErrorMessageKey = 'error.could_not_load_cert';
            }
            $log.debug('Got error while loading cert');
            $log.debug(error.message);
        });
    }];
});
