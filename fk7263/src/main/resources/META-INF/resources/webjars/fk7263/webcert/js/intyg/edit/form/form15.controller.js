angular.module('fk7263').controller('fk7263.EditCert.Form15Ctrl',
    ['$log', 'fk7263.Domain.IntygModel', '$scope', 'fk7263.EditCertCtrl.ViewStateService',
        function($log, model, $scope, viewState) {
            'use strict';
            $scope.model = model;
            $scope.viewState = viewState;

            $scope.$watch('viewState.common.viewState.doneLoading', function(newVal) {
                if (newVal) {
                    // check if all info is available from HSA. If not, display the info message that someone needs to update it
                    if (!$scope.model.grundData || !$scope.model.grundData.skapadAv ||
                        !$scope.model.grundData.skapadAv.vardenhet ||
                        $scope.model.grundData.skapadAv.vardenhet.postadress === undefined ||
                        $scope.model.grundData.skapadAv.vardenhet.postnummer === undefined ||
                        $scope.model.grundData.skapadAv.vardenhet.postort === undefined ||
                        $scope.model.grundData.skapadAv.vardenhet.telefonnummer === undefined ||
                        $scope.model.grundData.skapadAv.vardenhet.postadress === '' ||
                        $scope.model.grundData.skapadAv.vardenhet.postnummer === '' ||
                        $scope.model.grundData.skapadAv.vardenhet.postort === '' ||
                        $scope.model.grundData.skapadAv.vardenhet.telefonnummer === '') {
                        $scope.viewState.common.viewState.hsaInfoMissing = true;
                    } else {
                        $scope.viewState.common.hsaInfoMissing = false;
                    }
                }
            });
        }]);