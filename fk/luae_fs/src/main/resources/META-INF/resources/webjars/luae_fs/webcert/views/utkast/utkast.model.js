angular.module('luae_fs').factory('luae_fs.Domain.IntygModel',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
        'common.domain.BaseAtticModel', 'common.domain.ModelTransformService', 'common.ObjectHelper',
        function(GrundData, DraftModel, ModelAttr, BaseAtticModel, ModelTransform, ObjectHelper) {
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



            var luae_fsModel = BaseAtticModel._extend({
                init: function init() {
                    var grundData = GrundData.build();
                    init._super.call(this, 'luae_fsModel', {

                        'id': undefined,
                        'textVersion': undefined,
                        'grundData': grundData,

                        // Kategori 1 Grund för medicinskt underlag
                        'undersokningAvPatienten': undefined,           //KV_FKMU_0001_1
                        'journaluppgifter': undefined,                  //KV_FKMU_0001_3
                        'anhorigsBeskrivningAvPatienten': undefined,    //KV_FKMU_0001_4
                        'annatGrundForMU': undefined,                   //KV_FKMU_0001_5
                        'annatGrundForMUBeskrivning': undefined,
                        'kannedomOmPatient':undefined,

                        // Kategori 2 Andra medicinska utredningar och underlag
                        'underlagFinns':undefined,
                        'underlag':new ModelAttr('underlag', {fromTransform: underlagTransform}),



                        // Kategori 3 diagnos
                        'diagnoser':new ModelAttr('diagnoser', {fromTransform: diagnosTransform}),


                        // Kategori 4 Funktionsnedsättning
                        'funktionsnedsattningDebut': undefined,  //15.1
                        'funktionsnedsattningPaverkan': undefined,  //16.1

                        // Kategori 5 Övrigt
                        'ovrigt': undefined,

                        // Kategori 6 Kontakt
                        'kontaktMedFk': new ModelAttr( 'kontaktMedFk', { defaultValue : false }), // 26.1
                        'anledningTillKontakt': undefined, //26.2



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
                    return new DraftModel(new luae_fsModel());
                }
            });

            /**
             * Return the constructor function IntygModel
             */
            return luae_fsModel;

        }]);