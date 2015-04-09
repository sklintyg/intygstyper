describe('fk7263.EditCertCtrl.Form2Ctrl', function() {
    'use strict';

    var $scope;
    var $rootScope;
    var $log;
    var model;
    var viewState;

    // Load the webcert module and mock away everything that is not necessary.

    beforeEach(angular.mock.module('common', 'fk7263', function($provide) {

        // the below are now included in common
        //$provide.value('fk7263.Domain.DraftModel', __draftModel__);
        //$provide.value('fk7263.Domain.IntygModel', {});
        //$provide.value('fk7263.Domain.PatientModel', {});
        //$provide.value('fk7263.Domain.SkapadAvModel', {});
        //$provide.value('fk7263.Domain.VardenhetModel', {});

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
            model = _model_;
            viewState = _viewState_;

            $controller('fk7263.EditCert.Form2Ctrl' , { $scope: $scope, $log : $log, model : model, viewState : viewState });

        }]));

    describe('#change in avstangningSmittskydd should trigger an update in the attic', function() {
        beforeEach(function(){
            spyOn(model, 'atticUpdateForm2').and.callThrough();
            spyOn(model, 'clearForm2').and.callThrough();
            spyOn(model, 'atticHasForm2').and.returnValue(true);
            spyOn(model, 'atticRestoreForm2').and.callThrough();

            model.diagnosBeskrivning = 'diagnosBeskrivning';
            model.diagnosBeskrivning1 = 'diagnosBeskrivning1';
            model.diagnosBeskrivning2 = 'diagnosBeskrivning2';
            model.diagnosBeskrivning3 = 'diagnosBeskrivning3';
            model.diagnosKod = 'diagnosKod';
            model.diagnosKod2 = 'diagnosKod2';
            model.diagnosKod3 = 'diagnosKod3';
            model.diagnosKodsystem1 = 'diagnosKodsystem1';
            model.diagnosKodsystem2 = 'diagnosKodsystem2';
            model.diagnosKodsystem3 = 'diagnosKodsystem3';
            model.samsjuklighet = true;

        });

        it('when avstangningSmittskydd is true', function(){
            // ----- arrange
            // in arrange we setup our spies with expected return values
            viewState.common.doneLoading = true;
            viewState.avstangningSmittskyddValue = false;

            // ----- act
            $scope.$digest(); // register false on avstangningSmittskydd

            viewState.avstangningSmittskyddValue = true;  // this should trigger the watch event
            $scope.$digest();

            // ----- assert
            // expects
            expect(model.atticUpdateForm2).toHaveBeenCalled();
            expect(model.clearForm2).toHaveBeenCalled();

        });

        it('when avstangningSmittskydd is false', function(){
            // ----- arrange
            // in arrange we setup our spies with expected return values
            viewState.common.doneLoading = true;
            viewState.avstangningSmittskyddValue = true;

            // ----- act
            $scope.$digest(); // register false on avstangningSmittskydd

            viewState.avstangningSmittskyddValue = false;  // this should trigger the watch event
            $scope.$digest();

            // ----- assert
            // expects
            expect(model.atticRestoreForm2).toHaveBeenCalled();

        });

        it('can restore from the attic', function(){
            // ----- arrange
            model.diagnosBeskrivning = 'diagnosBeskrivning';
            model.diagnosBeskrivning1 = 'diagnosBeskrivning1';
            model.diagnosBeskrivning2 = 'diagnosBeskrivning2';
            model.diagnosBeskrivning3 = 'diagnosBeskrivning3';
            model.diagnosKod = 'diagnosKod';
            model.diagnosKod2 = 'diagnosKod2';
            model.diagnosKod3 = 'diagnosKod3';
            model.diagnosKodsystem1 = 'ICD_10_SE';
            model.diagnosKodsystem2 = 'ICD_10_SE';
            model.diagnosKodsystem3 = 'ICD_10_SE';
            model.samsjuklighet = true;

            viewState.common.doneLoading = true;

            // ----- act
            $scope.$apply(); // register false on avstangningSmittskydd


            viewState.avstangningSmittskyddValue = true;
            $scope.$apply();

            // ----- assert
            expect(model.diagnosBeskrivning).toBe(undefined);
            expect(model.diagnosBeskrivning1).toBe(undefined);
            expect(model.diagnosBeskrivning2).toBe(undefined);
            expect(model.diagnosBeskrivning3).toBe(undefined);
            expect(model.diagnosKod).toBe(undefined);
            expect(model.diagnosKod2).toBe(undefined);
            expect(model.diagnosKod3).toBe(undefined);
            expect(model.diagnosKodsystem1).toBe(undefined);
            expect(model.diagnosKodsystem2).toBe(undefined);
            expect(model.diagnosKodsystem3).toBe(undefined);
            expect(model.samsjuklighet).toBe(false);

            // ----- act
            viewState.avstangningSmittskyddValue = false;  // this should trigger the watch event
            $scope.$apply();

            // ----- assert
            // expects
            // attic check
            expect(model.diagnosBeskrivning).toBe('diagnosBeskrivning');
            expect(model.diagnosBeskrivning1).toBe('diagnosBeskrivning1');
            expect(model.diagnosBeskrivning2).toBe('diagnosBeskrivning2');
            expect(model.diagnosBeskrivning3).toBe('diagnosBeskrivning3');
            expect(model.diagnosKod).toBe('diagnosKod');
            expect(model.diagnosKod2).toBe('diagnosKod2');
            expect(model.diagnosKod3).toBe('diagnosKod3');
            expect(model.diagnosKodsystem1).toBe('ICD_10_SE');
            expect(model.diagnosKodsystem2).toBe('ICD_10_SE');
            expect(model.diagnosKodsystem3).toBe('ICD_10_SE');
            expect(model.samsjuklighet).toBe(true);

        });

    });

});