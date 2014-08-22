#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
define([
    'angular',
    '${artifactId}/webcert/js/services',
    '${artifactId}/webcert/js/controllers/EditCertCtrl',
    '${artifactId}/common/js/controllers/ViewCertCtrl'
], function (angular, services, EditCertCtrl, ViewCertCtrl) {
    'use strict';

    var moduleName = "${artifactId}.controllers";

    angular.module(moduleName, [services])
        .controller('${artifactId}.EditCertCtrl', EditCertCtrl)
        .controller('${artifactId}.ViewCertCtrl', ViewCertCtrl);

    return moduleName;
});
