
angular.module('luae_fs', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common', 'formly' ]);

// Inject language resources
angular.module('luae_fs').run(['common.messageService', 'luae_fs.messages',
    function(messageService, luaeFsMessages) {
        'use strict';

        messageService.addResources(luaeFsMessages);
    }]);
