angular.module('luae_fs').factory('luae_fs.FormFactory',
    ['luae_fs.FormFactoryHelper',
        function(FactoryHelper) {
            'use strict';


            var categoryNames = [
                null,
                'grundformu',
                'underlag',
                'diagnos',
                'funktionsnedsattning',
                'ovrigt',
                'kontakt'
            ];

            var formFields = [
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 1, categoryName: categoryNames[1]},
                    fieldGroup: [
                        //Fråga 1 -----
                        {type: 'headline', templateOptions: {label: 'FRG_1', level:4}},
                        {key: 'undersokningAvPatienten', type: 'date', templateOptions: {label: 'KV_FKMU_0001.UNDERSOKNING'}},
                        {key: 'journaluppgifter', type: 'date', templateOptions: {label: 'KV_FKMU_0001.JOURNALUPPGIFTER'}},
                        {
                            key: 'anhorigsBeskrivningAvPatienten',
                            type: 'date',
                            templateOptions: {label: 'KV_FKMU_0001.ANHORIG'}
                        },

                        {key: 'annatGrundForMU', type: 'date', templateOptions: {label: 'KV_FKMU_0001.ANNAT'}},
                        {
                            key: 'annatGrundForMUBeskrivning',
                            type: 'single-text',
                            className: 'fold-animation',
                            hideExpression: '!model.annatGrundForMU',
                            templateOptions: {label: 'DFR_1.3', help: 'DFR_1.3', indent: true}
                        },

                        //Fråga 2 -----
                        {key: 'kannedomOmPatient', type: 'singleDate', templateOptions: {label: 'FRG_2'}},

                        // Underlag
                        {key: 'underlagFinns', type: 'boolean', templateOptions: {label: 'FRG_3'}},
                        {
                            key: 'underlag', type: 'underlag',
                            className: 'slide-animation',
                            hideExpression: '!model.underlagFinns',
                            templateOptions: {
                                underlagsTyper: [
                                    'NEUROPSYKIATRISKT',
                                    'HABILITERING',
                                    'ARBETSTERAPEUT',
                                    'FYSIOTERAPEUT',
                                    'LOGOPED',
                                    'PSYKOLOG',
                                    'SKOLHALSOVARD',
                                    'SPECIALISTKLINIK',
                                    'VARD_UTOMLANDS',
                                    'OVRIGT_UTLATANDE'], //KV_FKMU_005
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
                    templateOptions: {category: 3, categoryName: categoryNames[3]},
                    fieldGroup: [
                        {type: 'headline', templateOptions: {label: 'FRG_6', level:4}},
                        {
                            key: 'diagnoser',
                            type: 'diagnos',
                            templateOptions: {diagnosBeskrivningLabel: 'DFR_6.1', diagnosKodLabel: 'DFR_6.2'}
                        }
                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 4, categoryName: categoryNames[4]},
                    fieldGroup: [
                        {
                            key: 'funktionsnedsattningDebut',
                            type: 'multi-text',
                            templateOptions: {label: 'DFR_15.1'}
                        },
                        {
                            key: 'funktionsnedsattningPaverkan',
                            type: 'multi-text',
                            templateOptions: {label: 'DFR_16.1'}
                        }

                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 5, categoryName: categoryNames[5]},
                    fieldGroup: [
                        {key: 'ovrigt', type: 'multi-text', templateOptions: {label: 'DFR_25.1'}}
                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 6, categoryName: categoryNames[6]},
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
                    templateOptions: {staticLabel: 'luae_fs.label.vardenhet', categoryName: 'vardenhet'},
                    fieldGroup: [
                        {type: 'label-vardenhet'},
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
