angular.module('ts-diabetes').controller('ts-diabetes.Utkast.IntentionController',
    ['$scope', '$log',
        'ts-diabetes.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;

            // --- intention
            $scope.$watch('viewState.intygModel.intygAvser.korkortstyp', function(valdaKorkortstyper) {
                if (viewState.intygModel.intygAvser && viewState.intygModel.intygAvser.korkortstyp) {
                    var targetTypes = ['C1', 'C1E', 'C', 'CE', 'D1', 'D1E', 'D', 'DE', 'TAXI'];
                    var visaKorkortd = false;
                    for (var i = 0; i < valdaKorkortstyper.length; i++) {
                        for (var j = 0; j < targetTypes.length; j++) {
                            if (valdaKorkortstyper[i].type === targetTypes[j] && valdaKorkortstyper[i].selected) {
                                visaKorkortd = true;
                                break;
                            }
                        }
                    }
                    $scope.viewState.korkortd = visaKorkortd;
                    if (visaKorkortd) {
                        viewState.intygModel.restoreFromAttic('hypoglykemier.egenkontrollBlodsocker');
                        viewState.intygModel.restoreFromAttic('hypoglykemier.allvarligForekomstVakenTid');
                        viewState.intygModel.restoreFromAttic('hypoglykemier.allvarligForekomstVakenTidObservationstid');
                        viewState.intygModel.restoreFromAttic('bedomning.lamplighetInnehaBehorighet');
                    } else {
                        viewState.intygModel.updateToAttic('hypoglykemier.egenkontrollBlodsocker');
                        viewState.intygModel.updateToAttic('hypoglykemier.allvarligForekomstVakenTid');
                        viewState.intygModel.updateToAttic('hypoglykemier.allvarligForekomstVakenTidObservationstid');
                        viewState.intygModel.updateToAttic('bedomning.lamplighetInnehaBehorighet');
                        viewState.intygModel.hypoglykemier.egenkontrollBlodsocker = undefined;
                        viewState.intygModel.hypoglykemier.allvarligForekomstVakenTid = undefined;
                        viewState.intygModel.bedomning.lamplighetInnehaBehorighet = undefined;
                    }
                }
            }, true);
            // --- intention

        }]);