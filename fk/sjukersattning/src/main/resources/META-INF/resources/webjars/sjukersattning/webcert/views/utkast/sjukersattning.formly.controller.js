angular.module('luse').controller('sjukersattning.EditCert.FormlyCtrl',
    ['$scope', 'sjukersattning.EditCertCtrl.ViewStateService', 'sjukersattning.FormFactory',
        function FormlyCtrl($scope, viewState, formFactory) {
            'use strict';

            $scope.viewState = viewState;

            $scope.model = viewState.intygModel;

            $scope.options = {
                formState:{viewState:viewState}
            };

            $scope.formFields = formFactory.formFields;

            formFactory.buildTillaggsFragor(viewState.intygModel, 10);
            $scope.$on('dynamicLabels.updated', function () {
                formFactory.buildTillaggsFragor(viewState.intygModel, 10);
            });

        }
    ]);