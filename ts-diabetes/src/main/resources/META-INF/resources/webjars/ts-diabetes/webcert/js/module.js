define([
    'angular',
    'webjars/ts-diabetes/webcert/js/messages',
    'webjars/ts-diabetes/webcert/js/controllers/EditCertCtrl',
    'webjars/ts-diabetes/webcert/js/controllers/ViewCertCtrl',
    'webjars/common/webcert/js/directives/wcEyeDecimal',
    'webjars/common/webcert/js/services/messageService'
], function(angular, messages, EditCertCtrl, ViewCertCtrl, wcEyeDecimal, messageService) {
    'use strict';

    var moduleName = 'ts-diabetes';

    var module = angular.module(moduleName, [ EditCertCtrl, ViewCertCtrl, wcEyeDecimal, messageService ]);

    module.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
            when('/ts-diabetes/edit/:certificateId', {
                templateUrl: '/web/webjars/ts-diabetes/webcert/views/edit-cert.html',
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
