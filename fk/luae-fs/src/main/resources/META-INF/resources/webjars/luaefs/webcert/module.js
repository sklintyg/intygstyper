/* global luaefs messages */
angular.module('luaefs', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common', 'formly' ]);

// Inject language resources
angular.module('luaefs').run(['common.messageService', 'common.dynamicLabelService',
    function(messageService, dynamicLabelService) {
        'use strict';

        messageService.addResources(luaefsMessages);
    }]);
