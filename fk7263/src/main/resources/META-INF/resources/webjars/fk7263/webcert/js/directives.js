define([
    'angular',
    'fk7263/common/js/directives/scrollHereIf',
    'fk7263/common/js/directives/wcDatePickerField'
], function(angular, scrollHereIf, wcDatePickerField) {
    'use strict';

    var moduleName = 'fk7263.directives';

    angular.module(moduleName, []).
        directive('scrollHereIf', scrollHereIf).
        directive('wcDatePickerField', wcDatePickerField);

    return moduleName;
});
