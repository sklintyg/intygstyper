angular.module('ts-bas').controller('ts-bas.Utkast.Form17Controller',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.cert = viewState.intygModel;
            $scope.viewState = viewState;

            $scope.$watch('cert.bedomning.kanInteTaStallning', function (behorighet) {
                if (!$scope.cert.bedomning) {
                    $scope.cert.bedomning = {
                        korkortstyp: {},
                        kanInteTaStallning: false
                    };
                }

                if(behorighet === undefined){
                    $scope.cert.bedomning.kanInteTaStallning = undefined;
                } else if (behorighet) {
                    $scope.cert.bedomning.kanInteTaStallning = true;
                    $scope.cert.updateToAttic('bedomning.korkortstyp');
                    $scope.cert.clear('bedomning.korkortstyp');
                } else {
                    $scope.cert.restoreFromAttic('bedomning.korkortstyp');
                    $scope.cert.bedomning.kanInteTaStallning = false;
                }
            });
            
        }]);