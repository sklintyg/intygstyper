/**
 * Directives used only in module app pages.
 */
angular.module('wc.fk7263.directives', []);

/**
 * Scrolls viewport to attached element if 'scrollHereIf' attribute evaluates as true
 */
angular.module('wc.fk7263.directives').directive('scrollHereIf', function() {
	return function(scope, element, attributes) {
		setTimeout(function() {
			if (scope.$eval(attributes.scrollHereIf)) {
				window.scrollTo(0, element[0].offsetTop - 100)
			}
		});
	}
});

/**
 *  /
 */
angular
		.module('wc.fk7263.directives')
		.directive(
				"wcDatePickerField",
				[
						'$rootScope',
						'$timeout',
						function($rootScope, $timeout) {
							return {
								restrict : "A",
								replace : true,
								scope : {
									targetModel : "=",
									domId : "@",
									onChange : "&"
								},
								controller : function($scope, $element, $attrs) {
									var format = function(date) {
										var dd = date.getDate();
										var mm = date.getMonth() + 1;
										var yyyy = date.getFullYear();
										return '' + yyyy + '-' + (mm <= 9 ? '0' + mm : mm) + '-'
												+ (dd <= 9 ? '0' + dd : dd);
									};
									// Convert javascript Date produced by datepicker to date string "yyyy-MM-dd"
									$scope.$watch("targetModel", function(newValue, oldValue) {
										if (newValue instanceof Date) {
											$scope.targetModel = format(newValue);
										}
									}, false);
									// Expose "now" as a model property for the template to render as todays date
									$scope.isOpen = false;
									$scope.toggleOpen = function() {
										$timeout(function() {
											$scope.isOpen = !$scope.isOpen;
										});
									}

								},
								template : '<span>'
										+ '<input id="{{domId}}" type="text" datepicker-popup ng-model="targetModel" is-open="isOpen" ng-change="onChange()"/>'
										+ '<button id="{{domId}}-toggle" class="btn" ng-click="toggleOpen()" ng-disabled="isOpen"><i class="icon-calendar"></i></button>'
										+ '</span>'
							}
						} ]);

angular
		.module('wc.fk7263.directives')
		.directive(
				"wcCertField",
				[
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
						} ]);