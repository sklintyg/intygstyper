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

/**
 * Directive for "Eye Decimals" with two way binding
 */
directives.directive('eyeDecimal', function() {
    return {
        restrict : 'A',
        require : 'ngModel',
        link : function(scope, elem, attrs, ngModelCtrl) {
            var decimalPoint = /[\,,\.]/
            var number = /[0-9]/

            function filter(s, p) {
                var filtered = "", dec = false;
                angular.forEach(s, function(c) {
                    if (number.test(c))
                        filtered += c;
                    if (!dec && decimalPoint.test(c)) {
                        dec = true;
                        filtered += ',';
                    }
                });
                return filtered.length <= 3 ? filtered : filtered.substring(3);
            }
            function format(value) {
                var l = value.length;
                var valForView = "";
                var valForModel;
                if (l === 0) {
                    valForModel = null;
                } else {

                    if (l === 1) {
                        if (value[0] === ',')
                            valForView = "0,0";
                        else {
                            valForView = value[0] + ",0";
                        }
                    } else if (l == 2) {
                        if (value[0] === ',') {
                            valForView = "0," + value[1];
                        } else if (value[1] === ',') {
                            valForView = value[0] + ',0';
                        } else {
                            valForView = value[0] + "," + value[1];
                        }
                    } else if (l == 3) {
                        if (value[0] === ',') {
                            valForView = "0," + value[1];
                        } else if (value[1] === ',') {
                            valForView = value;
                        } else if (value[2] === ',') {
                            valForView = value[0] + ',' + value[1];
                        }
                    }

                    valForModel = valForView;
                }

                // console.log("format(" + value + ") -> V(" + valForView + "), M(" + valForModel + ")");

                return {
                    valForView : valForView,
                    valForModel : valForModel
                }
            }
            function blurFormat() {
                var filtered = filter(this.value);
                var val = format(filtered);
                if (this.value != val.valForView)
                    this.value = val.valForView;
            }
            function eyeDecimal(valFromView) {

                var filtered = filter(valFromView);
                var val = format(filtered);

                if (filtered !== valFromView) {
                    ngModelCtrl.$setViewValue(filtered);
                    ngModelCtrl.$render();
                }
                return val.valForModel;
            }
            elem.bind("blur", blurFormat);
            ngModelCtrl.$parsers.push(eyeDecimal);
        }
    };
});