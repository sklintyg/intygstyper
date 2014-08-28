/* global tsDiabetesMessages */
angular.module('ts-diabetes', [ 'ui.bootstrap', 'ngCookies', 'ngRoute', 'ngSanitize', 'common' ]);

angular.module('ts-diabetes').config(['$routeProvider', function($routeProvider) {
    'use strict';

    $routeProvider.
        when('/ts-diabetes/view/:certificateId', {
            templateUrl: '/web/webjars/ts-diabetes/minaintyg/views/view-cert.html',
            controller: 'ts-diabetes.ViewCertCtrl',
            title: 'Läkarintyg Transportstyrelsen diabetes'
        }).
        when('/ts-diabetes/recipients', {
            templateUrl: '/web/webjars/ts-diabetes/minaintyg/views/recipients.html',
            controller: 'common.SendCertWizardCtrl'
        }).
        when('/ts-diabetes/summary', {
            templateUrl: '/web/webjars/ts-diabetes/minaintyg/views/send-summary.html',
            controller: 'common.SendCertWizardCtrl',
            title: 'Kontrollera och skicka intyget'
        }).
        when('/ts-diabetes/sent', {
            templateUrl: '/web/webjars/ts-diabetes/minaintyg/views/sent-cert.html',
            controller: 'common.SendCertWizardCtrl',
            title: 'Intyget skickat till mottagare'
        });
}]);

// Inject language resources
angular.module('ts-diabetes').run([ 'common.messageService',
    function(messageService) {
        'use strict';

        messageService.addResources(tsDiabetesMessages);
    }]);
