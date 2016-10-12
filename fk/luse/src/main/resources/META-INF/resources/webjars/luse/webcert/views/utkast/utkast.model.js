angular.module('luse').factory('luse.Domain.IntygModel',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
        'common.domain.BaseAtticModel', 'common.ObjectHelper',
        function(GrundData, DraftModel, ModelAttr, BaseAtticModel, ObjectHelper) {
            'use strict';

            var underlagFromTransform = function(underlagArray) {

                // We now always have a specific amount of underlag so add that number of empty elements  
                for(var i = 0; underlagArray.length < 3; i++){
                    underlagArray.push({
                        typ: null,
                        datum: null,
                        hamtasFran: null
                    });
                }
                
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

            var underlagToTransform = function(underlagArray) {

                var underlagCopy = angular.copy(underlagArray);

                // delete all rows with no values at all so as to not confuse backend with non-errors
                var i = 0;
                while(i < underlagCopy.length) {
                    if(ObjectHelper.isEmpty(underlagCopy[i].typ) &&
                        ObjectHelper.isEmpty(underlagCopy[i].datum) &&
                        ObjectHelper.isEmpty(underlagCopy[i].hamtasFran)){
                        underlagCopy.splice(i, 1);
                    } else {
                        i++;
                    }
                }
                
                return underlagCopy;
            };

            var diagnosFromTransform = function(diagnosArray) {

                // We now always have a specific amount of underlag so add that number of empty elements  
                for(var i = 0; diagnosArray.length < 3; i++){
                    diagnosArray.push({
                        diagnosKodSystem: 'ICD_10_SE',
                        diagnosKod : undefined,
                        diagnosBeskrivning : undefined
                    });
                }

                return diagnosArray;
            };

            var diagnosToTransform = function(diagnosArray) {
                var diagnosCopy = angular.copy(diagnosArray);

                // delete all rows with no values at all so as to not confuse backend with non-errors
                var i = 0;
                while(i < diagnosCopy.length) {
                    if(ObjectHelper.isEmpty(diagnosCopy[i].diagnosKod) &&
                        ObjectHelper.isEmpty(diagnosCopy[i].diagnosBeskrivning)){
                        diagnosCopy.splice(i, 1);
                    } else {
                        i++;
                    }
                }
                
                return diagnosCopy;
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
                        'motiveringTillInteBaseratPaUndersokning':undefined,
                        'kannedomOmPatient':undefined,

                        // Kategori 2 Andra medicinska utredningar och underlag
                        'underlagFinns':undefined,
                        'underlag':new ModelAttr('underlag', {fromTransform: underlagFromTransform, toTransform: underlagToTransform}),

                        // Kategori 3 Sjukdomsförlopp
                        'sjukdomsforlopp':undefined,

                        // Ketegori 4 diagnos
                        'diagnoser':new ModelAttr('diagnoser', {fromTransform: diagnosFromTransform, toTransform: diagnosToTransform}),
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
