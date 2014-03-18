define([
    'angular',
    'ts-bas/webcert/js/controllers',
    'ts-bas/webcert/js/messages',
    'ts-bas/webcert/js/services'
], function (angular, controllers, messages, services) {
    'use strict';

    var moduleName = 'ts-bas';

    var module = angular.module(moduleName, [controllers, services]);

    module.config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
            when('/ts-bas/test', {
                templateUrl : '/webjars/ts-bas/webcert/views/test.html',
                controller : 'ts-bas.TestCtrl'
            }).
            when('/ts-bas/edit', {
                templateUrl : '/webjars/ts-bas/webcert/views/edit.cert.html',
                controller : 'ts-bas.EditCertCtrl'
            }).
            when('/ts-bas/view/:certificateId', {
                templateUrl : '/webjars/ts-bas/common/views/view-cert.html',
                controller : 'ts-bas.ViewCertCtrl'
            });
    }]);

    return moduleName;
});
