/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Created by BESA on 2015-03-05.
 */

/**
 * Fragasvar text panel directive. Builds fragetext from qa model
 */
angular.module('fk7263').directive('fragasvarTextPanel',
    [
        function() {
            'use strict';

            return {
                restrict: 'A',
                replace: true,
                templateUrl: '/web/webjars/fk7263/webcert/views/intyg/fragasvar/fragasvarTextPanel.directive.html',
                scope: {
                    panelId: '@',
                    qa: '='
                },
                controller: function($scope, $element, $attrs) {

                }
            };
        }]);
