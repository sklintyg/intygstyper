/* global tsDiabetesMessages */
angular.module('ts-diabetes', [ 'ui.bootstrap', 'ngCookies', 'ngRoute', 'ngSanitize', 'common' ]);

angular.module('ts-diabetes').config(function($routeProvider) {
    'use strict';

    $routeProvider.
        when('/ts-diabetes/edit/:certificateId', {
            templateUrl: '/web/webjars/ts-diabetes/webcert/views/edit-cert.html',
            controller: 'ts-diabetes.EditCertCtrl'
        });
});

// Inject language resources
angular.module('ts-diabetes').run([ 'common.messageService',
    function(messageService) {
        'use strict';

        messageService.addResources(tsDiabetesMessages);
    }]);
