angular.module('ts-bas').service('ts-bas.IntygController.ViewStateService',
    ['$log', 'common.IntygViewStateService',
        function($log, CommonViewState) {
            'use strict';

            this.common = CommonViewState;

            this.reset = function() {
                this.common.reset();
                this.common.defaultRecipient = 'TS';
                this.common.intyg.type = 'ts-bas';
            };

            this.reset();
        }]);