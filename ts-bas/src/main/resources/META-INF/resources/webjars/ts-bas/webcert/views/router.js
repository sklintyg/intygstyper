/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('ts-bas').config(function($stateProvider) {
    'use strict';
    $stateProvider.
        state('ts-bas-edit', {
            url : '/ts-bas/edit/:certificateId',
            views : {
                '': {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/utkast.html',
                    controller: 'ts-bas.UtkastController' },

                'header@ts-bas-edit': {
                    templateUrl: '/web/webjars/common/webcert/utkast/utkast-header/utkast-header.html',
                    controller: 'common.UtkastHeader'
                },
                'footer@ts-bas-edit' : {
                    templateUrl: '/web/webjars/common/webcert/utkast/utkast-footer/utkast-footer.html',
                    controller: 'common.UtkastFooter'
                }
            }
        });
});