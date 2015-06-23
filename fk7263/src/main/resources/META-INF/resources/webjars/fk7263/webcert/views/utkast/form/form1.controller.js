angular.module('fk7263').controller('fk7263.EditCert.Form1Ctrl',
    ['$scope', '$log', 'fk7263.EditCertCtrl.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            var model = viewState.intygModel;
            $scope.model = model;
            $scope.viewState = viewState;

            $scope.onSmittskyddChange = function(){
                viewState.avstangningSmittskyddValue = model.avstangningSmittskydd;
            };

        }]);