angular.module('ts-bas').service('ts-bas.UtkastController.ViewStateService',
    ['$log', 'common.UtkastViewStateService',
        function($log, CommonViewState) {
            'use strict';

            this.common = CommonViewState;

            this.intygModel = undefined;
            this.draftModel = undefined;

            this.clearModel = function(){
                this.intygModel = undefined;
                this.draftModel = undefined;
            }

            this.setDraftModel = function(draftModel){
                this.draftModel = draftModel;
                this.intygModel = draftModel.content;
            };

            // STATIC CONSTANT: Input limits on text fields
            this.inputLimits = {
                'funktionsnedsattning': 180,
                'beskrivningRiskfaktorer': 180,
                'medvetandestorning': 180,
                'lakemedelOchDos': 180,
                'medicinering': 180,
                'kommentar': 500,
                'lakareSpecialKompetens': 130,
                'sjukhusvardtidpunkt': 40,
                'sjukhusvardvardinrattning': 40,
                'sjukhusvardanledning': 50
            };

            this.reset = function() {
                this.focusFirstInput = true;
                this.testerror = false;

                CommonViewState.reset();
            };

            this.reset();

        }]);