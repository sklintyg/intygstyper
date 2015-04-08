describe('fk7263.EditCertCtrl.Form6a711Ctrl', function() {
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

            $controller('fk7263.EditCert.Form6a711Ctrl' , { $scope: $scope, $log : $log, model : model, viewState : viewState });

        }]));

    describe('#change in avstangningSmittskydd should trigger an update in the attic', function() {
        beforeEach(function(){
            spyOn(model, 'atticUpdateForm6a711').and.callThrough();
            spyOn(model, 'clearForm6a711').and.callThrough();
            spyOn(model, 'atticRestoreForm6a711').and.callThrough();

            model.rekommendationKontaktArbetsformedlingen = 'rekommendationKontaktArbetsformedlingen';
            model.rekommendationKontaktForetagshalsovarden = 'rekommendationKontaktForetagshalsovarden';
            model.rekommendationOvrigt = 'rekommendationOvrigt';
            model.rekommendationOvrigtCheck = 'rekommendationOvrigtCheck';

            model.ressattTillArbeteAktuellt = 'ressattTillArbeteAktuellt';
            model.ressattTillArbeteEjAktuellt = 'ressattTillArbeteEjAktuellt';

            model.rehab = 'rehab';

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
            expect(model.atticUpdateForm6a711).toHaveBeenCalled();
            expect(model.clearForm6a711).toHaveBeenCalled();

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
            expect(model.atticRestoreForm6a711).toHaveBeenCalled();

        });

        it('can restore from the attic', function(){
            // ----- arrange
            viewState.common.doneLoading = true;

            // ----- act
            $scope.$apply(); // register false on avstangningSmittskydd


            viewState.avstangningSmittskyddValue = true;
            $scope.$apply();

            // ----- assert
            expect(model.rekommendationKontaktArbetsformedlingen).toBe(undefined);
            expect(model.rekommendationKontaktForetagshalsovarden).toBe(undefined);
            expect(model.rekommendationOvrigt).toBe(undefined);
            expect(model.rekommendationOvrigtCheck).toBe(undefined);

            expect(model.ressattTillArbeteAktuellt).toBe(undefined);
            expect(model.ressattTillArbeteEjAktuellt).toBe(undefined);

            expect(model.rehab).toBe(undefined);

            // ----- act
            viewState.avstangningSmittskyddValue = false;  // this should trigger the watch event
            $scope.$apply();

            // ----- assert
            // expects
            // restore from attic
            model.rekommendationKontaktArbetsformedlingen = 'rekommendationKontaktArbetsformedlingen';
            model.rekommendationKontaktForetagshalsovarden = 'rekommendationKontaktForetagshalsovarden';
            model.rekommendationOvrigt = 'rekommendationOvrigt';
            model.rekommendationOvrigtCheck = 'rekommendationOvrigtCheck';

            model.ressattTillArbeteAktuellt = 'ressattTillArbeteAktuellt';
            model.ressattTillArbeteEjAktuellt = 'ressattTillArbeteEjAktuellt';

            model.rehab = 'rehab';

        });

    });

});