define(['text!./wcDatePickerField.html'], function(template) {
    'use strict';

    return [ '$rootScope', '$timeout',
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
                    }, false);
                    // Expose 'now' as a model property for the template to render as todays date
                    $scope.isOpen = false;
                    $scope.toggleOpen = function() {
                        $timeout(function() {
                            $scope.isOpen = !$scope.isOpen;
                        });
                    };
                }
            };
        }
    ];
});
