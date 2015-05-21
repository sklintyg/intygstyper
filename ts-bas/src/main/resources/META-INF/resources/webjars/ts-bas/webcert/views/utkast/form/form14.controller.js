angular.module('ts-bas').controller('ts-bas.Utkast.Form14Controller',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.cert = viewState.intygModel;
            $scope.viewState = viewState;

            $scope.$watch('cert.sjukhusvard.sjukhusEllerLakarkontakt', function(vardatsPaSjukhus) {
                if (!vardatsPaSjukhus && $scope.cert.sjukhusvard) {
                    $scope.cert.updateToAttic('sjukhusvard.tidpunkt');
                    $scope.cert.updateToAttic('sjukhusvard.vardinrattning');
                    $scope.cert.updateToAttic('sjukhusvard.anledning');
                    $scope.cert.sjukhusvard.tidpunkt = '';
                    $scope.cert.sjukhusvard.vardinrattning = '';
                    $scope.cert.sjukhusvard.anledning = '';
                } else {
                    $scope.cert.restoreFromAttic('sjukhusvard.tidpunkt');
                    $scope.cert.restoreFromAttic('sjukhusvard.vardinrattning');
                    $scope.cert.restoreFromAttic('sjukhusvard.anledning');
                }
            }, true);
        }]);