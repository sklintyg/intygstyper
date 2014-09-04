describe('wcDatePickerFieldSpec', function() {
    'use strict';

    beforeEach(angular.mock.module('common', 'wcDatePickerField.html'));

    var $scope, form, template;

    // Create a form to test the validation directive on.
    beforeEach(angular.mock.inject(function($compile, $rootScope, $templateCache) {
        $scope = $rootScope;
        $scope.model = {
            test: null
        };

        //template = $templateCache.get('/web/webjars/common/webcert/js/directives/wcDatePickerField.html');
        //$templateCache.put('/templates/wcDatePickerField.html',template);

        var el = angular.element('<form name="form"><span wc-date-picker-field target-model="model.test" dom-id="test" invalid="false"></span></form>');
        form = $compile(el)($scope);
        $scope.$digest();
    }));

    // valid inputs
    it('should pass with a valid date format', function() {
        $scope.form.test.$setViewValue('2014-01-01');
        expect($scope.model.test).toEqual('2014-01-01');
    });
});
