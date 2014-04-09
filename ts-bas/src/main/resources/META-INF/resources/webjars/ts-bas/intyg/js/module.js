define([
    'angular',
    'ts-bas/intyg/js/controllers',
    'ts-bas/intyg/js/messages',
    'ts-bas/intyg/js/services',
], function (angular, controllers, messages, services) {
    'use strict';
    
    var moduleName = 'ts-bas';

    var module = angular.module(moduleName, [controllers, services]);

    module.config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
            when('/ts-bas/view/:certificateId', {
                templateUrl : '/web/webjars/ts-bas/intyg/views/view-cert.html',
                controller : 'ViewCertCtrl'
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
