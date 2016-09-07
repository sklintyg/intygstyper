
angular.module('luse', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common', 'formly' ]);

// Inject language resources
angular.module('luse').run(['common.messageService', 'luse.messages',
    function(messageService, luseMessages) {
        'use strict';

        messageService.addResources(luseMessages);
    }]);
