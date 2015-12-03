angular.module('sjukersattning').factory('sjukersattning.Domain.IntygModel',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
        'common.domain.BaseAtticModel',
        function(GrundData, DraftModel, ModelAttr, BaseAtticModel) {
            'use strict';

            var sjukersattningModel = BaseAtticModel._extend({
                init: function init() {
                    var grundData = GrundData.build();
                    init._super.call(this, 'sjukersattningModel', {

                        form2: ['undersokningAvPatienten',
                            'journaluppgifter',
                            'telefonkontaktMedPatienten',
                            'kannedomOmPatient',
                            new ModelAttr('underlag', {defaultValue: [{ 'typ': undefined, 'datum': undefined, 'bilaga': undefined }] })],

                        form4: [
                            new ModelAttr('diagnoser', {
                            defaultValue: [ { 'diagnosKodSystem': undefined, 'diagnosKod': undefined, 'diagnosBeskrivning': undefined },
                                { 'diagnosKodSystem': undefined, 'diagnosKod': undefined, 'diagnosBeskrivning': undefined },
                                { 'diagnosKodSystem': undefined, 'diagnosKod': undefined, 'diagnosBeskrivning': undefined } ]})
                        ],

                        form4b: ['diagnostisering',
                                 'nyBedomningDiagnos' ],

                        form5: [new ModelAttr( 'funktionsnedsattningar' , {
                            defaultValue:  [  ] })],

                        form6: ['aktivitetsbegransning'],

                        form7: ['avslutadBehandling',
                            'pagaendeBehandling',
                            'planeradBehandling'],

                        form8: [new ModelAttr('atgarder' , {defaultValue : [
                            { 'atgardsKod': undefined, 'atgardsKodSystem':undefined, 'atgardsBeskrivning': undefined}]}
                        )],

                        form9: [ 'aktivitetsFormaga',
                                 'prognos'],

                        form10: ['kommentar'],

                        form11: [ new ModelAttr( 'kontaktMedFk', { defaultValue : false}),
                                'anledningTillKontakt'],

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
