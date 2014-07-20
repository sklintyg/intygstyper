angular.module('fk7263', [ 'ui.bootstrap', 'ngCookies', 'ngRoute', 'ngSanitize', 'common' ]);

angular.module('fk7263').config(function($routeProvider) {
    'use strict';

    $routeProvider.
        when('/fk7263/edit/:certificateId', {
            templateUrl: '/web/webjars/fk7263/webcert/views/edit-cert.html',
            controller: 'fk7263.EditCertCtrl'
        });
});

// Inject language resources
angular.module('fk7263').run(['common.messageService',
    function(messageService) {
        'use strict';

        messageService.addResources(fk7263Messages);
    }]);
