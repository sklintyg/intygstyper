angular.module('lisjp', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common', 'formly' ]);

// Inject language resources
angular.module('lisjp').run(['common.messageService', 'lisjp.messages',
    function(messageService, lisjpMessages) {
        'use strict';

        messageService.addResources(lisjpMessages);
    }]);
