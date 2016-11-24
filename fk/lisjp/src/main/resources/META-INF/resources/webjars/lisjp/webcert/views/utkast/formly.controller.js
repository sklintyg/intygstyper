angular.module('lisjp').controller('lisjp.EditCert.FormlyCtrl',
    ['$scope', 'lisjp.EditCertCtrl.ViewStateService', 'lisjp.FormFactory', 'common.TillaggsfragorHelper', 'common.ArendeListViewStateService',
     'common.fmbViewState',
        function FormlyCtrl($scope, viewState, formFactory, tillaggsfragorHelper, ArendeListViewState, fmbViewState) {

            'use strict';

            $scope.viewState = viewState;

            $scope.model = viewState.intygModel;

            viewState.fmbViewState = fmbViewState.state;

            // hasKompletteringar needs to be here since a formly wrapper (validationGroup.formly.js) currently can not have a controller
            $scope.options = {
                formState:{viewState:viewState, hasKompletteringar:ArendeListViewState.hasKompletteringar.bind(ArendeListViewState)}
            };

            $scope.formFields = formFactory.getFormFields();

            tillaggsfragorHelper.buildTillaggsFragor($scope.formFields, viewState.intygModel, $scope.formFields.length - 1);
            $scope.$on('intyg.loaded', function () {
                tillaggsfragorHelper.buildTillaggsFragor($scope.formFields, viewState.intygModel, $scope.formFields.length - 1);
            });
        }
    ]);
