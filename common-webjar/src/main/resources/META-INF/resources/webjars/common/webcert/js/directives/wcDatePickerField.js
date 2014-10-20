angular.module('common').directive('wcDatePickerField',
    function($rootScope, $timeout) {
        'use strict';

        return {
            restrict: 'A',
            replace: true,
            scope: {
                targetModel: '=',
                domId: '@',
                invalid: '=',
                onChange: '&'
            },
            templateUrl: '/web/webjars/common/webcert/js/directives/wcDatePickerField.html',

            link: function(scope, element, attrs, ctrl) {
                ctrl.$parsers.unshift(function() { // hidden arg: viewValue
                    // fire onChange whenever viewValue changes
                    if (scope.onChange) {
                        scope.onChange();
                    }
                });
            },
            controller: function(scope) {
                scope.isOpen = false;
                scope.toggleOpen = function($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    $timeout(function() {
                        scope.isOpen = !scope.isOpen;
                    });
                };
            }
        };
    });
