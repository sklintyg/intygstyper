define([
    'angular',
    'text!webjars/common/webcert/js/directives/wcField.html',
    'webjars/common/webcert/js/services/messageService'
], function(angular, template, messageService) {
    'use strict';

    var moduleName = 'wcField';

    /**
     * wcField directive. Used to abstract common layout for full-layout form fields in cert modules
     */
    angular.module(moduleName, [ messageService ]).
        directive('wcField', [ messageService,
            function(messageService) {
                return {
                    restrict: 'A',
                    transclude: true,
                    replace: true,
                    template: template,
                    scope: {
                        fieldLabel: '@',
                        fieldNumber: '@?',
                        fieldHelpText: '@',
                        fieldHasErrors: '=',
                        fieldTooltipPlacement: '@',
                        filled: '&'
                    },
                    controller: function($scope) {

                        $scope.filled = $scope.$eval($scope.filled);

                        if ($scope.fieldNumber === null) {
                            $scope.fieldNumber = undefined;
                        }

                        if ($scope.fieldTooltipPlacement === undefined) {
                            $scope.placement = 'right';
                        } else {
                            $scope.placement = $scope.fieldTooltipPlacement;
                        }

                        if ($scope.filled === undefined) {
                            $scope.filled = true;
                        }

                        $scope.getMessage = function(key) {
                            return messageService.getProperty(key);
                        };
                    }
                };
            }
        ]);

    return moduleName;
});
