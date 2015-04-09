angular.module('fk7263').controller('fk7263.EditCert.Form6bCtrl',
    ['$scope', '$log', 'fk7263.Domain.IntygModel', 'fk7263.EditCertCtrl.ViewStateService',
        function($scope, $log, model, viewState) {
            'use strict';
            $scope.model = model;
            $scope.viewState = viewState;

            $scope.$watch('viewState.avstangningSmittskyddValue', function(newVal) {
                // only do this once the page is loaded and changes come from the gui!
                if(viewState.common.doneLoading) {
                    // Remove defaults not applicable when smittskydd is active
                    if (newVal === true) {
                        // 6b
                        model.atticUpdateForm6b();
                        model.clearForm6b();
                    } else if(model.atticHasForm6b()){
                        model.atticRestoreForm6b();
                    }
                }
            });
        }]);