define([
    'angular',
    'text!webjars/common/webcert/js/directives/wcEnableTooltip.html',
    'webjars/common/webcert/js/services/messageService'
], function(angular, template, messageService) {
    'use strict';

    var moduleName = 'wcEnableTooltip';

    /**
     * Enable tooltips for other components than wcFields
     */
    angular.module(moduleName, [ messageService ]).
        directive(moduleName, [ messageService,
            function(messageService) {
                return {
                    restrict: 'A',
                    transclude: true,
                    scope: {
                        fieldHelpText: '@'
                    },
                    controller: function($scope) {
                        $scope.getMessage = function(key) {
                            return messageService.getProperty(key);
                        };
                    },
                    template: template
                };
            }
        ]);

    return moduleName;
});
