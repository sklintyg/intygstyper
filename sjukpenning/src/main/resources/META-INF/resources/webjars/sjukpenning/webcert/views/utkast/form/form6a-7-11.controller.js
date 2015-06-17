angular.module('sjukpenning').controller('sjukpenning.EditCert.Form6a711Ctrl',
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
                    updateFormFromModel();
                }
            });

            function updateFormFromModel(){
                if(!model.rehabilitering){
                    model.rehabilitering = 'rehabiliteringEjAktuell';
                }

                if(!model.ressattTillArbeteAktuellt && !model.ressattTillArbeteEjAktuellt){
                    model.ressattTillArbeteAktuellt = false;
                    model.ressattTillArbeteEjAktuellt = true;
                }
            }

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