#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
angular.module('${artifactId}', [ 'ui.bootstrap', 'ngCookies', 'ui.router',, 'ngSanitize', 'common' ]);

angular.module('${artifactId}').config(['$stateProvider', function($stateProvider) {
    'use strict';

    // TODO: Configure $routeProvider
}]);

// Inject language resources
angular.module('${artifactId}').run([ 'common.messageService',
    function(messageService) {
        'use strict';

        messageService.addResources(${artifactId-safe}Messages);
    }]);
