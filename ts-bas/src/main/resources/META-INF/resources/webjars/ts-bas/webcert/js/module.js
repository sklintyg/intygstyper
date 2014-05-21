define([
    'angular',
    'ts-bas/webcert/js/controllers',
    'ts-bas/webcert/js/messages',
    'common/js/webcert/CertificateService',
    'common/js/webcert/ManageCertView',
    'common/js/webcert/eyeDecimal',
    'common/js/filters'
], function(angular, controllers, messages, wcCertificateService, wcManageCertView, eyeDecimal, filters) {
    'use strict';

    var moduleName = 'ts-bas';

    var module = angular.module(moduleName, [controllers, wcCertificateService, wcManageCertView, eyeDecimal, filters]);

    module.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
            when('/ts-bas/edit/:certificateId', {
                templateUrl: '/web/webjars/ts-bas/webcert/views/edit-cert.html',
                controller: 'ts-bas.EditCertCtrl'
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
