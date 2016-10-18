angular.module('luae_na').factory('luae_na.FormFactory',
    ['luae_na.FormFactoryHelper', 'common.UserModel',
        function(FactoryHelper, UserModel) {
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
            wrapper: 'wc-field-static',
            templateOptions: {staticLabel: 'common.intyg.patientadress', categoryName: 'patient'},
            fieldGroup: [
                {
                    key: 'grundData.patient.postadress',
                    type: 'single-text',
                    templateOptions: {staticLabel: 'Postadress', disabled: UserModel.isDjupintegration(), size: 'full', labelColSize: 3, formType: 'horizontal'}
                },
                {
                    key: 'grundData.patient.postnummer',
                    type: 'single-text',
                    templateOptions: {staticLabel: 'Postnummer', disabled: UserModel.isDjupintegration(), size: '5', labelColSize: 3, formType: 'horizontal'}
                },
                {
                    key: 'grundData.patient.postort',
                    type: 'single-text',
                    templateOptions: {staticLabel: 'Postort', disabled: UserModel.isDjupintegration(), labelColSize: 3, formType: 'horizontal'}
                },
                {
                    type: 'patient-address-updater',
                    hideExpression: function() { return UserModel.isDjupintegration(); },
                    templateOptions: { formType: 'horizontal', labelColSize: 3, hideFromSigned:true}
                }
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 1, categoryName: categoryNames[1], prototypeName: 'default'},
            fieldGroup: [
                {type: 'headline', templateOptions: {id:'FRG_1', label: 'FRG_1', level: 4, noH5:false}},
                {key: 'undersokningAvPatienten', type: 'date', templateOptions: {label: 'KV_FKMU_0001.UNDERSOKNING'}},
                {key: 'journaluppgifter', type: 'date', templateOptions: {label: 'KV_FKMU_0001.JOURNALUPPGIFTER'}},
                {key: 'anhorigsBeskrivningAvPatienten', type: 'date', templateOptions: {label: 'KV_FKMU_0001.ANHORIG'}},
                {key: 'annatGrundForMU', type: 'date', templateOptions: {label: 'KV_FKMU_0001.ANNAT'}},
                {
                    key: 'annatGrundForMUBeskrivning',
                    type: 'single-text',
                    className: 'fold-animation',
                    hideExpression: '!model.annatGrundForMU',
                    templateOptions: {label: 'DFR_1.3', help: 'DFR_1.3', indent: true}
                },
                {key: 'kannedomOmPatient', type: 'singleDate', templateOptions: {label: 'FRG_2'}},
                {key: 'underlagFinns', type: 'boolean', templateOptions: {label: 'FRG_3'}},
                {
                    key: 'underlag', type: 'underlag',
                    className: 'slide-animation',
                    hideExpression: '!model.underlagFinns',
                    templateOptions: {
                        underlagsTyper: ['NEUROPSYKIATRISKT',
                            'HABILITERING',
                            'ARBETSTERAPEUT',
                            'FYSIOTERAPEUT',
                            'LOGOPED',
                            'PSYKOLOG',
                            'FORETAGSHALSOVARD',
                            'SKOLHALSOVARD',
                            'SPECIALISTKLINIK',
                            'VARD_UTOMLANDS',
                            'OVRIGT_UTLATANDE'],
                        label: 'FRG_4', typLabel: 'DFR_4.1', datumLabel: 'DFR_4.2', hamtasFranLabel: 'DFR_4.3'
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
                {type: 'headline', templateOptions: {label: 'FRG_6', level:4, noH5:false}},
                {
                    key: 'diagnoser',
                    type: 'diagnos',
                    templateOptions: {diagnosBeskrivningLabel: 'DFR_6.1', diagnosKodLabel: 'DFR_6.2'}
                },
                {key: 'diagnosgrund', type: 'multi-text', templateOptions: {label: 'DFR_7.1'}},
                {key: 'nyBedomningDiagnosgrund', type: 'boolean', templateOptions: {label: 'FRG_45'}},
                {
                    key: 'diagnosForNyBedomning',
                    className: 'fold-animation',
                    type: 'multi-text',
                    templateOptions: {label: 'DFR_45.2'},
                    hideExpression: '!model.nyBedomningDiagnosgrund'
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
                {key: 'kontaktMedFk', type: 'checkbox-inline', templateOptions: {label: 'DFR_26.1'}},
                {
                    key: 'anledningTillKontakt',
                    type: 'multi-text',
                    className: 'fold-animation',
                    hideExpression: '!model.kontaktMedFk',
                    templateOptions: {label: 'DFR_26.2'}
                }
            ]
        },
        {
            wrapper: 'wc-field-static',
            templateOptions: {staticLabel: 'common.label.vardenhet', categoryName: 'vardenhet'},
            fieldGroup: [
                {
                    type: 'label-vardenhet'
                },
                {
                    key: 'grundData.skapadAv.vardenhet.postadress',
                    type: 'single-text',
                    templateOptions: {staticLabel: 'Postadress', size: 'full', labelColSize: 3, formType: 'horizontal'}
                },
                {
                    key: 'grundData.skapadAv.vardenhet.postnummer',
                    type: 'single-text',
                    templateOptions: {staticLabel: 'Postnummer', size: '5', labelColSize: 3, formType: 'horizontal'}
                },
                {
                    key: 'grundData.skapadAv.vardenhet.postort',
                    type: 'single-text',
                    templateOptions: {staticLabel: 'Postort', labelColSize: 3, formType: 'horizontal'}
                },
                {
                    key: 'grundData.skapadAv.vardenhet.telefonnummer',
                    type: 'single-text',
                    templateOptions: {staticLabel: 'Telefonnummer', labelColSize: 3, formType: 'horizontal'}
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
