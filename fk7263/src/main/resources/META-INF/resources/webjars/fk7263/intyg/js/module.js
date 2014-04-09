define([
        'angular',
        'fk7263/intyg/js/controllers',
        'fk7263/intyg/js/messages',
        'fk7263/intyg/js/services'
    ], function (angular, controllers, messages, services) {
        'use strict';

        var moduleName = 'fk7263';

        var module = angular.module(moduleName, [controllers, services]);

        module.config(['$routeProvider', function ($routeProvider) {
        	 $routeProvider.when('/fk7263/view/:certificateId', {
                 templateUrl : '/web/webjars/fk7263/intyg/views/view-cert.html',
                 controller : 'ViewCertCtrl',
 	            title : 'Läkarintyg FK7263'
             // }).when('/recipients', {
             // templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH +
             // '/views/recipients.html',
             // controller : 'SentCertWizardCtrl'
             }).when('/fk7263/statushistory', {
                 templateUrl : '/web/webjars/fk7263/intyg/views/status-history.html',
                 controller : 'ViewCertCtrl',
 			    title : 'Alla intygets händelser'
             }).when('/fk7263/summary', {
                 templateUrl : '/web/webjars/fk7263/intyg/views/send-summary.html',
                 controller : 'SentCertWizardCtrl',
 			    title : 'Kontrollera och skicka intyget'
             }).when('/fk7263/sent', {
                 templateUrl : '/web/webjars/fk7263/intyg/views/sent-cert.html',
                 controller : 'SentCertWizardCtrl',
 	            title : 'Intyget har skickats'
             }).when('/fk7263/fel/:errorCode', {
                 templateUrl : '/web/webjars/fk7263/intyg/views/error.html',
                 controller : 'ErrorCtrl',
 	            title : 'Fel'
             }).when('/fk7263/visafel/:errorCode', {
                 templateUrl : '/web/webjars/fk7263/intyg/views/error.html',
                 controller : 'ErrorCtrl',
                 title : 'Fel',
                 backLink: '/web/start'
             });
             
             //Configure interceptor provider
//             http403ResponseInterceptorProvider.setRedirectUrl("/web/start");
//             
//             $httpProvider.interceptors.push('httpRequestInterceptorCacheBuster');
//             $httpProvider.responseInterceptors.push('http403ResponseInterceptor');
         } ]);

        // Inject language resources
        // TODO: This only works since we always load webcert before the module, when the messageService
        // is moved to a commons project, make sure this is loaded for this module as well.
        module.run(['messageService',
            function (messageService) {
                messageService.addResources(messages);
            }]);

        return moduleName;
    });
