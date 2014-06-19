define([
    'angular',
    'text!webjars/fk7263/webcert/js/directives/wcDatePickerField.html'
], function(angular, template) {
    'use strict';

    var moduleName = 'wcDatePickerField';

    angular.module(moduleName, []).
        directive(moduleName, [ '$rootScope', '$timeout',
            function($rootScope, $timeout) {
                return {
                    restrict: 'A',
                    replace: true,
                    scope: {
                        targetModel: '=',
                        domId: '@',
                        onChange: '&'
                    },
                    template: template,

                    controller: function($scope) {
                        var format = function(date) {
                            var dd = date.getDate();
                            var mm = date.getMonth() + 1;
                            var yyyy = date.getFullYear();
                            return '' + yyyy + '-' + (mm <= 9 ? '0' + mm : mm) + '-' + (dd <= 9 ? '0' + dd : dd);
                        };
                        // Convert javascript Date produced by datepicker to date string 'yyyy-MM-dd'
                        $scope.$watch('targetModel', function(newValue) {
                            if (newValue instanceof Date) {
                                $scope.targetModel = format(newValue);
                            }
                            $scope.onChange();
                        }, false);

                        $scope.isOpen = false;
                        $scope.toggleOpen = function() {
                            $timeout(function() {
                                $scope.isOpen = !$scope.isOpen;
                            });
                        };
                    }
                };
            }
        ]);

    return moduleName;
});
