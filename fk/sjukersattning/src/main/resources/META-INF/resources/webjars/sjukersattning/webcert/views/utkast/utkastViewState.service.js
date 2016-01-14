angular.module('sjukersattning').service('sjukersattning.EditCertCtrl.ViewStateService',
    ['$log', 'common.UtkastViewStateService', 'sjukersattning.Domain.IntygModel', 'sjukersattning.EditCertCtrl.Helper',
        function($log, CommonViewState, IntygModel, helper) {
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

            this.extraqs = [
                { 'id': 0, label: 'Ange 1', help: 'Hjalp hjalp 1!', model: 'Test1'},
                { 'id': 1, label: 'Ange 2', help: 'Hjalp hjalp 2!', model: 'Test2'},
            ];

            this.underlagOptions = [
                { 'id': 0, label: 'Ange underlag eller utredning'},
                { 'id': 1, label: 'Neuropsykiatriskt utlåtande'},
                { 'id': 2, label: 'Underlag från habiliteringen' },
                { 'id': 3, label: 'Underlag från arbetsterapeut' },
                { 'id': 4, label: 'Underlag från fysioterapeut' },
                { 'id': 5, label: 'Underlag från logoped' },
                { 'id': 6, label: 'Underlag från psykolog' },
                { 'id': 7, label: 'Underlag från företagshälsovård' },
                { 'id': 8, label: 'Utredning av annan specialistklinik'},
                { 'id': 9, label: 'Utredning från vårdinrättning utomlands' },
                { 'id': 10, label: 'Övrigt' }
            ];

            this.funktionsnedsattningOptions = [
                { 'id': 1, 'label': 'Intellektuell funktion', selected: false, helpText: 'example helptext1', text : null } ,
                { 'id': 2, 'label': 'Kommunikation och social interaktion', selected: false, helpText: 'example helptext2', text : null },
                { 'id': 3, 'label': 'Uppmärksamhet och koncentration', selected: false, helpText: 'example helptext3', text : null },
                { 'id': 4, 'label': 'Annan psykisk funktion', selected: false, helpText: 'example helptext4',text : null },
                { 'id': 5, 'label': 'Syn, hörsel och tal', selected: false, helpText: 'example helptext5', text : null },
                { 'id': 6, 'label': 'Balans, koordination och motorik', selected: false, helpText: 'example helptext6', text : null },
                { 'id': 7, 'label': 'Annan kroppslig funktion', selected: false, helpText: 'example helptext7', text : null },
                { 'id': 8, 'label': 'Okänd', selected: false, text : null}
            ];

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