angular.module('ts-bas').controller('ts-bas.Utkast.Form5Controller',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.cert = viewState.intygModel;
            $scope.viewState = viewState;

            $scope.$watch('cert.diabetes.harDiabetes', function(harDiabetes) {
                if (!harDiabetes && $scope.cert.hjartKarl) {
                    $scope.cert.updateToAttic('diabetes.diabetesTyp');
                    $scope.cert.diabetes.diabetesTyp = undefined;
                } else {
                    $scope.cert.restoreFromAttic('diabetes.diabetesTyp');
                }
            }, true);

            $scope.$watch('cert.diabetes.diabetesTyp', function(diabetesTyp) {
                if (diabetesTyp !== 'DIABETES_TYP_2' && $scope.cert.diabetes) {
                    $scope.cert.updateToAttic('diabetes.kost');
                    $scope.cert.updateToAttic('diabetes.tabletter');
                    $scope.cert.updateToAttic('diabetes.insulin');
                    $scope.cert.diabetes.kost = undefined;
                    $scope.cert.diabetes.tabletter = undefined;
                    $scope.cert.diabetes.insulin = undefined;
                } else {
                    $scope.cert.restoreFromAttic('diabetes.kost');
                    $scope.cert.restoreFromAttic('diabetes.tabletter');
                    $scope.cert.restoreFromAttic('diabetes.insulin');
                }
            }, true);
            
        }]);