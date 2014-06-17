define([
    'angular',
    'text!webjars/common/webcert/js/directives/wcPrintHeader.html',
    'webjars/common/webcert/js/services/User'
], function(angular, template, User) {
    'use strict';

    var moduleName = 'wcPrintHeader';

    angular.module(moduleName, [ User ]).
        directive('wcPrintHeader', [ User,
            function(User) {

                return {
                    restrict: 'A',
                    replace: true,
                    scope: {
                        titleId: '@'
                    },
                    controller: function($scope) {
                        $scope.today = new Date();
                        $scope.user = User;
                    },
                    template: template
                };
            }
        ]);

    return moduleName;
});
