define([
    'angular',
    'rli/common/js/controllers/ViewCertCtrl'
], function(angular, ViewCertCtrl) {
    'use strict';

    var moduleName = 'rli.controllers';

    angular.module(moduleName, []).
        controller('rli.ViewCertCtrl', ViewCertCtrl);
    return moduleName;
});
