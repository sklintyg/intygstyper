angular.module('sjukersattning').factory('sjukersattning.Domain.IntygModel',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
        'common.domain.BaseAtticModel',
        function(GrundData, DraftModel, ModelAttr, BaseAtticModel) {
            'use strict';

            var underlagTransform = function(underlagArray) {
                if (underlagArray) {
                    underlagArray.forEach(function(underlag) {
                        if (!underlag.typ) {
                            underlag.typ = null;
                        }
                        if (!underlag.datum) {
                            underlag.datum = null;
                        }
                        if (!underlag.hamtasFran) {
                            underlag.hamtasFran = null;
                        }
                    });
                }
                return underlagArray;
            };

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

            var sjukersattningModel = BaseAtticModel._extend({
                init: function init() {
                    var grundData = GrundData.build();
                    init._super.call(this, 'sjukersattningModel', {

                        formUnderlag: ['undersokningAvPatienten',
                            'journaluppgifter',
                            'anhorigsBeskrivningAvPatienten',
                            'annatGrundForMU',
                            'annatGrundForMUBeskrivning',
                            'kannedomOmPatient',
                            'underlagFinns',
                            new ModelAttr('underlag', {fromTransform: underlagTransform})
                        ],

                        formSjukdomsforlopp: [
                            'sjukdomsforlopp'
                        ],

                        formDiagnos: [
                            new ModelAttr('diagnoser', {fromTransform: diagnosTransform})
                        ],

                        formDiagnos2: ['diagnosgrund',
                                 'nyBedomningDiagnosgrund' ],

                        formFunktionsnedsattning: ['funktionsnedsattningIntellektuell',
                            'funktionsnedsattningKommunikation',
                            'funktionsnedsattningKoncentration',
                            'funktionsnedsattningPsykisk',
                            'funktionsnedsattningSynHorselTal',
                            'funktionsnedsattningBalansKoordination',
                            'funktionsnedsattningAnnan'],

                        formAktivitetsBegransning: ['aktivitetsbegransning'],

                        formMedicinskaBehandlingar: ['avslutadBehandling',
                            'pagaendeBehandling',
                            'planeradBehandling',
                            'substansintag'],

                        formMedicinskaForutsattningar: [ 'medicinskaForutsattningarForArbete',
                                 'aktivitetsFormaga'],

                        formOvrigt: ['ovrigt'],

                        formKontakt: [ new ModelAttr( 'kontaktMedFk', { defaultValue : false }),
                                'anledningTillKontakt'],

                        tillaggsfragor: [ new ModelAttr( 'tillaggsfragor', { defaultValue : [] }) ],

                        misc: [ 'id',
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
