angular.module('luse').controller('luse.EditCert.FormlyCtrl',
    ['$scope', 'luse.EditCertCtrl.ViewStateService', 'luse.FormFactory', 'common.TillaggsfragorHelper',
        function FormlyCtrl($scope, viewState, formFactory, tillaggsfragorHelper) {
            'use strict';

            $scope.viewState = viewState;

            $scope.model = viewState.intygModel;

            $scope.options = {
                formState:{viewState:viewState}
            };

            $scope.formFields = formFactory.getFormFields();

            tillaggsfragorHelper.buildTillaggsFragor($scope.formFields, viewState.intygModel, $scope.formFields.length - 1);
            $scope.$on('intyg.loaded', function () {
                tillaggsfragorHelper.buildTillaggsFragor($scope.formFields, viewState.intygModel, $scope.formFields.length - 1);
            });

        }
    ]);