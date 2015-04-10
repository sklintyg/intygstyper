angular.module('ts-bas').service('ts-bas.UtkastController.ViewStateService',
    ['$log', 'ts-bas.Domain.IntygModel', 'common.IntygEditViewStateService',
        function($log, intygModel, CommonViewState) {
            'use strict';

            this.common = CommonViewState;

            // Input limit handling
            this.focusFirstInput = true;
            this.testerror = false;
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

        }]);