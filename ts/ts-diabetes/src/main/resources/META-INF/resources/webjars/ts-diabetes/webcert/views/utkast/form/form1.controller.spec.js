/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

describe('ts-diabetes.Utkast.Form1Controller', function() {
    'use strict';

    var ManageCertView;
    var UserModel;
    var wcFocus;
    var utkastNotifyService;
    var ModelAttr;
    var IntygModel;
    var viewState;
    var anchorScrollService;

    beforeEach(angular.mock.module('common', 'ts-diabetes', function($provide) {
        ManageCertView = jasmine.createSpyObj('common.ManageCertView', [ 'load' ]);
        UserModel = {};
        wcFocus = {};
        $provide.value('common.ManageCertView', ManageCertView);
        $provide.value('common.UserModel', UserModel);
        $provide.value('common.wcFocus', wcFocus);
        $provide.value('common.utkastNotifyService', utkastNotifyService);
        $provide.value('common.UtkastViewStateService',{intyg:{}, reset: function() {}});
        $provide.value('common.DateUtilsService', {});
        $provide.value('common.anchorScrollService', anchorScrollService);
    }));

    beforeEach(angular.mock.inject([
        'common.domain.ModelAttr',
        'ts-diabetes.Domain.IntygModel',
        function( _modelAttr_, _IntygModel_) {
            ModelAttr = _modelAttr_;
            IntygModel = _IntygModel_;
        }]));

    var $scope, ctrl;

    beforeEach(angular.mock.inject(['$controller',
        '$rootScope',
        'ts-diabetes.UtkastController.ViewStateService',
        function($controller, $rootScope, _viewState_) {
        viewState = _viewState_;
        $scope = $rootScope.$new();
        ctrl = $controller('ts-diabetes.Utkast.Form1Controller', { $scope: $scope });
    }]));
    
    it('should reset hidden fields when "diabetes.insulin" is set to false', function() {
        $scope.viewState.intygModel.diabetes.insulin = true;
        $scope.$digest();

        $scope.viewState.intygModel.diabetes.insulinBehandlingsperiod = '2014-10-10';
        $scope.viewState.intygModel.diabetes.insulin = false;
        $scope.$digest();

        expect($scope.viewState.intygModel.diabetes.insulinBehandlingsperiod).toBeNull();

        // When reenabled the previously selected values should be remembered
        $scope.viewState.intygModel.diabetes.insulin = true;
        $scope.$digest();
        expect($scope.viewState.intygModel.diabetes.insulinBehandlingsperiod).toBe('2014-10-10');
    });

    it('should reset hidden fields when "diabetes.insulin" is set to false', function() {
        $scope.viewState.intygModel.diabetes.insulin = true;
        $scope.viewState.intygModel.diabetes.insulinBehandlingsperiod = '2014-10-10';
        $scope.$digest();

        $scope.viewState.intygModel.diabetes.insulin = false;
        $scope.$digest();

        expect($scope.viewState.intygModel.diabetes.insulinBehandlingsperiod).toBeNull();
    });

});
