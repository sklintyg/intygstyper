define(['text!./viewFk7263.html'], function(template) {
    'use strict';

    return [
        function() {
            return {
                restrict: 'A',
                replace: true,
/*                scope: {
                    targetModel: '=',
                    domId: '@',
                    onChange: '&'
                },*/
                template: template,

                controller: function($scope) {

                }
            };
        }
    ];
});
