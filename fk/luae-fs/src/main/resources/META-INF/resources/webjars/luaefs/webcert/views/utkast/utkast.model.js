angular.module('luaefs').factory('luaefs.Domain.IntygModel',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
        'common.domain.BaseAtticModel', 'common.domain.ModelTransformService', 'common.ObjectHelper',
        function(GrundData, DraftModel, ModelAttr, BaseAtticModel, ModelTransform, ObjectHelper) {
            'use strict';

            var diagnosTransform = function(diagnosArray) {
                if (diagnosArray.length === 0) {
                    diagnosArray.push({
                        diagnosKodSystem: 'ICD_10_SE',
                        diagnosKod : undefined,
                        diagnosBeskrivning : undefined
                    });
                }
                return diagnosArray;
            };

            var sjukskrivningFromTransform = function(sjukskrivningArray) {

                var resultObject = {
                    1: {
                        period: {
                            from: '',
                            tom: ''
                        }
                    },
                    2: {
                        period: {
                            from: '',
                            tom: ''
                        }
                    },
                    3: {
                        period: {
                            from: '',
                            tom: ''
                        }
                    },
                    4: {
                        period: {
                            from: '',
                            tom: ''
                        }
                    }
                };

                for(var i = 0; i < sjukskrivningArray.length; i++){
                    resultObject[sjukskrivningArray[i].sjukskrivningsgrad] = {
                        period: sjukskrivningArray[i].period
                    };
                }

                return resultObject;
            };

            var sjukskrivningToTransform = function(sjukskrivningObject) {

                var resultArray = [];

                angular.forEach(sjukskrivningObject, function(value, key) {
                    if(!ObjectHelper.isEmpty(value.period.from) || !ObjectHelper.isEmpty(value.period.tom)) {
                        resultArray.push({
                            sjukskrivningsgrad: Number(key),
                            period: {
                                from: value.period.from,
                                tom: value.period.tom
                            }
                        });
                    }
                }, sjukskrivningObject);

                return resultArray;
            };

            var sjukpenningUtokadModel = BaseAtticModel._extend({
                init: function init() {
                    var grundData = GrundData.build();
                    init._super.call(this, 'sjukersattningModel', {

                        'id': undefined,
                        'textVersion': undefined,
                        'grundData': grundData,

                        // Kategori 1 Grund för medicinskt underlag
                        'undersokningAvPatienten': undefined,
                        'telefonkontaktMedPatienten': undefined,
                        'journaluppgifter': undefined,
                        'annatGrundForMU': undefined,
                        'annatGrundForMUBeskrivning': undefined,

                        // Kategori 2 sysselsättning
                        'sysselsattning': {
                            typ: undefined
                        },
                        'nuvarandeArbete' : undefined,
                        'arbetsmarknadspolitisktProgram': undefined,

                        // Kategori 3 diagnos
                        'diagnoser':new ModelAttr('diagnoser', {fromTransform: diagnosTransform}),

                        // Kategori 4 Sjukdomens konsekvenser
                        'funktionsnedsattning': undefined,
                        'aktivitetsbegransning': undefined,

                        // Kategori 5 Medicinska behandlingar / åtgärder
                        'pagaendeBehandling': undefined,
                        'planeradBehandling': undefined,

                        // Kategory 6 Bedömning
                        'sjukskrivningar': new ModelAttr('sjukskrivningar', {
                            defaultValue : {
                                1: {
                                    period: {
                                        from: '',
                                        tom: ''
                                    }
                                },
                                2: {
                                    period: {
                                        from: '',
                                        tom: ''
                                    }
                                },
                                3: {
                                    period: {
                                        from: '',
                                        tom: ''
                                    }
                                },
                                4: {
                                    period: {
                                        from: '',
                                        tom: ''
                                    }
                                }
                            },
                            fromTransform: sjukskrivningFromTransform,
                            toTransform: sjukskrivningToTransform
                        }),
                        'forsakringsmedicinsktBeslutsstod': undefined,
                        'arbetstidsforlaggning': undefined,
                        'arbetstidsforlaggningMotivering': undefined,
                        'arbetsresor': undefined,
                        'formagaTrotsBegransning': undefined,
                        'prognos': undefined,
                        'fortydligande': undefined,

                        // Kategori 7 Åtgärder
                        'arbetslivsinriktadeAtgarder': new ModelAttr('arbetslivsinriktadeAtgarder', {
                            toTransform: ModelTransform.toTypeTransform,
                            fromTransform: ModelTransform.fromTypeTransform
                        }),
                        'arbetslivsinriktadeAtgarderAktuelltBeskrivning': undefined,
                        'arbetslivsinriktadeAtgarderEjAktuelltBeskrivning': undefined,

                        // Kategori 8 Övrigt
                        'ovrigt': undefined,

                        // Kategori 9 Kontakt
                        'kontaktMedFk': new ModelAttr( 'kontaktMedFk', { defaultValue : false }),
                        'anledningTillKontakt': undefined,

                        // Kategori 9999 Tilläggsfrågor
                        'tillaggsfragor': new ModelAttr( 'tillaggsfragor', { defaultValue : [] })
                    });
                },
                update: function update(content, parent) {
                    if (parent) {
                        parent.content = this;
                    }
                    update._super.call(this, content);
                }

            }, {
                build : function(){
                    return new DraftModel(new sjukpenningUtokadModel());
                }
            });

            /**
             * Return the constructor function IntygModel
             */
            return sjukpenningUtokadModel;

        }]);
