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
            '$compile', '$templateCache', '$controller', '$rootScope', '$http', '$httpBackend', '$log', 'fk7263.Domain.IntygModel', 'fk7263.EditCertCtrl.ViewStateService',
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

            $scope.form8b.nedsattMed100from.$setViewValue(Date(2015,5,26));
            $scope.$apply();

            // ----- assert
            expect(model.nedsattMed100.from).toBe('2015-06-26');
        });

    });

    describe('#Intygdata gets done loading before controller is initialized', function() {

        beforeEach(angular.mock.inject([
            '$compile', '$templateCache', '$controller', '$rootScope', '$http', '$httpBackend', '$log', 'fk7263.Domain.IntygModel', 'fk7263.EditCertCtrl.ViewStateService',
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

            $scope.form8b.nedsattMed100from.$setViewValue(Date(2015,5,26));
            $scope.$apply();

            // ----- assert
            expect(model.nedsattMed100.from).toBe('2015-06-26');
        });

    });

});