'use strict';

/* 
 * App Module 
 */
angular.module('FK7263ViewCertApp', [ 'ui.bootstrap', 'controllers.fk7263.webcert', 'services.fk7263.wc', 'modules.messages', 'wc.common.directives' ]);
angular.module('FK7263ViewCertApp').config(
        [ '$routeProvider', function($routeProvider) {
            $routeProvider.when('/edit', {
                templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/webcert/views/edit-cert.html',
                controller : 'EditCertCtrl'
            // no Controller needed?
            }).when('/view', {
                templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/webcert/views/view-cert.html',
                controller : 'ViewCertCtrl'
            // no Controller needed?
            }).when('/view-mock', {
              templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/webcert/views/view-cert-mock.html',
              controller : 'ViewCertCtrl'
          // no Controller needed?
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
