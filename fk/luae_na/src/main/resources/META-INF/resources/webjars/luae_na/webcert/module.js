/* global luaeNaMessages */
angular.module('luae_na', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common', 'formly' ]);

// Inject language resources
angular.module('luae_na').run(['common.messageService', 'common.dynamicLabelService',
    function(messageService, dynamicLabelService) {
        'use strict';

        messageService.addResources(luaeNaMessages);
    }]);
