angular.module('sjukpenning').controller('sjukpenning.EditCert.Form7Ctrl',
    ['$scope', '$log', 'sjukpenning.EditCertCtrl.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            var model = viewState.intygModel;
            $scope.model = model;
            $scope.viewState = viewState;

            $scope.$watch('viewState.avstangningSmittskyddValue', function(newVal) {
                // only do this once the page is loaded and changes come from the gui!
                if(viewState.common.doneLoading) {
                    // Remove defaults not applicable when smittskydd is active
                    if (newVal === true) {
                        // 6b
                        model.updateToAttic(model.properties.form7);
                        model.clear(model.properties.form7);
                    } else {
                        if(model.isInAttic(model.properties.form7)){
                            model.restoreFromAttic(model.properties.form7);
                        }
                    }
                }
            });
        }]);