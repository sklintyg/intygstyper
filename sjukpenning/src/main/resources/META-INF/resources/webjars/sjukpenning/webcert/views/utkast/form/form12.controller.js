angular.module('sjukpenning').controller('sjukpenning.EditCert.Form12Ctrl',
    ['$scope', '$log', 'sjukpenning.EditCertCtrl.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            var model;
            model = viewState.intygModel;
            $scope.model = model;
            $scope.viewState = viewState;
        }]);