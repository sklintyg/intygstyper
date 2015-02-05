angular.module('fk7263').factory('fk7263.EditCertCtrl.DateRangeGroupModel',
    ['common.DateUtilsService', 'common.UtilsService', function( dateUtils, utils) {
        'use strict';

        /**
         * Constructor, with class name
         */
        function DateRangeGroupModel(certModel, certFormModel, nedsattInvalidModel, groupName, strict, useModelValue) {
            // Public properties, assigned to the instance ('this')
            
            this.certModel = certModel;
            this.certFormModel = certFormModel;
            this.nedsattInvalidModel = nedsattInvalidModel;
            
            this.fromName = groupName + 'from';
            this.tomName = groupName + 'tom';
            this.nedsattFrom = null;
            this.nedsattTom = null;
            if (useModelValue) {
                if (this.certModel[groupName]) {
                    this.nedsattFrom = this.certModel[groupName].from;
                    this.nedsattTom = this.certModel[groupName].tom;
                }
            } else {
                this.nedsattFrom = this.certFormModel[this.fromName].$viewValue;
                this.nedsattTom = this.certFormModel[this.tomName].$viewValue;
            }

            if (strict === undefined || strict === true) {
                this.nedsattFrom = dateUtils.convertDateStrict(this.certFormModel[this.fromName].$viewValue);
                this.nedsattTom = dateUtils.convertDateStrict(this.certFormModel[this.tomName].$viewValue);
            } else {
                this.nedsattFrom = this.certFormModel[this.fromName].$viewValue;
                this.nedsattTom = this.certFormModel[this.tomName].$viewValue;
            }
        }

        // ---- private functions
        // place any private functions here...

        // ---- public functions
        DateRangeGroupModel.prototype.isMarkedInvalid = function() {
            return this.nedsattInvalidModel[this.fromName] || this.nedsattInvalidModel[this.tomName];
        };

        DateRangeGroupModel.prototype.hasValidDates = function() {
            return (utils.isValidString(this.nedsattFrom) && utils.isValidString(this.nedsattTom));
        };

        DateRangeGroupModel.prototype.isValid = function() {
            return (this.hasValidDates() && !this.isMarkedInvalid());
        };

        DateRangeGroupModel.prototype.isSame = function(otherGroup) {
            return (this.fromName === otherGroup.fromName);
        };

        DateRangeGroupModel.build = function(certModel, certFormModel, nedsattInvalidModel, groupName, strict, useModelValue) {
            return new DateRangeGroupModel(certModel, certFormModel, nedsattInvalidModel, groupName, strict, useModelValue);
        };

        /**
         * Return the constructor function DateRangeGroupModel
         */
        return DateRangeGroupModel;

    }]);