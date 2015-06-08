angular.module('ts-bas').controller('ts-bas.Utkast.IntentionController',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;

            $scope.$watch('viewState.intygModel.intygAvser.korkortstyp', function(valdaKorkortstyper) {
                if (viewState.intygModel.intygAvser && viewState.intygModel.intygAvser.korkortstyp) {
                    var prev = $scope.viewState.korkortd;
                    $scope.viewState.korkortd = isHighlevelKorkortChecked(valdaKorkortstyper);
                    if ($scope.viewState.korkortd && $scope.viewState.korkortd !== prev) {
                        viewState.intygModel.restoreFromAttic('horselBalans.svartUppfattaSamtal4Meter');
                        viewState.intygModel.restoreFromAttic('funktionsnedsattning.otillrackligRorelseformaga');
                    } else {
                        if($scope.viewState.korkortd === false){
                            viewState.intygModel.updateToAttic('horselBalans.svartUppfattaSamtal4Meter');
                            viewState.intygModel.updateToAttic('funktionsnedsattning.otillrackligRorelseformaga');
                            viewState.intygModel.horselBalans.svartUppfattaSamtal4Meter = undefined;
                            viewState.intygModel.funktionsnedsattning.otillrackligRorelseformaga = undefined;
                        }
                    }
                }
            }, true);

            function isHighlevelKorkortChecked(valdaKorkortstyper) {
                var targetTypes = ['D1', 'D1E', 'D', 'DE', 'TAXI'];
                var visaKorkortd = false;
                for (var i = 0; i < valdaKorkortstyper.length; i++) {
                    for (var j = 0; j < targetTypes.length; j++) {
                        if (valdaKorkortstyper[i].type === targetTypes[j] && valdaKorkortstyper[i].selected) {
                            visaKorkortd = true;
                            break;
                        }
                    }
                }
                return visaKorkortd;
            }

        }]);