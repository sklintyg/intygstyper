define([
    'angular',
    'webjars/common/webcert/js/directives/message',
    'webjars/common/webcert/js/directives/wcEnableTooltip',
    'webjars/common/webcert/js/directives/wcEyeDecimal',
    'webjars/common/webcert/js/directives/wcField',
    'webjars/common/webcert/js/directives/wcFieldSingle',
    'webjars/common/webcert/js/directives/wcFocusMe',
    'webjars/common/webcert/js/directives/wcHeader',
    'webjars/common/webcert/js/directives/wcMaxLength',
    'webjars/common/webcert/js/directives/wcSpinner'
], function(angular, message, wcEnableTooltip, wcEyeDecimal, wcField, wcFieldSingle, wcFocusMe, wcHeader, wcMaxLength,
    wcSpinner) {
    'use strict';

    var moduleName = 'common.directives';

    angular.module(moduleName, [ message, wcEnableTooltip, wcEyeDecimal, wcField, wcFieldSingle, wcFocusMe, wcHeader,
        wcMaxLength, wcSpinner ]);

    return moduleName;
});
