describe('sjukersattning.EditCertCtrl.Form4Ctrl', function() {
    'use strict';

    var $scope;
    var $rootScope;
    var $log;
    var model;
    var viewState;

    // Load the webcert module and mock away everything that is not necessary.

    beforeEach(angular.mock.module('common', 'sjukersattning', function(/*$provide*/) {

        // the below are now included in common
        //$provide.value('sjukersattning.domain.DraftModel', __draftModel__);
        //$provide.value('sjukersattning.domain.IntygModel', {});
        //$provide.value('sjukersattning.domain.PatientModel', {});
        //$provide.value('sjukersattning.domain.SkapadAvModel', {});
        //$provide.value('sjukersattning.domain.VardenhetModel', {});

    }));

    // Get references to the object we want to test from the context.

    beforeEach(angular.mock.inject([
        '$controller',
        '$rootScope',
        '$httpBackend',
        '$log',
        'sjukersattning.Domain.IntygModel',
        'sjukersattning.EditCertCtrl.ViewStateService',
        function( $controller, _$rootScope_, _$httpBackend_, _$log_, _model_, _viewState_) {
            $rootScope = _$rootScope_;
            $scope = $rootScope.$new();
            $log = _$log_;

            var IntygModel = _model_;
            model = new IntygModel();
            viewState = _viewState_;
            viewState.intygModel = model;

            $controller('sjukersattning.EditCert.Form4Ctrl' , { $scope: $scope, $log : $log, model : model, viewState : viewState });

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