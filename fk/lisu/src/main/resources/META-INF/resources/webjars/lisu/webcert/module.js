angular.module('lisu', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common', 'formly' ]);

// Inject language resources
angular.module('lisu').run(['common.messageService', 'lisu.messages',
    function(messageService, lisuMessages) {
        'use strict';

        messageService.addResources(lisuMessages);
    }]);
