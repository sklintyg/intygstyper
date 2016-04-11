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

angular.module('fk7263').factory('fk7263.LastEffectiveDateNoticeModel',
function() {
    'use strict';
    function LastEffectiveDateNoticeModel () {
        this.value = null;
        this.isVisible = false;
    }

    LastEffectiveDateNoticeModel.prototype.hide = function () {
        this.isVisible = false;
    };

    LastEffectiveDateNoticeModel.prototype.show = function () {
        this.isVisible = true;
    };

    LastEffectiveDateNoticeModel.prototype.hasBeenClosed = function () {
        return !!this.value && !this.isVisible;
    };

    LastEffectiveDateNoticeModel.prototype.set = function (val) {
        this.value = val;
    };

    LastEffectiveDateNoticeModel.prototype.reset = function () {
        this.value = null;
        this.isVisible = false;
    };

    return LastEffectiveDateNoticeModel;
});
