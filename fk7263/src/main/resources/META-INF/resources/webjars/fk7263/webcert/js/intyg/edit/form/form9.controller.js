angular.module('fk7263').controller('fk7263.EditCert.Form9Ctrl',
    ['$log', '$scope', 'fk7263.EditCertCtrl.ViewStateService',
        function($log, $scope, viewState) {
            'use strict';
            var model;
            model = viewState.intygModel;
            $scope.model = model;
            $scope.viewState = viewState;

        }]);