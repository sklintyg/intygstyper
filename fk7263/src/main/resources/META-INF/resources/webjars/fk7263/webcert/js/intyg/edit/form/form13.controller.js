angular.module('fk7263').controller('fk7263.EditCert.Form13Ctrl',
    ['$scope', '$log', 'fk7263.EditCertCtrl.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            var model = viewState.intygModel;
            $scope.model = model;
            $scope.viewState = viewState;

            $scope.inputLimits = {
                ovrigt: 360 // = combined field 13 (and dependencies that end up in field 13) limit
            };
            $scope.teckenKvar = function(){
                var kvar = viewState.inputLimits.ovrigt - viewState.getTotalOvrigtLength();
                return kvar;
            };

        }]);