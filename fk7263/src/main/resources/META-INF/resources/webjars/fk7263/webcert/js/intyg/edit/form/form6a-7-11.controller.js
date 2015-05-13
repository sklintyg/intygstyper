angular.module('fk7263').controller('fk7263.EditCert.Form6a711Ctrl',
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
                    // Remove defaults not applicable when smittskydd is active
                    if (newVal === true) {
                        $scope.radioGroups.ressattTillArbete = travelStates.NULL;
                        $scope.radioGroups.rehab = rehabStates.NULL;
                        model.atticUpdateForm6a711();
                        model.clearForm6a711();
                    } else {
                        model.atticRestoreForm6a711();
                    }

                }
            });

            // once we've doneLoading we can set the radion buttons to the model state.
            $scope.$watch('viewState.common.doneLoading', function(newVal) {
                if(newVal) {
                    //updateFormFromModel();
                }
            });

            $scope.onOvrigtChange = function(){
                if(!model.rekommendationOvrigtCheck){
                    model.updateToAttic(model.properties.form6a);
                    model.rekommendationOvrigt = undefined;
                } else {
                    model.updateToAttic(model.properties.form6a);
                    model.rekommendationOvrigtCheck = true;
                }
            };

        }]);