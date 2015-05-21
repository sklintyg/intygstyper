angular.module('ts-diabetes').controller('ts-diabetes.Utkast.Form2Controller',
    ['$scope', '$log',
        'ts-diabetes.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.cert = viewState.intygModel;
            $scope.viewState = viewState;

            // --- form2
            $scope.$watch('cert.hypoglykemier.teckenNedsattHjarnfunktion',
                function(forekommerTeckenNedsattHjarnfunktion) {
                    if (!forekommerTeckenNedsattHjarnfunktion && $scope.cert.hypoglykemier) {
                        $scope.cert.updateToAttic('hypoglykemier.saknarFormagaKannaVarningstecken');
                        $scope.cert.updateToAttic('hypoglykemier.allvarligForekomst');
                        $scope.cert.updateToAttic('hypoglykemier.allvarligForekomstTrafiken');

                        $scope.cert.hypoglykemier.saknarFormagaKannaVarningstecken = undefined;
                        $scope.cert.hypoglykemier.allvarligForekomst = undefined;
                        $scope.cert.hypoglykemier.allvarligForekomstTrafiken = undefined;
                    } else {
                        $scope.cert.restoreFromAttic('hypoglykemier.saknarFormagaKannaVarningstecken');
                        $scope.cert.restoreFromAttic('hypoglykemier.allvarligForekomst');
                        $scope.cert.restoreFromAttic('hypoglykemier.allvarligForekomstTrafiken');
                    }
                }, true);

            $scope.$watch('cert.hypoglykemier.allvarligForekomst', function(haftAllvarligForekomst) {
                if (!haftAllvarligForekomst && $scope.cert.hypoglykemier) {
                    $scope.cert.updateToAttic('hypoglykemier.allvarligForekomstBeskrivning');
                    $scope.cert.hypoglykemier.allvarligForekomstBeskrivning = undefined;
                } else {
                    $scope.cert.restoreFromAttic('hypoglykemier.allvarligForekomstBeskrivning');
                }
            }, true);
            $scope.$watch('cert.hypoglykemier.allvarligForekomstTrafiken', function(haftAllvarligForekomstTrafiken) {
                if (!haftAllvarligForekomstTrafiken && $scope.cert.hypoglykemier) {
                    $scope.cert.updateToAttic('hypoglykemier.allvarligForekomstTrafikBeskrivning');
                    $scope.cert.hypoglykemier.allvarligForekomstTrafikBeskrivning = undefined;
                } else {
                    $scope.cert.restoreFromAttic('hypoglykemier.allvarligForekomstTrafikBeskrivning');
                }
            }, true);

            // hypoglykemier.allvarligForekomstVakenTidObservationstid
            var addDateParser = function(form){
                if(form && form.allvarligForekomstVakenTidObservationstid){
                    var formElement = form.allvarligForekomstVakenTidObservationstid;
                    formElement.$parsers.push(function(viewValue) {
                        $scope.cert.hypoglykemier.allvarligForekomstVakenTidObservationstid = formElement.$viewValue;
                        return viewValue;
                    });
                }
            };
            $scope.$watch('cert.hypoglykemier.allvarligForekomstVakenTid', function(haftAllvarligForekomstVakenTid) {
                if (!haftAllvarligForekomstVakenTid && $scope.cert.hypoglykemier) {
                    $scope.cert.updateToAttic('hypoglykemier.allvarligForekomstVakenTidObservationstid');
                    $scope.cert.hypoglykemier.allvarligForekomstVakenTidObservationstid = undefined;
                } else {
                    $scope.cert.restoreFromAttic('hypoglykemier.allvarligForekomstVakenTidObservationstid');
                }
            }, true);
            // --- form2

        }]);