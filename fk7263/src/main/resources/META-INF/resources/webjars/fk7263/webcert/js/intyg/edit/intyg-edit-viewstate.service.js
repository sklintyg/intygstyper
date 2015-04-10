angular.module('fk7263').service('fk7263.EditCertCtrl.ViewStateService',
    ['$log', 'fk7263.Domain.IntygModel', 'common.UtkastViewStateService', 'fk7263.EditCertCtrl.Helper',
        function($log, intygModel, CommonViewState, helper) {
            'use strict';

            this.common = CommonViewState;

            this.avstangningSmittskyddVal = intygModel.avstangningSmittskydd;
            this.avstangningSmittskydd = function() {
                return intygModel.avstangningSmittskydd;
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
            }

            /**
             * Calculate total length of all fields ending up in Övrigt in the external model
             * @returns {*}
             */
            this.getTotalOvrigtLength = function() {

                var totalOvrigtLength = 0;
                var hasNedsatt = intygModel.kommentar ||
                    intygModel.annanReferensBeskrivning ||
                    intygModel.nedsattMed25Beskrivning ||
                    intygModel.nedsattMed50Beskrivning ||
                    intygModel.nedsattMed75Beskrivning ||
                    intygModel.arbetsformagaPrognosGarInteAttBedomaBeskrivning;
                if(hasNedsatt) {
                    totalOvrigtLength += helper.getLengthOrZero(intygModel.kommentar) +
                    helper.getLengthOrZero(intygModel.annanReferensBeskrivning) +
                    helper.getLengthOrZero(intygModel.nedsattMed25Beskrivning) +
                    helper.getLengthOrZero(intygModel.nedsattMed50Beskrivning) +
                    helper.getLengthOrZero(intygModel.nedsattMed75Beskrivning) +
                    helper.getLengthOrZero(intygModel.arbetsformagaPrognosGarInteAttBedomaBeskrivning);
                }
                return totalOvrigtLength;
            };

            /**
             * Limit length of field dependent on field 13 in the external model
             * @param field
             */
            this.limitOtherField = function(field) {
                if (intygModel[field]) {
                    intygModel[field] = this.limitOvrigtLength(intygModel[field]);
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

        }]);