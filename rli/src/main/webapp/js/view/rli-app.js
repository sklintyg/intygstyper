'use strict';

/* App Module */
/*
 * Cant seem to inject rootscope in .config, so for routing parameters, we use
 * the global JS config object for now
 */
var RLIApp = angular.module('RLIViewCertApp', [ 'ui.bootstrap', 'services.certService', 'controllers.rli.ViewCertCtrl', 'directives.mi', 'modules.messages' ]).config([ '$routeProvider', function($routeProvider) {
    $routeProvider.when('/view', {
        templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/views/view-cert.html',
        controller : 'ViewCertCtrl'
    }).when('/fel', {
        templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/views/error.html'
    // no Controller needed?
    }).otherwise({
        redirectTo : '/view'
    });
} ]);

RLIApp.run([ '$rootScope', 'messageService', function($rootScope, messageService) {
    $rootScope.lang = 'sv';
    $rootScope.DEFAULT_LANG = 'sv';
    $rootScope.MODULE_CONFIG = MODULE_CONFIG;
    messageService.addResources(commonMessageResources);
    messageService.addResources(rliMessages);

} ]);
