angular.module('ts-bas', [ 'ui.bootstrap', 'ngCookies', 'ngRoute', 'ngSanitize', 'common' ]);

angular.module('ts-bas').config(['$routeProvider', function($routeProvider) {
    'use strict';

    $routeProvider.
        when('/ts-bas/edit/:certificateId', {
            templateUrl: '/web/webjars/ts-bas/webcert/views/edit-cert.html',
            controller: 'ts-bas.EditCertCtrl'
        });
}]);

// Inject language resources
angular.module('ts-bas').run([ 'common.messageService',
    function(messageService) {
        'use strict';

        messageService.addResources(tsBasMessages);
    }]);
