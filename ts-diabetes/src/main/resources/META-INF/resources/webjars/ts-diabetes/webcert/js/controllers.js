define([
    'angular',
    'ts-diabetes/webcert/js/controllers/EditCertCtrl',
    'ts-diabetes/common/js/controllers/ViewCertCtrl'
], function(angular, EditCertCtrl, ViewCertCtrl) {
    'use strict';

    var moduleName = 'ts-diabetes.controllers';

    angular.module(moduleName, []).
        controller('ts-diabetes.EditCertCtrl', EditCertCtrl).
        controller('ts-diabetes.ViewCertCtrl', ViewCertCtrl);

    return moduleName;
});
