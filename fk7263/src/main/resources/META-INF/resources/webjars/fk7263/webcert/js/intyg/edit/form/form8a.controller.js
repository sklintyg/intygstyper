angular.module('fk7263').controller('fk7263.EditCert.Form8aCtrl',
    ['$scope', '$log', 'fk7263.Domain.IntygModel', 'fk7263.EditCertCtrl.ViewStateService',
        function($scope, $log, model, viewState) {
            'use strict';
            $scope.model = model;
            $scope.viewState = viewState;

            $scope.$watch('viewState.avstangningSmittskyddValue', function(newVal) {
                // only do this once the page is loaded and changes come from the gui!
                if(viewState.common.doneLoading) {
                    if (newVal === true) {
                        // 2. Diagnos
                        model.atticUpdateForm8a();
                        model.clearForm8a();
                    } else if(model.atticHasForm8a()){
                        model.atticRestoreForm8a();
                    }
                }
            });

        }]);