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

            $controller('fk7263.EditCert.Form6a711Ctrl' , { $scope: $scope, $log : $log, model : model, viewState : viewState });

        }]));

    describe('#change in avstangningSmittskydd should trigger an update in the attic', function() {
        beforeEach(function(){

            model.rekommendationKontaktArbetsformedlingen = 'rekommendationKontaktArbetsformedlingen';
            model.rekommendationKontaktForetagshalsovarden = 'rekommendationKontaktForetagshalsovarden';
            model.rekommendationOvrigt = 'rekommendationOvrigt';
            model.rekommendationOvrigtCheck = 'rekommendationOvrigtCheck';

            model.ressattTillArbeteAktuellt = 'ressattTillArbeteAktuellt';
            model.ressattTillArbeteEjAktuellt = 'ressattTillArbeteEjAktuellt';

            model.rehabilitering = 'rehabilitering';

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

            expect(model.rehabilitering).toBe(undefined);

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

            model.rehabilitering = 'rehabilitering';

        });

    });

});