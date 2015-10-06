angular.module('sjukpenning').service('sjukpenning.EditCertCtrl.Helper',
    [ function( ) {
        'use strict';

        this.limitLength = function limitLength(limit, totalLength, val) {
            if (totalLength > limit) {
                // Remove characters over limit from current field
                return val.substr(0, val.length - (totalLength - limit));
            }
            return val;
        };

        this.getLengthOrZero = function getLengthOrZero(value) {
            if (typeof (value) !== 'string') {
                return 0;
            } else {
                return value.length;
            }
        };

    }]);