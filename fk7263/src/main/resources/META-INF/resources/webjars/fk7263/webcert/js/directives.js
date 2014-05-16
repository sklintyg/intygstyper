define([
    'angular',
/*    'fk7263/webcert/js/directives/qa',
    'fk7263/webcert/js/directives/view',*/
    'fk7263/common/js/directives/scrollHereIf',
    'fk7263/common/js/directives/wcDatePickerField'
], function(angular, /*qa, view,*/ scrollHereIf, wcDatePickerField) {
    'use strict';

    var moduleName = 'fk7263.directives';

    angular.module(moduleName, []).
/*        directive('qa', qa).
        directive('view', view).*/
        directive('scrollHereIf', scrollHereIf).
        directive('wcDatePickerField', wcDatePickerField);

    return moduleName;
});
