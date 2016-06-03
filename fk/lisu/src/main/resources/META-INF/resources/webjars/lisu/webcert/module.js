/* global lisuMessages */
angular.module('lisu', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common', 'formly' ]);

// Inject language resources
angular.module('lisu').run(['common.messageService',
    function(messageService) {
        'use strict';

        messageService.addResources(lisuMessages);
    }]);
