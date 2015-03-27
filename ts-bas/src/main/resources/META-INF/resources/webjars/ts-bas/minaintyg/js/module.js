/* global tsBasMessages */
angular.module('ts-bas', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ts-bas.router', 'ngSanitize', 'common' ]);

// Inject language resources
angular.module('ts-bas').run([ 'common.messageService',
    function(messageService) {
        'use strict';

        messageService.addResources(tsBasMessages);
    }]);
