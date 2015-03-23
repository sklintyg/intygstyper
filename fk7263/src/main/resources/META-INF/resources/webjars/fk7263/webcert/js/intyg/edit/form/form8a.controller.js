angular.module('fk7263').controller('fk7263.EditCert.Form8aCtrl',
    ['$log', 'fk7263.Domain.IntygModel', '$scope', 'fk7263.EditCertCtrl.ViewStateService',
        function($log, model, $scope, viewState) {
            'use strict';
            $scope.model = model;
            $scope.viewState = viewState;

            $scope.inputLimits = {
                nuvarandeArbetsuppgifter: 120
            }

            $scope.$watch('viewState.avstangningSmittskyddValue', function(newVal) {
                // only do this once the page is loaded and changes come from the gui!
                if(viewState.common.doneLoading) {
                    // Remove defaults not applicable when smittskydd is active
                    if (newVal === true) {
                        $scope.model.nuvarandeArbete = false;
                        $scope.model.nuvarandeArbetsuppgifter = undefined;
                        $scope.model.arbetsloshet = false;
                        $scope.model.foraldrarledighet = false;
                    }
                }
            });

        }]);