angular.module('lisjp').factory('lisjp.FormFactory',
    ['common.DateUtilsService', 'common.ObjectHelper', 'common.UserModel', 'common.FactoryTemplatesHelper',
        function(DateUtils, ObjectHelper, UserModel, FactoryTemplates) {
            'use strict';

            var categoryNames = {
                1:'grundformu',
                2:'sysselsattning',
                3:'diagnos',
                4:'funktionsnedsattning',
                5:'medicinskaBehandlingar',
                6:'bedomning',
                7:'atgarder',
                8:'ovrigt',
                9:'kontakt',
                10:'smittbararpenning'
            };

            var formFields = [
                FactoryTemplates.adress,
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 10, categoryName: categoryNames[10]},
                    fieldGroup: [
                        {key: 'avstangningSmittskydd', type: 'checkbox-inline', templateOptions: {label: 'FRG_27'}}
                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 1, categoryName: categoryNames[1]},
                    hideExpression: 'model.avstangningSmittskydd',
                    fieldGroup: [
                        {
                            type: 'headline',
                            templateOptions: {id: 'FRG_1', label: 'FRG_1', level: 4, noH5: false, required: true}
                        },
                        {
                            type: 'headline',
                            className: 'col-md-6 no-space-left',
                            templateOptions: {label: 'DFR_1.1', hideFromSigned: true}
                        },
                        {
                            type: 'headline',
                            className: 'col-md-6',
                            templateOptions: {label: 'DFR_1.2', hideFromSigned: true}
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
                                    key: 'undersokningAvPatienten', type: 'date', className: 'small-gap',
                                    templateOptions: {label: 'KV_FKMU_0001.UNDERSOKNING', hideWhenEmpty: true}
                                },
                                {
                                    key: 'telefonkontaktMedPatienten', type: 'date', className: 'small-gap',
                                    templateOptions: {label: 'KV_FKMU_0001.TELEFONKONTAKT', hideWhenEmpty: true}
                                },
                                {
                                    key: 'journaluppgifter', type: 'date', className: 'small-gap',
                                    templateOptions: {label: 'KV_FKMU_0001.JOURNALUPPGIFTER', hideWhenEmpty: true}
                                },
                                {
                                    key: 'annatGrundForMU', type: 'date',
                                    templateOptions: {
                                        label: 'KV_FKMU_0001.ANNAT',
                                        hideWhenEmpty: true,
                                        hideKompletteringText: true
                                    }
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
                                kompletteringKey: 'annatGrundForMU',
                                required: true
                            }
                        }, {
                            key: 'motiveringTillInteBaseratPaUndersokning',
                            type: 'multi-text',
                            className: 'fold-animation',
                            hideExpression: 'model.undersokningAvPatienten || !(model.telefonkontaktMedPatienten || model.journaluppgifter || model.annatGrundForMU)',
                            templateOptions: {
                                bold: 'bold',
                                forceHeadingTypeLabel: true,
                                staticLabelId: 'smi.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning',
                                subTextId: 'smi.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning.info',
                                subTextDynId: 'FRG_25',
                                hideFromSigned: true,
                                required: true
                            }
                        }
                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 2, categoryName: categoryNames[2]},
                    hideExpression: 'model.avstangningSmittskydd',
                    fieldGroup: [
                        {
                            key: 'sysselsattning', type: 'check-group',
                            templateOptions: {
                                label: 'FRG_28',
                                code: 'KV_FKMU_0002',
                                choices: ['NUVARANDE_ARBETE',
                                    'ARBETSSOKANDE',
                                    'FORALDRALEDIG',
                                    'STUDIER',
                                    'PROGRAM'
                                ],
                                required: true
                            }
                        },
                        {
                            key: 'nuvarandeArbete', type: 'multi-text',
                            className: 'fold-animation',
                            hideExpression: '!model.sysselsattning["NUVARANDE_ARBETE"]',
                            templateOptions: {label: 'FRG_29', required: true}
                        },
                        {
                            key: 'arbetsmarknadspolitisktProgram', type: 'multi-text',
                            className: 'fold-animation',
                            hideExpression: '!model.sysselsattning["PROGRAM"]',
                            templateOptions: {label: 'FRG_30', required: true}
                        }
                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 3, categoryName: categoryNames[3]},
                    fieldGroup: [
                        {
                            wrapper: 'fmb-wrapper',
                            templateOptions: {
                                relatedFormId: categoryNames[3],
                                fieldName: 'DIAGNOS',
                                panelClass: 'sit-fmb-medium',
                                hideFromSigned: true
                            },
                            fieldGroup: [
                                {
                                    type: 'headline',
                                    templateOptions: {label: 'FRG_6', level: 4, noH5: false, required: true}
                                },
                                {
                                    key: 'diagnoser',
                                    type: 'diagnos',
                                    data: {enableFMB: true},
                                    templateOptions: {diagnosBeskrivningLabel: 'DFR_6.1', diagnosKodLabel: 'DFR_6.2'}
                                }
                            ]
                        }
                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 4, categoryName: categoryNames[4]},
                    hideExpression: 'model.avstangningSmittskydd',
                    fieldGroup: [
                        {
                            wrapper: 'fmb-wrapper',
                            templateOptions: {
                                relatedFormId: categoryNames[4],
                                fieldName: 'FUNKTIONSNEDSATTNING',
                                panelClass: 'sit-fmb-small',
                                hideFromSigned: true
                            },
                            fieldGroup: [
                                {
                                    key: 'funktionsnedsattning', type: 'multi-text', templateOptions: {
                                    label: 'DFR_35.1',
                                    required: 'FRG_ONLY'
                                }
                                }
                            ]
                        },
                        {
                            wrapper: 'fmb-wrapper',
                            templateOptions: {
                                relatedFormId: 'aktivitetsbegransning',
                                fieldName: 'AKTIVITETSBEGRANSNING',
                                panelClass: 'sit-fmb-large',
                                hideFromSigned: true
                            },
                            fieldGroup: [
                                {
                                    key: 'aktivitetsbegransning', type: 'multi-text', templateOptions: {
                                    label: 'DFR_17.1',
                                    required: 'FRG_ONLY'
                                }
                                }
                            ]
                        }
                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 5, categoryName: categoryNames[5]},
                    hideExpression: 'model.avstangningSmittskydd',
                    fieldGroup: [
                        {key: 'pagaendeBehandling', type: 'multi-text', templateOptions: {label: 'DFR_19.1'}},
                        {key: 'planeradBehandling', type: 'multi-text', templateOptions: {label: 'DFR_20.1'}}
                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 6, categoryName: categoryNames[6]},
                    fieldGroup: [
                        {
                            wrapper: 'fmb-wrapper',
                            templateOptions: {
                                relatedFormId: categoryNames[6],
                                fieldName: 'ARBETSFORMAGA',
                                panelClass: 'sit-fmb-large',
                                hideFromSigned: true
                            },
                            fieldGroup: [
                                {
                                    key: 'sjukskrivningar', type: 'sjukskrivningar',
                                    templateOptions: {
                                        label: 'DFR_32.1',
                                        code: 'KV_FKMU_0003',
                                        fields: [
                                            'EN_FJARDEDEL',
                                            'HALFTEN',
                                            'TRE_FJARDEDEL',
                                            'HELT_NEDSATT'
                                        ],
                                        required: true
                                    }
                                },{
                                    key: 'motiveringTillTidigtStartdatumForSjukskrivning', type: 'multi-text',
                                    className: 'fold-animation',
                                    hideExpression: function($viewValue, $modelValue, scope) {
                                        var hide = true;
                                        var warnings = scope.options.formState.viewState.common.validation.warningMessagesByField;
                                        if (warnings) {
                                            angular.forEach(warnings.sjukskrivningar, function(w) {
                                               if (w.message === 'lisjp.validation.bedomning.sjukskrivningar.tidigtstartdatum') {
                                                   hide = false;
                                               }
                                            });
                                        }
                                        return hide;
                                    },
                                    templateOptions: {
                                        bold: 'bold',
                                        forceHeadingTypeLabel: true,
                                        staticLabelId: 'lisjp.label.sjukskrivningar.tidigtstartdatum.motivering',
                                        staticHelpId: 'lisjp.label.sjukskrivningar.tidigtstartdatum.motivering.help',
                                        subTextId: 'lisjp.label.sjukskrivningar.tidigtstartdatum.motivering.info',
                                        subTextDynId: 'FRG_25',
                                        hideWhenEmpty: true
                                    }
                                }
                            ]
                        },
                        {
                            key: 'forsakringsmedicinsktBeslutsstod',
                            type: 'multi-text',
                            templateOptions: {label: 'DFR_37.1'},
                            hideExpression: 'model.avstangningSmittskydd'
                        },
                        {
                            key: 'arbetstidsforlaggning', type: 'boolean',
                            className: 'fold-animation',
                            hideExpression: function($viewValue, $modelValue, scope) {

                                if (scope.model.avstangningSmittskydd) {
                                    return true;
                                }

                                var sjukskrivningar = scope.model.sjukskrivningar;

                                var nedsatt75under = false;
                                angular.forEach(sjukskrivningar, function(item, key) {
                                    if (!nedsatt75under && key !== 'HELT_NEDSATT') {
                                        if (item.period && DateUtils.isDate(item.period.from) &&
                                            DateUtils.isDate(item.period.tom)) {
                                            nedsatt75under = true;
                                        }
                                    }
                                });

                                return !nedsatt75under;
                            },
                            templateOptions: {label: 'FRG_33', hideKompletteringText: true, required: true}
                        },
                        {
                            key: 'arbetstidsforlaggningMotivering', type: 'multi-text',
                            className: 'fold-animation',
                            hideExpression: function($viewValue, $modelValue, scope) {
                                if (scope.model.avstangningSmittskydd) {
                                    return true;
                                }
                                return scope.model.arbetstidsforlaggning !== true;
                            },
                            templateOptions: {label: 'DFR_33.2', kompletteringKey: 'arbetstidsforlaggning', required: true}
                        },
                        {key: 'arbetsresor', type: 'boolean', templateOptions: {label: 'DFR_34.1'}, hideExpression: 'model.avstangningSmittskydd'},
                        {
                            key: 'prognos', type: 'prognos',
                            hideExpression: 'model.avstangningSmittskydd',
                            templateOptions: {
                                label: 'FRG_39',
                                required: true,
                                code: 'KV_FKMU_0006',
                                choices: [{id: 'STOR_SANNOLIKHET', showDropDown: false},
                                    {id: 'ATER_X_ANTAL_DGR', showDropDown: true},
                                    {id: 'PROGNOS_OKLAR', showDropDown: false},
                                    {id: 'SANNOLIKT_INTE', showDropDown: false}
                                ],
                                prognosDagarTillArbeteCode: 'KV_FKMU_0007',
                                prognosDagarTillArbeteTyper: ['TRETTIO_DGR',
                                    'SEXTIO_DGR',
                                    'NITTIO_DGR',
                                    'HUNDRAATTIO_DAGAR',
                                    'TREHUNDRASEXTIOFEM_DAGAR'
                                ]
                            },
                            watcher: {
                                expression: 'model.prognos.typ',
                                listener: function _prognosTypListener(field, newValue, oldValue, scope, stopWatching) {
                                    var model = scope.model;
                                    if (newValue === 'ATER_X_ANTAL_DGR') {
                                        model.restoreFromAttic('prognos.dagarTillArbete');
                                    } else {
                                        if (oldValue === 'ATER_X_ANTAL_DGR') {
                                            model.updateToAttic('prognos.dagarTillArbete');
                                        }
                                        model.clear('prognos.dagarTillArbete');
                                    }
                                }
                            }
                        }
                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 7, categoryName: categoryNames[7]},
                    hideExpression: 'model.avstangningSmittskydd',
                    fieldGroup: [
                        {
                            key: 'arbetslivsinriktadeAtgarder',
                            type: 'check-group',
                            templateOptions: {
                                label: 'FRG_40',
                                required: true,
                                descLabel: 'DFR_40.2',
                                code: 'KV_FKMU_0004',
                                choices: ['EJ_AKTUELLT',
                                    'ARBETSTRANING',
                                    'ARBETSANPASSNING',
                                    'SOKA_NYTT_ARBETE',
                                    'BESOK_ARBETSPLATS',
                                    'ERGONOMISK',
                                    'HJALPMEDEL',
                                    'KONFLIKTHANTERING',
                                    'KONTAKT_FHV',
                                    'OMFORDELNING',
                                    'OVRIGA_ATGARDER'
                                ]
                            },
                            watcher: [{
                                type: '$watch',
                                watchDeep: true,
                                expression: 'model.arbetslivsinriktadeAtgarder',
                                listener: function(field, newValue, oldValue, scope) {
                                    if (oldValue && newValue !== oldValue) {
                                        if(newValue.EJ_AKTUELLT !== oldValue.EJ_AKTUELLT) {
                                            if(newValue.EJ_AKTUELLT){
                                                angular.forEach(scope.model.arbetslivsinriktadeAtgarder, function(atgard, key) {
                                                    if (key !== 'EJ_AKTUELLT') {
                                                        scope.model.arbetslivsinriktadeAtgarder[key] = undefined;
                                                    }
                                                });
                                            }
                                        } else {
                                            angular.forEach(scope.model.arbetslivsinriktadeAtgarder, function(atgard, key) {
                                                if (key !== 'EJ_AKTUELLT' && atgard) {
                                                    scope.model.arbetslivsinriktadeAtgarder.EJ_AKTUELLT = undefined;
                                                }
                                            });
                                        }
                                    }
                                }
                            }]
                        }, {
                            key: 'arbetslivsinriktadeAtgarderBeskrivning',
                            type: 'multi-text',
                            className: 'fold-animation',
                            hideExpression: function($viewValue, $modelValue, scope) {
                                var hide = true;
                                angular.forEach(scope.model.arbetslivsinriktadeAtgarder, function(atgard, key) {
                                    if (atgard === true && key !== 'EJ_AKTUELLT') {
                                        hide = false;
                                        return;
                                    }
                                });
                                return hide;
                            },
                            templateOptions: {label: 'FRG_44', required: true}
                        }
                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 8, categoryName: categoryNames[8]},
                    fieldGroup: [
                        {key: 'ovrigt', type: 'multi-text', templateOptions: {label: 'DFR_25.1'}}
                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 9, categoryName: categoryNames[9]},
                    hideExpression: 'model.avstangningSmittskydd',
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
