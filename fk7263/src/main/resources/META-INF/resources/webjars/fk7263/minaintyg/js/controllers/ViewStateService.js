angular.module('fk7263').service('fk7263.ViewStateService',
    ['$log', 'common.ViewStateService',
        function($log, CommonViewState) {
            'use strict';

            this.common = CommonViewState;

            this.reset = function() {
                this.common.reset();
                this.common.intygProperties.defaultRecipient = 'FK';
                this.common.intygProperties.type = 'fk7263';
            };

            this.reset();
        }]);
