angular.module('fk7263').controller('fk7263.EditCert.Form5Ctrl',
    ['$scope', '$log', 'fk7263.Domain.IntygModel', 'fk7263.EditCertCtrl.ViewStateService',
        function($scope, $log, model, viewState) {
            'use strict';
            $scope.model = model;
            $scope.viewState = viewState;

            $scope.$watch('viewState.avstangningSmittskyddValue', function(newVal, oldVal) {
                if(newVal === oldVal){
                    return;
                }
                // only do this once the page is loaded and changes come from the gui!
                if(viewState.common.doneLoading) {
                    // Remove defaults not applicable when smittskydd is active
                    if (newVal === true) {
                        // 5. funktionsnedsättning
                        model.atticUpdateForm5();
                        model.clearForm5();
                    } else if(model.atticHasForm5()){
                        model.atticRestoreForm5();
                    }
                }
            });
        }]);