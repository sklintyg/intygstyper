describe('sjukersattning.EditCertCtrl.FormUnderlagCtrl', function() {
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
        'sjukersattning.Domain.IntygModel',  // sjukersattning.Domain.IntygModel
        'sjukersattning.EditCertCtrl.ViewStateService',
        function( $controller, _$rootScope_, _$httpBackend_, _$log_, _model_, _viewState_) {
            $rootScope = _$rootScope_;
            $scope = $rootScope.$new();
            $log = _$log_;
            var IntygModel = _model_;
            model = new IntygModel();
            viewState = _viewState_;
            viewState.intygModel = model;

            $controller('sjukersattning.EditCert.Form2Ctrl' , { $scope: $scope, $log : $log, model : model, viewState : viewState });

        }])
    );

    describe('#intygbaseratpa INIT should contain a default underlag object in initialization', function() {
        it('some scope function', function(){
            // ----- arrange
            var underlag = model.underlag;

            // ----- act
            var testProp = underlag.typ;
            $log.debug('viewModel present?' + JSON.stringify($scope.viewModel));
            // ----- assert
            expect(testProp).toBe(undefined);
        });

    });

    describe('#intygbaseratpa andra medicinska utredningar set', function(){
       it('andra medicinska utredningar should be false by default', function(){
           //Arrange
           var viewModelUnderlag = $scope.viewModel;
              //console.log(test);
           var testProp = viewModelUnderlag.radioMedicalChecked;

           // Assert
           expect(testProp).toBe(false);

       });

    });



});