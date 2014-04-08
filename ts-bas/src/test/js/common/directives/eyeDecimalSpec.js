'use strict';
define(
		[ 'angular', 'angularMocks', 'ts-bas/webcert/js/directives' ],
		function(angular, mocks, directives) {

			describe(
					'eyeSpec',
					function() {

						beforeEach(mocks.module('ts-bas.directives'));

						var $scope, form;

						// Create a form to test the validation directive on.
						beforeEach(mocks.inject(function($compile, $rootScope) {
							$scope = $rootScope;
							$scope.model = {
								test : null
							};

							var el = angular
									.element('<form name="form"><input id="test" name="test" type="text" class="small-decimal" ts-eye-decimal ng-model="model.test"></form>');
							form = $compile(el)($scope);
							$scope.$digest();
						}));

						var blur = function() {
							form.find("test").triggerHandler('blur');
						}

						// valid inputs
						it('should pass with a valid decimal number with format "n,n"', function() {
							$scope.form.test.$setViewValue('1');
							expect($scope.model.test).toEqual(1.0);
						});
						it('should pass with a valid decimal number with format "n,"', function() {
							$scope.form.test.$setViewValue("1,");
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
		});
