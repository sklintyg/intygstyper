/**
 * wcField directive. Used to abstract common layout for full-layout form fields in cert modules
 */
angular.module('common').directive('wcField',
    [ 'common.messageService',
        function(messageService) {
            'use strict';

            return {
                restrict: 'A',
                transclude: true,
                replace: true,
                templateUrl: '/web/webjars/common/webcert/js/directives/wcField.html',
                scope: {
                    fieldLabel: '@',
                    fieldNumber: '@?',
                    fieldHelpText: '@',
                    fieldHasErrors: '=',
                    fieldTooltipPlacement: '@',
                    filled: '=?'
                },
                controller: function($scope) {

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
        }]);
