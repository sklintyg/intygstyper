angular.module('sjukpenning').controller('sjukpenning.EditCert.Form9Ctrl',
    ['$log', '$scope', 'sjukpenning.EditCertCtrl.ViewStateService',
        function($log, $scope, viewState) {
            'use strict';
            var model;
            model = viewState.intygModel;
            $scope.model = model;
            $scope.viewState = viewState;

        }]);