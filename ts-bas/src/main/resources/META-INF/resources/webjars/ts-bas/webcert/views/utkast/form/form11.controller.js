angular.module('ts-bas').controller('ts-bas.Utkast.Form11Controller',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.cert = viewState.intygModel;
            $scope.viewState = viewState;

            $scope.$watch('cert.narkotikaLakemedel.teckenMissbruk || cert.narkotikaLakemedel.foremalForVardinsats',
                function(shown) {
                    if (shown) {
                        $scope.cert.restoreFromAttic('narkotikaLakemedel.provtagningBehovs');
                    } else {
                        $scope.cert.updateToAttic('narkotikaLakemedel.provtagningBehovs');
                        $scope.cert.narkotikaLakemedel.provtagningBehovs = undefined;
                    }
                }, true);

            $scope.$watch('cert.narkotikaLakemedel.lakarordineratLakemedelsbruk', function(anvanderOrdineradNarkotika) {
                if (!anvanderOrdineradNarkotika && $scope.cert.narkotikaLakemedel) {
                    $scope.cert.updateToAttic('narkotikaLakemedel.lakemedelOchDos');
                    $scope.cert.narkotikaLakemedel.lakemedelOchDos = '';
                } else {
                    $scope.cert.restoreFromAttic('narkotikaLakemedel.lakemedelOchDos');
                }
            }, true);
            
        }]);