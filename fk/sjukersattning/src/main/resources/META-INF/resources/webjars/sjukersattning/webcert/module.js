/* global sjukersattning messages */
angular.module('sjukersattning', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common' ]);

// Inject language resources
angular.module('sjukersattning').run(['common.messageService', 'common.dynamicLabelService',
    function(messageService, dynamicLabelService) {
        'use strict';

        messageService.addResources(sjukersattningMessages);
    }]);
