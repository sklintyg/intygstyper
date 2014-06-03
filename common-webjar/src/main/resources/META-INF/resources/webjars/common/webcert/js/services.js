define([
    'angular',
    'webjars/common/webcert/js/services/CertificateService',
    'webjars/common/webcert/js/services/dialogService',
    'webjars/common/webcert/js/services/fragaSvarCommonService',
    'webjars/common/webcert/js/services/http403ResponseInterceptor',
    'webjars/common/webcert/js/services/httpRequestInterceptorCacheBuster',
    'webjars/common/webcert/js/services/ManageCertView',
    'webjars/common/webcert/js/services/messageService',
    'webjars/common/webcert/js/services/statService',
    'webjars/common/webcert/js/services/User'
], function(angular, CertificateService, dialogService, fragaSvarCommonService, http403ResponseInterceptor,
    httpRequestInterceptorCacheBuster, ManageCertView, messageService, statService, User) {
    'use strict';

    var moduleName = 'common.services';

    angular.module(moduleName, [ CertificateService, dialogService, fragaSvarCommonService, http403ResponseInterceptor,
        httpRequestInterceptorCacheBuster, ManageCertView, messageService, statService, User ]);

    return moduleName;
});
