angular.module('fk7263').controller('fk7263.EditCert.Form13Ctrl',
    ['$log', 'fk7263.Domain.IntygModel', '$scope', 'fk7263.EditCertCtrl.ViewStateService',
        function($log, model, $scope, viewState) {
            'use strict';
            $scope.model = model;
            $scope.viewState = viewState;

            $scope.inputLimits = {
                ovrigt: 360 // = combined field 13 (and dependencies that end up in field 13) limit
            };
            $scope.teckenKvar = function(){
                var kvar = viewState.inputLimits.ovrigt - viewState.getTotalOvrigtLength()
                return kvar;
            }

        }]);