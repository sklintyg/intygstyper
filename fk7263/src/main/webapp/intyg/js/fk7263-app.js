'use strict';

/* App Module */
/*
 * Cant seem to inject rootscope in .config, so for routing parameters, we use
 * the global JS config object for now
 */
var FK7263App = angular.module('FK7263ViewCertApp', [ 'ui.bootstrap', 'services.certService', 'controllers.fk7263', 'directives.mi', 'modules.messages', 'services.util' ]).config(
        [ '$routeProvider', '$httpProvider', 'http403ResponseInterceptorProvider', function($routeProvider, $httpProvider, http403ResponseInterceptorProvider) {
            $routeProvider.when('/view', {
                templateUrl : MODULE_CONFIG.PROXY_PREFIX + '/intyg/views/view-cert.html',
                controller : 'ViewCertCtrl',
	            title : 'Läkarintyg FK7263'
            // }).when('/recipients', {
            // templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH +
            // '/views/recipients.html',
            // controller : 'SentCertWizardCtrl'
            }).when('/statushistory', {
                templateUrl : MODULE_CONFIG.PROXY_PREFIX + '/intyg/views/status-history.html',
                controller : 'ViewCertCtrl',
			    title : 'Alla intygets händelser'
            }).when('/summary', {
                templateUrl : MODULE_CONFIG.PROXY_PREFIX + '/intyg/views/send-summary.html',
                controller : 'SentCertWizardCtrl',
			    title : 'Kontrollera och skicka intyget'
            }).when('/sent', {
                templateUrl : MODULE_CONFIG.PROXY_PREFIX + '/intyg/views/sent-cert.html',
                controller : 'SentCertWizardCtrl',
	            title : 'Intyget har skickats'
            }).when('/fel/:errorCode', {
                templateUrl : MODULE_CONFIG.PROXY_PREFIX + '/intyg/views/error.html',
                controller : 'ErrorCtrl',
	            title : 'Fel'
            }).otherwise({
                redirectTo : '/view'
            });
            
            //Configure interceptor provider
            http403ResponseInterceptorProvider.setRedirectUrl("/web/start");
            
            $httpProvider.interceptors.push('httpRequestInterceptorCacheBuster');
            $httpProvider.responseInterceptors.push('http403ResponseInterceptor');
        } ]);

FK7263App.run([ '$rootScope', '$route','messageService', function($rootScope, $route, messageService) {
    $rootScope.lang = 'sv';
    $rootScope.DEFAULT_LANG = 'sv';
    $rootScope.MODULE_CONFIG = MODULE_CONFIG;
    messageService.addResources(commonMessageResources);
    messageService.addResources(fk7263Messages);

	// Update page title
	$rootScope.page_title = 'Titel';
    $rootScope.$on('$routeChangeSuccess', function() {
        //Seems like this is also called when redirecting with a 
        //partially populated $route.current without the $$route part 
        if ($route.current.$$route){
            $rootScope.page_title = $route.current.$$route.title + ' | Mina intyg';
        }
    });
} ]);
