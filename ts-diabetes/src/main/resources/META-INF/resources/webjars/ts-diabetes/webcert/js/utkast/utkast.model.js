angular.module('ts-diabetes').factory('ts-diabetes.Domain.IntygModel',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
     'common.domain.BaseAtticModel',
    function(grundData, DraftModel, ModelAttr, BaseAtticModel) {
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

        var IntygModel = BaseAtticModel.extend({
            init: function() {
                this._super('IntygModel', {
                    vardkontakt: ['typ', 'idkontroll'],
                    intygAvser: [
                        new ModelAttr('korkortstyp', {fromTransform: korkortstypTransform})
                    ],
                    diabetes: [
                        'diabetestyp',
                        'observationsperiod',
                        'endastKost',
                        'tabletter',
                        'insulin',
                        'insulinBehandlingsperiod',
                        'annanBehandlingBeskrivning'],
                    hypoglykemier: [
                        'kunskapOmAtgarder',
                        'teckenNedsattHjarnfunktion',
                        'saknarFormagaKannaVarningstecken',
                        'allvarligForekomst',
                        'allvarligForekomstBeskrivning',
                        'allvarligForekomstTrafiken',
                        'allvarligForekomstTrafikBeskrivning',
                        'allvarligForekomstVakenTid',
                        'allvarligForekomstVakenTidObservationstid',
                        'egenkontrollBlodsocker'],
                    syn: [
                        'separatOgonlakarintyg',
                        'synfaltsprovningUtanAnmarkning',
                        new ModelAttr('hoger', {fromTransform: synTransform}),
                        new ModelAttr('vanster', {fromTransform: synTransform}),
                        new ModelAttr('binokulart', {fromTransform: synTransform}),
                        'diplopi',
                        'synfaltsprovning',
                        'provningOgatsRorlighet'],
                    bedomning: [
                        new ModelAttr('korkortstyp', {fromTransform: korkortstypTransform}),
                        'lakareSpecialKompetens',
                        'lamplighetInnehaBehorighet'
                    ],

                    misc: ['kommentar', 'id', new ModelAttr('grundData', {defaultValue: grundData})]
                });
                this.draftModel = new DraftModel(this);
            },

            update: function(content, parent) {
                if (parent) {
                    parent.content = this;
                }
                this._super(content);
            }
        });

        /**
         * Return the constructor function IntygModel
         */
        return IntygModel;
    }]);