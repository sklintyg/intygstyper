angular.module('luae_na').service('luae_na.EditCertCtrl.ViewStateService',
    ['$log', '$state', 'common.UtkastViewStateService', 'luae_na.Domain.IntygModel',
        'common.dynamicLabelService',
        function($log, $state, CommonViewState, IntygModel, dynamicLabelService) {
            'use strict';

            this.common = CommonViewState;

            this.intygModel = undefined;
            this.draftModel = undefined;

            this.setDraftModel = function(draftModel){
                this.draftModel = draftModel;
                this.intygModel = draftModel.content;
                this.underlagOptions = [];
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
                planeradBehandling: 999,
                forslagTillAtgard: 999
            };

            this.underlagOptions = [];

            this.updateUnderlagOptions = function() {

                this.underlagOptions = [
                    { 'id': null, label: 'Ange underlag eller utredning'},
                    { 'id': 1, label: dynamicLabelService.getProperty('KV_FKMU_0005.1.RBK')},
                    { 'id': 2, label: dynamicLabelService.getProperty('KV_FKMU_0005.2.RBK')},
                    { 'id': 3, label: dynamicLabelService.getProperty('KV_FKMU_0005.3.RBK')},
                    { 'id': 4, label: dynamicLabelService.getProperty('KV_FKMU_0005.4.RBK')},
                    { 'id': 5, label: dynamicLabelService.getProperty('KV_FKMU_0005.5.RBK')},
                    { 'id': 6, label: dynamicLabelService.getProperty('KV_FKMU_0005.6.RBK')},
                    { 'id': 7, label: dynamicLabelService.getProperty('KV_FKMU_0005.7.RBK')},
                    { 'id': 9, label: dynamicLabelService.getProperty('KV_FKMU_0005.9.RBK')},
                    { 'id': 10, label: dynamicLabelService.getProperty('KV_FKMU_0005.10.RBK')},
                    { 'id': 11, label: dynamicLabelService.getProperty('KV_FKMU_0005.11.RBK')}
                ];
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