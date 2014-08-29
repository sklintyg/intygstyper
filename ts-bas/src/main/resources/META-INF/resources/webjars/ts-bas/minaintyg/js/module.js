/* global tsBasMessages */
angular.module('ts-bas', [ 'ui.bootstrap', 'ngCookies', 'ngRoute', 'ngSanitize', 'common' ]);

angular.module('ts-bas').config(['$routeProvider', function($routeProvider) {
    'use strict';

    $routeProvider.
        when('/ts-bas/view/:certificateId', {
            templateUrl: '/web/webjars/ts-bas/minaintyg/views/view-cert.html',
            controller: 'ts-bas.ViewCertCtrl',
            title: 'Läkarintyg Transportstyrelsen Bas'
        }).
        when('/ts-bas/recipients', {
            templateUrl: '/web/webjars/ts-bas/minaintyg/views/recipients.html',
            controller: 'common.SendCertWizardCtrl'
        }).
        when('/ts-bas/summary', {
            templateUrl: '/web/webjars/ts-bas/minaintyg/views/send-summary.html',
            controller: 'common.SendCertWizardCtrl',
            title: 'Kontrollera och skicka intyget'
        }).
        when('/ts-bas/sent', {
            templateUrl: '/web/webjars/ts-bas/minaintyg/views/sent-cert.html',
            controller: 'common.SendCertWizardCtrl',
            title: 'Intyget skickat till mottagare'
        });
}]);

// Inject language resources
angular.module('ts-bas').run([ 'common.messageService',
    function(messageService) {
        'use strict';

        messageService.addResources(tsBasMessages);
    }]);
