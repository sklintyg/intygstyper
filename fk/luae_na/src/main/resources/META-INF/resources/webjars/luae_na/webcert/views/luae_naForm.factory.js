angular.module('luae_na').factory('luae_na.FormFactory',
    ['luae_na.FormFactoryHelper', 'common.UserModel', 'common.FactoryTemplatesHelper',
        function(FactoryHelper, UserModel, FactoryTemplates) {
            'use strict';

            var categoryNames = {
                1:'grundformu',
                2:'underlag',
                3:'sjukdomsforlopp',
                4:'diagnos',
                5:'funktionsnedsattning',
                6:'aktivitetsbegransning',
                7:'medicinskabehandlingar',
                8:'medicinskaforutsattningarforarbete',
                9:'ovrigt',
                10:'kontakt'
            };

            var formFields = [
                FactoryTemplates.adress,
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 1, categoryName: categoryNames[1]},
                    fieldGroup: [
                        {
                            type: 'headline',
                            templateOptions: {id: 'FRG_1', label: 'FRG_1', level: 4, noH5After: false, required: true}
                        },
                        {
                            wrapper: 'validationGroup',
                            templateOptions: {
                                type: 'check-group',
                                validationGroup: 'baserasPa',
                                kompletteringGroup: 'baseratPa'
                            },
                            fieldGroup: [
                                {
                                    key: 'undersokningAvPatienten',
                                    type: 'date',
                                    className: 'small-gap',
                                    templateOptions: {
                                        label: 'KV_FKMU_0001.UNDERSOKNING', hideWhenEmpty: true
                                    }
                                }, {
                                    key: 'journaluppgifter', type: 'date', className: 'small-gap', templateOptions: {
                                        label: 'KV_FKMU_0001.JOURNALUPPGIFTER', hideWhenEmpty: true
                                    }
                                }, {
                                    key: 'anhorigsBeskrivningAvPatienten',
                                    className: 'small-gap',
                                    type: 'date',
                                    templateOptions: {
                                        label: 'KV_FKMU_0001.ANHORIG', hideWhenEmpty: true
                                    }
                                }, {
                                    key: 'annatGrundForMU', type: 'date', templateOptions: {
                                        label: 'KV_FKMU_0001.ANNAT', hideWhenEmpty: true, hideKompletteringText: true
                                    }
                                }
                            ]
                        }, {
                            key: 'annatGrundForMUBeskrivning',
                            type: 'single-text-vertical',
                            className: 'fold-animation',
                            hideExpression: '!model.annatGrundForMU',
                            templateOptions: {
                                label: 'DFR_1.3',
                                help: 'DFR_1.3',
                                required: true,
                                size: 'full',
                                kompletteringKey: 'annatGrundForMU'
                            }
                        },
                        {
                            key: 'kannedomOmPatient',
                            type: 'singleDate',
                            templateOptions: {label: 'FRG_2', required: true}
                        },
                        {key: 'underlagFinns', type: 'boolean', templateOptions: {label: 'FRG_3', required: true}},
                        {
                            key: 'underlag',
                            type: 'underlag',
                            className: 'slide-animation',
                            hideExpression: '!model.underlagFinns',
                            templateOptions: {
                                maxUnderlag: 3,
                                underlagsTyper: ['NEUROPSYKIATRISKT',
                                    'HABILITERING',
                                    'ARBETSTERAPEUT',
                                    'FYSIOTERAPEUT',
                                    'LOGOPED',
                                    'PSYKOLOG',
                                    'FORETAGSHALSOVARD',
                                    'SPECIALISTKLINIK',
                                    'VARD_UTOMLANDS',
                                    'OVRIGT_UTLATANDE'
                                ],
                                typLabel: 'FRG_4',
                                datumLabel: 'DFR_4.2',
                                hamtasFranLabel: 'DFR_4.3'
                            },
                            watcher: {
                                expression: 'model.underlagFinns',
                                listener: FactoryHelper.underlagListener
                            }
                        }
                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 4, categoryName: categoryNames[4]},
                    fieldGroup: [
                        {
                            type: 'headline',
                            templateOptions: {label: 'FRG_6', level: 4, noH5After: false, required: true}
                        },
                        {
                            key: 'diagnoser',
                            type: 'diagnos',
                            templateOptions: {diagnosBeskrivningLabel: 'DFR_6.1', diagnosKodLabel: 'DFR_6.2'}
                        },
                        {key: 'diagnosgrund', type: 'multi-text', templateOptions: {label: 'DFR_7.1', required: true}},
                        {
                            key: 'nyBedomningDiagnosgrund',
                            type: 'boolean',
                            templateOptions: {label: 'FRG_45', required: true, hideKompletteringText: true}
                        },
                        {
                            key: 'diagnosForNyBedomning',
                            className: 'fold-animation',
                            type: 'multi-text',
                            templateOptions: {
                                label: 'DFR_45.2',
                                required: true,
                                kompletteringKey: 'nyBedomningDiagnosgrund'
                            },
                            hideExpression: '!model.nyBedomningDiagnosgrund'
                        }
                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 3, categoryName: categoryNames[3]},
                    fieldGroup: [
                        {
                            key: 'sjukdomsforlopp',
                            type: 'multi-text',
                            templateOptions: {label: 'DFR_5.1', required: true}
                        }
                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 5, categoryName: categoryNames[5], required: true},
                    fieldGroup: [{
                        wrapper: 'validationGroup',
                        templateOptions: {type: 'text-group', validationGroup: 'funktionsnedsattning'},
                        fieldGroup: [
                            {
                                key: 'funktionsnedsattningIntellektuell',
                                type: 'check-multi-text',
                                templateOptions: {frgId: '8'}
                            },
                            {
                                key: 'funktionsnedsattningKommunikation',
                                type: 'check-multi-text',
                                templateOptions: {frgId: '9'}
                            },
                            {
                                key: 'funktionsnedsattningKoncentration',
                                type: 'check-multi-text',
                                templateOptions: {frgId: '10'}
                            },
                            {
                                key: 'funktionsnedsattningPsykisk',
                                type: 'check-multi-text',
                                templateOptions: {frgId: '11'}
                            },
                            {
                                key: 'funktionsnedsattningSynHorselTal',
                                type: 'check-multi-text',
                                templateOptions: {frgId: '12'}
                            },
                            {
                                key: 'funktionsnedsattningBalansKoordination',
                                type: 'check-multi-text',
                                templateOptions: {frgId: '13'}
                            },
                            {
                                key: 'funktionsnedsattningAnnan',
                                type: 'check-multi-text',
                                templateOptions: {frgId: '14'}
                            }]
                    }]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 6, categoryName: categoryNames[6]},
                    fieldGroup: [{
                        key: 'aktivitetsbegransning',
                        type: 'multi-text',
                        templateOptions: {label: 'DFR_17.1', required: true}
                    }]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 7, categoryName: categoryNames[7]},
                    fieldGroup: [
                        {key: 'avslutadBehandling', type: 'check-multi-text', templateOptions: {frgId: '18'}},
                        {key: 'pagaendeBehandling', type: 'check-multi-text', templateOptions: {frgId: '19'}},
                        {key: 'planeradBehandling', type: 'check-multi-text', templateOptions: {frgId: '20'}},
                        {key: 'substansintag', type: 'check-multi-text', templateOptions: {frgId: '21'}}
                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 8, categoryName: categoryNames[8]},
                    fieldGroup: [
                        {
                            key: 'medicinskaForutsattningarForArbete',
                            type: 'multi-text',
                            templateOptions: {label: 'DFR_22.1', required: true}
                        },
                        {key: 'formagaTrotsBegransning', type: 'multi-text', templateOptions: {label: 'DFR_23.1'}},
                        {key: 'forslagTillAtgard', type: 'multi-text', templateOptions: {label: 'DFR_24.1'}}
                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 9, categoryName: categoryNames[9]},
                    fieldGroup: [
                        {key: 'ovrigt', type: 'multi-text', templateOptions: {label: 'DFR_25.1'}}
                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 10, categoryName: categoryNames[10]},
                    fieldGroup: [
                        {
                            key: 'kontaktMedFk',
                            type: 'checkbox-inline',
                            templateOptions: {label: 'DFR_26.1', hideKompletteringText: true}
                        },
                        {
                            key: 'anledningTillKontakt',
                            type: 'multi-text',
                            className: 'fold-animation',
                            hideExpression: '!model.kontaktMedFk',
                            templateOptions: {label: 'DFR_26.2', kompletteringKey: 'kontaktMedFk'}
                        }
                    ]
                },
                FactoryTemplates.vardenhet
            ];

            return {
                getFormFields: function() {
                    return angular.copy(formFields);
                },
                getCategoryNames: function() {
                    return angular.copy(categoryNames);
                }
            };
        }]);
