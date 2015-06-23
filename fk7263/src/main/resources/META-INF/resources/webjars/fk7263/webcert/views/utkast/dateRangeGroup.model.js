angular.module('fk7263').factory('fk7263.EditCertCtrl.DateRangeGroupModel',
    ['common.DateUtilsService', 'common.UtilsService', function( dateUtils, utils) {
        'use strict';

        /**
         * Constructor, with class name
         */
        function DateRangeGroupModel(_$scope, groupName, id) {
            // Public properties, assigned to the instance ('this')

            this._$scope = _$scope;
            this.certFormModel = _$scope.form8b;
            this.certModel = _$scope.model;
            this.groupName = groupName;
            this.fromName = groupName + 'from';
            this.tomName = groupName + 'tom';

            this.nedsattFormFrom = this.certFormModel[this.fromName];
            this.nedsattFormTom = this.certFormModel[this.tomName];

            this.nedsattInvalidFrom = false;
            this.nedsattInvalidTom = false;
            this.workState = false;

            this.id = id;

            this.addNedsattParser();

            // on creation the cert model is used and then there after the certForm ( the form model ) is used.
            // this is a bit weird but it's to do with validation etc and the crazy angular stuff thats put on the ng-model attributes.
            // it's much easier in other more complete frameworks ...
            this.useCert = false;
        }

        // parsers
        DateRangeGroupModel.prototype.addNedsattParser = function addNedsattParser(){
            var self = this;

            // convert the date to an iso string
            function nedsattParser( dateRangeGroup, viewValue) {

                viewValue = dateUtils.convertDateToISOString(viewValue);

                dateRangeGroup.workState = dateRangeGroup.isValid();

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
            return !otherGroup ? false : otherGroup.groupName === this.groupName;
        };


        DateRangeGroupModel.prototype.setCert = function(cert) {
            this.certModel = cert;
        };

        DateRangeGroupModel.prototype.setUseCert = function(val) {
            this.useCert = val;
        };

        DateRangeGroupModel.prototype.nedsattFrom = function(val) {
            if(val){
                if (this.useCert) {
                    this.certModel[this.groupName].from = val;
                } else {
                    this.nedsattFormFrom.$viewValue = val;
                }
            } else {
                if (this.useCert) {
                    return this.certModel[this.groupName] ? this.certModel[this.groupName].from : null;
                } else {
                    return this.nedsattFormFrom ? this.nedsattFormFrom.$viewValue : null;
                }
            }
        };

        DateRangeGroupModel.prototype.nedsattTom = function(val) {
            if(val){
                if (this.useCert) {
                    this.certModel[this.groupName].tom = val;
                } else {
                    this.nedsattFormTom.$viewValue = val;
                }
            } else {
                if (this.useCert) {
                    return this.certModel[this.groupName] ? this.certModel[this.groupName].tom : null;
                } else {
                    return this.nedsattFormTom ? this.nedsattFormTom.$viewValue : null;
                }
            }
        };

        DateRangeGroupModel.prototype.certFrom = function() {
            return this.certModel[this.groupName] ? this.certModel[this.groupName].from : null;
        };

        DateRangeGroupModel.prototype.certTom = function() {
            return this.certModel[this.groupName] ? this.certModel[this.groupName].tom : null;
        };

        DateRangeGroupModel.prototype.setCertFrom = function(val) {
            if(!this.certModel[this.groupName]){
                this.certModel[this.groupName] = {from:null, tom:null};
            }
            if(val === undefined){
                this.certModel[this.groupName] = undefined;
            } else {
                this.certModel[this.groupName].from = val;
            }
        };

        DateRangeGroupModel.prototype.setCertTom = function(val) {
            if(!this.certModel[this.groupName]){
                this.certModel[this.groupName] = {from:null, tom:null};
            }
            if(val === undefined){
                this.certModel[this.groupName] = undefined;
            } else {
                this.certModel[this.groupName].tom = val;
            }
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

            if(this.isSame(dateRangeGroup) || (!this.hasValidDates() || !dateRangeGroup.hasValidDates())){
                return;
            }

            var fromThis = this.momentFrom(),
                tomThis = this.momentTom(),
                fromThat = dateRangeGroup.momentFrom(),
                tomThat = dateRangeGroup.momentTom();

            //          |       |
            //      |               |
            if( dateUtils.isBeforeOrEqual(fromThat, fromThis) && dateUtils.isAfterOrEqual(tomThat, tomThis) ){
                this.setDateInvalidState(true);
                dateRangeGroup.setDateInvalidState(true);
            }
            //          |       |
            //      |       |
            if(dateUtils.isBeforeOrEqual(fromThat,fromThis) &&
                (dateUtils.isAfterOrEqual(tomThat, fromThis) && dateUtils.isBeforeOrEqual(tomThat, tomThis) ) ) {
                this.setDateInvalidState(true);
                dateRangeGroup.setDateInvalidState(true);
            }
            //          |       |
            //             |  |
            if (dateUtils.isAfterOrEqual(fromThat, fromThis) && dateUtils.isBeforeOrEqual(tomThat, tomThis)) {
                this.setDateInvalidState(true);
                dateRangeGroup.setDateInvalidState(true);
            }
            //          |       |
            //              |       |
            if (dateUtils.isAfterOrEqual(fromThat, fromThis) &&
                dateUtils.isBeforeOrEqual(fromThat, tomThis) && (dateUtils.isAfterOrEqual(tomThat, tomThis) )){
                this.setDateInvalidState(true);
                dateRangeGroup.setDateInvalidState(true);
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
        };

        DateRangeGroupModel.prototype.isTomSame = function(tom){
            return this.momentTom().isSame(tom, 'day');
        };

        DateRangeGroupModel.prototype.setDateValidity = function() {
            if (this.isValid()) {
                var fromThis = this.momentFrom(),
                    tomThis = this.momentTom();
                if (this.isFromAfterTom()) {
                    this.setDateInvalidState(true);
                } else if(dateUtils.isSame(fromThis, tomThis)){
                    this.setDateInvalidState(true);
                }
                else {
                    this.setDateInvalidState(false);
                }
                this.workState = true;
            } else {
                this.setDateInvalidState(false);
                this.workState = false;
            }
        };

        DateRangeGroupModel.prototype.isMarkedInvalid = function() {
            return this.nedsattInvalidFrom || this.nedsattInvalidTom;
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

        DateRangeGroupModel.prototype.setDateInvalidState = function (value) {
            this.setDateInvalidStateFrom(value);
            this.setDateInvalidStateTom(value);
        };

        DateRangeGroupModel.prototype.setDateInvalidStateFrom = function (value) {
            this.nedsattInvalidFrom = value;
            if(this.nedsattFormFrom) {
                this.nedsattFormFrom.$setValidity(this.fromName, value);
            }
        };

        DateRangeGroupModel.prototype.setDateInvalidStateTom = function (value) {
            this.nedsattInvalidTom = value;
            if (this.nedsattFormTom) {
                this.nedsattFormTom.$setValidity(this.tomName, value);
            }
        };

        DateRangeGroupModel.build = function(_$scope, groupName, id) {
            return new DateRangeGroupModel(_$scope, groupName, id);
        };

        /**
         * Return the constructor function DateRangeGroupModel
         */
        return DateRangeGroupModel;

    }]);