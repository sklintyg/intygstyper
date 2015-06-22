angular.module('fk7263').controller('fk7263.EditCert.Form8aCtrl',
    ['$scope', '$log', 'fk7263.EditCertCtrl.ViewStateService',
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
                    if (newVal === true) {
                        model.updateToAttic(model.properties.form8a);
                        model.clear(model.properties.form8a);
                    } else if(model.isInAttic(model.properties.form8a)){
                        model.restoreFromAttic(model.properties.form8a);
                    }
                }
            });

        }]);