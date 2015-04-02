angular.module('fk7263').controller('fk7263.EditCert.Form15Ctrl',
    ['$scope', '$log', 'fk7263.Domain.IntygModel', 'fk7263.EditCertCtrl.ViewStateService',
        function($scope, $log, model, viewState) {
            'use strict';
            $scope.model = model;
            $scope.viewState = viewState;

            $scope.$watch('viewState.common.doneLoading', function(newVal) {
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
                        $scope.viewState.common.hsaInfoMissing = true;
                    } else {
                        $scope.viewState.common.hsaInfoMissing = false;
                    }
                }
            });
        }]);