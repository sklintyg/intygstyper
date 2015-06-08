angular.module('ts-diabetes').controller('ts-diabetes.Utkast.Form2Controller',
    ['$scope', '$log',
        'ts-diabetes.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;

            // --- form2
            $scope.$watch('viewState.intygModel.hypoglykemier.teckenNedsattHjarnfunktion',
                function(forekommerTeckenNedsattHjarnfunktion) {
                    if (!forekommerTeckenNedsattHjarnfunktion && viewState.intygModel.hypoglykemier) {
                        viewState.intygModel.updateToAttic('hypoglykemier.saknarFormagaKannaVarningstecken');
                        viewState.intygModel.updateToAttic('hypoglykemier.allvarligForekomst');
                        viewState.intygModel.updateToAttic('hypoglykemier.allvarligForekomstTrafiken');

                        viewState.intygModel.hypoglykemier.saknarFormagaKannaVarningstecken = undefined;
                        viewState.intygModel.hypoglykemier.allvarligForekomst = undefined;
                        viewState.intygModel.hypoglykemier.allvarligForekomstTrafiken = undefined;
                    } else {
                        viewState.intygModel.restoreFromAttic('hypoglykemier.saknarFormagaKannaVarningstecken');
                        viewState.intygModel.restoreFromAttic('hypoglykemier.allvarligForekomst');
                        viewState.intygModel.restoreFromAttic('hypoglykemier.allvarligForekomstTrafiken');
                    }
                }, true);

            $scope.$watch('viewState.intygModel.hypoglykemier.allvarligForekomst', function(haftAllvarligForekomst) {
                if (!haftAllvarligForekomst && viewState.intygModel.hypoglykemier) {
                    viewState.intygModel.updateToAttic('hypoglykemier.allvarligForekomstBeskrivning');
                    viewState.intygModel.hypoglykemier.allvarligForekomstBeskrivning = undefined;
                } else {
                    viewState.intygModel.restoreFromAttic('hypoglykemier.allvarligForekomstBeskrivning');
                }
            }, true);
            $scope.$watch('viewState.intygModel.hypoglykemier.allvarligForekomstTrafiken', function(haftAllvarligForekomstTrafiken) {
                if (!haftAllvarligForekomstTrafiken && viewState.intygModel.hypoglykemier) {
                    viewState.intygModel.updateToAttic('hypoglykemier.allvarligForekomstTrafikBeskrivning');
                    viewState.intygModel.hypoglykemier.allvarligForekomstTrafikBeskrivning = undefined;
                } else {
                    viewState.intygModel.restoreFromAttic('hypoglykemier.allvarligForekomstTrafikBeskrivning');
                }
            }, true);

            // hypoglykemier.allvarligForekomstVakenTidObservationstid
            var addDateParser = function(form){
                if(form && form.allvarligForekomstVakenTidObservationstid){
                    var formElement = form.allvarligForekomstVakenTidObservationstid;
                    formElement.$parsers.push(function(viewValue) {
                        viewState.intygModel.hypoglykemier.allvarligForekomstVakenTidObservationstid = formElement.$viewValue;
                        return viewValue;
                    });
                }
            };
            $scope.$watch('viewState.intygModel.hypoglykemier.allvarligForekomstVakenTid', function(haftAllvarligForekomstVakenTid) {
                if (!haftAllvarligForekomstVakenTid && viewState.intygModel.hypoglykemier) {
                    viewState.intygModel.updateToAttic('hypoglykemier.allvarligForekomstVakenTidObservationstid');
                    viewState.intygModel.hypoglykemier.allvarligForekomstVakenTidObservationstid = undefined;
                } else {
                    viewState.intygModel.restoreFromAttic('hypoglykemier.allvarligForekomstVakenTidObservationstid');
                }
            }, true);
            // --- form2

        }]);