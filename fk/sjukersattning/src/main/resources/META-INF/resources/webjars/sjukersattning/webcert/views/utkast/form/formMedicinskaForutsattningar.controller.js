angular.module('sjukersattning').controller('sjukersattning.EditCert.FormMedicinskaForutsattningarCtrl',
    ['$scope', '$log', 'sjukersattning.EditCertCtrl.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            var model = viewState.intygModel;
            $scope.model = model;

            $scope.viewState = viewState;

}]);