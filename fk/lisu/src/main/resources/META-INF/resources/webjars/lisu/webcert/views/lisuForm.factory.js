angular.module('lisu').factory('lisu.FormFactory',
    ['common.DateUtilsService', 'common.ObjectHelper', 'common.fmb.ViewStateService', 'common.UserModel',
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
        'kontakt'
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
                }
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 1, categoryName: categoryNames[1]},
            fieldGroup: [
                {type: 'headline', templateOptions: {label: 'FRG_1', level: 4}},
                {type: 'headline', className: 'col-md-6 no-space-left', templateOptions: {label: 'DFR_1.1', hideFromSigned:true}},
                {type: 'headline', className: 'col-md-6', templateOptions: {label: 'DFR_1.2', hideFromSigned:true}},
                {key: 'undersokningAvPatienten', type: 'date', templateOptions: {label: 'KV_FKMU_0001.1'}},
                {key: 'telefonkontaktMedPatienten', type: 'date', templateOptions: {label: 'KV_FKMU_0001.2'}},
                {key: 'journaluppgifter', type: 'date', templateOptions: {label: 'KV_FKMU_0001.3'}},
                {key: 'annatGrundForMU', type: 'date', templateOptions: {label: 'KV_FKMU_0001.5'}},
                {
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
                    key: 'sysselsattning.typ', type: 'radio-group',
                    templateOptions: {
                        label: 'DFR_28.1',
                        code: 'KV_FKMU_0002',
                        choices: [1, 2, 3, 4, 5]
                    }
                },
                {key: 'nuvarandeArbete', type: 'multi-text',
                    className: 'fold-animation',
                    hideExpression: 'model.sysselsattning.typ != 1',
                    templateOptions: {label: 'DFR_29.1'}},
                {key: 'arbetsmarknadspolitisktProgram', type: 'multi-text',
                    className: 'fold-animation',
                    hideExpression: 'model.sysselsattning.typ != 5',
                    templateOptions: {label: 'DFR_30.1'}}
            ]
        },
        {
            wrapper: 'wc-field',
            templateOptions: {category: 3, categoryName: categoryNames[3]},
            fieldGroup: [
                { type: 'fmb',
                    templateOptions: {relatedFormId: categoryNames[3], helpTextContents: 'DIAGNOS', panelClass: 'sit-fmb-medium'},
                    hideExpression:  function($viewValue, $modelValue, scope) {
                        return _missingInfoForFmbKey(scope, 'DIAGNOS');
                    }
                },
                {type: 'headline', templateOptions: {label: 'FRG_6', level:4}},
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
                    templateOptions: {relatedFormId: categoryNames[4], helpTextContents: 'FUNKTIONSNEDSATTNING', panelClass: 'sit-fmb-small'},
                    hideExpression:  function($viewValue, $modelValue, scope) {
                        return _missingInfoForFmbKey(scope, 'FUNKTIONSNEDSATTNING');
                    }
                },
                {key: 'funktionsnedsattning', type: 'multi-text', templateOptions: {label: 'DFR_35.1'}},

                {type: 'fmb',
                    templateOptions: {relatedFormId: categoryNames[3], helpTextContents: 'AKTIVITETSBEGRANSNING', panelClass: 'sit-fmb-large'},
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
                {type: 'headline', templateOptions: {label: 'FRG_32'}},
                { type: 'fmb',
                    templateOptions: {relatedFormId: categoryNames[3], helpTextContents: 'ARBETSFORMAGA', panelClass: 'sit-fmb-large'},
                    hideExpression:  function($viewValue, $modelValue, scope) {
                        return _missingInfoForFmbKey(scope, 'ARBETSFORMAGA');
                    }
                },
                {
                    key: 'sjukskrivningar', type: 'sjukskrivningar',
                    templateOptions: {
                        label: 'DFR_32.1',
                        code: 'KV_FKMU_0003',
                        fields: [1, 2, 3, 4]
                    }
                },
                {key: 'forsakringsmedicinsktBeslutsstod', type: 'multi-text', templateOptions: {label: 'DFR_37.1'}},
                {key: 'arbetstidsforlaggning', type: 'boolean',
                    className: 'fold-animation',
                    hideExpression: function($viewValue, $modelValue, scope) {

                        var sjukskrivningar = scope.model.sjukskrivningar;

                        var nedsatt75under = false;
                        angular.forEach(sjukskrivningar, function(item, key) {
                           if(!nedsatt75under && key > 1) {
                               if(item.period && DateUtils.isDate(item.period.from) && DateUtils.isDate(item.period.tom)) {
                                   nedsatt75under = true;
                               }
                           }
                        });

                        return !nedsatt75under;
                    },
                    templateOptions: {label: 'DFR_33.1'}},
                {key: 'arbetstidsforlaggningMotivering', type: 'multi-text',
                    className: 'fold-animation',
                    hideExpression: function($viewValue, $modelValue, scope) {
                        return scope.model.arbetstidsforlaggning !== true;
                    },
                    templateOptions: {label: 'DFR_33.2'}},
                {key: 'arbetsresor', type: 'boolean', templateOptions: {label: 'DFR_34.1'}},

                {key: 'formagaTrotsBegransning', type: 'multi-text', templateOptions: {label: 'DFR_23.1'}},

                { key: 'prognos.typ', type: 'radio-group',
                    templateOptions: {
                        label: 'DFR_39.1',
                        code: 'KV_FKMU_0006',
                        choices: [1, 3, 4, 5]
                    },
                    watcher: {
                        expression: 'model.prognos.typ',
                        listener:  function _prognosTypListener(field, newValue, oldValue, scope, stopWatching) {
                            var model = scope.model;
                            if (newValue === 5) {
                                model.restoreFromAttic('prognos.dagarTillArbete');
                            } else {
                                model.updateToAttic('prognos.dagarTillArbete');
                                model.clear('prognos.dagarTillArbete');
                            }
                        }
                    }
                },
                { key: 'prognos.dagarTillArbete', type: 'radio-group',
                    className: 'fold-animation',
                    hideExpression: function($viewValue, $modelValue, scope) {
                        return !ObjectHelper.isDefined(scope.model.prognos) || scope.model.prognos.typ !== 5;
                    },
                    templateOptions: {
                        label: 'DFR_39.3',
                        code: 'KV_FKMU_0007',
                        choices: [1, 2, 3, 4]
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
                        label: 'DFR_40.1',
                        code: 'KV_FKMU_0004',
                        choices: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
                    },
                    expressionProperties: {
                        // Disable option 1 if any option 2-11 is selected
                        'templateOptions.disabled[1]': function($viewValue, $modelValue) {
                            if (!$modelValue) {
                                return;
                            }
                            var disabled = false;
                            for(var i=2; i<=11; i++) {
                                if ($modelValue[i]) {
                                    disabled = true;
                                }
                            }
                            return disabled;
                        },
                        // Disable option 2-11 if option 1 is selected
                        'templateOptions.disabled[2]': 'model.arbetslivsinriktadeAtgarder[1]',
                        'templateOptions.disabled[3]': 'model.arbetslivsinriktadeAtgarder[1]',
                        'templateOptions.disabled[4]': 'model.arbetslivsinriktadeAtgarder[1]',
                        'templateOptions.disabled[5]': 'model.arbetslivsinriktadeAtgarder[1]',
                        'templateOptions.disabled[6]': 'model.arbetslivsinriktadeAtgarder[1]',
                        'templateOptions.disabled[7]': 'model.arbetslivsinriktadeAtgarder[1]',
                        'templateOptions.disabled[8]': 'model.arbetslivsinriktadeAtgarder[1]',
                        'templateOptions.disabled[9]': 'model.arbetslivsinriktadeAtgarder[1]',
                        'templateOptions.disabled[10]': 'model.arbetslivsinriktadeAtgarder[1]',
                        'templateOptions.disabled[11]': 'model.arbetslivsinriktadeAtgarder[1]'
                    }
                },
                {
                    key: 'arbetslivsinriktadeAtgarderAktuelltBeskrivning',
                    type: 'multi-text',
                    className: 'fold-animation',
                    hideExpression: function($viewValue, $modelValue, scope) {
                        var arrayAtgarder = [];
                        angular.forEach(scope.model.arbetslivsinriktadeAtgarder, function(atgard, key) {
                            if(atgard === true) {
                                arrayAtgarder.push(key);
                            }
                        });

                        var results = arrayAtgarder.filter(function(item) {
                            return Number(item) > 1;
                        });
                        return results.length <= 0;
                    },
                    templateOptions: {label: 'DFR_40.2'}
                },
                {
                    key: 'arbetslivsinriktadeAtgarderEjAktuelltBeskrivning',
                    type: 'multi-text',
                    className: 'fold-animation',
                    hideExpression: function($viewValue, $modelValue, scope) {
                        var arrayAtgarder = [];
                        angular.forEach(scope.model.arbetslivsinriktadeAtgarder, function(atgard, key) {
                            if(atgard === true) {
                                arrayAtgarder.push(key);
                            }
                        });

                        var results = arrayAtgarder.filter(function(item) {
                            return Number(item) === 1;
                        });
                        return results.length <= 0;
                    },
                    templateOptions: {label: 'DFR_40.3'}
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
