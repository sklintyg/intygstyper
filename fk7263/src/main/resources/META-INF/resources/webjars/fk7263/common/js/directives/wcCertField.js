define(
		['text!./wcCertField.html'], function(template) {
			'use strict';
			return ['$rootScope',function($rootScope) {
				return {
					restrict : "A",
					transclude : true,
					replace : true,
					template : template,
					scope : {
						fieldLabel : "@",
						fieldNumber : "@",
						filled : "=?"
					}
				};
			}];
		});