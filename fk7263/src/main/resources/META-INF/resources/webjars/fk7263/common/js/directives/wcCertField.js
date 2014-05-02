define(['text!./wcCertField.html'], function(template) {
    'use strict';

    return [function() {
        return {
            restrict: 'A',
            transclude: true,
            replace: true,
            template: template,
            scope: {
                fieldLabel: '@',
                fieldNumber: '@',
                filled: '=?'
            }
        };
    }];
});
