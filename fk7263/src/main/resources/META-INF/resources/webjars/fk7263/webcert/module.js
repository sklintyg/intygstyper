/* global fk7263Messages */
angular.module('fk7263', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common' ]);

// Inject language resources
angular.module('fk7263').run(['common.messageService',
    function(messageService) {
        'use strict';

        messageService.addResources(fk7263Messages);
    }]);
