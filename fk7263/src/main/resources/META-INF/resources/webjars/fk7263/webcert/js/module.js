define([
    'angular',
    'webjars/fk7263/webcert/js/messages',
    'webjars/fk7263/webcert/js/controllers/EditCertCtrl',
    'webjars/fk7263/webcert/js/controllers/QACtrl',
    'webjars/fk7263/webcert/js/controllers/ViewCertCtrl',
    'webjars/fk7263/webcert/js/directives/wcDatePickerField',
    'webjars/common/webcert/js/services/messageService'
], function(angular, messages, EditCertCtrl, QACtrl, ViewCertCtrl, wcDatePickerField, messageService) {
    'use strict';

    var moduleName = 'fk7263';

    var module = angular.module(moduleName, [ EditCertCtrl, QACtrl, ViewCertCtrl, wcDatePickerField, messageService ]);

    module.config([ '$routeProvider', function($routeProvider) {
        $routeProvider.
            when('/fk7263/edit/:certificateId', {
                templateUrl: '/web/webjars/fk7263/webcert/views/edit-cert.html',
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
