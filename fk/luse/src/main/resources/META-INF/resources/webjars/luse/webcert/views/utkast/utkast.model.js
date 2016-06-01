angular.module('luse').factory('luse.Domain.IntygModel',
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
                        diagnosKodSystem: 'ICD_10_SE',
                        diagnosKod : undefined,
                        diagnosBeskrivning : undefined
                    });
                }
                return diagnosArray;
            };

            var LuseModel = BaseAtticModel._extend({
                init: function init() {
                    var grundData = GrundData.build();
                    init._super.call(this, 'luseModel', {

                        'id': undefined,
                        'textVersion': undefined,
                        'grundData': grundData,

                        // Kategori 1 Grund för medicinskt underlag
                        'undersokningAvPatienten':undefined,
                        'journaluppgifter':undefined,
                        'anhorigsBeskrivningAvPatienten':undefined,
                        'annatGrundForMU':undefined,
                        'annatGrundForMUBeskrivning':undefined,
                        'kannedomOmPatient':undefined,

                        // Kategori 2 Andra medicinska utredningar och underlag
                        'underlagFinns':undefined,
                        'underlag':new ModelAttr('underlag', {fromTransform: underlagTransform}),

                        // Kategori 3 Sjukdomsförlopp
                        'sjukdomsforlopp':undefined,

                        // Ketegori 4 diagnos
                        'diagnoser':new ModelAttr('diagnoser', {fromTransform: diagnosTransform}),
                        'diagnosgrund': undefined,
                        'nyBedomningDiagnosgrund': undefined,
                        'diagnosForNyBedomning' : undefined,

                        // Ketagori 5 Funktionsnedsättning
                        'funktionsnedsattningIntellektuell': undefined,
                        'funktionsnedsattningKommunikation': undefined,
                        'funktionsnedsattningKoncentration': undefined,
                        'funktionsnedsattningPsykisk': undefined,
                        'funktionsnedsattningSynHorselTal': undefined,
                        'funktionsnedsattningBalansKoordination': undefined,
                        'funktionsnedsattningAnnan': undefined,

                        // Kategori 6 Aktivitetsbegransning
                        'aktivitetsbegransning': undefined,

                        // Kategori 7 Medicinska behandlingar/åtgärder
                        'avslutadBehandling': undefined,
                        'pagaendeBehandling': undefined,
                        'planeradBehandling': undefined,
                        'substansintag': undefined,

                        // Kategori 8 Medicinska förutsättningar för arbete
                        'medicinskaForutsattningarForArbete': undefined,
                        'formagaTrotsBegransning': undefined,

                        // Kategori 9 Övrigt
                        'ovrigt': undefined,

                        // Kategori 10 Kontakt
                        'kontaktMedFk' : new ModelAttr( 'kontaktMedFk', { defaultValue : false }),
                        'anledningTillKontakt': undefined,

                        // Kategori 9999 Tilläggsfrågor
                        'tillaggsfragor': [ new ModelAttr( 'tillaggsfragor', { defaultValue : [] }) ]
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
                    return new DraftModel(new LuseModel());
                }
            });

            /**
             * Return the constructor function IntygModel
             */
            return LuseModel;

        }]);
