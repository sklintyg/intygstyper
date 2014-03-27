define(
		[],
		function() {
			'use strict';

			return [ function() {
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
					} ]

		});