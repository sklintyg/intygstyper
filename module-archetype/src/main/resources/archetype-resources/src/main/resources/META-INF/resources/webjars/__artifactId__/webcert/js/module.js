#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
define([
    'angular',
    '${artifactId}/webcert/js/controllers',
    '${artifactId}/webcert/js/messages',
    '${artifactId}/webcert/js/services',
    '${artifactId}/webcert/js/directives'
], function (angular, controllers, messages, services, directives) {
    'use strict';

    var moduleName = '${artifactId}';

    var module = angular.module(moduleName, [controllers, services, directives]);

    module.config(['${symbol_dollar}routeProvider', function (${symbol_dollar}routeProvider) {
        ${symbol_dollar}routeProvider.
            when('/${artifactId}/edit/:certificateId', {
                templateUrl : '/web/webjars/${artifactId}/webcert/views/edit-cert.html',
                controller : '${artifactId}.EditCertCtrl'
            }).
            when('/${artifactId}/view/:certificateId', {
                templateUrl : '/web/webjars/${artifactId}/common/views/view-cert.html',
                controller : '${artifactId}.ViewCertCtrl'
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
