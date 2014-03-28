define([], function() {
	'use strict';
	
	return [ function(scope, element, attributes) {
		setTimeout(function() {
			if (scope.$eval(attributes.scrollHereIf)) {
				window.scrollTo(0, element[0].offsetTop - 100)
			}
		});
	}];
});