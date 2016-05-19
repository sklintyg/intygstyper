/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

angular.module('ts-diabetes').factory('ts-diabetes.Domain.IntygModel',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
     'common.domain.BaseAtticModel',
    function(GrundData, DraftModel, ModelAttr, BaseAtticModel) {
        'use strict';

        var TsDiabetesModel = BaseAtticModel._extend({
            init: function init() {
                var grundData = GrundData.build();
                init._super.call(this, 'IntygModel', {
                    id: undefined,
                    typ: undefined,
                    kommentar:undefined,
                    textVersion: undefined,
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
                        endastKost: false,
                        tabletter: false,
                        insulin: false,
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
                        kanInteTaStallning:false,
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
