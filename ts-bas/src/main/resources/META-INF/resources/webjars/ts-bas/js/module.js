define([ 'angular',
    './controllers',
    './routes'
], function (angular, controllers, routes) {
    'use strict';

    function init(portal, baseUrl) {
        console.log("loading TS-bas")
        controllers(portal)
        routes(portal, baseUrl)
    }

    return init;
});
