angular.module('lisu').controller('sjukpenning-utokad.EditCert.FormlyCtrl',
    ['$scope', 'sjukpenning-utokad.EditCertCtrl.ViewStateService', 'sjukpenning-utokad.FormFactory', 'common.TillaggsfragorHelper', 'sjukpenningUttokad.fmb.ViewStateService',
        function FormlyCtrl($scope, viewState, formFactory, tillaggsfragorHelper, fmbViewState) {
            'use strict';

            $scope.viewState = viewState;

            $scope.model = viewState.intygModel;

            $scope.fmb = fmbViewState.state;

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