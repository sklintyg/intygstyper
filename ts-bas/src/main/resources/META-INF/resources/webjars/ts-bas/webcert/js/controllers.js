define([
    'angular',
    'ts-bas/webcert/js/controllers/EditCertCtrl',
    'ts-bas/common/js/controllers/ViewCertCtrl'
], function(angular, EditCertCtrl, ViewCertCtrl) {
    'use strict';

    var moduleName = 'ts-bas.controllers';

    angular.module(moduleName, []).
        controller('ts-bas.EditCertCtrl', EditCertCtrl).
        controller('ts-bas.ViewCertCtrl', ViewCertCtrl);

    return moduleName;
});
