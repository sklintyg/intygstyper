angular.module('ts-bas').factory('ts-bas.Domain.IntygModel',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', function(grundData, draftModel) {
        'use strict';

        // private
        var _intygModel, _attic;


        /**
         * Constructor, with class name
         */
        function IntygModel() {

            this.id = undefined;

            this.typ = undefined; // NOT intygtyp, this is TS-intyg type.

            this.kommentar = undefined;
            
            this.grundData = grundData;

            this.vardkontakt = {
                typ: undefined,
                idkontroll: undefined
            };

            this.intygAvser = {
                'korkortstyp' : [
                    {'type': 'C1', 'selected': false},
                    {'type': 'C1E', 'selected': false},
                    {'type': 'C', 'selected': false},
                    {'type': 'CE', 'selected': false},
                    {'type': 'D1', 'selected': false},
                    {'type': 'D1E', 'selected': false},
                    {'type': 'D', 'selected': false},
                    {'type': 'DE', 'selected': false},
                    {'type': 'TAXI', 'selected': false},
                    {'type': 'ANNAT', 'selected': false}
                ]
            };

            this.syn = {
                'synfaltsdefekter' :  undefined,
                'nattblindhet' : undefined,
                'progressivOgonsjukdom' : undefined,
                'diplopi' : undefined,
                'nystagmus' : undefined,
                'hogerOga' : {
                'utanKorrektion' : undefined,
                    'medKorrektion' : undefined,
                    'kontaktlins' : undefined
                },

                'vansterOga' : {
                    'utanKorrektion' : undefined,
                        'medKorrektion' : undefined,
                        'kontaktlins' : undefined
                },

                'binokulart' : {
                    'utanKorrektion' : undefined,
                        'medKorrektion' : undefined
                },
                'korrektionsglasensStyrka' : undefined
            };

            this.horselBalans = {
                'balansrubbningar' : undefined,
                    'svartUppfattaSamtal4Meter' : undefined
            };

            this.funktionsnedsattning = {
                'funktionsnedsattning' : undefined,
                'otillrackligRorelseformaga' : undefined,
                'beskrivning' : undefined
            };

            this.hjartKarl = {
                'hjartKarlSjukdom' : undefined,
                'hjarnskadaEfterTrauma' : undefined,
                'riskfaktorerStroke' : undefined,
                'beskrivningRiskfaktorer' : undefined
            };

            this.diabetes = {
                'harDiabetes' : undefined,
                'diabetesTyp' : undefined,
                'kost' : undefined
            };

            this.neurologi = {
                'neurologiskSjukdom' : undefined
            };

            this.medvetandestorning = {
                'medvetandestorning' : undefined,
                'beskrivning' : undefined
            };

            this.njurar = {
                'nedsattNjurfunktion' : undefined
            };

            this.kognitivt = {
                'sviktandeKognitivFunktion' : undefined
            };

            this.somnVakenhet = {
                'teckenSomnstorningar' : undefined
            };

            this.narkotikaLakemedel = {
                'teckenMissbruk' : undefined,
                'foremalForVardinsats' : undefined,
                'lakarordineratLakemedelsbruk' : undefined,
                'lakemedelOchDos' : undefined,
                'provtagningBehovs' : undefined
            };

            this.psykiskt = {
                'psykiskSjukdom' : undefined
            };

            this.utvecklingsstorning = {
                'psykiskUtvecklingsstorning' : undefined,
                'harSyndrom' : undefined
            };

            this.sjukhusvard = {
                'sjukhusEllerLakarkontakt' : undefined,
                    'tidpunkt' : undefined,
                    'vardinrattning' : undefined,
                    'anledning' : undefined
            };

            this.medicinering = {
                'stadigvarandeMedicinering' : undefined,
                'beskrivning' : undefined
            };

            this.bedomning = {
                'korkortstyp' : [
                    {'type': 'C1', 'selected': false},
                    {'type': 'C1E', 'selected': false},
                    {'type': 'C', 'selected': false},
                    {'type': 'CE', 'selected': false},
                    {'type': 'D1', 'selected': false},
                    {'type': 'D1E', 'selected': false},
                    {'type': 'D', 'selected': false},
                    {'type': 'DE', 'selected': false},
                    {'type': 'TAXI', 'selected': false},
                    {'type': 'ANNAT', 'selected': false}
                ],
                'kanInteTaStallning' : undefined,
                'lakareSpecialKompetens' : undefined
            };
        }

        // override the original update method
        IntygModel.prototype.update = function(content) {
            // refresh the model data

            if (content !== undefined) {
                this.id = content.id;
                this.typ = content.typ;

                this.kommentar = content.kommentar;

                this.grundData = content.grundData;

                this.vardkontakt = {
                    typ: content.vardkontakt.typ,
                    idkontroll: content.vardkontakt.idkontroll
                };

                this.intygAvser = {
                    'korkortstyp' : content.intygAvser.korkortstyp
                };

                this.syn = {
                    'synfaltsdefekter' :  content.syn.synfaltsdefekter,
                    'nattblindhet' : content.syn.nattblindhet,
                    'progressivOgonsjukdom' : content.syn.progressivOgonsjukdom,
                    'diplopi' : content.syn.diplopi,
                    'nystagmus' : content.syn.nystagmus,
                    'hogerOga' : content.syn.hogerOga,

                    'vansterOga' : content.syn.vansterOga,

                    'binokulart' : content.syn.binokulart,
                    'korrektionsglasensStyrka' : content.syn.korrektionsglasensStyrka
                };

                this.horselBalans = {
                    'balansrubbningar' : content.horselBalans.balansrubbningar,
                    'svartUppfattaSamtal4Meter' : content.horselBalans.svartUppfattaSamtal4Meter
                };

                this.funktionsnedsattning = {
                    'funktionsnedsattning' : content.funktionsnedsattning.funktionsnedsattning,
                    'otillrackligRorelseformaga' : content.funktionsnedsattning.otillrackligRorelseformaga,
                    'beskrivning' : content.funktionsnedsattning.beskrivning
                };

                this.hjartKarl = {
                    'hjartKarlSjukdom' : content.hjartKarl.hjartKarlSjukdom,
                    'hjarnskadaEfterTrauma' : content.hjartKarl.hjarnskadaEfterTrauma,
                    'riskfaktorerStroke' : content.hjartKarl.riskfaktorerStroke,
                    'beskrivningRiskfaktorer' : content.hjartKarl.beskrivningRiskfaktorer
                };

                this.diabetes = {
                    'harDiabetes' : content.diabetes.harDiabetes,
                    'diabetesTyp' : content.diabetes.diabetesTyp,
                    'kost' : content.diabetes.kost
                };

                this.neurologi = {
                    'neurologiskSjukdom' : content.neurologi.neurologiskSjukdom
                };

                this.medvetandestorning = {
                    'medvetandestorning' : content.medvetandestorning.medvetandestorning,
                    'beskrivning' : content.medvetandestorning.beskrivning
                };

                this.njurar = {
                    'nedsattNjurfunktion' : content.njurar.nedsattNjurfunktion
                };

                this.kognitivt = {
                    'sviktandeKognitivFunktion' : content.kognitivt.sviktandeKognitivFunktion
                };

                this.somnVakenhet = {
                    'teckenSomnstorningar' : content.somnVakenhet.teckenSomnstorningar
                };

                this.narkotikaLakemedel = {
                    'teckenMissbruk' : content.narkotikaLakemedel.teckenMissbruk,
                    'foremalForVardinsats' : content.narkotikaLakemedel.foremalForVardinsats,
                    'lakarordineratLakemedelsbruk' : content.narkotikaLakemedel.lakarordineratLakemedelsbruk,
                    'lakemedelOchDos' : content.narkotikaLakemedel.lakemedelOchDos,
                    'provtagningBehovs' : content.narkotikaLakemedel.provtagningBehovs
                };

                this.psykiskt = {
                    'psykiskSjukdom' : content.psykiskt.psykiskSjukdom
                };

                this.utvecklingsstorning = {
                    'psykiskUtvecklingsstorning' : content.utvecklingsstorning.psykiskUtvecklingsstorning,
                    'harSyndrom' : content.utvecklingsstorning.harSyndrom
                };

                this.sjukhusvard = {
                    'sjukhusEllerLakarkontakt' : content.sjukhusvard.sjukhusEllerLakarkontakt,
                    'tidpunkt' : content.sjukhusvard.tidpunkt,
                    'vardinrattning' : content.sjukhusvard.vardinrattning,
                    'anledning' : content.sjukhusvard.anledning
                };

                this.medicinering = {
                    'stadigvarandeMedicinering' : content.medicinering.stadigvarandeMedicinering,
                    'beskrivning' : content.medicinering.beskrivning
                };

                this.bedomning = {
                    'korkortstyp' : content.bedomning.korkortstyp,
                    'kanInteTaStallning' : content.bedomning.kanInteTaStallning,
                    'lakareSpecialKompetens' : content.bedomning.lakareSpecialKompetens
                };
            }
        };

        IntygModel.prototype.prepare = function() {
        };

        // attic functions
        IntygModel.prototype.attic = function(){
            return _attic;
        };

        _intygModel = new IntygModel();
        _attic = new IntygModel();

        draftModel.content = _intygModel;

        /**
         * Return the constructor function IntygModel
         */
        return _intygModel;
    }]);