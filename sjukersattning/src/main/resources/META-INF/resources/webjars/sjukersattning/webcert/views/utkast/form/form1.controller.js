angular.module('sjukersattning').controller('sjukersattning.EditCert.Form1Ctrl',
    ['$scope', '$log', 'sjukersattning.EditCertCtrl.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            var model = viewState.intygModel;
            $scope.model = model;
            $scope.viewState = viewState;

            $scope.onSmittskyddChange = function(){
                viewState.avstangningSmittskyddValue = model.avstangningSmittskydd;
            };

        }]);
