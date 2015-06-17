angular.module('sjukpenning').factory('sjukpenning.Domain.IntygModel',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
        'common.domain.BaseAtticModel',
        function(GrundData, DraftModel, ModelAttr, BaseAtticModel) {
            'use strict';


            // --- Transform functions
            var nedsattFromTransform = function(nedsatt) {
                if (!nedsatt || (!nedsatt.from && !nedsatt.tom)) {
                    return undefined;
                } else {
                    return nedsatt;
                }
            };
            var sjukpenningModel = BaseAtticModel._extend({
                init: function init() {
                    //console.log('--- init fk gd' + GrundData);
                    var grundData = GrundData.build();
                    init._super.call(this, 'sjukpenningModel', {

                        form1: [new ModelAttr('avstangningSmittskydd', {defaultValue: false})],

                        form2: ['diagnosBeskrivning',
                                'diagnosBeskrivning1',
                                'diagnosBeskrivning2',
                                'diagnosBeskrivning3',
                                'diagnosKod',
                                'diagnosKod2',
                                'diagnosKod3',
                                'diagnosKodsystem1',
                                'diagnosKodsystem2',
                                'diagnosKodsystem3',
                                new ModelAttr('samsjuklighet', {defaultValue: false})],

                        form3:  ['sjukdomsforlopp'],

                        form4:  ['funktionsnedsattning'],

                        form4b:['annanReferens',
                                'annanReferensBeskrivning',
                                'journaluppgifter',
                                'telefonkontaktMedPatienten',
                                'undersokningAvPatienten'],

                        form5: ['aktivitetsbegransning'],

                        form6a:['rekommendationKontaktArbetsformedlingen',
                                'rekommendationKontaktForetagshalsovarden',
                                'rekommendationOvrigt',
                                'rekommendationOvrigtCheck'],

                        form6b:['atgardInomSjukvarden',
                                'annanAtgard'],

                        form7: ['rehabilitering',
                                new ModelAttr('rehab', {
                                    trans : true, // we don't want this going back to the server!!
                                    defaultValue : 'rehabiliteringEjAktuell',
                                    linkedProperty:{
                                        props:['rehabilitering'],
                                        update:function(model, props){
                                            if(props.rehabilitering === 'rehabiliteringAktuell'){
                                                return 'JA';
                                            } else if(props.rehabilitering === 'rehabiliteringEjAktuell'){
                                                return 'NEJ';
                                            } else if(props.rehabilitering === 'rehabiliteringGarInteAttBedoma'){
                                                return 'GAREJ';
                                            } else {
                                                return '';
                                            }
                                        },
                                        set : function(value){ // this is the model
                                            switch (value) {
                                            case 'JA':
                                                this.rehabilitering = 'rehabiliteringAktuell';
                                                break;
                                            case 'NEJ':
                                                this.rehabilitering = 'rehabiliteringEjAktuell';
                                                break;
                                            case 'GAREJ':
                                                this.rehabilitering = 'rehabiliteringGarInteAttBedoma';
                                                break;
                                            default :
                                                this.rehabilitering = undefined;
                                            }
                                        }
                                    }})],

                        form8a:['arbetsloshet',
                                'foraldrarledighet',
                                'nuvarandeArbete',
                                'nuvarandeArbetsuppgifter'],

                        form8b:['tjanstgoringstid',
                                new ModelAttr('nedsattMed100', {fromTransform: nedsattFromTransform}),
                                new ModelAttr('nedsattMed25', {fromTransform: nedsattFromTransform}),
                                'nedsattMed25Beskrivning',
                                new ModelAttr('nedsattMed50', {fromTransform: nedsattFromTransform}),
                                'nedsattMed50Beskrivning',
                                new ModelAttr('nedsattMed75', {fromTransform: nedsattFromTransform}),
                                'nedsattMed75Beskrivning'],

                        form9: ['arbetsformagaPrognos'],

                        form10:['arbetsformagaPrognosGarInteAttBedomaBeskrivning',
                                'prognosBedomning'],

                        form11:['ressattTillArbeteAktuellt',
                                'ressattTillArbeteEjAktuellt',
                                new ModelAttr('ressattTillArbete', {
                                    trans : true, // we don't want this going back to the server!!
                                    linkedProperty:{
                                        props:['ressattTillArbeteAktuellt','ressattTillArbeteEjAktuellt'],
                                        update:function(model, props){
                                            if(props.ressattTillArbeteAktuellt){
                                                return 'JA';
                                            } else if(props.ressattTillArbeteEjAktuellt){
                                                return 'NEJ';
                                            } else {
                                                return '';
                                            }
                                        },
                                        set : function(value){ // 'this' is the model
                                            this.ressattTillArbeteAktuellt = value === 'JA';
                                            this.ressattTillArbeteEjAktuellt = value === 'NEJ';

                                            this.updateToAttic(['ressattTillArbete', 'ressattTillArbeteAktuellt', 'ressattTillArbeteEjAktuellt']);
                                        }
                                }})],

                        form12:['kontaktMedFk'],

                        form13:['kommentar'],

                        misc:  ['forskrivarkodOchArbetsplatskod',
                                'namnfortydligandeOchAdress', 'id',
                                new ModelAttr('grundData', {defaultValue: grundData})]

                    });

                },

                update: function update(content, parent) {
                    if (parent) {
                        parent.content = this;
                    }
                    update._super.call(this, content);
                },

                atticUpdateForm6a711 : function(){
                    var props = this.properties.form6a.concat(this.properties.form7.concat(this.properties.form11));
                    this.updateToAttic(props);
                },
                clearForm6a711 : function(){
                    var props = this.properties.form6a.concat(this.properties.form7.concat(this.properties.form11));
                    this.clear(props);
                },
                atticRestoreForm6a711 : function(){
                    var props = this.properties.form6a.concat(this.properties.form7.concat(this.properties.form11));
                    this.restoreFromAttic(props);
                }
            }, {
                build : function(){
                    //console.log('----- build *****' + DraftModel + ',' + sjukpenningModel._members);
                    return new DraftModel(new sjukpenningModel());
                }
            });

            /**
             * Return the constructor function IntygModel
             */
            return sjukpenningModel;

        }]);
