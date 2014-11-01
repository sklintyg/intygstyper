#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
angular.module('${artifactId}', [ 'ui.bootstrap', 'ngCookies', 'ngRoute', 'ngSanitize', 'common' ]);

angular.module('${artifactId}').config(['$routeProvider', function($routeProvider) {
    'use strict';

    // TODO: Configure $routeProvider
}]);

// Inject language resources
angular.module('${artifactId}').run([ 'common.messageService',
    function(messageService) {
        'use strict';

        messageService.addResources(${artifactId-safe}Messages);
    }]);
