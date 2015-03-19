angular.module('fk7263').service('fk7263.EditCertCtrl.ViewStateService',
    ['$log','fk7263.Domain.IntygModel','common.CertViewState', function( $log, intygModel, CertViewState ) {
        'use strict';

        this.common = CertViewState;

        this.avstangningSmittskyddVal = intygModel.avstangningSmittskydd;

        this.avstangningSmittskydd = function(){
            return intygModel.avstangningSmittskydd;
        }

    }]);