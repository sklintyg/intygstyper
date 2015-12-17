angular.module('sjukpenning').controller('sjukpenning.EditCert.Form10Ctrl',
    ['$scope', '$log', 'sjukpenning.EditCertCtrl.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.model = viewState.intygModel;
            $scope.viewState = viewState;

            $scope.inputLimits = {
                ovrigt: 360
            };

        }]);