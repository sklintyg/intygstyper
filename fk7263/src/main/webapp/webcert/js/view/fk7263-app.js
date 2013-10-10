'use strict';

/* 
 * App Module 
 */
angular.module('FK7263ViewCertApp', [ 'ui.bootstrap', 'wc.fk7263.controllers', 'wc.fk7263.directives', 'wc.fk7263.services', 'wc.fragasvarmodule', 'modules.messages', 'wc.common.directives' ]);
angular.module('FK7263ViewCertApp').config(
        [ '$routeProvider', function($routeProvider) {
            $routeProvider.when('/edit', {
                templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/webcert/views/edit-cert.html',
                controller : 'EditCertCtrl'
            }).when('/view', {
                templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/webcert/views/view-cert.html',
                controller : 'ViewCertCtrl'
            }).otherwise({
                redirectTo : '/view'
            });
        } ]);

angular.module('FK7263ViewCertApp').run([ '$rootScope', 'messageService', function($rootScope, messageService) {
    $rootScope.lang = 'sv';
    $rootScope.DEFAULT_LANG = 'sv';
    $rootScope.MODULE_CONFIG = MODULE_CONFIG;
    // Add WC user context info
    $rootScope.WC_CONTEXT = WC_CONTEXT;
    messageService.addResources(commonMessageResources);
    messageService.addResources(fk7263Messages);
} ]);
