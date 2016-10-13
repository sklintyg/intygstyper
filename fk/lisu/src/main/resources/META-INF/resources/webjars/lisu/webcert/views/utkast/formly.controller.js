angular.module('lisu').controller('lisu.EditCert.FormlyCtrl',
    ['$scope', 'lisu.EditCertCtrl.ViewStateService', 'lisu.FormFactory', 'common.TillaggsfragorHelper',
     'common.fmb.ViewStateService',
        function FormlyCtrl($scope, viewState, formFactory, tillaggsfragorHelper, fmbViewState) {

            'use strict';

            $scope.viewState = viewState;

            $scope.model = viewState.intygModel;

            viewState.fmbViewState = fmbViewState.state;

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