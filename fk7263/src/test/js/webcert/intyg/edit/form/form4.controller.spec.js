describe('fk7263.EditCertCtrl.Form4Ctrl', function() {
    'use strict';

    var $scope;
    var $rootScope;
    var $log;
    var model;
    var viewState;

    // Load the webcert module and mock away everything that is not necessary.

    beforeEach(angular.mock.module('common', 'fk7263', function($provide) {

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

            $controller('fk7263.EditCert.Form4Ctrl' , { $scope: $scope, $log : $log, model : model, viewState : viewState });

        }]));

    describe('#change in avstangningSmittskydd should trigger an update in the attic', function() {
        beforeEach(function(){

            model.funktionsnedsattning = 'funktionsnedsattning';

        });

        it('can restore from the attic', function(){
            // ----- arrange
            model.funktionsnedsattning = 'funktionsnedsattning';

            viewState.common.doneLoading = true;

            // ----- act
            $scope.$apply(); // register false on avstangningSmittskydd


            viewState.avstangningSmittskyddValue = true;
            $scope.$apply();

            // ----- assert
            expect(model.funktionsnedsattning).toBe(undefined);

            // ----- act
            viewState.avstangningSmittskyddValue = false;  // this should trigger the watch event
            $scope.$apply();

            // ----- assert
            // expects
            // attic check
            expect(model.funktionsnedsattning).toBe('funktionsnedsattning');

        });

    });

});