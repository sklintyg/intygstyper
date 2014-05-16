define([
    'angular',
    'ts-bas/webcert/js/controllers',
    'ts-bas/webcert/js/messages',
    'common/js/webcert/CertificateService',
    'common/js/webcert/ManageCertView',
    'common/js/webcert/eyeDecimal'
], function(angular, controllers, messages, wcCertificateService, wcManageCertView, eyeDecimal) {
    'use strict';

    var moduleName = 'ts-bas';

    var module = angular.module(moduleName, [controllers, wcCertificateService, wcManageCertView, eyeDecimal]);

    module.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
            when('/ts-bas/edit/:certificateId', {
                templateUrl: '/web/webjars/ts-bas/webcert/views/edit-cert.html',
                controller: 'ts-bas.EditCertCtrl'
            }).
            when('/ts-bas/view/:certificateId', {
                templateUrl: '/web/webjars/ts-bas/common/views/view-cert.html',
                controller: 'ts-bas.ViewCertCtrl'
            });
    }]);

    // Inject language resources
    // NOTE: This only works since we always load webcert before the module, when the messageService
    // is moved to a commons project, make sure this is loaded for this module as well.
    module.run(['messageService',
        function(messageService) {
            messageService.addResources(messages);
        }
    ]);

    return moduleName;
});