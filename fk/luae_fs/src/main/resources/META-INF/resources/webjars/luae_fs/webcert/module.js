/* global luaeFsMessages */
angular.module('luae_fs', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common', 'formly' ]);

// Inject language resources
angular.module('luae_fs').run(['common.messageService', 'common.dynamicLabelService',
    function(messageService, dynamicLabelService) {
        'use strict';

        messageService.addResources(luaeFsMessages);
    }]);
