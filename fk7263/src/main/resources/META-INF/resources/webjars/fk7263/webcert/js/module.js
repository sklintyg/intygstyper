define([
    'angular',
    'webjars/fk7263/webcert/js/messages',
    'webjars/fk7263/webcert/js/controllers/EditCertCtrl',
    'webjars/fk7263/webcert/js/controllers/QACtrl',
    'webjars/fk7263/webcert/js/controllers/ViewCertCtrl',
    'webjars/fk7263/webcert/js/directives/wcDatePickerField',
    'webjars/common/webcert/js/services/messageService',
    'webjars/common/webcert/js/directives',
    'text!webjars/fk7263/webcert/views/edit-cert.html'
], function(angular, messages, EditCertCtrl, QACtrl, ViewCertCtrl, wcDatePickerField, messageService, directives, editCertTemplate) {
    'use strict';

    var moduleName = 'fk7263';

    var module = angular.module(moduleName, [ EditCertCtrl, QACtrl, ViewCertCtrl, wcDatePickerField, messageService, directives]);

    module.config([ '$routeProvider', function($routeProvider) {
        $routeProvider.
            when('/fk7263/edit/:certificateId', {
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
