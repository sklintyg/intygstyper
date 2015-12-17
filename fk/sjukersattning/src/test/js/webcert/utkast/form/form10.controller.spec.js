describe('sjukersattning.EditCertCtrl.Form10Ctrl', function() {
    'use strict';

    var $scope;
    var $rootScope;
    var $log;
    var model;
    var viewState;

    // Load the webcert module and mock away everything that is not necessary.

    beforeEach(angular.mock.module('common', 'sjukersattning', function(/*$provide*/) {

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

            $controller('sjukersattning.EditCert.Form10Ctrl' , { $scope: $scope, $log : $log, model : model, viewState : viewState });

        }]));

    describe('#change in avstangningSmittskydd should trigger an update in the attic', function() {
        beforeEach(function(){

            model.prognosBedomning = 'prognosBedomning';
            model.arbetsformagaPrognosGarInteAttBedomaBeskrivning = 'arbetsformagaPrognosGarInteAttBedomaBeskrivning';

        });

        it('can restore from the attic', function(){
            // ----- arrange
            viewState.common.doneLoading = true;

            // ----- act
            $scope.$apply(); // register false on avstangningSmittskydd


            viewState.avstangningSmittskyddValue = true;
            $scope.$apply();

            // ----- assert
            expect(model.prognosBedomning).toBe(undefined);
            expect(model.arbetsformagaPrognosGarInteAttBedomaBeskrivning).toBe(undefined);

            // ----- act
            viewState.avstangningSmittskyddValue = false;  // this should trigger the watch event
            $scope.$apply();

            // ----- assert
            // expects
            // restore from attic
            model.prognosBedomning = 'prognosBedomning';
            model.arbetsformagaPrognosGarInteAttBedomaBeskrivning = 'arbetsformagaPrognosGarInteAttBedomaBeskrivning';


        });

    });

    describe('#change in sysselsattning to Arbetsloshet should trigger an update in the attic', function() {
        beforeEach(function(){

            model.prognosBedomning = 'prognosBedomning';
            model.arbetsformagaPrognosGarInteAttBedomaBeskrivning = 'arbetsformagaPrognosGarInteAttBedomaBeskrivning';

        });

        it('can restore from the attic', function(){
            // ----- arrange
            viewState.common.doneLoading = true;

            // ----- act
            $scope.$apply(); // register false on avstangningSmittskydd


            // set sysselsattning to Arbetsloshet only
            viewState.sysselsattningValue = [false, true, false];
            $scope.$apply();

            // ----- assert
            expect(model.prognosBedomning).toBe(undefined);
            expect(model.arbetsformagaPrognosGarInteAttBedomaBeskrivning).toBe(undefined);

            // ----- act
            viewState.sysselsattningValue = [true, false, false]; // this should trigger the watch event
            $scope.$apply();

            // ----- assert
            // expects
            // restore from attic
            model.prognosBedomning = 'prognosBedomning';
            model.arbetsformagaPrognosGarInteAttBedomaBeskrivning = 'arbetsformagaPrognosGarInteAttBedomaBeskrivning';
        });

    });
});
