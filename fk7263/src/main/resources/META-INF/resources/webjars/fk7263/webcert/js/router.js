/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('fk7263').config(function($stateProvider) {
    'use strict';
    $stateProvider.
        state('fk7263-edit', {
            url : '/fk7263/edit/:certificateId',
            templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/intyg-edit.html',
            controller: 'fk7263.EditCertCtrl'
        });
});