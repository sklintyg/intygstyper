angular.module('ts-diabetes').factory('ts-diabetes.Domain.IntygModel',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
     'common.domain.BaseAtticModel',
    function(GrundData, DraftModel, ModelAttr, BaseAtticModel) {
        'use strict';

        // --- Transform functions
        var synTransform = function(syn) {
            if (!syn || (!syn.utanKorrektion && !syn.medKorrektion && !syn.kontaktlins)) {
                return undefined;
            } else {
                return syn;
            }
        };

        var korkortstypTransform = function(korkortstyp) {
            if (!korkortstyp) {
                return undefined;
            } else {
                return korkortstyp;
            }
        };

        var TsDiabetesModel = BaseAtticModel._extend({
            init: function init() {
                var grundData = GrundData.build();
                init._super.call(this, 'IntygModel', {
                    id: undefined,
                    typ: undefined,
                    kommentar:undefined,
                    grundData: grundData,
                    vardkontakt: {
                        typ: undefined,
                        idkontroll: undefined
                    },
                    intygAvser: {
                        korkortstyp: new ModelAttr('korkortstyp', {defaultValue:[
                            {'type': 'AM', 'selected': false},
                            {'type': 'A1', 'selected': false},
                            {'type': 'A2', 'selected': false},
                            {'type': 'A', 'selected': false},
                            {'type': 'B', 'selected': false},
                            {'type': 'BE', 'selected': false},
                            {'type': 'TRAKTOR', 'selected': false},
                            {'type': 'C1', 'selected': false},
                            {'type': 'C1E', 'selected': false},
                            {'type': 'C', 'selected': false},
                            {'type': 'CE', 'selected': false},
                            {'type': 'D1', 'selected': false},
                            {'type': 'D1E', 'selected': false},
                            {'type': 'D', 'selected': false},
                            {'type': 'DE', 'selected': false},
                            {'type': 'TAXI', 'selected': false}
                        ]})
                    },
                    diabetes: {
                        diabetestyp : undefined,
                        observationsperiod: undefined,
                        endastKost: undefined,
                        tabletter: undefined,
                        insulin: undefined,
                        insulinBehandlingsperiod: undefined,
                        annanBehandlingBeskrivning: undefined
                    },
                    hypoglykemier: {
                        kunskapOmAtgarder: undefined,
                        teckenNedsattHjarnfunktion: undefined,
                        saknarFormagaKannaVarningstecken: undefined,
                        allvarligForekomst: undefined,
                        allvarligForekomstBeskrivning: undefined,
                        allvarligForekomstTrafiken: undefined,
                        allvarligForekomstTrafikBeskrivning: undefined,
                        allvarligForekomstVakenTid: undefined,
                        allvarligForekomstVakenTidObservationstid: undefined,
                        egenkontrollBlodsocker: undefined
                    },
                    syn: {
                        separatOgonlakarintyg: undefined,
                        synfaltsprovningUtanAnmarkning: undefined,
                        hoger : {
                            utanKorrektion : undefined,
                            medKorrektion : undefined
                        },
                        vanster : {
                            utanKorrektion : undefined,
                            medKorrektion : undefined
                        },
                        binokulart : {
                            utanKorrektion : undefined,
                            medKorrektion : undefined
                        },
                        diplopi: undefined,
                        synfaltsprovning: undefined,
                        provningOgatsRorlighet: undefined},
                    bedomning: {
                        korkortstyp: new ModelAttr('korkortstyp',
                            {defaultValue:[
                                { type: 'AM', selected: false },
                                { type: 'A1', selected: false },
                                { type: 'A2', selected: false },
                                { type: 'A', selected: false },
                                { type: 'B', selected: false },
                                { type: 'BE', selected: false },
                                { type: 'TRAKTOR', selected: false },
                                { type: 'C1', selected: false },
                                { type: 'C1E', selected: false },
                                { type: 'C', selected: false },
                                { type: 'CE', selected: false },
                                { type: 'D1', selected: false },
                                { type: 'D1E', selected: false },
                                { type: 'D', selected: false },
                                { type: 'DE', selected: false },
                                { type: 'TAXI', selected: false }
                            ]}),
                        lakareSpecialKompetens: undefined,
                        lamplighetInnehaBehorighet: undefined
                    }
                });
                this.draftModel = new DraftModel(this);
            },

            update: function update(content, parent) {
                if (parent) {
                    parent.content = this;
                }
                update._super.call(this, content);
            }
        }, {
            build : function(){
                return new DraftModel(new TsDiabetesModel());
            }
        });

        /**
         * Return the constructor function IntygModel
         */
        return TsDiabetesModel;
    }]);