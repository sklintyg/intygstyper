angular.module('ts-bas').service('ts-bas.UtkastController.ViewStateService',
    ['$log', 'ts-bas.Domain.IntygModel', 'common.UtkastViewStateService',
        function($log, IntygModel, CommonViewState) {
            'use strict';

            this.common = CommonViewState;

            this.intygModel = undefined;
            this.draftModel = undefined;

            this.clearModel = function() {
                this.intygModel = undefined;
                this.draftModel = undefined;
            };

            this.setDraftModel = function(draftModel) {
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
                this.korkortd = false;
                this.identitet = [
                    {label: 'ID-kort *', id: 'ID_KORT'},
                    {label: 'Företagskort eller tjänstekort **', id: 'FORETAG_ELLER_TJANSTEKORT'},
                    {label: 'Svenskt körkort', id: 'KORKORT'},
                    {label: 'Personlig kännedom', id: 'PERS_KANNEDOM'},
                    {label: 'Försäkran enligt 18 kap. 4§ ***', id: 'FORSAKRAN_KAP18'},
                    {label: 'Pass ****', id: 'PASS'}
                ];
                this.behorighet = null;
                this.kravYtterligareUnderlag = false; // if any field is set to JA this should be true to show info.

                CommonViewState.reset();
                CommonViewState.intyg.type = 'ts-bas';
                this.setDraftModel(IntygModel._members.build());
                return this;
            };

            this.setBehorighet = function(model){
                if (model.bedomning && model.bedomning.kanInteTaStallning) {
                    this.behorighet = 'KANINTETASTALLNING';
                } else {
                    this.behorighet = 'BEDOMNING';
                }
            };

            // This is not so pretty, but necessary? Can it be improved?
            // this could be moved into the model as a linked property, although thats alot of properties ...
            // maybe introduce an after update so the update of the linked property happens at
            // the end of update cycle instead of each individual property change.
            function noKravYtterligareUnderlagFieldsFilled(intygModel) {
                return (!intygModel.syn && !intygModel.horselBalans && !intygModel.funktionsnedsattning &&
                    !intygModel.hjartKarl && !intygModel.diabetes && !intygModel.neurologi &&
                    !intygModel.medvetandestorning && !intygModel.njurar && !intygModel.kognitivt &&
                    !intygModel.somnVakenhet && !intygModel.narkotikaLakemedel && !intygModel.psykiskt &&
                    !intygModel.utvecklingsstorning && !intygModel.sjukhusvard && !intygModel.medicinering);
            }

            function anyKravYtterligareUnderlagFieldsFilled(intygModel) {
                return (intygModel.syn.synfaltsdefekter === true || intygModel.syn.nattblindhet === true ||
                    intygModel.syn.progressivOgonsjukdom === true || intygModel.syn.diplopi === true ||
                    intygModel.syn.nystagmus === true || intygModel.horselBalans.balansrubbningar === true ||
                    intygModel.horselBalans.svartUppfattaSamtal4Meter === true ||
                    intygModel.funktionsnedsattning.funktionsnedsattning === true ||
                    intygModel.funktionsnedsattning.otillrackligRorelseformaga === true ||
                    intygModel.hjartKarl.hjartKarlSjukdom === true ||
                    intygModel.hjartKarl.hjarnskadaEfterTrauma === true ||
                    intygModel.hjartKarl.riskfaktorerStroke === true ||
                    intygModel.diabetes.harDiabetes === true || intygModel.neurologi.neurologiskSjukdom === true ||
                    intygModel.medvetandestorning.medvetandestorning === true ||
                    intygModel.njurar.nedsattNjurfunktion === true ||
                    intygModel.kognitivt.sviktandeKognitivFunktion === true ||
                    intygModel.somnVakenhet.teckenSomnstorningar === true ||
                    intygModel.narkotikaLakemedel.teckenMissbruk === true ||
                    intygModel.narkotikaLakemedel.foremalForVardinsats === true ||
                    intygModel.narkotikaLakemedel.provtagningBehovs === true ||
                    intygModel.narkotikaLakemedel.lakarordineratLakemedelsbruk ||
                    intygModel.psykiskt.psykiskSjukdom === true ||
                    intygModel.utvecklingsstorning.psykiskUtvecklingsstorning === true ||
                    intygModel.utvecklingsstorning.harSyndrom === true ||
                    intygModel.sjukhusvard.sjukhusEllerLakarkontakt === true ||
                    intygModel.medicinering.stadigvarandeMedicinering === true);
            }

            this.updateKravYtterligareUnderlag = function() {
                this.kravYtterligareUnderlag = false;
                if (!this.intygModel || noKravYtterligareUnderlagFieldsFilled(this.intygModel)) {
                    this.kravYtterligareUnderlag = false;
                } else if(anyKravYtterligareUnderlagFieldsFilled(this.intygModel)) {
                    this.kravYtterligareUnderlag = true;
                }
            };

            this.reset();
        }]);