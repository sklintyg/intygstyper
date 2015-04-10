angular.module('ts-diabetes').factory('ts-diabetes.Domain.IntygModel',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', function(grundData, draftModel) {
        'use strict';

        // private
        var _intygModel;


        /**
         * Constructor, with class name
         */
        function IntygModel() {

            this.id = undefined;
            this.typ = undefined;

            this.grundData = grundData;

            this.vardkontakt = undefined;
            this.intygAvser = undefined;
            this.diabetes = undefined;
            this.hypoglykemier = undefined;
            this.syn = undefined;
            this.bedomning = undefined;
            this.kommentar = undefined;
        }

        // override the original update method
        IntygModel.prototype.update = function(content) {
            // refresh the model data

            if (content !== undefined) {
                this.id = content.id;
                this.typ = content.typ;

                this.grundData.update(content.grundData);

                if (content.vardkontakt) {
                    this.vardkontakt = {
                        typ: content.vardkontakt.typ,
                        idkontroll: content.vardkontakt.idkontroll
                    };
                }
                else {
                    this.vardkontakt = undefined;
                }

                this.intygAvser = {
                    korkortstyp: this.getKorkortstyp(content.intygAvser.korkortstyp)
                };

                if (content.diabetes) {
                    this.diabetes = {
                        diabetestyp: content.diabetes.diabetestyp,
                        observationsperiod: content.diabetes.observationsperiod,
                        endastKost: content.diabetes.endastKost,
                        tabletter: content.diabetes.tabletter,
                        insulin: content.diabetes.insulin,
                        insulinBehandlingsperiod: content.diabetes.insulinBehandlingsperiod,
                        annanBehandlingBeskrivning: content.diabetes.annanBehandlingBeskrivning
                    };
                }
                else {
                    this.diabetes = null;
                }

                if (content.hypoglykemier) {
                    this.hypoglykemier = {
                        kunskapOmAtgarder: content.hypoglykemier.kunskapOmAtgarder,
                        teckenNedsattHjarnfunktion: content.hypoglykemier.teckenNedsattHjarnfunktion,
                        saknarFormagaKannaVarningstecken: content.hypoglykemier.saknarFormagaKannaVarningstecken,
                        allvarligForekomst: content.hypoglykemier.allvarligForekomst,
                        allvarligForekomstBeskrivning: content.hypoglykemier.allvarligForekomstBeskrivning,
                        allvarligForekomstTrafiken: content.hypoglykemier.allvarligForekomstTrafiken,
                        allvarligForekomstTrafikBeskrivning: content.hypoglykemier.allvarligForekomstTrafikBeskrivning,
                        allvarligForekomstVakenTid: content.hypoglykemier.allvarligForekomstVakenTid,
                        allvarligForekomstVakenTidObservationstid: content.hypoglykemier.allvarligForekomstVakenTidObservationstid,
                        egenkontrollBlodsocker: content.hypoglykemier.egenkontrollBlodsocker
                    };
                }
                else {
                    this.hypoglykemier = undefined;
                }

                if (content.syn) {
                    this.syn = {
                        separatOgonlakarintyg: content.syn.separatOgonlakarintyg,
                        synfaltsprovningUtanAnmarkning: content.syn.synfaltsprovningUtanAnmarkning,
                        hoger: this.getSyn(content.syn.hoger),
                        vanster: this.getSyn(content.syn.vanster),
                        binokulart: this.getSyn(content.syn.binokulart),
                        diplopi: content.syn.diplopi,
                        synfaltsprovning: content.syn.synfaltsprovning,
                        provningOgatsRorlighet: content.syn.provningOgatsRorlighet
                    };
                }
                else {
                    this.syn = undefined;
                }

                this.bedomning = {
                    korkortstyp                    : this.getKorkortstyp(content.bedomning.korkortstyp),
                    lakareSpecialKompetens         : content.bedomning.lakareSpecialKompetens,
                    lamplighetInnehaBehorighet     : content.bedomning.lamplighetInnehaBehorighet
                };
                this.kommentar = content.kommentar;
            }
        };

        IntygModel.prototype.prepare = function() {
            if(!this.hypoglykemier.teckenNedsattHjarnfunktion) {
                this.hypoglykemier.saknarFormagaKannaVarningstecken = undefined;
                this.hypoglykemier.allvarligForekomst = undefined;
                this.hypoglykemier.allvarligForekomstTrafiken = undefined;
            }
        };

        IntygModel.prototype.getSyn = function(syn) {
            if (!syn) {
                return undefined;
            } else {
                return syn;
            }
        }

        IntygModel.prototype.getKorkortstyp = function(korkortstyp) {
            if (!korkortstyp) {
                return undefined;
            } else {
                return korkortstyp;
            }
/*            return [
                {'type': 'AM', 'selected': true},
                {'type': 'A1', 'selected': true},
                {'type': 'A2', 'selected': true},
                {'type': 'A', 'selected': true},
                {'type': 'B', 'selected': true},
                {'type': 'BE', 'selected': true},
                {'type': 'TRAKTOR', 'selected': true},
                {'type': 'C1', 'selected': true},
                {'type': 'C1E', 'selected': true},
                {'type': 'C', 'selected': true},
                {'type': 'CE', 'selected': true},
                {'type': 'D1', 'selected': true},
                {'type': 'D1E', 'selected': true},
                {'type': 'D', 'selected': true},
                {'type': 'DE', 'selected': true},
                {'type': 'TAXI', 'selected': true}
            ];*/
        };

        _intygModel = new IntygModel();

        draftModel.content = _intygModel;

        /**
         * Return the constructor function IntygModel
         */
        return _intygModel;

    }]);