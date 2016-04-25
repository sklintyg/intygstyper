angular.module('luae_fs').service('luae_fs.IntygController.ViewStateService',
    ['$log', 'common.IntygViewStateService',
        function($log, CommonViewState) {
            'use strict';

            this.common = CommonViewState;

            this.reset = function() {
                this.common.reset();
                this.common.defaultRecipient = 'FK';
                this.common.intygProperties.type = 'luae_fs';
            };

            this.reset();
        }]);