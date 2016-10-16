angular.module('luse').factory('luse.FormFactory', ['luse.FormFactoryHelper', 'common.UserModel', function(FactoryHelper, UserModel) {
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

    var formFields = [{
            wrapper: 'wc-field-static',
            templateOptions: { staticLabel: 'common.intyg.patientadress', categoryName: 'patient' },
            fieldGroup: [{
                    key: 'grundData.patient.postadress',
                    type: 'single-text',
                    templateOptions: { staticLabel: 'Postadress', required: true, disabled: UserModel.isDjupintegration(), size: 'full', labelColSize: 3, formType: 'horizontal' }
                },
                {
                    key: 'grundData.patient.postnummer',
                    type: 'single-text',
                    templateOptions: { staticLabel: 'Postnummer', required: true, disabled: UserModel.isDjupintegration(), size: '5', labelColSize: 3, formType: 'horizontal' }
                },
                {
                    key: 'grundData.patient.postort',
                    type: 'single-text',
                    templateOptions: { staticLabel: 'Postort', required: true, disabled: UserModel.isDjupintegration(), labelColSize: 3, formType: 'horizontal' }
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
            templateOptions: { category: 1, categoryName: categoryNames[1], prototypeName: 'default' },
            fieldGroup: [
                { type: 'headline', templateOptions: { id: 'FRG_1', label: 'FRG_1', level: 4, noH5After:false, required: true } },
                { key: 'undersokningAvPatienten', type: 'date', templateOptions: { label: 'KV_FKMU_0001.UNDERSOKNING' } },
                { key: 'journaluppgifter', type: 'date', templateOptions: { label: 'KV_FKMU_0001.JOURNALUPPGIFTER' } },
                { key: 'anhorigsBeskrivningAvPatienten', type: 'date', templateOptions: { label: 'KV_FKMU_0001.ANHORIG' } },
                { key: 'annatGrundForMU', type: 'date', templateOptions: { label: 'KV_FKMU_0001.ANNAT' } },
                {
                    key: 'annatGrundForMUBeskrivning',
                    type: 'single-text',
                    className: 'fold-animation',
                    hideExpression: '!model.annatGrundForMU',
                    templateOptions: { label: 'DFR_1.3', help: 'DFR_1.3', required:true }
                },
                {
                    key: 'motiveringTillInteBaseratPaUndersokning',
                    type: 'multi-text',
                    className: 'fold-animation',
                    hideExpression: 'model.undersokningAvPatienten || !( model.journaluppgifter || model.anhorigBeskrivningAvPatienten || model.annatGrundForMU)',
                    templateOptions: {
                        bold: 'bold', 
                        staticLabelId: 'luse.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning',
                        subTextId: 'luse.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning.info',
                        subTextDynId: 'FRG_25',
                        hideFromSigned:true
                    }
                },
                { key: 'kannedomOmPatient', type: 'singleDate', templateOptions: { label: 'FRG_2', required: true } },
                { key: 'underlagFinns', type: 'boolean', templateOptions: { label: 'FRG_3', required: true } },

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
            templateOptions: { category: 4, categoryName: categoryNames[4] },
            fieldGroup: [
                { type: 'headline', templateOptions: { label: 'FRG_6', level: 4, noH5After:false } },
                {
                    key: 'diagnoser',
                    type: 'diagnos',
                    templateOptions: { diagnosBeskrivningLabel: 'DFR_6.1', diagnosKodLabel: 'DFR_6.2' }
                },
                { key: 'diagnosgrund', type: 'multi-text', templateOptions: { label: 'DFR_7.1', required: true } },
                { key: 'nyBedomningDiagnosgrund', type: 'boolean', templateOptions: { label: 'FRG_45', required: true } },
                {
                    key: 'diagnosForNyBedomning',
                    className: 'fold-animation',
                    type: 'multi-text',
                    templateOptions: { label: 'DFR_45.2', required: true },
                    hideExpression: '!model.nyBedomningDiagnosgrund'
                }
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: { category: 3, categoryName: categoryNames[3] },
            fieldGroup: [
                { key: 'sjukdomsforlopp', type: 'multi-text', templateOptions: { label: 'DFR_5.1', required: true } }
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: { category: 5, categoryName: categoryNames[5], required: true },
            fieldGroup: [
                { key: 'funktionsnedsattningIntellektuell', type: 'check-multi-text', templateOptions: { frgId: '8' } },
                { key: 'funktionsnedsattningKommunikation', type: 'check-multi-text', templateOptions: { frgId: '9' } },
                { key: 'funktionsnedsattningKoncentration', type: 'check-multi-text', templateOptions: { frgId: '10' } },
                { key: 'funktionsnedsattningPsykisk', type: 'check-multi-text', templateOptions: { frgId: '11' } },
                { key: 'funktionsnedsattningSynHorselTal', type: 'check-multi-text', templateOptions: { frgId: '12' } },
                { key: 'funktionsnedsattningBalansKoordination', type: 'check-multi-text', templateOptions: { frgId: '13' } },
                { key: 'funktionsnedsattningAnnan', type: 'check-multi-text', templateOptions: { frgId: '14' } }
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: { category: 6, categoryName: categoryNames[6] },
            fieldGroup: [{
                key: 'aktivitetsbegransning',
                type: 'multi-text',
                templateOptions: { label: 'DFR_17.1', required: true }
            }]
        },
        {
            wrapper: 'wc-field',
            templateOptions: { category: 7, categoryName: categoryNames[7] },
            fieldGroup: [
                { key: 'avslutadBehandling', type: 'check-multi-text', templateOptions: { frgId: '18' } },
                { key: 'pagaendeBehandling', type: 'check-multi-text', templateOptions: { frgId: '19' } },
                { key: 'planeradBehandling', type: 'check-multi-text', templateOptions: { frgId: '20' } },
                { key: 'substansintag', type: 'check-multi-text', templateOptions: { frgId: '21' } }
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: { category: 8, categoryName: categoryNames[8] },
            fieldGroup: [
                { key: 'medicinskaForutsattningarForArbete', type: 'multi-text', templateOptions: { label: 'DFR_22.1', required: true } },
                { key: 'formagaTrotsBegransning', type: 'multi-text', templateOptions: { label: 'DFR_23.1' } }
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: { category: 9, categoryName: categoryNames[9] },
            fieldGroup: [
                { key: 'ovrigt', type: 'multi-text', templateOptions: { label: 'DFR_25.1' } }
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: { category: 10, categoryName: categoryNames[10] },
            fieldGroup: [
                { key: 'kontaktMedFk', type: 'checkbox-inline', templateOptions: { label: 'DFR_26.1' } },
                {
                    key: 'anledningTillKontakt',
                    type: 'multi-text',
                    className: 'fold-animation',
                    hideExpression: '!model.kontaktMedFk',
                    templateOptions: { label: 'DFR_26.2' }
                }
            ]
        },
        {
            wrapper: 'wc-field-static',
            templateOptions: { staticLabel: 'common.label.vardenhet', categoryName: 'vardenhet' },
            fieldGroup: [{
                    type: 'label-vardenhet'
                },
                {
                    key: 'grundData.skapadAv.vardenhet.postadress',
                    type: 'single-text',
                    templateOptions: { staticLabel: 'Postadress', size: 'full', labelColSize: 3, formType: 'horizontal', required: true }
                },
                {
                    key: 'grundData.skapadAv.vardenhet.postnummer',
                    type: 'single-text',
                    templateOptions: { staticLabel: 'Postnummer', size: '5', labelColSize: 3, formType: 'horizontal', required: true }
                },
                {
                    key: 'grundData.skapadAv.vardenhet.postort',
                    type: 'single-text',
                    templateOptions: { staticLabel: 'Postort', labelColSize: 3, formType: 'horizontal', required: true }
                },
                {
                    key: 'grundData.skapadAv.vardenhet.telefonnummer',
                    type: 'single-text',
                    templateOptions: { staticLabel: 'Telefonnummer', labelColSize: 3, formType: 'horizontal', required: true }
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
