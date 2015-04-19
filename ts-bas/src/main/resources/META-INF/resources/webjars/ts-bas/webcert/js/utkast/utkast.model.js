angular.module('ts-bas').factory('ts-bas.Domain.IntygModel',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
        'common.domain.BaseAtticModel',
        function(GrundData, DraftModel, ModelAttr, BaseAtticModel) {
        'use strict';

        /**
         * Constructor, with class name
         */
        var TsBasModel = BaseAtticModel._extend({
            init: function init() {
                var grundData = GrundData.build();
                init._super.call(this, 'TsBasModel', {
                    id: undefined,
                    typ: undefined, // NOT intygtyp, this is TS-intyg type.
                    kommentar: undefined,
                    grundData: grundData,
                    vardkontakt: {
                        typ: undefined,
                        idkontroll: undefined
                    },
                    intygAvser: {
                        'korkortstyp': new ModelAttr('korkortstyp',
                            {defaultValue:[
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
                        ]})
                    },
                    syn: {
                        'synfaltsdefekter': undefined,
                        'nattblindhet': undefined,
                        'progressivOgonsjukdom': undefined,
                        'diplopi': undefined,
                        'nystagmus': undefined,
                        'hogerOga': {
                            'utanKorrektion': undefined,
                            'medKorrektion': undefined,
                            'kontaktlins': undefined
                        },

                        'vansterOga': {
                            'utanKorrektion': undefined,
                            'medKorrektion': undefined,
                            'kontaktlins': undefined
                        },

                        'binokulart': {
                            'utanKorrektion': undefined,
                            'medKorrektion': undefined
                        },
                        'korrektionsglasensStyrka': undefined
                    },
                    horselBalans: {
                        'balansrubbningar': undefined,
                        'svartUppfattaSamtal4Meter': undefined
                    },
                    funktionsnedsattning: {
                        'funktionsnedsattning': undefined,
                        'otillrackligRorelseformaga': undefined,
                        'beskrivning': undefined
                    },
                    hjartKarl: {
                        'hjartKarlSjukdom': undefined,
                        'hjarnskadaEfterTrauma': undefined,
                        'riskfaktorerStroke': undefined,
                        'beskrivningRiskfaktorer': undefined
                    },
                    diabetes: {
                        'harDiabetes': undefined,
                        'diabetesTyp': undefined,
                        'tabletter' : undefined,
                        'insulin' : undefined,
                        'kost': undefined
                    },
                    neurologi: {
                        'neurologiskSjukdom': undefined
                    },
                    medvetandestorning: {
                        'medvetandestorning': undefined,
                        'beskrivning': undefined
                    },
                    njurar: {
                        'nedsattNjurfunktion': undefined
                    },
                    kognitivt: {
                        'sviktandeKognitivFunktion': undefined
                    },
                    somnVakenhet: {
                        'teckenSomnstorningar': undefined
                    },
                    narkotikaLakemedel: {
                        'teckenMissbruk': undefined,
                        'foremalForVardinsats': undefined,
                        'lakarordineratLakemedelsbruk': undefined,
                        'lakemedelOchDos': undefined,
                        'provtagningBehovs': undefined
                    },
                    psykiskt: {
                        'psykiskSjukdom': undefined
                    },
                    utvecklingsstorning: {
                        'psykiskUtvecklingsstorning': undefined,
                        'harSyndrom': undefined
                    },
                    sjukhusvard: {
                        'sjukhusEllerLakarkontakt': undefined,
                        'tidpunkt': undefined,
                        'vardinrattning': undefined,
                        'anledning': undefined
                    },
                    medicinering: {
                        'stadigvarandeMedicinering': undefined,
                        'beskrivning': undefined
                    },
                    bedomning: {
                        'korkortstyp': new ModelAttr('korkortstyp',
                            {defaultValue:[
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
                        ]}),
                        'kanInteTaStallning': undefined,
                        'lakareSpecialKompetens': undefined
                    }
                });
            },
            update: function update(content, parent) {
                if (parent) {
                    parent.content = this;
                }
                update._super.call(this, content);
            }
        }, {
            build : function(){
                return new DraftModel(new TsBasModel());
            }
        });

        /**
         * Return the constructor function IntygModel
         */
        return TsBasModel;
    }]);