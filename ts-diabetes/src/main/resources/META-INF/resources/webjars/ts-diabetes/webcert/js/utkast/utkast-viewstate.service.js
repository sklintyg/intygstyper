angular.module('ts-diabetes').service('ts-diabetes.UtkastController.ViewStateService',
    ['$log', 'common.UtkastViewStateService',
        function($log, CommonViewState) {
            'use strict';

            this.common = CommonViewState;

            this.intygModel = undefined;
            this.draftModel = undefined;

            this.clearModel = function(){
                this.intygModel = undefined;
                this.draftModel = undefined;
            }

            this.setDraftModel = function(draftModel){
                this.draftModel = draftModel;
                this.intygModel = draftModel.content;
            };

        }]);