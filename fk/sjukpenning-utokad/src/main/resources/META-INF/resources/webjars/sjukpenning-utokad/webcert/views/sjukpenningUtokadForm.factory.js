angular.module('lisu').factory('sjukpenning-utokad.FormFactory', function() {
    'use strict';

    var formFields = [
        {
            wrapper: 'wc-field',
            templateOptions: {category: 1, categoryName: 'grundformu'},
            fieldGroup: [
                {type: 'label', templateOptions: {label: 'FRG_1'}},
                {type: 'label', templateOptions: {label: 'DFR_1.1'}},
                {type: 'label', templateOptions: {label: 'DFR_1.2'}},
                {key: 'undersokningAvPatienten', type: 'date', templateOptions: {label: 'KV_FKMU_0001.1'}},
                {key: 'journaluppgifter', type: 'date', templateOptions: {label: 'KV_FKMU_0001.2'}},
                {key: 'anhorigsBeskrivningAvPatienten', type: 'date', templateOptions: {label: 'KV_FKMU_0001.3'}},
                {key: 'annatGrundForMU', type: 'date', templateOptions: {label: 'KV_FKMU_0001.5'}},
                {
                    key: 'annatGrundForMUBeskrivning',
                    type: 'single-text',
                    className: 'dfr_1_3',
                    hideExpression: '!model.annatGrundForMU',
                    templateOptions: {label: 'DFR_1.3', help: 'DFR_1.3', indent: true}
                }
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 2, categoryName: 'sysselsattning'},
            fieldGroup: [
                {
                    key: 'sysselsattning.typ', type: 'radio-group',
                    templateOptions: {
                        label: 'DFR_28.1',
                        code: 'KV_FKMU_0002',
                        choices: [1, 2, 3, 4, 5]
                    }
                },
                {key: 'nuvarandeArbete', type: 'multi-text', templateOptions: {label: 'DFR_29.1'}},
                {key: 'arbetsmarknadspolitisktProgram', type: 'multi-text', templateOptions: {label: 'DFR_30.1'}}
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 3, categoryName: 'diagnos'},
            fieldGroup: [
                {
                    key: 'diagnoser',
                    type: 'diagnos',
                    templateOptions: {diagnosBeskrivningLabel: 'DFR_6.1', diagnosKodLabel: 'DFR_6.2'}
                },
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 4, categoryName: 'funktionsnedsattning'},
            fieldGroup: [
                {key: 'funktionsnedsattning', type: 'multi-text', templateOptions: {label: 'DFR_35.1'}},
                {key: 'aktivitetsbegransning', type: 'multi-text', templateOptions: {label: 'DFR_17.1'}},
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 5, categoryName: 'medicinskaBehandlingar'},
            fieldGroup: [
                {key: 'pagaendeBehandling', type: 'multi-text', templateOptions: {label: 'DFR_19.1'}},
                {key: 'planeradBehandling', type: 'multi-text', templateOptions: {label: 'DFR_20.1'}},
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 6, categoryName: 'bedomning'},
            fieldGroup: [
                {type: 'label', templateOptions: {label: 'FRG_32'}},
                {
                    key: 'sjukskrivningar', type: 'sjukskrivningar',
                    templateOptions: {
                        label: 'DFR_32.1',
                        code: 'KV_FKMU_0003',
                        fields: [1, 2, 3, 4]
                    }
                },
                {key: 'forsakringsmedicinsktBeslutsstod', type: 'multi-text', templateOptions: {label: 'DFR_37.1'}},
                {key: 'arbetstidsforlaggning', type: 'boolean', templateOptions: {label: 'DFR_33.1'}},
                {key: 'arbetstidsforlaggningMotivering', type: 'multi-text', templateOptions: {label: 'DFR_33.2'}},
                {key: 'arbetsresor', type: 'boolean', templateOptions: {label: 'DFR_34.1'}},
                {key: 'formagaTrotsBegransning', type: 'multi-text', templateOptions: {label: 'DFR_23.1'}},
                {
                    key: 'prognos.prognosTyp', type: 'radio-group',
                    templateOptions: {
                        label: 'DFR_39.1',
                        code: 'KV_FKMU_0006',
                        choices: [1, 2, 3, 4]
                    }
                },
                {key: 'prognos.fortydligande', type: 'multi-text', templateOptions: {label: 'DFR_39.2'}},
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 7, categoryName: 'atgarder'},
            fieldGroup: [
                {type: 'label', templateOptions: {label: 'FRG_40'}},
                {
                    key: 'arbetslivsinriktadeAtgarder', type: 'check-group',
                    templateOptions: {
                        label: 'DFR_40.1',
                        code: 'KV_FKMU_0004',
                        choices: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
                    }
                },
                {
                    key: 'arbetslivsinriktadeAtgarderAktuelltBeskrivning',
                    type: 'multi-text',
                    templateOptions: {label: 'DFR_40.2'}
                },
                {
                    key: 'arbetslivsinriktadeAtgarderEjAktuelltBeskrivning',
                    type: 'multi-text',
                    templateOptions: {label: 'DFR_40.3'}
                },
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 8, categoryName: 'ovrigt'},
            fieldGroup: [
                {key: 'ovrigt', type: 'multi-text', templateOptions: {label: 'DFR_25.1'}},
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 9, categoryName: 'kontakt'},
            fieldGroup: [
                {key: 'kontaktMedFk', type: 'boolean', templateOptions: {label: 'DFR_26.1'}},
                {
                    key: 'anledningTillKontakt',
                    type: 'multi-text',
                    hideExpression: '!model.kontaktMedFk',
                    templateOptions: {label: 'DFR_26.2'}
                }
            ]
        },
        {
            wrapper: 'wc-field-static',
            templateOptions: {staticLabel: 'lisu.label.vardenhet'},
            fieldGroup: [
                {type: 'label-vardenhet'},
                {
                    key: 'grundData.skapadAv.vardenhet.postadress',
                    type: 'single-text',
                    templateOptions: {staticLabel: 'Postadress', size: 'full'}
                },
                {
                    key: 'grundData.skapadAv.vardenhet.postnummer',
                    type: 'single-text',
                    templateOptions: {staticLabel: 'Postnummer', size: '5'}
                },
                {
                    key: 'grundData.skapadAv.vardenhet.postort',
                    type: 'single-text',
                    templateOptions: {staticLabel: 'Postort'}
                },
                {
                    key: 'grundData.skapadAv.vardenhet.telefonnummer',
                    type: 'single-text',
                    templateOptions: {staticLabel: 'Telefonnummer'}
                }
            ]
        }
    ];

    return {
        getFormFields: function() {
            return angular.copy(formFields);
        }
    };
});