define([
    'angular',
    'fk7263/common/js/directives/scrollHereIf',
    'fk7263/common/js/directives/wcDatePickerField',
    'fk7263/common/js/directives/wcCertField'
], function(angular, scrollHereIf, wcDatePickerField, wcCertField) {
    'use strict';

    var moduleName = 'fk7263.directives';

    angular.module(moduleName, []).
        directive('scrollHereIf', scrollHereIf).
        directive('wcDatePickerField', wcDatePickerField).
        directive('wcCertField', wcCertField);

    return moduleName;
});
