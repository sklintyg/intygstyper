
angular.module('luae_na', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common', 'formly' ]);

// Inject language resources
angular.module('luae_na').run(['common.messageService', 'luae_na.messages',
    function(messageService, luaeNaMessages) {
        'use strict';

        messageService.addResources(luaeNaMessages);
    }]);
