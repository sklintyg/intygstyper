'use strict';

/* App Module */
/*
 * Cant seem to inject rootscope in .config, so for routing parameters, we use
 * the global JS config object for now
 */
var FK7263App = angular.module('FK7263ViewCertApp', [ 'ui.bootstrap', 'services.certService', 'controllers.fk7263.ViewCertCtrl', 'directives.mi', 'modules.messages' ]).config(
        [ '$routeProvider', function($routeProvider) {
            $routeProvider.when('/view', {
                templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/views/view-cert.html',
                controller : 'ViewCertCtrl'
            }).when('/recipients', {
               templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/views/recipients.html',
               controller : 'RecipientCertCtrl'
            }).when('/summary', {
               templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/views/send-summary.html',
               controller : 'SummaryCertCtrl'
            }).when('/sent', {
               templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/views/sent-cert.html',
               controller : 'SentCertCtrl'
            }).when('/fel', {
               templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/views/error.html'
            // no Controller needed?
            }).otherwise({
                redirectTo : '/view'
            });
        } ]);

FK7263App.run([ '$rootScope', 'messageService', function($rootScope, messageService) {
    $rootScope.lang = 'sv';
    $rootScope.DEFAULT_LANG = 'sv';
    $rootScope.MODULE_CONFIG = MODULE_CONFIG;
    messageService.addResources(fk7263Messages);
} ]);
