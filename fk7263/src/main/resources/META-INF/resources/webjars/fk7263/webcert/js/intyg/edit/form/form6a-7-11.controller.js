angular.module('fk7263').controller('fk7263.EditCert.Form6a711Ctrl',
    ['$scope', '$log', 'fk7263.EditCertCtrl.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            var model = viewState.intygModel;
            $scope.model = model;
            $scope.viewState = viewState;

            $scope.radioGroups = {
                ressattTillArbete : 'NEJ',
                rehab : 'NEJ'
            };

            var travelStates = {NEJ:'NEJ', JA:'JA', NULL:''};

            var rehabStates = {NEJ:'NEJ', JA:'JA', GAREJ:'GAREJ', NULL:''};

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
                        updateFormFromModel();
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
                setRehabGroupFromModel();
                setTravelGroupFromModel();
            }

            function setRehabGroupFromModel(){
                if (model.rehabilitering !== undefined) {
                    switch (model.rehabilitering) {
                    case 'rehabiliteringAktuell':
                        $scope.radioGroups.rehab = rehabStates.JA;
                        break;
                    case 'rehabiliteringEjAktuell':
                        $scope.radioGroups.rehab = rehabStates.NEJ;
                        break;
                    case 'rehabiliteringGarInteAttBedoma':
                        $scope.radioGroups.rehab = rehabStates.GAREJ;
                        break;
                    default :
                        $scope.radioGroups.rehab = rehabStates.NULL;
                    }
                }
            }

            function setTravelGroupFromModel(){
                if($scope.model.ressattTillArbeteAktuellt){
                    $scope.radioGroups.ressattTillArbete = travelStates.JA;
                } else if($scope.model.ressattTillArbeteEjAktuellt){
                    $scope.radioGroups.ressattTillArbete = travelStates.NEJ;
                }
            }

            $scope.onRehabChange = function(){
                switch ($scope.radioGroups.rehab) {
                case rehabStates.JA:
                    model.rehabilitering = 'rehabiliteringAktuell';
                    break;
                case rehabStates.NEJ:
                    model.rehabilitering = 'rehabiliteringEjAktuell';
                    break;
                case rehabStates.GAREJ:
                    model.rehabilitering = 'rehabiliteringGarInteAttBedoma';
                    break;
                default :
                    model.rehabilitering = undefined;
                }
            };

            $scope.onTravelChange = function(){
                $scope.model.ressattTillArbeteAktuellt = false;
                $scope.model.ressattTillArbeteEjAktuellt = false;
                if ($scope.radioGroups.ressattTillArbete === travelStates.JA) {
                    $scope.model.ressattTillArbeteAktuellt = true;
                    $scope.model.ressattTillArbeteEjAktuellt = false;
                } else if($scope.radioGroups.ressattTillArbete === travelStates.NEJ){
                    $scope.model.ressattTillArbeteAktuellt = false;
                    $scope.model.ressattTillArbeteEjAktuellt = true;
                } else {
                    $scope.model.rekommendationKontaktArbetsformedlingen = false;
                    $scope.model.rekommendationKontaktForetagshalsovarden = false;
                    $scope.model.rekommendationOvrigtCheck = false;
                    $scope.model.rekommendationOvrigt = undefined;
                }
            };

            $scope.onOvrigtChange = function(){
                if(!model.rekommendationOvrigtCheck){
                    model.updateToAttic(model.properties.form6a)
                    model.rekommendationOvrigt = undefined;
                } else {
                    model.updateToAttic(model.properties.form6a)
                    model.rekommendationOvrigtCheck = true;
                }
            };

        }]);