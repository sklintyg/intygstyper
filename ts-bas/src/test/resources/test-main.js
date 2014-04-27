var tests = [];
for ( var file in window.__karma__.files) {
	if (window.__karma__.files.hasOwnProperty(file)) {
		if (/Spec\.js$/.test(file)) {
			tests.push(file);
		}
	}
}

var WEBJARS = '/base/target/webjardependencies/';
var TS_BAS = '/base/ts-bas/src/main/resources/META-INF/resources/webjars/ts-bas';

require.config({
	paths : {

		angular : WEBJARS + 'angularjs/angular',
		angularScenario : WEBJARS + 'angularjs/angular-scenario',
		angularMocks : WEBJARS + 'angularjs/angular-mocks',
		angularCookies : WEBJARS + 'angularjs/angular-cookies',
		angularRoute : WEBJARS + 'angularjs/angular-route.min',
		angularSanitize : WEBJARS + 'angularjs/angular-sanitize.min',
		angularSwedish : WEBJARS + 'angularjs/1.2.14/angular-locale_sv-se',
		angularUiBootstrap : WEBJARS + 'angular-ui-bootstrap/ui-bootstrap-tpls',
		text : WEBJARS + 'requirejs-text/text',

		'ts-bas' : TS_BAS
	},
	deps : tests,

	shim : {
		'angular' : {
			'exports' : 'angular'
		},
		'angularScenario' : [ 'angular' ],
		'angularMocks' : {
			deps : [ 'angular' ],
			exports : 'angular.mock'
		},
		'angularCookies' : [ 'angular' ],
		'angularRoute' : [ 'angular' ],
		'angularSanitize' : [ 'angular' ],
		'angularSwedish' : [ 'angular' ],
		'angularUiBootstrap' : [ 'angular' ]
	},
	priority : [ 'angular' ],
	callback : window.__karma__.start
});

require([ 'angular', 'angularMocks', 'angularScenario', 'angularRoute', 'angularSanitize', 'angularCookies',
		'angularSwedish', 'angularUiBootstrap' ], function(angular, mocks) {
	'use strict';
});
