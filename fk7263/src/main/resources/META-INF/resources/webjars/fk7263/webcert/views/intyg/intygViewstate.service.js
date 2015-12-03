angular.module('fk7263').service('fk7263.IntygController.ViewStateService',
    ['$log', 'common.IntygViewStateService',
        function($log, CommonViewState) {
            'use strict';

            this.common = CommonViewState;
            this.intygModel = {};

            this.reset = function() {
                this.common.reset();
                this.common.defaultRecipient = 'FK';
                this.common.intyg.type = 'fk7263';
            };

            // Fix for Angular 1.4 / WEBCERT-2236
            this.has8a = function() {
                if(this.intygModel.nuvarandeArbetsuppgifter != null || this.intygModel.arbetsloshet != null || this.intygModel.foraldrarledighet != null) {
                    return 'true';
                } else {
                    return 'false';
                }
            };

            this.reset();
        }]);