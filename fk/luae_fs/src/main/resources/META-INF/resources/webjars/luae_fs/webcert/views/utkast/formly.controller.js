angular.module('luae_fs').controller('luae_fs.EditCert.FormlyCtrl',
    ['$scope', 'luae_fs.EditCertCtrl.ViewStateService', 'luae_fs.FormFactory', 'common.TillaggsfragorHelper',
        function FormlyCtrl($scope, viewState, formFactory, tillaggsfragorHelper) {
            'use strict';

            $scope.viewState = viewState;

            $scope.model = viewState.intygModel;

            $scope.options = {
                formState:{viewState:viewState}
            };

            $scope.formFields = formFactory.getFormFields();

            tillaggsfragorHelper.buildTillaggsFragor($scope.formFields, viewState.intygModel, $scope.formFields.length - 1);
            $scope.$on('dynamicLabels.updated', function () {
                tillaggsfragorHelper.buildTillaggsFragor($scope.formFields, viewState.intygModel, $scope.formFields.length - 1);
            });
        }
    ]);