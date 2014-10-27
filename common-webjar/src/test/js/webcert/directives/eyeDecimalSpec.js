describe('eyeSpec', function() {
    'use strict';

    beforeEach(angular.mock.module('common'));

    var $scope, form;

    // Create a form to test the validation directive on.
    beforeEach(angular.mock.inject(function($compile, $rootScope) {
        $scope = $rootScope;
        $scope.model = {
            test: null
        };

        var el = angular
            .element('<form name="form"><input id="test" name="test" type="text" class="small-decimal" ' +
                'wc-decimal-number wc-decimal-max-numbers="2" ng-model="model.test"></form>');
        form = $compile(el)($scope);
        $scope.$digest();
    }));

    // valid inputs
    it('should pass with a valid decimal number with format "n,n"', function() {
        $scope.form.test.$setViewValue('1');
        expect($scope.model.test).toEqual(1.0);
    });
    it('should pass with a valid decimal number with format "n,"', function() {
        $scope.form.test.$setViewValue('1,');
        expect($scope.model.test).toEqual(1.0);
    });
    it('should pass with a valid decimal number with format "n."', function() {
        $scope.form.test.$setViewValue('1.');
        expect($scope.model.test).toEqual(1.0);
    });
    it('should pass with a valid decimal number with format ","', function() {
        $scope.form.test.$setViewValue(',');
        expect($scope.model.test).toEqual(0.0);
    });
    it('should pass with a valid decimal number with format ",1"', function() {
        $scope.form.test.$setViewValue(',1');
        expect($scope.model.test).toEqual(0.1);
    });
    it('should pass with a valid decimal number with format "145"', function() {
        $scope.form.test.$setViewValue('145');
        expect($scope.model.test).toEqual(1.4);
    });

    // invalid formats resulting in null
    it('should pass with a valid decimal number with format ""', function() {
        $scope.form.test.$setViewValue('');
        expect($scope.model.test).toEqual(null);
    });
    it('should pass with a valid decimal number with format "asdf"', function() {
        $scope.form.test.$setViewValue('asdf');
        expect($scope.model.test).toEqual(null);
    });
});
