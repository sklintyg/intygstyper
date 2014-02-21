/*
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
'use strict';

/* Directives */
/* Used for textarea capable of displaying and inputting json */

var directives = angular.module('wc.ts-diabetes.directives', []);

directives.directive('jsonText', function() {
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

