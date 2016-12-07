angular.module('luae_fs').factory('luae_fs.FormFactory',
    ['luae_fs.FormFactoryHelper', 'common.UserModel', 'common.FactoryTemplatesHelper',
        function(FactoryHelper, UserModel, FactoryTemplates) {
            'use strict';


            var categoryNames = {
                1:'grundformu',
                2:'underlag',
                3:'diagnos',
                4:'funktionsnedsattning',
                5:'ovrigt',
                6:'kontakt'
            };

            var formFields = [
                FactoryTemplates.adress,
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 1, categoryName: categoryNames[1]},
                    fieldGroup: [
                        //Fråga 1 -----
                        {type: 'headline', templateOptions: {id: 'FRG_1', label: 'FRG_1', level: 4, noH5: false}},
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
                                    templateOptions: {label: 'KV_FKMU_0001.UNDERSOKNING'}
                                },
                                {
                                    key: 'journaluppgifter',
                                    type: 'date',
                                    templateOptions: {label: 'KV_FKMU_0001.JOURNALUPPGIFTER'}
                                },
                                {
                                    key: 'anhorigsBeskrivningAvPatienten',
                                    type: 'date',
                                    templateOptions: {label: 'KV_FKMU_0001.ANHORIG'}
                                },
                                {
                                    key: 'annatGrundForMU',
                                    type: 'date',
                                    templateOptions: {label: 'KV_FKMU_0001.ANNAT', hideKompletteringText: true}
                                }
                            ]
                        }, {
                            key: 'annatGrundForMUBeskrivning',
                            type: 'single-text',
                            className: 'fold-animation',
                            hideExpression: '!model.annatGrundForMU',
                            templateOptions: {
                                label: 'DFR_1.3',
                                help: 'DFR_1.3',
                                indent: true,
                                kompletteringKey: 'annatGrundForMU'
                            }
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
                        {type: 'headline', templateOptions: {label: 'FRG_6', level: 4, noH5: false}},
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
