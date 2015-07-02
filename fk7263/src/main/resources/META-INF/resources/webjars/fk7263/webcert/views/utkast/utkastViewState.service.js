angular.module('fk7263').service('fk7263.EditCertCtrl.ViewStateService',
    ['$log', 'common.UtkastViewStateService', 'fk7263.Domain.IntygModel', 'fk7263.EditCertCtrl.Helper',
        function($log, CommonViewState, IntygModel, helper) {
            'use strict';

            this.common = CommonViewState;

            this.intygModel = undefined;
            this.draftModel = undefined;

            this.setDraftModel = function(draftModel){
                this.draftModel = draftModel;
                this.intygModel = draftModel.content;
                this.avstangningSmittskyddValue = this.intygModel.avstangningSmittskydd;
                this.sysselsattningValue = [this.intygModel.nuvarandeArbete, this.intygModel.arbetsloshet, this.intygModel.foraldrarledighet];
            };

            this.avstangningSmittskydd = function() {
                return this.intygModel.avstangningSmittskydd;
            };

            this.isArbetslos = function() {
                var sysselsattning = [this.intygModel.nuvarandeArbete, this.intygModel.arbetsloshet, this.intygModel.foraldrarledighet];

                if (JSON.stringify(sysselsattning) === JSON.stringify([false, true, false])) {
                    return true;
                }
                return false;
            };

            this.inputLimits = {
                arbetsformagaPrognos: 600,
                nuvarandeArbetsuppgifter: 120,
                atgardInomSjukvarden: 66,
                annanAtgard: 66,
                aktivitetsbegransning: 1100,
                funktionsnedsattning: 450,
                sjukdomsforlopp: 270,
                diagnosBeskrivning :220,
                ovrigt: 360 // = combined field 13 (and dependencies that end up in field 13) limit
            };

            this.reset = function() {
                CommonViewState.reset();
                CommonViewState.intyg.type = 'fk7263';
                this.setDraftModel(IntygModel._members.build());
                return this;
            };

            this.clearModel = function(){
                this.intygModel = undefined;
                this.draftModel = undefined;
            };

            /**
             * Calculate total length of all fields ending up in Övrigt in the external model
             * @returns {*}
             */
            this.getTotalOvrigtLength = function() {

                var totalOvrigtLength = 0;
                var hasNedsatt = this.intygModel.kommentar ||
                    this.intygModel.annanReferensBeskrivning ||
                    this.intygModel.nedsattMed25Beskrivning ||
                    this.intygModel.nedsattMed50Beskrivning ||
                    this.intygModel.nedsattMed75Beskrivning ||
                    this.intygModel.arbetsformagaPrognosGarInteAttBedomaBeskrivning;
                if(hasNedsatt) {
                    totalOvrigtLength += helper.getLengthOrZero(this.intygModel.kommentar) +
                    helper.getLengthOrZero(this.intygModel.annanReferensBeskrivning) +
                    helper.getLengthOrZero(this.intygModel.nedsattMed25Beskrivning) +
                    helper.getLengthOrZero(this.intygModel.nedsattMed50Beskrivning) +
                    helper.getLengthOrZero(this.intygModel.nedsattMed75Beskrivning) +
                    helper.getLengthOrZero(this.intygModel.arbetsformagaPrognosGarInteAttBedomaBeskrivning);
                }
                return totalOvrigtLength;
            };

            /**
             * Limit length of field dependent on field 13 in the external model
             * @param field
             */
            this.limitOtherField = function(field) {
                if (this.intygModel[field]) {
                    this.intygModel[field] = this.limitOvrigtLength(this.intygModel[field]);
                }
            };

            this.limitOvrigtLength = function (val) {
                var totalOvrigtLength = this.getTotalOvrigtLength();
                if (totalOvrigtLength > this.inputLimits.ovrigt) {
                    // Remove characters over limit from current field
                    return val.substr(0, val.length - (totalOvrigtLength - this.inputLimits.ovrigt));
                }
                return val;
            };

            this.reset();
        }]);