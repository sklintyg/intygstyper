describe('fk7263.EditCertCtrl.Form8bCtrl', function() {
    'use strict';

    var $scope;
    var $rootScope;
    var $log;
    var model;
    var viewState;

    beforeEach(angular.mock.module('common', 'fk7263', 'htmlTemplates', function() {
    }));

    describe('#Intygdata gets done loading after controller is initialized', function() {

        beforeEach(angular.mock.inject([
            '$compile',
            '$templateCache',
            '$controller',
            '$rootScope',
            '$http',
            '$httpBackend',
            '$log',
            'fk7263.Domain.IntygModel',
            'fk7263.EditCertCtrl.ViewStateService',
            function( $compile, $templateCache, $controller, _$rootScope_, _$http_, _$httpBackend_, _$log_, _model_, _viewState_) {
                $rootScope = _$rootScope_;
                $scope = $rootScope.$new();
                $log = _$log_;
                var IntygModel = _model_;
                model = new IntygModel();
                viewState = _viewState_;
                viewState.intygModel = model;

                $scope.form8b = {};
                $scope.form8b.nedsattMed100from = { $parsers: [] };
                $scope.form8b.nedsattMed100tom = { $parsers: [] };
                $controller('fk7263.EditCert.Form8bCtrl' , { $scope: $scope, $log : $log, model : model, viewState : viewState });
                $scope.$digest();
                viewState.common.doneLoading = true;
                $scope.$digest();
            }]));

        it('It converts a date object correctly into string representation', function() {
            // ----- act
            $scope.$apply();

            $scope.form8b.nedsattMed100from.$viewValue = new Date(2015,5,26,0,0,0,0);

            // Simulate datepicker parser
            angular.forEach($scope.form8b.nedsattMed100from.$parsers, function(k) {
                $scope.form8b.nedsattMed100from.$modelValue = k($scope.form8b.nedsattMed100from.$viewValue);
            });
            $scope.$apply();

            // ----- assert
            expect(model.nedsattMed100.from).toBe('2015-06-26');
        });
    });

    describe('#Intygdata gets done loading before controller is initialized', function() {

        beforeEach(angular.mock.inject([
            '$compile',
            '$templateCache',
            '$controller',
            '$rootScope',
            '$http',
            '$httpBackend',
            '$log',
            'fk7263.Domain.IntygModel',
            'fk7263.EditCertCtrl.ViewStateService',
            function( $compile, $templateCache, $controller, _$rootScope_, _$http_, _$httpBackend_, _$log_, _model_, _viewState_) {
                $rootScope = _$rootScope_;
                $scope = $rootScope.$new();
                $log = _$log_;
                var IntygModel = _model_;
                model = new IntygModel();
                viewState = _viewState_;
                viewState.intygModel = model;

                $scope.form8b = {};
                $scope.form8b.nedsattMed100from = { $parsers: [] };
                $scope.form8b.nedsattMed100tom = { $parsers: [] };
                viewState.common.doneLoading = true;
                $scope.$digest();
                $controller('fk7263.EditCert.Form8bCtrl' , { $scope: $scope, $log : $log, model : model, viewState : viewState });
                $scope.$digest();
            }]));

        it('It converts a date object correctly into string representation', function() {
            // ----- act
            $scope.$apply();

            $scope.form8b.nedsattMed100from.$viewValue = new Date(2015,5,26,0,0,0,0);

            // Simulate datepicker parser
            angular.forEach($scope.form8b.nedsattMed100from.$parsers, function(k) {
                $scope.form8b.nedsattMed100from.$modelValue = k($scope.form8b.nedsattMed100from.$viewValue);
            });
            $scope.$apply();

            // ----- assert
            expect(model.nedsattMed100.from).toBe('2015-06-26');
        });

    });

});