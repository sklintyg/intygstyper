angular.module('ts-bas').controller('ts-bas.Utkast.IntentionController',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.cert = viewState.intygModel;
            $scope.viewState = viewState;

            $scope.$watch('cert.intygAvser.korkortstyp', function(valdaKorkortstyper) {
                if ($scope.cert.intygAvser && $scope.cert.intygAvser.korkortstyp) {
                    var prev = $scope.viewState.korkortd;
                    $scope.viewState.korkortd = isHighlevelKorkortChecked(valdaKorkortstyper);
                    if ($scope.viewState.korkortd && $scope.viewState.korkortd !== prev) {
                        $scope.cert.restoreFromAttic('horselBalans.svartUppfattaSamtal4Meter');
                        $scope.cert.restoreFromAttic('funktionsnedsattning.otillrackligRorelseformaga');
                    } else {
                        if($scope.viewState.korkortd === false){
                            $scope.cert.updateToAttic('horselBalans.svartUppfattaSamtal4Meter');
                            $scope.cert.updateToAttic('funktionsnedsattning.otillrackligRorelseformaga');
                            $scope.cert.horselBalans.svartUppfattaSamtal4Meter = undefined;
                            $scope.cert.funktionsnedsattning.otillrackligRorelseformaga = undefined;
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