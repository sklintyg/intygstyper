angular.module('fk7263').controller('fk7263.EditCert.Form12Ctrl',
    ['$scope', '$log', 'fk7263.Domain.IntygModel', 'fk7263.EditCertCtrl.ViewStateService',
        function($scope, $log, model, viewState) {
            'use strict';
            $scope.model = model;
            $scope.viewState = viewState;
        }]);