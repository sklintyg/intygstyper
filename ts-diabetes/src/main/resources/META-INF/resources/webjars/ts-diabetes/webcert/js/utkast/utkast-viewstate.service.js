angular.module('ts-diabetes').service('ts-diabetes.UtkastController.ViewStateService',
    ['$log', 'common.IntygEditViewStateService',
        function($log, CommonViewState) {
            'use strict';

            this.common = CommonViewState;
        }]);