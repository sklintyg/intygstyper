define([
    'angular'
], function(angular) {
    'use strict';

    var moduleName = 'wcFieldSingle';

    /**
     * wcFieldSingle directive. Used to abstract common layout for single-line form fields in cert modules
     */
    angular.module(moduleName, []).
        directive('wcFieldSingle', function() {
            return {
                restrict: 'A',
                transclude: true,
                replace: true,
                scope: {
                    fieldNumber: '@'
                },
                template: '<div class="body-row body-row-single clearfix">' +
                    '<h4 class="cert-field-number" ng-if="fieldNumber != undefined">' +
                    '<span message key="modules.label.field"></span> {{fieldNumber}}</h4>' +
                    '<span ng-transclude></span>' +
                    '</div>'
            };
        });

    return moduleName;
});
