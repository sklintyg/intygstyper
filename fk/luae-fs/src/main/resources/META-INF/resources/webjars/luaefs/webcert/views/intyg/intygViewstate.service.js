angular.module('luaefs').service('luaefs.IntygController.ViewStateService',
    ['$log', 'common.IntygViewStateService',
        function($log, CommonViewState) {
            'use strict';

            this.common = CommonViewState;

            this.reset = function() {
                this.common.reset();
                this.common.defaultRecipient = 'FK';
                this.common.intyg.type = 'luaefs';
            };

            this.reset();
        }]);