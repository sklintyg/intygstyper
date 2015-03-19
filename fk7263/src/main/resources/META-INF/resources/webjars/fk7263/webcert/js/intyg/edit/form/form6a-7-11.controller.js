angular.module('fk7263').controller('fk7263.EditCert.Form6a711Ctrl',
    ['$log', 'fk7263.Domain.IntygModel', '$scope', 'fk7263.EditCertCtrl.ViewStateService',
        function($log, model, $scope, viewState) {
            'use strict';
            $scope.model = model;
            $scope.viewState = viewState;

            $scope.radioGroups = {
                ressattTillArbete : 'NEJ',
                rehab : 'NEJ'
            };

            var travelStates = {NEJ:'NEJ', JA:'JA', NULL:''};

            var rehabStates = {NEJ:'NEJ', JA:'JA', GAREJ:'GAREJ', NULL:''};

            function setRehabGroup(){
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

            function setTravelGroup(){
                if($scope.model.ressattTillArbeteAktuellt){
                    $scope.radioGroups.ressattTillArbete = travelStates.JA;
                } else if($scope.model.ressattTillArbeteEjAktuellt){
                    $scope.radioGroups.ressattTillArbete = travelStates.NEJ;
                }
            }

            setRehabGroup();
            setTravelGroup();

            $scope.$watch('viewState.avstangningSmittskyddValue', function(newVal) {
                // only do this once the page is loaded and changes come from the gui!
                if(viewState.common.viewState.doneLoading) {
                    // Remove defaults not applicable when smittskydd is active
                    if (newVal === true) {
                        $scope.radioGroups.ressattTillArbete = travelStates.NULL;
                        $scope.radioGroups.rehab = rehabStates.NULL;
                    } else {
                        if (!$scope.radioGroups.ressattTillArbete || $scope.radioGroups.ressattTillArbete.length == 0) {
                            $scope.radioGroups.ressattTillArbete = travelStates.NEJ;
                        }
                        if (!$scope.radioGroups.rehab || $scope.radioGroups.rehab.length == 0) {
                            $scope.radioGroups.rehab = rehabStates.NEJ;
                        }
                    }
                    // set the model values
                    $scope.onRehabChange();
                    $scope.onTravelChange();
                }
            });


            $scope.onRehabChange = function(){
                switch ($scope.radioGroups.rehab) {
                case 'JA':
                    model.rehabilitering = 'rehabiliteringAktuell';
                    break;
                case 'NEJ':
                    model.rehabilitering = 'rehabiliteringEjAktuell';
                    break;
                case 'GAREJ':
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
            }

        }]);