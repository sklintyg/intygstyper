define([
    'angular',
    'fk7263/webcert/js/controllers',
    'fk7263/webcert/js/messages',
    'fk7263/webcert/js/services',
    'fk7263/webcert/js/directives'
], function (angular, controllers, messages, services, directives) {
    'use strict';

    var moduleName = 'fk7263';

    var module = angular.module(moduleName, [controllers, services, directives]);

    module.config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
            when('/fk7263/edit/:certificateId', {
                templateUrl : '/web/webjars/fk7263/webcert/views/edit-cert.html',
                controller : 'fk7263.EditCertCtrl'
            }).
            when('/fk7263/view/:certificateId', {
                templateUrl : '/web/webjars/fk7263/common/views/view-cert.html',
                controller : 'fk7263.ViewCertCtrl'
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
