angular.module('lisjp').factory('lisjp.FormFactory',
    ['common.DateUtilsService', 'common.ObjectHelper', 'common.fmbViewState', 'common.UserModel',
        function(DateUtils, ObjectHelper, fmbViewState, UserModel) {

            'use strict';

    var categoryNames = [
        null,
        'grundformu',
        'sysselsattning',
        'diagnos',
        'funktionsnedsattning',
        'medicinskaBehandlingar',
        'bedomning',
        'atgarder',
        'ovrigt',
        'kontakt',
        'smittbararpenning'
    ];

    // Determine if we have fmb data available for a specific section or not
    var _missingInfoForFmbKey = function(scope, fmbKey) {
        return !(ObjectHelper.isDefined(fmbViewState.state.formData[fmbKey]));
    };

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
            templateOptions: {category: 10, categoryName: categoryNames[10]},
            fieldGroup: [
                {key: 'avstangningSmittskydd', type: 'checkbox-inline', templateOptions: {label: 'FRG_27'}}
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 1, categoryName: categoryNames[1]},
            fieldGroup: [
                {type: 'headline', templateOptions: {id:'FRG_1', label: 'FRG_1', level: 4, noH5:false}},
                {type: 'headline', className: 'col-md-6 no-space-left', templateOptions: {label: 'DFR_1.1', hideFromSigned:true}},
                {type: 'headline', className: 'col-md-6', templateOptions: {label: 'DFR_1.2', hideFromSigned:true}},
                {
                    wrapper: 'validationGroup',
                    templateOptions: { type:'check-group', validationGroup: 'baserasPa' },
                    fieldGroup: [
                        {key: 'undersokningAvPatienten', type: 'date', className: 'small-gap',
                            templateOptions: {label: 'KV_FKMU_0001.UNDERSOKNING', hideWhenEmpty: true}},
                        {key: 'telefonkontaktMedPatienten', type: 'date', className: 'small-gap',
                            templateOptions: {label: 'KV_FKMU_0001.TELEFONKONTAKT', hideWhenEmpty: true}},
                        {key: 'journaluppgifter', type: 'date', className: 'small-gap',
                            templateOptions: {label: 'KV_FKMU_0001.JOURNALUPPGIFTER', hideWhenEmpty: true}},
                        {key: 'annatGrundForMU', type: 'date',
                            templateOptions: {label: 'KV_FKMU_0001.ANNAT', hideWhenEmpty: true}}
                    ]
                },{
                    key: 'annatGrundForMUBeskrivning',
                    type: 'single-text',
                    className: 'fold-animation',
                    hideExpression: '!model.annatGrundForMU',
                    templateOptions: {label: 'DFR_1.3', help: 'DFR_1.3', indent: true}
                }
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 2, categoryName: categoryNames[2]},
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
                        ]
                    }
                },
                {key: 'nuvarandeArbete', type: 'multi-text',
                    className: 'fold-animation',
                    hideExpression: '!model.sysselsattning["NUVARANDE_ARBETE"]',
                    templateOptions: {label: 'FRG_29'}},
                {key: 'arbetsmarknadspolitisktProgram', type: 'multi-text',
                    className: 'fold-animation',
                    hideExpression: '!model.sysselsattning["PROGRAM"]',
                    templateOptions: {label: 'FRG_30'}}
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 3, categoryName: categoryNames[3]},
            fieldGroup: [
                { type: 'fmb',
                    templateOptions: {relatedFormId: categoryNames[3], helpTextContents: 'DIAGNOS', panelClass: 'sit-fmb-medium', hideFromSigned:true},
                    hideExpression:  function($viewValue, $modelValue, scope) {
                        return _missingInfoForFmbKey(scope, 'DIAGNOS');
                    }
                },
                {type: 'headline', templateOptions: {label: 'FRG_6', level:4, noH5:false}},
                {
                    key: 'diagnoser',
                    type: 'diagnos',
                    data: {  enableFMB: true},
                    templateOptions: {diagnosBeskrivningLabel: 'DFR_6.1', diagnosKodLabel: 'DFR_6.2'}
                }
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 4, categoryName: categoryNames[4]},
            fieldGroup: [
                {
                    type: 'fmb',
                    templateOptions: {relatedFormId: categoryNames[4], helpTextContents: 'FUNKTIONSNEDSATTNING', panelClass: 'sit-fmb-small', hideFromSigned:true},
                    hideExpression:  function($viewValue, $modelValue, scope) {
                        return _missingInfoForFmbKey(scope, 'FUNKTIONSNEDSATTNING');
                    }
                },
                {key: 'funktionsnedsattning', type: 'multi-text', templateOptions: {label: 'DFR_35.1'}},

                {type: 'fmb',
                    templateOptions: {relatedFormId: categoryNames[3], helpTextContents: 'AKTIVITETSBEGRANSNING', panelClass: 'sit-fmb-large', hideFromSigned:true},
                    hideExpression:  function($viewValue, $modelValue, scope) {
                        return _missingInfoForFmbKey(scope, 'AKTIVITETSBEGRANSNING');
                    }
                },
                {key: 'aktivitetsbegransning', type: 'multi-text', templateOptions: {label: 'DFR_17.1'}}
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 5, categoryName: categoryNames[5]},
            fieldGroup: [
                {key: 'pagaendeBehandling', type: 'multi-text', templateOptions: {label: 'DFR_19.1'}},
                {key: 'planeradBehandling', type: 'multi-text', templateOptions: {label: 'DFR_20.1'}}
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 6, categoryName: categoryNames[6]},
            fieldGroup: [
                {type: 'headline', templateOptions: {label: 'FRG_32', hideFromSigned:true}},
                { type: 'fmb',
                    templateOptions: {relatedFormId: categoryNames[3], helpTextContents: 'ARBETSFORMAGA', panelClass: 'sit-fmb-large', hideFromSigned:true},
                    hideExpression:  function($viewValue, $modelValue, scope) {
                        return _missingInfoForFmbKey(scope, 'ARBETSFORMAGA');
                    }
                },
                {
                    key: 'sjukskrivningar', type: 'sjukskrivningar',
                    templateOptions: {
                        label: 'DFR_32.1',
                        code: 'KV_FKMU_0003',
                        fields: ['HELT_NEDSATT',
                            'TRE_FJARDEDEL',
                            'HALFTEN',
                            'EN_FJARDEDEL'
                        ]
                    }
                },
                {key: 'forsakringsmedicinsktBeslutsstod', type: 'multi-text', templateOptions: {label: 'DFR_37.1'}},
                {key: 'arbetstidsforlaggning', type: 'boolean',
                    className: 'fold-animation',
                    hideExpression: function($viewValue, $modelValue, scope) {

                        var sjukskrivningar = scope.model.sjukskrivningar;

                        var nedsatt75under = false;
                        angular.forEach(sjukskrivningar, function(item, key) {
                           if(!nedsatt75under && key !== 'HELT_NEDSATT') {
                               if(item.period && DateUtils.isDate(item.period.from) && DateUtils.isDate(item.period.tom)) {
                                   nedsatt75under = true;
                               }
                           }
                        });

                        return !nedsatt75under;
                    },
                    templateOptions: {label: 'FRG_33'}},
                {key: 'arbetstidsforlaggningMotivering', type: 'multi-text',
                    className: 'fold-animation',
                    hideExpression: function($viewValue, $modelValue, scope) {
                        return scope.model.arbetstidsforlaggning !== true;
                    },
                    templateOptions: {label: 'DFR_33.2'}},
                {key: 'arbetsresor', type: 'boolean', templateOptions: {label: 'DFR_34.1'}},

                { key: 'prognos', type: 'prognos',
                    templateOptions: {
                        label: 'FRG_39',
                        code: 'KV_FKMU_0006',
                        choices: [{ id: 'STOR_SANNOLIKHET', showDropDown : false },
                            { id: 'SANNOLIKT_INTE', showDropDown : false },
                            { id: 'PROGNOS_OKLAR', showDropDown : false },
                            { id: 'ATER_X_ANTAL_DGR', showDropDown : true }
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
                        listener:  function _prognosTypListener(field, newValue, oldValue, scope, stopWatching) {
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
            fieldGroup: [
                {
                    key: 'arbetslivsinriktadeAtgarder', type: 'check-group',
                    templateOptions: {
                        label: 'FRG_40',
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
                    expressionProperties: {
                        // Disable option 'EJ_AKTUELLT' if any other option is selected
                        'templateOptions.disabled["EJ_AKTUELLT"]': function($viewValue, $modelValue) {
                            if (!$modelValue) {
                                return;
                            }
                            var disabled = false;
                            angular.forEach($modelValue, function(item, key) {
                                if(item === true && key !== 'EJ_AKTUELLT') {
                                    disabled = true;
                                }
                            });
                            return disabled;
                        },
                        // Disable other options if option 'EJ_AKTUELLT' is selected
                        'templateOptions.disabled["ARBETSTRANING"]': 'model.arbetslivsinriktadeAtgarder["EJ_AKTUELLT"]',
                        'templateOptions.disabled["ARBETSANPASSNING"]': 'model.arbetslivsinriktadeAtgarder["EJ_AKTUELLT"]',
                        'templateOptions.disabled["SOKA_NYTT_ARBETE"]': 'model.arbetslivsinriktadeAtgarder["EJ_AKTUELLT"]',
                        'templateOptions.disabled["BESOK_ARBETSPLATS"]': 'model.arbetslivsinriktadeAtgarder["EJ_AKTUELLT"]',
                        'templateOptions.disabled["ERGONOMISK"]': 'model.arbetslivsinriktadeAtgarder["EJ_AKTUELLT"]',
                        'templateOptions.disabled["HJALPMEDEL"]': 'model.arbetslivsinriktadeAtgarder["EJ_AKTUELLT"]',
                        'templateOptions.disabled["KONFLIKTHANTERING"]': 'model.arbetslivsinriktadeAtgarder["EJ_AKTUELLT"]',
                        'templateOptions.disabled["KONTAKT_FHV"]': 'model.arbetslivsinriktadeAtgarder["EJ_AKTUELLT"]',
                        'templateOptions.disabled["OMFORDELNING"]': 'model.arbetslivsinriktadeAtgarder["EJ_AKTUELLT"]',
                        'templateOptions.disabled["OVRIGA_ATGARDER"]': 'model.arbetslivsinriktadeAtgarder["EJ_AKTUELLT"]'
                    }
                },
                {
                    key: 'arbetslivsinriktadeAtgarderBeskrivning',
                    type: 'multi-text',
                    className: 'fold-animation',
                    hideExpression: function($viewValue, $modelValue, scope) {
                        var hide = true;
                        angular.forEach(scope.model.arbetslivsinriktadeAtgarder, function(atgard, key) {
                            if(atgard === true && key !== 'EJ_AKTUELLT') {
                                hide = false;
                                return;
                            }
                        });
                        return hide;
                    },
                    templateOptions: {label: 'FRG_44'}
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
