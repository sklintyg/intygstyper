angular.module('ts-bas').service('ts-bas.UtkastController.ViewStateService',
    ['$log', 'ts-bas.Domain.IntygModel', 'common.UtkastViewStateService',
        function($log, IntygModel, CommonViewState) {
            'use strict';

            this.common = CommonViewState;

            this.intygModel = undefined;
            this.draftModel = undefined;

            this.clearModel = function() {
                CommonViewState.intygModel = undefined;
                CommonViewState.draftModel = undefined;
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
                this.focusFirstInput = true;
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
            };

            this.setBehorighet = function(model){
                if (model.bedomning && model.bedomning.kanInteTaStallning) {
                    this.behorighet = 'KANINTETASTALLNING';
                } else {
                    this.behorighet = 'BEDOMNING';
                }
            }

            // This is not so pretty, but necessary? Can it be improved?
            this.updateKravYtterligareUnderlag = function() {
                
                if (!this.intygModel ||
                    (!this.intygModel.syn && !this.intygModel.horselBalans && !this.intygModel.funktionsnedsattning &&
                        !this.intygModel.hjartKarl && !this.intygModel.diabetes && !this.intygModel.neurologi &&
                        !this.intygModel.medvetandestorning && !this.intygModel.njurar && !this.intygModel.kognitivt &&
                        !this.intygModel.somnVakenhet && !this.intygModel.narkotikaLakemedel && !this.intygModel.psykiskt &&
                        !this.intygModel.utvecklingsstorning && !this.intygModel.sjukhusvard && !this.intygModel.medicinering)) {
                    this.kravYtterligareUnderlag = false;
                } else if (this.intygModel.syn.synfaltsdefekter === true || this.intygModel.syn.nattblindhet === true ||
                    this.intygModel.syn.progressivOgonsjukdom === true || this.intygModel.syn.diplopi === true ||
                    this.intygModel.syn.nystagmus === true || this.intygModel.horselBalans.balansrubbningar === true ||
                    this.intygModel.horselBalans.svartUppfattaSamtal4Meter === true ||
                    this.intygModel.funktionsnedsattning.funktionsnedsattning === true ||
                    this.intygModel.funktionsnedsattning.otillrackligRorelseformaga === true ||
                    this.intygModel.hjartKarl.hjartKarlSjukdom === true ||
                    this.intygModel.hjartKarl.hjarnskadaEfterTrauma === true ||
                    this.intygModel.hjartKarl.riskfaktorerStroke === true ||
                    this.intygModel.diabetes.harDiabetes === true || this.intygModel.neurologi.neurologiskSjukdom === true ||
                    this.intygModel.medvetandestorning.medvetandestorning === true ||
                    this.intygModel.njurar.nedsattNjurfunktion === true ||
                    this.intygModel.kognitivt.sviktandeKognitivFunktion === true ||
                    this.intygModel.somnVakenhet.teckenSomnstorningar === true ||
                    this.intygModel.narkotikaLakemedel.teckenMissbruk === true ||
                    this.intygModel.narkotikaLakemedel.foremalForVardinsats === true ||
                    this.intygModel.narkotikaLakemedel.provtagningBehovs === true ||
                    this.intygModel.narkotikaLakemedel.lakarordineratLakemedelsbruk ||
                    this.intygModel.psykiskt.psykiskSjukdom === true ||
                    this.intygModel.utvecklingsstorning.psykiskUtvecklingsstorning === true ||
                    this.intygModel.utvecklingsstorning.harSyndrom === true ||
                    this.intygModel.sjukhusvard.sjukhusEllerLakarkontakt === true ||
                    this.intygModel.medicinering.stadigvarandeMedicinering === true) {
                    this.kravYtterligareUnderlag = true;
                } else {
                    this.kravYtterligareUnderlag = false;
                }
            };
        
            this.reset();

        }]);