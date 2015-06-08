angular.module('ts-bas').controller('ts-bas.Utkast.Form5Controller',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;

            $scope.$watch('viewState.intygModel.diabetes.harDiabetes', function(harDiabetes) {
                if (!harDiabetes && viewState.intygModel.hjartKarl) {
                    viewState.intygModel.updateToAttic('diabetes.diabetesTyp');
                    viewState.intygModel.diabetes.diabetesTyp = undefined;
                } else {
                    viewState.intygModel.restoreFromAttic('diabetes.diabetesTyp');
                }
            }, true);

            $scope.$watch('viewState.intygModel.diabetes.diabetesTyp', function(diabetesTyp) {
                if (diabetesTyp !== 'DIABETES_TYP_2' && viewState.intygModel.diabetes) {
                    viewState.intygModel.updateToAttic('diabetes.kost');
                    viewState.intygModel.updateToAttic('diabetes.tabletter');
                    viewState.intygModel.updateToAttic('diabetes.insulin');
                    viewState.intygModel.diabetes.kost = undefined;
                    viewState.intygModel.diabetes.tabletter = undefined;
                    viewState.intygModel.diabetes.insulin = undefined;
                } else {
                    viewState.intygModel.restoreFromAttic('diabetes.kost');
                    viewState.intygModel.restoreFromAttic('diabetes.tabletter');
                    viewState.intygModel.restoreFromAttic('diabetes.insulin');
                }
            }, true);
            
        }]);