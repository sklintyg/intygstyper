angular.module('sjukersattning').controller('sjukersattning.EditCert.Form11Ctrl',
    ['$scope', '$log', 'sjukersattning.EditCertCtrl.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            var model;
            model = viewState.intygModel;
            $scope.model = model;
            $scope.viewState = viewState;

        }]);