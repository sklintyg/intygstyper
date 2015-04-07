angular.module('fk7263').controller('fk7263.EditCert.Form15Ctrl',
    ['$scope', '$log', 'fk7263.Domain.IntygModel', 'fk7263.EditCertCtrl.ViewStateService',
        function($scope, $log, model, viewState) {
            'use strict';
            $scope.model = model;
            $scope.viewState = viewState;

            $scope.$watch('viewState.common.doneLoading', function ( newVal, oldVal ) {
                if(newVal && (newVal === oldVal) ){
                    return;
                }

                var doesntHaveVardenhet = !$scope.model.grundData || !$scope.model.grundData.skapadAv ||
                    !$scope.model.grundData.skapadAv.vardenhet;

                    // check if all info is available from HSA. If not, display the info message that someone needs to update it
                    if (doesntHaveVardenhet || $scope.model.grundData.skapadAv.vardenhet.isMissingInfo()) {
                        $scope.viewState.common.hsaInfoMissing = true;
                    } else {
                        $scope.viewState.common.hsaInfoMissing = false;
                    }

            });
        }]);