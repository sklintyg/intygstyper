angular.module('lisu').controller('sjukpenning-utokad.EditCert.FormlyCtrl',
    ['$scope', 'sjukpenning-utokad.EditCertCtrl.ViewStateService', 'sjukpenning-utokad.FormFactory',
        'common.TillaggsfragorHelper', 'common.fmb.ViewStateService', '$log',
        function FormlyCtrl($scope, viewState, formFactory, tillaggsfragorHelper, fmbViewState, $log) {
            'use strict';

            $scope.viewState = viewState;

            viewState.fmbViewState = fmbViewState.state;

            $scope.formlyModel = viewState;

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