define([
    'angular',
    'text!webjars/common/webcert/js/directives/wcSpinner.html'
], function(angular, template) {
    'use strict';

    var moduleName = 'wcSpinner';

    angular.module(moduleName, []).
        directive('wcSpinner', function() {
            return {
                restrict: 'A',
                transclude: true,
                replace: true,
                scope: {
                    label: '@',
                    showSpinner: '=',
                    showContent: '='
                },
                template: template
            };
        });

    return moduleName;
});
