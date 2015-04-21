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
                CommonViewState.reset();
                CommonViewState.intyg.type = 'ts-diabetes';
                this.setDraftModel(IntygModel._members.build());
            };

            this.reset();

        }]);