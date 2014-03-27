define(
		[],
		function() {
			'use strict';

			return [
					'$rootScope',
					function($rootScope) {
						return {
							restrict : "A",
							transclude : true,
							replace : true,
							scope : {
								fieldLabel : "@",
								fieldNumber : "@",
								filled : "=?"
							},
							template : '<div class="body-row clearfix">'
									+ '<h4 class="cert-field-number"><span message key="view.label.field"></span> {{fieldNumber}}</h4>'
									+ '<h3 class="title" ng-class="{ \'unfilled\' : !filled}"><span message key="{{ fieldLabel }}"></span> <span class="cert-field-blank" ng-hide="filled"><span message key="view.label.blank"></span></span></h3>'
									+ '<span class="text" ng-show="filled">' + '<span ng-transclude></span>'
									+ '</span>' + '</div>'

						}
					} ]

		});