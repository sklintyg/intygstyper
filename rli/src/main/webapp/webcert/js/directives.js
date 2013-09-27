'use strict';

/* Directives */
angular.module('RLIEditCertApp').directive('jsonText', function() {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attr, ngModel) {            
          function into(input) {
            return JSON.parse(input);
          }
          function out(data) {
            return JSON.stringify(data, undefined, 4);
          }
          ngModel.$parsers.push(into);
          ngModel.$formatters.push(out);

        }
    };
});