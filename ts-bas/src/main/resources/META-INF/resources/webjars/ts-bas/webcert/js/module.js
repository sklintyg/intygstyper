define([
    'angular',
    'ts-bas/webcert/js/controllers',
    'ts-bas/webcert/js/messages',
    'ts-bas/webcert/js/services',
    'ts-bas/webcert/js/directives'
], function (angular, controllers, messages, services, directives) {
    'use strict';

    var moduleName = 'ts-bas';

    var module = angular.module(moduleName, [controllers, services, directives]);

    module.config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
            when('/ts-bas/edit/:certificateId', {
                templateUrl : '/web/webjars/ts-bas/webcert/views/edit-cert.html',
                controller : 'ts-bas.EditCertCtrl'
            }).
            when('/ts-bas/view/:certificateId', {
                templateUrl : '/web/webjars/ts-bas/common/views/view-cert.html',
                controller : 'ts-bas.ViewCertCtrl'
            });
    }]);

    // Inject language resources
    // TODO: This only works since we always load webcert before the module, when the messageService
    // is moved to a commons project, make sure this is loaded for this module as well.
    module.run(['messageService',
        function (messageService) {
            messageService.addResources(messages);
        }]);

    return moduleName;
});
