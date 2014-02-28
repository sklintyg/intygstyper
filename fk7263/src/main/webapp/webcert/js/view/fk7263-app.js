'use strict';

/*
 * App Module
 */
angular.module('FK7263ViewCertApp', [ 'ui.bootstrap', 'ngRoute', 'ngSanitize', 'wc.fk7263.controllers', 'wc.fk7263.directives', 'wc.fk7263.services', 'wc.fragasvarmodule', 'modules.messages', 'wc.common',
        'wc.utils', 'wc.common.fragasvarmodule' ]);
angular.module('FK7263ViewCertApp').config([ '$routeProvider', '$httpProvider', 'http403ResponseInterceptorProvider', function($routeProvider, $httpProvider, http403ResponseInterceptorProvider) {
    $routeProvider.when('/edit', {
        templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/webcert/views/edit-cert.html',
        controller : 'EditCertCtrl'
    }).when('/view', {
        templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/webcert/views/view-cert.html',
        controller : 'ViewCertCtrl'
    }).otherwise({
        redirectTo : '/view'
    });

    // Add cache buster interceptor
    $httpProvider.interceptors.push('httpRequestInterceptorCacheBuster');

    // Configure 403 interceptor provider
    http403ResponseInterceptorProvider.setRedirectUrl("/error.jsp?reason=denied");
    $httpProvider.responseInterceptors.push('http403ResponseInterceptor');
} ]);

// Global config of default date picker config (individual attributes can be
// overridden per directive usage)
angular.module('FK7263ViewCertApp').constant('datepickerPopupConfig', {
    dateFormat : "yyyy-MM- dd",
    closeOnDateSelection : true,
    appendToBody : false,
    showWeeks : true,
    closeText : 'OK',
    currentText : "Idag",
    toggleWeeksText : "Visa Veckor",
    clearText : "Rensa"
});

angular.module('FK7263ViewCertApp').run([ '$rootScope', 'messageService', 'User', function($rootScope, messageService, User) {
    $rootScope.lang = 'sv';
    $rootScope.DEFAULT_LANG = 'sv';
    $rootScope.MODULE_CONFIG = MODULE_CONFIG;

    // Add WC user context info
    User.setUserContext(WC_CONTEXT);
    messageService.addResources(commonMessageResources);
    messageService.addResources(fk7263Messages);
} ]);
