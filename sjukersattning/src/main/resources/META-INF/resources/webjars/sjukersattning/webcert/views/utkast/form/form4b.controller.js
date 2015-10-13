angular.module('sjukersattning').controller('sjukersattning.EditCert.Form4bCtrl',
    ['$scope', '$log', 'sjukersattning.EditCertCtrl.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            var model = viewState.intygModel;
            $scope.model = model;

            $scope.viewState = viewState;

            $scope.viewModel = {
                nyBedomningDiagnos: '',
                diagnosticering: ''
            };

            $scope.onChangeNyBedomning = function(){
                $scope.model.nyBedomningDiagnos = $scope.viewModel.nyBedomningDiagnos;
                $log.info("$scope.viewModel.nyBedomningDiagnos" + $scope.viewModel.nyBedomningDiagnos);
            };

}]);