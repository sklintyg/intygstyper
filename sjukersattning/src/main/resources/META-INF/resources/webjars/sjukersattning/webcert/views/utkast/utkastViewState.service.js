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

            this.underlagOptions = [
                { 'sortValue': 0, 'id': 'default', label: 'Ange underlag eller utredning'},
                { 'sortValue': 1, 'id': 'Neuropsykiatriskt utlatande', label: 'Neuropsykiatriskt utlåtande'},
                { 'sortValue': 2, 'id': 'Underlag fran habiliteringen', label: 'Underlag från habiliteringen' },
                { 'sortValue': 3, 'id': 'underlagFranarbetsterapeut', label: 'Underlag från arbetsterapeut' },
                { 'sortValue': 4, 'id': 'underlagFranfysioterapeut', label: 'Underlag från fysioterapeut' },
                { 'sortValue': 5, 'id': 'underlagFranlogoped', label: 'Underlag från logoped' },
                { 'sortValue': 6, 'id': 'underlagFranpsykolog', label: 'Underlag från psykolog' },
                { 'sortValue': 7, 'id': 'underlagFranföretagshalsovard', label: 'Underlag från företagshälsovård' },
                { 'sortValue': 8, 'id': 'utredningAvAnnanSpecialistklinik', label: 'Utredning av annan specialistklinik'},
                { 'sortValue': 9, 'id': 'utredningFranVardinrattningUtomlands', label: 'Utredning från vårdinrättning utomlands' },
                { 'sortValue': 10, 'id': 'ovrigt', label: 'Övrigt' }
            ];

            this.funktionsnedsattningOptions = [
                { 'sortValue': 1, 'id': 'intellektuell', 'label': 'Intellektuell funktion', selected: false, helpText: 'example helptext1', text : null } ,
                { 'sortValue': 2, 'id': 'kommunikation', 'label': 'Kommunikation och social interaktion', selected: false, helpText: 'example helptext2', text : null },
                { 'sortValue': 3, 'id': 'koncentration', 'label': 'Uppmärksamhet och koncentration', selected: false, helpText: 'example helptext3', text : null },
                { 'sortValue': 4, 'id': 'annan_psykisk', 'label': 'Annan psykisk funktion', selected: false, helpText: 'example helptext4',text : null },
                { 'sortValue': 5, 'id': 'syn_horsel_tal', 'label':'Syn, hörsel och tal', selected: false, helpText: 'example helptext5', text : null },
                { 'sortValue': 6, 'id': 'balans', 'label': 'Balans, koordination och motorik', selected: false, helpText: 'example helptext6', text : null },
                { 'sortValue': 7, 'id': 'annan_kroppslig', 'label': 'Annan kroppslig funktion', selected: false, helpText: 'example helptext7', text : null },
                { 'sortValue': 8, 'id': 'okand', 'label': 'Okänd', selected: false, text : null}
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