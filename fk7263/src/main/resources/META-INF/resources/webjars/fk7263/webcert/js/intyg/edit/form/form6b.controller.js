angular.module('fk7263').controller('fk7263.EditCert.Form6bCtrl',
    ['$log', 'fk7263.Domain.IntygModel', '$scope', 'fk7263.EditCertCtrl.ViewStateService',
        function($log, model, $scope, viewState) {
            'use strict';
            $scope.model = model;
            $scope.viewState = viewState;

            $scope.inputLimits = {
                atgardInomSjukvarden: 66,
                annanAtgard: 66 }
        }]);