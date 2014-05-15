define([
    'angular',
    'fk7263/common/js/directives/viewFk7263',
    'fk7263/common/js/directives/scrollHereIf',
    'fk7263/common/js/directives/wcDatePickerField'
], function(angular, viewFk7263, scrollHereIf, wcDatePickerField) {
    'use strict';

    var moduleName = 'fk7263.directives';

    angular.module(moduleName, []).
        directive('viewFk7263', viewFk7263).
        directive('scrollHereIf', scrollHereIf).
        directive('wcDatePickerField', wcDatePickerField);

    return moduleName;
});
