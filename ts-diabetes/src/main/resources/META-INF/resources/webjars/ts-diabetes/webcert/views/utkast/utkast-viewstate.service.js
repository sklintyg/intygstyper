angular.module('ts-diabetes').service('ts-diabetes.UtkastController.ViewStateService',
    ['$log', 'ts-diabetes.Domain.IntygModel', 'common.UtkastViewStateService',
        function($log, IntygModel, CommonViewState) {
            'use strict';

            this.common = CommonViewState;

            this.intygModel = undefined;
            this.draftModel = undefined;

            this.clearModel = function(){
                CommonViewState.intygModel = undefined;
                CommonViewState.draftModel = undefined;
                this.intygModel = undefined;
                this.draftModel = undefined;
            };

            this.setDraftModel = function(draftModel){
                this.draftModel = draftModel;
                this.intygModel = draftModel.content;
            };

            this.reset = function() {
                this.focusFirstInput = true;
                this.korkortd = false;
                this.tomorrowDate = moment().format('YYYY-MM-DD');

                this.identitet = [
                    {label: 'ID-kort *', id: 'ID_KORT'},
                    {label: 'Företagskort eller tjänstekort **', id: 'FORETAG_ELLER_TJANSTEKORT'},
                    {label: 'Svenskt körkort', id: 'KORKORT'},
                    {label: 'Personlig kännedom', id: 'PERS_KANNEDOM'},
                    {label: 'Försäkran enligt 18 kap. 4§ ***', id: 'FORSAKRAN_KAP18'},
                    {label: 'Pass ****', id: 'PASS'}
                ];
                this.behorighet = null;

                CommonViewState.reset();
                CommonViewState.intyg.type = 'ts-diabetes';
                this.setDraftModel(IntygModel._members.build());
                return this;
            };

            this.reset();

        }]);