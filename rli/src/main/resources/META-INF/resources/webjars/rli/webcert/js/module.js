define([
    'angular',
    'rli/webcert/js/controllers',
    'rli/webcert/js/messages',
    'common/js/webcert/CertificateService',
    'common/js/webcert/ManageCertView',
    'common/js/webcert/eyeDecimal'
], function(angular, controllers, messages, wcCertificateService, wcManageCertView, eyeDecimal) {
    'use strict';

    var moduleName = 'rli';

    var module = angular.module(moduleName, [controllers,  wcCertificateService, wcManageCertView, eyeDecimal]);

    module.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
            when('/rli/edit/:certificateId', {
                templateUrl: '/web/webjars/rli/webcert/views/edit-cert.html',
                controller: 'rli.EditCertCtrl'
            }).
            when('/rli/view/:certificateId', {
                templateUrl: '/web/webjars/rli/common/views/view-cert.html',
                controller: 'rli.ViewCertCtrl'
            });
    }]);

    // Inject language resources
    // TODO: This only works since we always load webcert before the module, when the messageService
    // is moved to a commons project, make sure this is loaded for this module as well.
    module.run(['messageService',
        function(messageService) {
            messageService.addResources(messages);
        }
    ]);

    return moduleName;
});
