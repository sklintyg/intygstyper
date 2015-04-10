angular.module('ts-bas').service('ts-bas.UtkastController.ViewStateService',
    ['$log', 'ts-bas.Domain.IntygModel', 'common.UtkastViewStateService',
        function($log, intygModel, CommonViewState) {
            'use strict';

            this.common = CommonViewState;

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