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
                        {type: 'headline', templateOptions: {label: 'FRG_1'}},
                        {type: 'headline', className: 'col-md-6 no-space-left', templateOptions: {label: 'DFR_1.1'}},
                        {type: 'headline', className: 'col-md-6', templateOptions: {label: 'DFR_1.2'}},
                        {key: 'undersokningAvPatienten', type: 'date', templateOptions: {label: 'KV_FKMU_0001.1'}},
                        {key: 'journaluppgifter', type: 'date', templateOptions: {label: 'KV_FKMU_0001.3'}},
                        {
                            key: 'anhorigsBeskrivningAvPatienten',
                            type: 'date',
                            templateOptions: {label: 'KV_FKMU_0001.4'}
                        },

                        {key: 'annatGrundForMU', type: 'date', templateOptions: {label: 'KV_FKMU_0001.5'}},
                        {
                            key: 'annatGrundForMUBeskrivning',
                            type: 'single-text',
                            className: 'fold-animation',
                            hideExpression: '!model.annatGrundForMU',
                            templateOptions: {label: 'DFR_1.3', help: 'DFR_1.3', indent: true}
                        },
                        {key: 'kannedomOmPatient', type: 'date', templateOptions: {label: 'DFR_2.1'}}
                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 2, categoryName: categoryNames[2], prototypeName: 'default'},
                    fieldGroup: [
                        {key: 'underlagFinns', type: 'boolean', templateOptions: {label: 'DFR_3.1'}},
                        {
                            key: 'underlag', type: 'underlag',
                            className: 'slide-animation',
                            hideExpression: '!model.underlagFinns',
                            templateOptions: {
                                underlagsTyper: [1, 2, 3, 4, 5, 6, 8, 9, 10, 11], //KV_FKMU_005
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
                            type: 'single-text',
                            templateOptions: {label: 'DFR_15.1'}
                        },
                        {
                            key: 'funktionsnedsattningPaverkan',
                            type: 'single-text',
                            templateOptions: {label: 'DFR_16.1'}
                        }

                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 5, categoryName: categoryNames[5]},
                    fieldGroup: [
                        {key: 'ovrigt', type: 'multi-text'}
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
