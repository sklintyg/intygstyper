define([
    'angular',
    'webjars/ts-bas/webcert/js/messages',
    'webjars/ts-bas/webcert/js/controllers/EditCertCtrl',
    'webjars/ts-bas/webcert/js/controllers/ViewCertCtrl',
    'webjars/common/webcert/js/directives/wcEyeDecimal',
    'webjars/common/webcert/js/services/messageService',
    'text!webjars/ts-bas/webcert/views/edit-cert.html'
], function(angular, messages, EditCertCtrl, ViewCertCtrl, wcEyeDecimal, messageService, editCertTemplate) {
    'use strict';

    var moduleName = 'ts-bas';

    var module = angular.module(moduleName, [ EditCertCtrl, ViewCertCtrl, wcEyeDecimal, messageService ]);

    module.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
            when('/ts-bas/edit/:certificateId', {
                template: editCertTemplate,
                controller: EditCertCtrl
            });
    }]);

    // Inject language resources
    module.run([ messageService,
        function(messageService) {
            messageService.addResources(messages);
        }
    ]);

    return moduleName;
});
