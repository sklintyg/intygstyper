define([
    'angular',
    'fk7263/webcert/js/services/fragaSvarService'
], function(angular, fragaSvarService) {
    'use strict';

    var moduleName = 'fk7263.services';

    angular.module(moduleName, []).
        factory('fk7263.fragaSvarService', fragaSvarService);

    return moduleName;
});
