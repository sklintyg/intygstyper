angular.module('ts-bas').controller('ts-bas.Utkast.Form11Controller',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;

            $scope.$watch('viewState.intygModel.narkotikaLakemedel.teckenMissbruk || viewState.intygModel.narkotikaLakemedel.foremalForVardinsats',
                function(shown) {
                    if (shown) {
                        viewState.intygModel.restoreFromAttic('narkotikaLakemedel.provtagningBehovs');
                    } else {
                        viewState.intygModel.updateToAttic('narkotikaLakemedel.provtagningBehovs');
                        viewState.intygModel.narkotikaLakemedel.provtagningBehovs = undefined;
                    }
                }, true);

            $scope.$watch('viewState.intygModel.narkotikaLakemedel.lakarordineratLakemedelsbruk', function(anvanderOrdineradNarkotika) {
                if (!anvanderOrdineradNarkotika && viewState.intygModel.narkotikaLakemedel) {
                    viewState.intygModel.updateToAttic('narkotikaLakemedel.lakemedelOchDos');
                    viewState.intygModel.narkotikaLakemedel.lakemedelOchDos = '';
                } else {
                    viewState.intygModel.restoreFromAttic('narkotikaLakemedel.lakemedelOchDos');
                }
            }, true);
        }]);