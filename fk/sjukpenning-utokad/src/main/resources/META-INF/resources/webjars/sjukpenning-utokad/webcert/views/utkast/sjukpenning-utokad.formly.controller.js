angular.module('lisu').controller('sjukpenning-utokad.EditCert.FormlyCtrl',
    ['$scope', 'sjukpenning-utokad.EditCertCtrl.ViewStateService', 'sjukpenning-utokad.FormFactory',
        function FormlyCtrl($scope, viewState, formFactory) {
            'use strict';

            $scope.viewState = viewState;

            $scope.model = viewState.intygModel;

            $scope.options = {
                formState:{viewState:viewState}
            };

            $scope.formFields = formFactory.getFormFields();

            formFactory.buildTillaggsFragor($scope.formFields, viewState.intygModel, 9);
            $scope.$on('dynamicLabels.updated', function () {
                formFactory.buildTillaggsFragor($scope.formFields, viewState.intygModel, 9);
            });

        }
    ]);