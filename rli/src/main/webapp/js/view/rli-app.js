'use strict';

/* App Module */

var RLIApp = angular.module('RLIViewCertApp', []).config([ '$routeProvider', function ($routeProvider) {
    $routeProvider.when('/view', {
        templateUrl: RLI_CONTEXT_PATH +'/views/view-cert.html',
        controller: 'ViewCertCtrl'
    }).when('/send', {
        templateUrl: RLI_CONTEXT_PATH + '/views/send.html', //TBD
        controller: 'SendCtrl'
    }).otherwise({
            redirectTo: '/view' 
        });
} ]);

RLIApp.run(['$rootScope', function ($rootScope) {
    $rootScope.lang = 'sv';
    $rootScope.DEFAULT_LANG = 'sv';
}]);
