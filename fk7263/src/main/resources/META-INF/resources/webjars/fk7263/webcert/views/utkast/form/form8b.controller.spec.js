/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

describe('fk7263.EditCertCtrl.Form8bCtrl', function() {
    'use strict';

    var $scope;
    var $rootScope;
    var $log;
    var model;
    var viewState;

    beforeEach(angular.mock.module('common', 'fk7263', 'htmlTemplates', function($provide) {
        // Decorators that update form input names and interpolates them. Needed for datepicker directives templates dynamic name attributes
        $provide.decorator('ngModelDirective', function($delegate) {
            var ngModel = $delegate[0], controller = ngModel.controller;
            ngModel.controller = ['$scope', '$element', '$attrs', '$injector', function(scope, element, attrs, $injector) {
                var $interpolate = $injector.get('$interpolate');
                attrs.$set('name', $interpolate(attrs.name || '')(scope));
                $injector.invoke(controller, this, {
                    '$scope': scope,
                    '$element': element,
                    '$attrs': attrs
                });
            }];
            return $delegate;
        });
        $provide.decorator('formDirective', function($delegate) {
            var form = $delegate[0], controller = form.controller;
            form.controller = ['$scope', '$element', '$attrs', '$injector', function(scope, element, attrs, $injector) {
                var $interpolate = $injector.get('$interpolate');
                attrs.$set('name', $interpolate(attrs.name || attrs.ngForm || '')(scope));
                $injector.invoke(controller, this, {
                    '$scope': scope,
                    '$element': element,
                    '$attrs': attrs
                });
            }];
            return $delegate;
        });
    }));

    describe('#Intygdata gets done loading after controller is initialized', function() {

        beforeEach(angular.mock.inject([
            '$compile', '$templateCache', '$controller', '$rootScope', '$http', '$httpBackend', '$log',
            'fk7263.Domain.IntygModel', 'fk7263.EditCertCtrl.ViewStateService',
            function( $compile, $templateCache, $controller, _$rootScope_, _$http_, _$httpBackend_, _$log_, _model_, _viewState_) {
                $rootScope = _$rootScope_;
                $scope = $rootScope.$new();
                $log = _$log_;
                var IntygModel = _model_;
                model = new IntygModel();
                viewState = _viewState_;
                viewState.intygModel = model;

                var viewHtml = $templateCache.get('/web/webjars/fk7263/webcert/views/utkast/form/form8b.html');
                var element = $compile(viewHtml)($scope);
                $controller('fk7263.EditCert.Form8bCtrl',  {
                    $scope: $scope,
                    $log : $log,
                    model : model,
                    viewState : viewState,
                    $element: element
                });
                $scope.$digest();

                viewState.common.doneLoading = true;
                $scope.$digest();
            }]));

        it('It converts a date object correctly into string representation', function() {
            // ----- act
            $scope.$apply();

            $scope.form8b.nedsattMed100from.$setViewValue(new Date(2015,5,26,0,0,0,0));
            $scope.$apply();

            // ----- assert
            expect(model.nedsattMed100.from).toBe('2015-06-26');
        });

    });

    describe('#Verify tom-date command sequence', function() {

        beforeEach(angular.mock.inject([
            '$compile', '$templateCache', '$controller', '$rootScope', '$http', '$httpBackend', '$log',
            'fk7263.Domain.IntygModel', 'fk7263.EditCertCtrl.ViewStateService',
            function( $compile, $templateCache, $controller, _$rootScope_, _$http_, _$httpBackend_, _$log_, _model_, _viewState_) {
                $rootScope = _$rootScope_;
                $scope = $rootScope.$new();
                $log = _$log_;
                var IntygModel = _model_;
                model = new IntygModel();
                viewState = _viewState_;
                viewState.intygModel = model;

                var viewHtml = $templateCache.get('/web/webjars/fk7263/webcert/views/utkast/form/form8b.html');
                var element = $compile(viewHtml)($scope);
                $controller('fk7263.EditCert.Form8bCtrl',  {
                    $scope: $scope,
                    $log : $log,
                    model : model,
                    viewState : viewState,
                    $element: element
                });
                $scope.$digest();

                viewState.common.doneLoading = true;
                $scope.$digest();
            }]));

        it('converts a tom-date code correctly into a future date string representation', function() {
            // ----- act
            $scope.$apply();

            $scope.form8b.nedsattMed100from.$setViewValue(new Date(2015,5,16,0,0,0,0));
            $scope.$apply();
            // ----- assert
            expect(model.nedsattMed100.from).toBe('2015-06-16');

            $scope.form8b.nedsattMed100tom.$setViewValue('d10');
            $scope.onToFieldBlur($scope.field8b.nedsattMed100);
            $scope.$apply();

            expect(model.nedsattMed100.tom).toBe('2015-06-25');
        });

        it('converts a tom-date code correctly into a future date string representation plus weeks', function() {
            // ----- act
            $scope.$apply();

            $scope.form8b.nedsattMed100from.$setViewValue(new Date(2015,5,16,0,0,0,0));
            $scope.$apply();
            // ----- assert
            expect(model.nedsattMed100.from).toBe('2015-06-16');

            $scope.form8b.nedsattMed100tom.$setViewValue('v6');
            $scope.onToFieldBlur($scope.field8b.nedsattMed100);
            $scope.$apply();

            expect(model.nedsattMed100.tom).toBe('2015-07-27');
        });

        it('converts a tom-date code correctly into a future date string representation plus months', function() {
            // ----- act
            $scope.$apply();

            $scope.form8b.nedsattMed100from.$setViewValue(new Date(2015,5,16,0,0,0,0));
            $scope.$apply();
            // ----- assert
            expect(model.nedsattMed100.from).toBe('2015-06-16');

            $scope.form8b.nedsattMed100tom.$setViewValue('m6');
            $scope.onToFieldBlur($scope.field8b.nedsattMed100);
            $scope.$apply();

            expect(model.nedsattMed100.tom).toBe('2015-12-16');
        });

    });

    describe('#Intygdata gets done loading before controller is initialized', function() {

        beforeEach(angular.mock.inject([
            '$compile', '$templateCache', '$controller', '$rootScope', '$http', '$httpBackend', '$log',
            'fk7263.Domain.IntygModel', 'fk7263.EditCertCtrl.ViewStateService',
            function( $compile, $templateCache, $controller, _$rootScope_, _$http_, _$httpBackend_, _$log_, _model_, _viewState_) {
                $rootScope = _$rootScope_;
                $scope = $rootScope.$new();
                $log = _$log_;
                var IntygModel = _model_;
                model = new IntygModel();
                viewState = _viewState_;
                viewState.intygModel = model;

                viewState.common.doneLoading = true;
                $scope.$digest();

                var viewHtml = $templateCache.get('/web/webjars/fk7263/webcert/views/utkast/form/form8b.html');
                var element = $compile(viewHtml)($scope);
                $controller('fk7263.EditCert.Form8bCtrl',  {
                    $scope: $scope,
                    $log : $log,
                    model : model,
                    viewState : viewState,
                    $element: element
                });
                $scope.$digest();
            }]));

        it('It converts a date object correctly into string representation', function() {
            // ----- act
            $scope.$apply();

            $scope.form8b.nedsattMed100from.$setViewValue(new Date(2015,5,26,0,0,0,0));
            $scope.$apply();

            // ----- assert
            expect(model.nedsattMed100.from).toBe('2015-06-26');
        });

    });

});
