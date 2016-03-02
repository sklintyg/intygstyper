angular.module('luse').factory('sjukersattning.FormFactory', ['luse.FormFactoryHelper', function(FactoryHelper) {
    'use strict';

    var categoryNames = [
        null,
        'grundformu',
        'underlag',
        'sjukdomsforlopp',
        'diagnos',
        'funktionsnedsattning',
        'aktivitetsbegransning',
        'medicinskabehandlingar',
        'medicinskaforutsattningarforarbete',
        'ovrigt',
        'kontakt'
    ];

    var formFields = [
        {
            wrapper: 'wc-field',
            templateOptions: {category: 1, categoryName: categoryNames[1], prototypeName: 'default'},
            fieldGroup: [
                {type: 'headline', templateOptions: {label: 'FRG_1', level: '4'}},
                {type: 'headline', className: 'col-md-6 no-space-left', templateOptions: {label: 'DFR_1.1', level: '4'}},
                {type: 'headline', className: 'col-md-6', templateOptions: {label: 'DFR_1.2'}},
                {key: 'undersokningAvPatienten', type: 'date', templateOptions: {label: 'KV_FKMU_0001.1'}},
                {key: 'journaluppgifter', type: 'date', templateOptions: {label: 'KV_FKMU_0001.3'}},
                {key: 'anhorigsBeskrivningAvPatienten', type: 'date', templateOptions: {label: 'KV_FKMU_0001.4'}},
                {key: 'annatGrundForMU', type: 'date', templateOptions: {label: 'KV_FKMU_0001.5'}},
                {
                    key: 'annatGrundForMUBeskrivning',
                    type: 'single-text',
                    className: 'fold-animation',
                    hideExpression: '!model.annatGrundForMU',
                    templateOptions: {label: 'DFR_1.3', help: 'DFR_1.3', indent: true},
                    watcher: {
                        expression: 'model.annatGrundForMU',
                        listener: FactoryHelper.annatGrundBeskrivningListener
                    }
                },
                {key: 'kannedomOmPatient', type: 'date', templateOptions: {label: 'DFR_2.1'}}
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 2, categoryName: categoryNames[2]},
            fieldGroup: [
                {key: 'underlagFinns', type: 'boolean', templateOptions: {label: 'DFR_3.1'}},
                {
                    key: 'underlag', type: 'underlag',
                    className: 'fold-animation',
                    hideExpression: '!model.underlagFinns',
                    templateOptions: {
                        underlagsTyper: [1, 2, 3, 4, 5, 6, 7, 9, 10, 11],
                        typLabel: 'DFR_4.1', datumLabel: 'DFR_4.2', hamtasFranLabel: 'DFR_4.3'
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
            templateOptions: {category: 3, categoryName: categoryNames[3]},
            fieldGroup: [
                {key: 'sjukdomsforlopp', type: 'multi-text', templateOptions: {label: 'DFR_5.1'}}
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 4, categoryName: categoryNames[4]},
            fieldGroup: [
                {
                    key: 'diagnoser',
                    type: 'diagnos',
                    templateOptions: {diagnosBeskrivningLabel: 'DFR_6.1', diagnosKodLabel: 'DFR_6.2'}
                },
                {key: 'diagnosgrund', type: 'multi-text', templateOptions: {label: 'DFR_7.1'}},
                {key: 'nyBedomningDiagnosgrund', type: 'boolean', templateOptions: {label: 'DFR_7.2'}}
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 5, categoryName: categoryNames[5]},
            fieldGroup: [
                {key: 'funktionsnedsattningIntellektuell', type: 'multi-text', templateOptions: {label: 'DFR_8.1'}},
                {key: 'funktionsnedsattningKommunikation', type: 'multi-text', templateOptions: {label: 'DFR_9.1'}},
                {key: 'funktionsnedsattningKoncentration', type: 'multi-text', templateOptions: {label: 'DFR_10.1'}},
                {key: 'funktionsnedsattningPsykisk', type: 'multi-text', templateOptions: {label: 'DFR_11.1'}},
                {key: 'funktionsnedsattningSynHorselTal', type: 'multi-text', templateOptions: {label: 'DFR_12.1'}},
                {
                    key: 'funktionsnedsattningBalansKoordination',
                    type: 'multi-text',
                    templateOptions: {label: 'DFR_13.1'}
                },
                {key: 'funktionsnedsattningAnnan', type: 'multi-text', templateOptions: {label: 'DFR_14.1'}}
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 6, categoryName: categoryNames[6]},
            fieldGroup: [
                {
                    key: 'aktivitetsbegransning', type: 'multi-text', templateOptions: {label: 'DFR_17.1'}
                }
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 7, categoryName: categoryNames[7]},
            fieldGroup: [
                {key: 'avslutadBehandling', type: 'multi-text', templateOptions: {label: 'DFR_18.1'}},
                {key: 'pagaendeBehandling', type: 'multi-text', templateOptions: {label: 'DFR_19.1'}},
                {key: 'planeradBehandling', type: 'multi-text', templateOptions: {label: 'DFR_20.1'}},
                {key: 'substansintag', type: 'multi-text', templateOptions: {label: 'DFR_21.1'}}
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 8, categoryName: categoryNames[8]},
            fieldGroup: [
                {key: 'medicinskaForutsattningarForArbete', type: 'multi-text', templateOptions: {label: 'DFR_22.1'}},
                {key: 'aktivitetsFormaga', type: 'multi-text', templateOptions: {label: 'DFR_23.1'}}
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
                {key: 'kontaktMedFk', type: 'checkbox-inline', templateOptions: {label: 'DFR_26.1'}},
                {
                    key: 'anledningTillKontakt',
                    type: 'multi-text',
                    className: 'fold-animation',
                    hideExpression: '!model.kontaktMedFk',
                    templateOptions: {label: 'DFR_26.2'},
                    watcher: {
                        expression: 'model.kontaktMedFk',
                        listener: FactoryHelper.anledningKontaktListener
                    }
                }
            ]
        },
        {
            wrapper: 'wc-field-static',
            templateOptions: {staticLabel: 'luse.label.vardenhet'},
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
        },
        getCategoryNames: function() {
            return angular.copy(categoryNames);
        }
    };
}]);