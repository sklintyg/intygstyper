define([ 'angular',
         'fk7263/common/js/directives/eyeDecimal', 
         'fk7263/common/js/directives/scrollHereIf',
         'fk7263/common/js/directives/wcDatePickerField',
         'fk7263/common/js/directives/wcCertField'
], function(angular, eyeDecimal, scrollHereIf, wcDatePickerField, wcCertField) {
	'use strict';

	var moduleName = "fk7263.directives";

	angular.module(moduleName, [])
		.directive("fk7263.eyeDecimal", eyeDecimal)
		.directive("fk7263.scrollHereIf", scrollHereIf)
		.directive("fk7263.wcDatePickerField", wcDatePickerField)
		.directive("fk7263.wcCertField", wcCertField);

	return moduleName;
});
