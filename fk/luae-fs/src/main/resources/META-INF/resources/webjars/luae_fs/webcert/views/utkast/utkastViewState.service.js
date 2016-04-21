angular.module('luae_fs').service('luae_fs.EditCertCtrl.ViewStateService',
    ['$log', '$state', 'common.UtkastViewStateService', 'luae_fs.Domain.IntygModel',
        'common.dynamicLabelService',
        function($log, $state, CommonViewState, IntygModel, dynamicLabelService) {
            'use strict';

            this.common = CommonViewState;

            this.intygModel = undefined;
            this.draftModel = undefined;

            this.setDraftModel = function(draftModel){
                this.draftModel = draftModel;
                this.intygModel = draftModel.content;
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
                ovrigt: 360,
                finishedBehandling: 999,
                pagaendeBehandling: 999,
                planeradBehandling: 999
            };

            this.reset = function() {
                CommonViewState.reset();
                CommonViewState.intyg.type = $state.current.data.intygType;
                this.setDraftModel(IntygModel._members.build());
                return this;
            };

            this.clearModel = function(){
                this.intygModel = undefined;
                this.draftModel = undefined;
            };

            this.reset();
        }]);