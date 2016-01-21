angular.module('sjukersattning').service('sjukersattning.EditCertCtrl.ViewStateService',
    ['$log', 'common.UtkastViewStateService', 'sjukersattning.Domain.IntygModel', 'sjukersattning.EditCertCtrl.Helper',
        'common.dynamicLabelService',
        function($log, CommonViewState, IntygModel, helper, dynamicLabelService) {
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
                planeradBehandling: 999
            };

            this.underlagOptions = [];

            this.updateUnderlagOptions = function() {

                this.underlagOptions = [
                    { 'id': null, label: 'Ange underlag eller utredning'},
                    { 'id': 'KV_FKMU_0005.1.RBK', label: dynamicLabelService.getProperty('KV_FKMU_0005.1.RBK')},
                    { 'id': 'KV_FKMU_0005.2.RBK', label: dynamicLabelService.getProperty('KV_FKMU_0005.2.RBK')},
                    { 'id': 'KV_FKMU_0005.3.RBK', label: dynamicLabelService.getProperty('KV_FKMU_0005.3.RBK')},
                    { 'id': 'KV_FKMU_0005.4.RBK', label: dynamicLabelService.getProperty('KV_FKMU_0005.4.RBK')},
                    { 'id': 'KV_FKMU_0005.5.RBK', label: dynamicLabelService.getProperty('KV_FKMU_0005.5.RBK')},
                    { 'id': 'KV_FKMU_0005.6.RBK', label: dynamicLabelService.getProperty('KV_FKMU_0005.6.RBK')},
                    { 'id': 'KV_FKMU_0005.7.RBK', label: dynamicLabelService.getProperty('KV_FKMU_0005.7.RBK')},
                    { 'id': 'KV_FKMU_0005.9.RBK', label: dynamicLabelService.getProperty('KV_FKMU_0005.9.RBK')},
                    { 'id': 'KV_FKMU_0005.10.RBK', label: dynamicLabelService.getProperty('KV_FKMU_0005.10.RBK')},
                    { 'id': 'KV_FKMU_0005.11.RBK', label: dynamicLabelService.getProperty('KV_FKMU_0005.11.RBK')},
                ];
            }

            this.reset = function() {
                CommonViewState.reset();
                CommonViewState.intyg.type = 'sjukersattning';
                this.setDraftModel(IntygModel._members.build());
                return this;
            };

            this.clearModel = function(){
                this.intygModel = undefined;
                this.draftModel = undefined;
            };

            this.reset();
        }]);