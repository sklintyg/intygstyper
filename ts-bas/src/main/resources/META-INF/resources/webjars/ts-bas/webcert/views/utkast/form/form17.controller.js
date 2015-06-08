angular.module('ts-bas').controller('ts-bas.Utkast.Form17Controller',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;

            $scope.$watch('viewState.intygModel.bedomning.kanInteTaStallning', function (behorighet) {
                if (!viewState.intygModel.bedomning) {
                    viewState.intygModel.bedomning = {
                        korkortstyp: {},
                        kanInteTaStallning: false
                    };
                }

                if(behorighet === undefined){
                    viewState.intygModel.bedomning.kanInteTaStallning = undefined;
                } else if (behorighet) {
                    viewState.intygModel.bedomning.kanInteTaStallning = true;
                    viewState.intygModel.updateToAttic('bedomning.korkortstyp');
                    viewState.intygModel.clear('bedomning.korkortstyp');
                } else {
                    viewState.intygModel.restoreFromAttic('bedomning.korkortstyp');
                    viewState.intygModel.bedomning.kanInteTaStallning = false;
                }
            });
            
        }]);