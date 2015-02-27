angular.module('fk7263').factory('fk7263.EditCertCtrl.DateRangeGroupModel',
    ['common.DateUtilsService', 'common.UtilsService', '$log', function( dateUtils, utils, $log) {
        'use strict';

        /**
         * Constructor, with class name
         */
        function DateRangeGroupModel(_$scope, workState, certFormModel, nedsattInvalidModel, groupName, id) {
            // Public properties, assigned to the instance ('this')
            this.workState = workState;
            this.certFormModel = certFormModel;

            this.groupName = groupName;
            this.fromName = groupName + 'from';
            this.tomName = groupName + 'tom';

            this.nedsattFormFrom = this.certFormModel[this.fromName];
            this.nedsattFormTom = this.certFormModel[this.tomName];

            this._$scope = _$scope;
            this.addNedsattParser();
            this.id = id;

        }

        // parsers
        DateRangeGroupModel.prototype.addNedsattParser = function addNedsattParser(){
            var self = this;

            // convert the date to an iso string
            function nedsattParser( dateRangeGroup, viewValue) {

                viewValue = dateUtils.convertDateToISOString(viewValue);

                if (!dateRangeGroup.hasAValidDate()) {
                    // uncheck check since both dates are undefined or empty
                    dateRangeGroup.setWorkState(false);

                } else if (dateRangeGroup.hasAValidDate()) {
                    // One of the dates is valid
                    dateRangeGroup.setWorkState(true); // Check nedsatt checkbox
                }

                return viewValue;
            }

            if (self.nedsattFormFrom) {

                // remove the first parser
                if (self.nedsattFormFrom.$parsers.length > 1) {
                    self.nedsattFormFrom.$parsers.shift();
                }
                // add the nedsattParser to the first in the list
                self.nedsattFormFrom.$parsers.unshift(function(viewValue){
                    return nedsattParser(self, viewValue);
                });

            }

            if (self.nedsattFormFrom) {

                if (self.nedsattFormTom.$parsers.length > 1) {
                    self.nedsattFormTom.$parsers.shift();
                }
                self.nedsattFormTom.$parsers.unshift(function(viewValue){
                    return nedsattParser(self, viewValue);
                });
            }

        };

        DateRangeGroupModel.prototype.equals = function(otherGroup) {
            if(otherGroup && otherGroup.groupName == this.groupName){
                return true;
            } else {
                return false;
            }
        };

        DateRangeGroupModel.prototype.nedsattFrom = function() {
            return this.nedsattFormFrom ? this.nedsattFormFrom.$viewValue : null;
        };

        DateRangeGroupModel.prototype.nedsattInvalidFrom = function(val) {
            if(val !== undefined){
                this._$scope.nedsattInvalid[this.fromName] = val;
            }
            return this._$scope.nedsattInvalid[this.fromName];
        };

        DateRangeGroupModel.prototype.nedsattInvalidTom = function(val) {
            if(val !== undefined){
                this._$scope.nedsattInvalid[this.tomName] = val;
            }
            return this._$scope.nedsattInvalid[this.tomName];
        };

        DateRangeGroupModel.prototype.nedsattTom = function() {
            return this.nedsattFormTom ? this.nedsattFormTom.$viewValue : null;
        };

        DateRangeGroupModel.prototype.momentStrictFrom = function() {
            return dateUtils.convertDateStrict(this.nedsattFrom());
        };

        DateRangeGroupModel.prototype.momentStrictTom = function() {
            return dateUtils.convertDateStrict(this.nedsattTom());
        };

        DateRangeGroupModel.prototype.momentFrom = function() {
            return dateUtils.toMoment(this.nedsattFrom());
        };

        DateRangeGroupModel.prototype.momentTom = function() {
            return dateUtils.toMoment(this.nedsattTom());
        };

        DateRangeGroupModel.prototype.isFromAfterTom = function() {
            var momentFrom = this.momentFrom(), momentTom = this.momentTom();
            return momentFrom.isValid() && momentTom.isValid() &&
                momentFrom.isAfter(momentTom);
        };

        DateRangeGroupModel.prototype.markDateRangeValidity = function(dateRangeGroup) {

            if(this.isSame(dateRangeGroup)
                || (!this.hasValidDates() || !dateRangeGroup.hasValidDates())){
                return;
            }

            $log.log("markDateRangeValidity : " + this.groupName + "," + dateRangeGroup.groupName);
            var fromThis = this.momentFrom(),
                tomThis = this.momentTom(),
                fromThat = dateRangeGroup.momentFrom(),
                tomThat = dateRangeGroup.momentTom();

            // this from and tom
            // from and tom on same date
            if(dateUtils.isSame(fromThis, tomThis)){
                this.setDateInvalidState(true);
            }
            if(dateUtils.isSame(fromThat, tomThat)){
                dateRangeGroup.setDateInvalidState(true);
            }
            //          |       |
            //      |               |
            if( dateUtils.isBeforeOrEqual(fromThat, fromThis) && dateUtils.isAfterOrEqual(tomThat, tomThis) ){
                this.setDateInvalidState(true);
                dateRangeGroup.setDateInvalidState(true);
                $log.log("1. f2 b f1 and t2 a t1");
            }
            //          |       |
            //      |       |
            if(dateUtils.isBeforeOrEqual(fromThat,fromThis) && (dateUtils.isAfterOrEqual(tomThat, fromThis) && dateUtils.isBeforeOrEqual(tomThat, tomThis) ) ) {
                this.setDateInvalidState(true);
                dateRangeGroup.setDateInvalidState(true);
                $log.log("2. f2 b f1 and (t2 a f1 and t2 b t1)");
            }
            //          |       |
            //             |  |
            if (dateUtils.isAfterOrEqual(fromThat, fromThis) && dateUtils.isBeforeOrEqual(tomThat, tomThis)) {
                this.setDateInvalidState(true);
                dateRangeGroup.setDateInvalidState(true);
                $log.log("3. f2 a f1 and t2 b f1");
            }
            //          |       |
            //              |       |
            if (dateUtils.isAfterOrEqual(fromThat, fromThis) && dateUtils.isBeforeOrEqual(fromThat, tomThis) && (dateUtils.isAfterOrEqual(tomThat, tomThis) )){
                this.setDateInvalidState(true);
                dateRangeGroup.setDateInvalidState(true);
                $log.log("4. f2 a f1 and f2 b t1");
            }

        };

        DateRangeGroupModel.prototype.isFromBefore = function(val) {
            return this.momentFrom().isBefore(val);
        };


        DateRangeGroupModel.prototype.isTomBefore = function(val) {
            return this.momentTom().isBefore(val);
        };

        DateRangeGroupModel.prototype.isFromAfter = function(val) {
            return this.momentFrom().isBefore(val);
        };

        DateRangeGroupModel.prototype.isTomAfter = function(val) {
            return this.momentTom().isBefore(val);
        };

        DateRangeGroupModel.prototype.isTomAfterOrEqual = function(val) {
            return this.momentTom().isBefore(val) || this.momentTom().isSame(val, 'day');
        };

        DateRangeGroupModel.prototype.isFromSame = function(from){
            return this.momentFrom().isSame(from, 'day');
        }

        DateRangeGroupModel.prototype.isTomSame = function(tom){
            return this.momentTom().isSame(tom, 'day');
        }

        DateRangeGroupModel.prototype.setDateValidity = function() {
            if (this.isValid()) {
                if (this.isFromAfterTom()) {
                    this.setDateInvalidState(true);
                } else {
                    this.setDateInvalidState(false);
                }
            } else {
                this.setDateInvalidState(false);
            }
        };

        DateRangeGroupModel.prototype.isMarkedInvalid = function() {
            return this.nedsattInvalidFrom() || this.nedsattInvalidTom();
        };

        DateRangeGroupModel.prototype.hasValidDates = function() {
            return (utils.isValidString(this.nedsattFrom()) && utils.isValidString(this.nedsattTom()));
        };

        DateRangeGroupModel.prototype.hasAValidDate = function() {
            return (utils.isValidString(this.nedsattFrom()) || utils.isValidString(this.nedsattTom()));
        };

        DateRangeGroupModel.prototype.isValid = function() {
            return (this.hasValidDates() && !this.isMarkedInvalid());
        };

        DateRangeGroupModel.prototype.isSame = function(otherGroup) {
            return (this.fromName === otherGroup.fromName);
        };

        DateRangeGroupModel.prototype.setWorkState = function(val) {
            this.workState[this.groupName] = val;
        };

        DateRangeGroupModel.prototype.setDateInvalidState = function (value) {
            this.setDateInvalidStateFrom(value);
            this.setDateInvalidStateTom(value);
        };

        DateRangeGroupModel.prototype.setDateInvalidStateFrom = function (value) {
            this.nedsattInvalidFrom(value);
            this.nedsattFormFrom.$setValidity(this.fromName, value);
        };

        DateRangeGroupModel.prototype.setDateInvalidStateTom = function (value) {
            this.nedsattInvalidTom(value);
            this.nedsattFormTom.$setValidity(this.tomName, value);
        }

        DateRangeGroupModel.build = function(workState, certModel, certFormModel, nedsattInvalidModel, groupName, strict, useModelValue) {
            return new DateRangeGroupModel(workState, certModel, certFormModel, nedsattInvalidModel, groupName, strict, useModelValue);
        };

        /**
         * Return the constructor function DateRangeGroupModel
         */
        return DateRangeGroupModel;

    }]);