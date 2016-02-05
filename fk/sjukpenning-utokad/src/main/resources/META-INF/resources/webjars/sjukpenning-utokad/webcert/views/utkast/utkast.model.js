angular.module('lisu').factory('sjukpenning-utokad.Domain.IntygModel',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
        'common.domain.BaseAtticModel',
        function(GrundData, DraftModel, ModelAttr, BaseAtticModel) {
            'use strict';

            var diagnosTransform = function(diagnosArray) {
                if (diagnosArray.length === 0) {
                    diagnosArray.push({
                        diagnosKodSystem: undefined,
                        diagnosKod : undefined,
                        diagnosBeskrivning : undefined
                    });
                }
                return diagnosArray;
            };

            var sjukskrivningTransform = function(sjukskrivningArray) {
                if (sjukskrivningArray.length === 0) {
                    sjukskrivningArray.push({
                        sjukskrivningsgrad: undefined,
                        period : {
                            from: '',
                            tom: ''
                        }
                    });
                }
                return sjukskrivningArray;
            };

            var arbetslivsAtgarderTransform = function(arbetsatgarderArray) {
                /*if (arbetsatgarderArray.length === 0) {
                    arbetsatgarderArray.push();
                }*/
                return arbetsatgarderArray;
            };

            var sjukersattningModel = BaseAtticModel._extend({
                init: function init() {
                    var grundData = GrundData.build();
                    init._super.call(this, 'sjukersattningModel', {

                        // Dessa nycklar på grupperna används inte någonstans på något sätt. Men strängarna måste matcha backend för att load/save ska fungera
                        formUnderlag: ['undersokningAvPatienten',
                            'telefonkontaktMedPatienten',
                            'journaluppgifter',
                            'anhorigsBeskrivningAvPatienten',
                            'annatGrundForMU',
                            'annatGrundForMUBeskrivning'
                        ],

                        formSysselsattning: [
                            'sysselsattning',
                            'nuvarandeArbete',
                            'arbetsmarknadspolitisktProgram'
                        ],

                        formDiagnos: [
                            new ModelAttr('diagnoser', {fromTransform: diagnosTransform})
                        ],

                        formFunktionsnedsattning: [
                            'funktionsnedsattning',
                            'aktivitetsbegransning'
                        ],

                        formMedicinskaBehandlingar: [
                            'pagaendeBehandling',
                            'planeradBehandling'
                        ],

                        formBedomning: [
                            new ModelAttr('sjukskrivningar', {fromTransform: sjukskrivningTransform}),
                            'forsakringsmedicinsktBeslutsstod',
                            'arbetstidsforlaggning',
                            'arbetstidsforlaggningMotivering',
                            'arbetsresor',
                            'formagaTrotsBegransning',
                            'prognos',
                            'fortydligande'
                        ],

                        formAtgarder: [
                            new ModelAttr('arbetslivsinriktadeAtgarder', {fromTransform: arbetslivsAtgarderTransform}),
                            'arbetslivsinriktadeAtgarderAktuelltBeskrivning',
                            'arbetslivsinriktadeAtgarderEjAktuelltBeskrivning',
                        ],

                        formOvrigt: ['ovrigt'],

                        formKontakt: [ new ModelAttr( 'kontaktMedFk', { defaultValue : false }),
                                'anledningTillKontakt'],

                        tillaggsfragor: [ new ModelAttr( 'tillaggsfragor', { defaultValue : [] }) ],

                        misc: [ 'id',
                                'textVersion',
                                new ModelAttr('grundData', {defaultValue: grundData})]
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
                    return new DraftModel(new sjukersattningModel());
                }
            });

            /**
             * Return the constructor function IntygModel
             */
            return sjukersattningModel;

        }]);
