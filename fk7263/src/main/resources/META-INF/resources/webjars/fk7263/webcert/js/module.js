define([
    'angular',
    'fk7263/webcert/js/controllers',
    'fk7263/webcert/js/messages',
    'fk7263/webcert/js/services',
    'fk7263/webcert/js/directives',
    'common/js/webcert/CertificateService',
    'common/js/webcert/ManageCertView',
    'webjars/common/js/wc-common-fragasvar-module',
    'webjars/common/js/wc-utils'
], function(angular, controllers, messages, services, directives,
            wcCertificateService, wcManageCertView, wcCommonFragaSvarModule, wcUtils) {
    'use strict';

    var moduleName = 'fk7263';

    var module = angular.module(moduleName, [controllers, services, directives,
                                wcCertificateService, wcManageCertView, wcCommonFragaSvarModule, wcUtils]);

    module.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
            when('/fk7263/edit/:certificateId', {
                templateUrl: '/web/webjars/fk7263/webcert/views/edit-cert.html',
                controller: 'fk7263.EditCertCtrl'
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
