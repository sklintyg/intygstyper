define([
    'angular',
//    'common/js/webcert/CertificateService',
    'fk7263/webcert/js/services/fragaSvarService'
], function(angular, /*CertificateService,*/ fragaSvarService) {
    'use strict';

    var moduleName = 'fk7263.services';

    angular.module(moduleName, []).
//        factory('CommonCertificateService', CertificateService).
        factory('fk7263.fragaSvarService', fragaSvarService);

    return moduleName;
});
