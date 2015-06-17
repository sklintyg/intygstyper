angular.module('sjukpenning').controller('sjukpenning.EditCert.Form13Ctrl',
    ['$scope', '$log', 'sjukpenning.EditCertCtrl.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.model = viewState.intygModel;
            $scope.viewState = viewState;

            $scope.inputLimits = {
                ovrigt: 360 // = combined field 13 (and dependencies that end up in field 13) limit
            };
            $scope.teckenKvar = function(){
                return viewState.inputLimits.ovrigt - viewState.getTotalOvrigtLength();
            };

        }]);