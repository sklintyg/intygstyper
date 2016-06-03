/* global luseMessages */
angular.module('luse', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common', 'formly' ]);

// Inject language resources
angular.module('luse').run(['common.messageService', 'common.dynamicLabelService',
    function(messageService, dynamicLabelService) {
        'use strict';

        messageService.addResources(luseMessages);
    }]);
