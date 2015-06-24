angular.module('sjukpenning').controller('sjukpenning.EditCert.Form16Ctrl',
    ['$scope', '$log', 'sjukpenning.EditCertCtrl.ViewStateService', 'common.UtilsService',
        'common.DateUtilsService',
        function($scope, $log, viewState, utils, dateUtils) {
            'use strict';
            var model = viewState.intygModel;
            $scope.model = model;

            $scope.viewState = viewState;


        }]);