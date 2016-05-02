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

describe('ts-diabetes.domain.IntygModel', function() {
    'use strict';

    var IntygModel;
    var ModelAttr;

    // Load the webcert module and mock away everything that is not necessary.

    beforeEach(angular.mock.module('common', 'fk7263', function(/*$provide*/) {
    }));

    // Get references to the object we want to test from the context.

    beforeEach(angular.mock.inject([
        'common.domain.ModelAttr',
        'fk7263.domain.IntygModel',
        function(_modelAttr_, _IntygModel_) {
            ModelAttr = _modelAttr_;
            IntygModel = _IntygModel_;
        }]));
});
