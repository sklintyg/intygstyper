angular.module('fk7263').controller('fk7263.EditCert.Form4Ctrl',
    ['$scope', '$log', 'fk7263.EditCertCtrl.ViewStateService', 'fk7263.fmb.ViewStateService',
        function($scope, $log, viewState, fmbViewState) {
            'use strict';
            var model = viewState.intygModel;
            $scope.model = model;
            $scope.viewState = viewState;

            $scope.fmb = fmbViewState.state;

            $scope.$watch('viewState.avstangningSmittskyddValue', function(newVal, oldVal) {
                if(newVal === oldVal){
                    return;
                }
                // only do this once the page is loaded and changes come from the gui!
                if(viewState.common.doneLoading) {
                    // Remove defaults not applicable when smittskydd is active
                    if (newVal === true) {
                        model.updateToAttic(model.properties.form4);
                        model.clear(model.properties.form4);
                    } else {
                        model.restoreFromAttic(model.properties.form4);
                    }
                }
            });
        }]);
