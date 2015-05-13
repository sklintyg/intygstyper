angular.module('ts-diabetes').service('ts-diabetes.IntygController.ViewStateService',
    ['$log', 'common.IntygViewStateService',
        function($log, CommonViewState) {
            'use strict';

            this.common = CommonViewState;

            this.reset = function() {
                this.common.reset();
                this.common.defaultRecipient = 'TS';
                this.common.intyg.type = 'ts-diabetes';

                this.intygAvser = ''; // holds built list of selected körkortstyper for intyg avser
                this.bedomning = ''; // holds built list of selected körkortstyper for bedomning
            };

            this.reset();
        }]);