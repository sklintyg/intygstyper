/* global sjukpenning messages */
angular.module('sjukpenning', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common' ]);

// Inject language resources
angular.module('sjukpenning').run(['common.messageService',
    function(messageService) {
        'use strict';

        messageService.addResources(sjukpenningMessages);
    }]);
