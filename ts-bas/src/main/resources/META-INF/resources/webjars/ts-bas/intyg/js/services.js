define([
    'angular',
    'ts-bas/intyg/js/services/certificateService'
], function(angular, certificateService) {
    'use strict';

    var moduleName = 'ts-bas.services';

    angular.module(moduleName, []).
        factory('ts-bas.certificateService', certificateService);

    return moduleName;
});
