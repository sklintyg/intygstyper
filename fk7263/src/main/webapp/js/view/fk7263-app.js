'use strict';

/* App Module */
/*
 * Cant seem to inject rootscope in .config, so for routing parameters, we use
 * the global JS config object for now
 */
var FK7263App = angular.module('FK7263ViewCertApp', [ 'ui.bootstrap', 'services.certService', 'controllers.fk7263', 'directives.mi', 'modules.messages', 'services.util' ]).config(
        [ '$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
            $routeProvider.when('/view', {
                templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/views/view-cert.html',
                controller : 'ViewCertCtrl'
            // }).when('/recipients', {
            // templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH +
            // '/views/recipients.html',
            // controller : 'SentCertWizardCtrl'
            }).when('/statushistory', {
                templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/views/status-history.html',
                controller : 'ViewCertCtrl'
            }).when('/summary', {
                templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/views/send-summary.html',
                controller : 'SentCertWizardCtrl'
            }).when('/sent', {
                templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/views/sent-cert.html',
                controller : 'SentCertWizardCtrl'
            }).when('/fel/:errorCode', {
                templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/views/error.html',
                controller : 'ErrorCtrl'
            }).otherwise({
                redirectTo : '/view'
            });

            $httpProvider.interceptors.push('httpRequestInterceptorCacheBuster');
        } ]);

FK7263App.run([ '$rootScope', 'messageService', function($rootScope, messageService) {
    $rootScope.lang = 'sv';
    $rootScope.DEFAULT_LANG = 'sv';
    $rootScope.MODULE_CONFIG = MODULE_CONFIG;
    messageService.addResources(commonMessageResources);
    messageService.addResources(fk7263Messages);
} ]);
