angular.module('fk7263').service('fk7263.EditCertCtrl.DateRangeGroupsService',
    ['common.DateUtilsService', 'common.UtilsService','fk7263.EditCertCtrl.DateRangeGroupModel', '$log', '$filter', function( dateUtils, utils, DateRangeGroupModel, $log, $filter) {
        'use strict';

        /**
         * Constructor, with class name
         */
        // 8b ---------------------------------------------
        function DateRangeGroupsService(_$scope){

            this.datesOutOfRange = _$scope.datesOutOfRange;
            this.datesPeriodTooLong = _$scope.datesPeriodTooLong;

            this.nedsattMed25 = DateRangeGroupModel.build(_$scope, 'nedsattMed25', '25', this);
            _$scope.field8b.nedsattMed25 = this.nedsattMed25;

            this.nedsattMed50 = DateRangeGroupModel.build(_$scope, 'nedsattMed50', '50', this);
            _$scope.field8b.nedsattMed50 = this.nedsattMed50;

            this.nedsattMed75 = DateRangeGroupModel.build(_$scope, 'nedsattMed75', '75', this);
            _$scope.field8b.nedsattMed75 = this.nedsattMed75;

            this.nedsattMed100 = DateRangeGroupModel.build(_$scope, 'nedsattMed100', '100', this);
            _$scope.field8b.nedsattMed100 = this.nedsattMed100;

            this.dateRangeGroups = [this.nedsattMed25,this.nedsattMed50,this.nedsattMed75,this.nedsattMed100];

            this._$scope = _$scope;

            this.certModel = _$scope.cert;

            this.today = new Date();
            this.today.setHours(0, 0, 0, 0);

        };

        DateRangeGroupsService.prototype.validateDatesWithCert = function validateDatesWithCert(cert) {
            this.setCert(cert);
            this.setUseCert(true);
            this.validateDates();
            this.onArbetsformagaDatesUpdated();
            this.setUseCert(false);
        };

        DateRangeGroupsService.prototype.setCert = function setCert(cert) {
            angular.forEach(this.dateRangeGroups, function(dateRangeGroup){
                dateRangeGroup.setCert(cert);
            });
        };

        /**
         * Revalidate 8b dates
         */
        DateRangeGroupsService.prototype.validateDates = function validateDates() {
            this.resetDateInvalidStates();
            this.validateDateRangeOrder(); // Set invalid if from dates are after tom dates
            this.validateDatePeriods(); // Set invalid if date periods overlap
        };

        DateRangeGroupsService.prototype.setUseCert = function setUseCert(value) {
            angular.forEach(this.dateRangeGroups, function(dateRangeGroup){
                dateRangeGroup.setUseCert(value);
            });
        };

        DateRangeGroupsService.prototype.resetDateInvalidStates = function resetDateInvalidStates() {
            angular.forEach(this.dateRangeGroups, function(dateRangeGroup){
                dateRangeGroup.setDateInvalidState(false);
            });
        };

        DateRangeGroupsService.prototype.validateDateRangeOrder = function validateDateRangeOrder() {

            // Update others still marked as invalid as well if they previously conflicted with the changed one
            angular.forEach(this.dateRangeGroups, function(dateRangeGroup){
                // do the validity check
                dateRangeGroup.setDateValidity();
            });
        }

        /**
         * Validate 8b date periods so they don't overlap or wrap in any way
         */
        DateRangeGroupsService.prototype.validateDatePeriods = function validateDatePeriods() {
            var self = this;
            var compared = [];
            var ids = [];
            angular.forEach(self.dateRangeGroups, function(dateRangeGroup){

                // check with all other period groups after nedsatt period if periods overlap
                angular.forEach(self.dateRangeGroups, function(compareNedsattGroup){
                    ids[0] = dateRangeGroup.id + compareNedsattGroup.id;
                    ids[1] = compareNedsattGroup.id + dateRangeGroup.id;
                    // filter out comparison based on previous values
                    if(compared.indexOf(ids[0]) < 0 && compared.indexOf(ids[1]) < 0){
                        dateRangeGroup.markDateRangeValidity(compareNedsattGroup);
                        compared.push( dateRangeGroup.id+compareNedsattGroup.id );
                    }
                });
            });
        }

        /**
         * 8b: Called when checks or dates for Arbetsförmåga are changed. Update dependency controls here
         */
        DateRangeGroupsService.prototype.onArbetsformagaDatesUpdated = function onArbetsformagaDatesUpdated() {

            var startEndMoments = this.findStartEndMoments();

            this.updateTotalCertDays(startEndMoments);
            this.checkArbetsformagaDatesRange(startEndMoments.minMoment);
            this.checkArbetsformagaDatesPeriodLength(startEndMoments.minMoment, startEndMoments.maxMoment);

        };

        /**
         * 8b: find earliest and latest dates (as moment objects) for arbetsförmåga
         * @returns {{minMoment: null, maxMoment: null}}
         */
        DateRangeGroupsService.prototype.findStartEndMoments = function findStartEndMoments() {
            var moments = {
                minMoment: null,
                maxMoment: null
            };
            var startMoments = [];
            var endMoments = [];

            // these should be fetched from the model
            angular.forEach(this.dateRangeGroups, function(dateRangeGroup){
                if(!dateRangeGroup.toBeforeFrom) {
                    dateRangeGroup.useCert = true;
                    var dateValue = dateRangeGroup.momentStrictFrom();
                    if (dateValue && dateValue.isValid()) {
                        startMoments.push(dateValue);
                    }
                    dateValue = dateRangeGroup.momentStrictTom();
                    if (dateValue && dateValue.isValid()) {
                        endMoments.push(dateValue);
                    }
                }
            });

            if (startMoments.length > 0) {
                moments.minMoment = moment.min(startMoments);
            }
            if (endMoments.length > 0) {
                moments.maxMoment = moment.max(endMoments);
            }
            return moments;
        };

        /**
         * Calculate total days between the earliest and the latest dates supplied in the 8b controls
         * @type {boolean}
         */
        DateRangeGroupsService.prototype.updateTotalCertDays = function(moments) {
            var count = 0;
            angular.forEach(this.dateRangeGroups, function(dateRangeGroup){
                count += dateRangeGroup.daysBetween;
            });
            this._$scope.totalCertDays = count;
        };

        /**
         * 8b: Check that the earliest startdate in arbetsförmåga is no more than a week before today and no more than 6 months in the future
         * @type {boolean}
         */
        DateRangeGroupsService.prototype.checkArbetsformagaDatesRange = function checkArbetsformagaDatesRange(startMoment) {
            this.datesOutOfRange = (dateUtils.olderThanAWeek(startMoment) || dateUtils.isDateOutOfRange(startMoment));
        };

        /**
         * 8b: Check that the period between the earliest startdate and the latest end date is no more than 6 months in the future
         * @type {boolean}
         */
        DateRangeGroupsService.prototype.checkArbetsformagaDatesPeriodLength = function checkArbetsformagaDatesPeriodLength(startMoment, endMoment) {
            this.datesPeriodTooLong = !dateUtils.areDatesWithinMonthRange(startMoment, endMoment);
        };

        DateRangeGroupsService.prototype.onChangeWorkStateCheck = function(nedsattModelName) {
            if (this.certModel !== undefined) {
                var nedsatt = this[nedsattModelName];
                if (nedsatt.workState) {

                    // Set from date
                    // find highest max date
                    var moments = this.findStartEndMoments();
                    if (!nedsatt.certFrom() || !dateUtils.isDate(nedsatt.certFrom())) {

                        if (moments.maxMoment !== null && moments.maxMoment.isValid()) {
                            nedsatt.setCertFrom(moments.maxMoment.add('days', 1).format('YYYY-MM-DD'));
                        } else {
                            // if no max moment is available, use today
                            nedsatt.setCertFrom(moment(this.today).format('YYYY-MM-DD'));
                        }

                    }

                    // Set tom date
                    if (nedsatt.certFrom() && (!nedsatt.certTom() || !dateUtils.isDate(nedsatt.certTom()))) {
                        nedsatt.setCertTom( dateUtils.toMoment(nedsatt.certFrom()).add('days', 6).format('YYYY-MM-DD') );
                    }
                } else {
                    // Remove dates
                    nedsatt.setCertFrom( undefined );
                    nedsatt.setCertTom( undefined );
                    nedsatt.nedsattFrom('');
                    nedsatt.nedsattTom('');
                }
                // re validate dates
                this.setUseCert(true);
                this.validateDates();
                this.onArbetsformagaDatesUpdated();
                this.setUseCert(false);
            }
        };

        // static build ************************************************************************************************
        DateRangeGroupsService.build = function(_$scope){
            return new DateRangeGroupsService(_$scope);
        }

        /**
         * Return the constructor function DateRangeGroupModel
         */
        return DateRangeGroupsService;

    }]);