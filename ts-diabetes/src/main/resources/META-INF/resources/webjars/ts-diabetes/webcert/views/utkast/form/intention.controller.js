angular.module('ts-diabetes').controller('ts-diabetes.Utkast.IntentionController',
    ['$scope', '$log',
        'ts-diabetes.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.cert = viewState.intygModel;
            $scope.viewState = viewState;

            // --- intention
            $scope.$watch('cert.intygAvser.korkortstyp', function(valdaKorkortstyper) {
                if ($scope.cert.intygAvser && $scope.cert.intygAvser.korkortstyp) {
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
                        $scope.cert.restoreFromAttic('hypoglykemier.egenkontrollBlodsocker');
                        $scope.cert.restoreFromAttic('hypoglykemier.allvarligForekomstVakenTid');
                        $scope.cert.restoreFromAttic('hypoglykemier.allvarligForekomstVakenTidObservationstid');
                        $scope.cert.restoreFromAttic('bedomning.lamplighetInnehaBehorighet');
                    } else {
                        $scope.cert.updateToAttic('hypoglykemier.egenkontrollBlodsocker');
                        $scope.cert.updateToAttic('hypoglykemier.allvarligForekomstVakenTid');
                        $scope.cert.updateToAttic('hypoglykemier.allvarligForekomstVakenTidObservationstid');
                        $scope.cert.updateToAttic('bedomning.lamplighetInnehaBehorighet');
                        $scope.cert.hypoglykemier.egenkontrollBlodsocker = undefined;
                        $scope.cert.hypoglykemier.allvarligForekomstVakenTid = undefined;
                        $scope.cert.bedomning.lamplighetInnehaBehorighet = undefined;
                    }
                }
            }, true);
            // --- intention

        }]);