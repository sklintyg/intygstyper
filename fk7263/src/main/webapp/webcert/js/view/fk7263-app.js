'use strict';

/* App Module */
/*
 * Cant seem to inject rootscope in .config, so for routing parameters, we use
 * the global JS config object for now
 */
var FK7263App = angular.module('FK7263EditCertApp', [ 'ui.bootstrap', 'controllers.fk7263.webcert', 'modules.messages', 'wc.common.directives' ]).config(
        [ '$routeProvider', function($routeProvider) {
            $routeProvider.when('/edit', {
                templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/webcert/views/edit-cert.html',
                controller : 'EditCertCtrl'
            // no Controller needed?
            }).when('/view', {
              templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/webcert/views/view-cert.html',
              controller : 'ViewCertCtrl'
              	// no Controller needed?
            }).otherwise({
                redirectTo : '/edit'
            });
        } ]);

FK7263App.run([ '$rootScope', 'messageService', function($rootScope, messageService) {
    $rootScope.lang = 'sv';
    $rootScope.DEFAULT_LANG = 'sv';
    $rootScope.MODULE_CONFIG = MODULE_CONFIG;
    messageService.addResources(commonMessageResources);
    messageService.addResources(fk7263Messages);
} ]);
