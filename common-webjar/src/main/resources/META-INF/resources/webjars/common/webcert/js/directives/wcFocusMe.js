define([
    'angular'
], function(angular) {
    'use strict';

    var moduleName = 'wcFocusMe';

    /**
     * FocusMe directive. Used to set focus to an element via model value
     */
    angular.module(moduleName, []).
        directive(moduleName, function() {
            return {
                scope: { trigger: '=wcFocusMe' },
                link: function(scope, element) {
                    scope.$watch('trigger', function(value) {
                        if (value === true) {
                            element[0].focus();
                            scope.trigger = false;
                        }
                    });
                }
            };
        });

    return moduleName;
});
