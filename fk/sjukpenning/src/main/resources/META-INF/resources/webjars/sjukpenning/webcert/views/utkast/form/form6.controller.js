angular.module('sjukpenning').controller('sjukpenning.EditCert.Form6Ctrl',
    ['$scope', '$log', 'sjukpenning.EditCertCtrl.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            var model = viewState.intygModel;
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
                        model.updateToAttic(model.properties.form6);
                        model.clear(model.properties.form6);
                    } else if(model.isInAttic(model.properties.form6)){
                        model.restoreFromAttic(model.properties.form6);
                    }
                }
            });
        }]);