'use strict';

/* Directives */
/* Used for textarea capable of displaying and inputting json */
angular.module('RLIEditCertApp').directive('jsonText', function() {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attr, ngModel) {            
          /* Parse input from textarea as JSON */
          function into(input) {
            return JSON.parse(input);
          }
          /* Make nice formatted JSON for output, indent is set to 4 */
          function out(data) {
            return JSON.stringify(data, undefined, 4);
          }
          ngModel.$parsers.push(into);
          ngModel.$formatters.push(out);

        }
    };
});