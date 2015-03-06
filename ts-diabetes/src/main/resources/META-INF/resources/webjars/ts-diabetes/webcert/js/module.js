/* global tsDiabetesMessages */
angular.module('ts-diabetes', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ts-diabetes.router', 'ngSanitize', 'common' ]);

// Inject language resources
angular.module('ts-diabetes').run([ 'common.messageService',
    function(messageService) {
        'use strict';

        messageService.addResources(tsDiabetesMessages);
    }]);
