angular.module('sjukpenning').controller('sjukpenning.EditCert.Form15Ctrl',
    ['$scope', '$log', 'sjukpenning.EditCertCtrl.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.model = viewState.intygModel;
            $scope.viewState = viewState;

            $scope.$watch('viewState.common.doneLoading', function ( newVal, oldVal ) {
                if(newVal && (newVal === oldVal) ){
                    return;
                }

                var doesntHaveVardenhet = !$scope.model.grundData || !$scope.model.grundData.skapadAv ||
                    !$scope.model.grundData.skapadAv.vardenhet;

                    // check if all info is available from HSA. If not, display the info message that someone needs to update it
                $scope.viewState.common.hsaInfoMissing =
                    doesntHaveVardenhet || $scope.model.grundData.skapadAv.vardenhet.isMissingInfo();

            });
        }]);