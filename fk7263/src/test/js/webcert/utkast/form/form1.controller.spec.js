describe('fk7263.EditCertCtrl.Form1Ctrl', function() {
    'use strict';

    var $scope;
    var $rootScope;
    var $log;
    var model;
    var viewState;

    // Load the webcert module and mock away everything that is not necessary.

    beforeEach(angular.mock.module('common', 'fk7263', function(/*$provide*/) {

        // the below are now included in common
        //$provide.value('fk7263.domain.DraftModel', __draftModel__);
        //$provide.value('fk7263.domain.IntygModel', {});
        //$provide.value('fk7263.domain.PatientModel', {});
        //$provide.value('fk7263.domain.SkapadAvModel', {});
        //$provide.value('fk7263.domain.VardenhetModel', {});

    }));

    // Get references to the object we want to test from the context.

    beforeEach(angular.mock.inject([
        '$controller',
        '$rootScope',
        '$httpBackend',
        '$log',
        'fk7263.Domain.IntygModel',
        'fk7263.EditCertCtrl.ViewStateService',
        function( $controller, _$rootScope_, _$httpBackend_, _$log_, _model_, _viewState_) {
            $rootScope = _$rootScope_;
            $scope = $rootScope.$new();
            $log = _$log_;
            var IntygModel = _model_;
            model = new IntygModel();
            viewState = _viewState_;
            viewState.intygModel = model;

            $controller('fk7263.EditCert.Form1Ctrl' , { $scope: $scope, $log : $log, model : model, viewState : viewState });

        }]));

    describe('#avstangningSmittskydd should change on view state when model changes', function() {
        it('some scope function', function(){
            // ----- arrange
            // in arrange we setup our spies with expected return values
            model.avstangningSmittskydd = false;

            // ----- act
            // call the function
            $scope.onSmittskyddChange();
            // ----- assert
            // expects

            expect(viewState.avstangningSmittskyddValue).toBe(false);

            // ----- arrange
            // in arrange we setup our spies with expected return values
            model.avstangningSmittskydd = true;

            // ----- act
            // call the function
            $scope.onSmittskyddChange();
            // ----- assert
            // expects

            expect(viewState.avstangningSmittskyddValue).toBe(true);
        });

    });

});