describe('fk7263.EditCertCtrl.Form1Ctrl', function() {
    'use strict';

    var $scope;
    var $rootScope;
    var $log;
    var model;
    var viewState;

    // Load the webcert module and mock away everything that is not necessary.
    beforeEach(angular.mock.module('common'));

    beforeEach(angular.mock.module('fk7263', function($provide) {

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

            $controller('fk7263.EditCert.Form1Ctrl' , { $scope: $scope, $log : $log, model : model, viewState : viewState });

        }]));

    describe('#testEvents', function() {
        it('on some event', function(){

            // ----- arrange
            // spies, mocks

            // kick off the window change event
            //$rootScope.$broadcast('fk7263.ViewCertCtrl.load');

            // ------ act
            // promises are resolved/dispatched only on next $digest cycle
            $rootScope.$apply();

            // ------ assert
            // dialog should be opened
            // expects

            expect(true).toBe(true);
        });

    });

    describe('#testScopeFunctions', function() {
        it('some scope function', function(){
            // ----- arrange
            // in arrange we setup our spies with expected return values


            // ----- act
            // call the function

            // ----- assert
            // expects

            expect(true).toBe(true);
        });

    });

});