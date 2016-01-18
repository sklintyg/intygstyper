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

describe('ts-diabetes.Utkast.IntentionController', function() {
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

    var $scope, ctrl, form2, form4;

    beforeEach(angular.mock.inject(['$controller',
        '$rootScope',
        'ts-diabetes.UtkastController.ViewStateService',
        function($controller, $rootScope, _viewState_) {
        viewState = _viewState_;
        $scope = $rootScope.$new();
        ctrl = $controller('ts-diabetes.Utkast.IntentionController', { $scope: $scope });
        form2 = $controller('ts-diabetes.Utkast.Form2Controller', { $scope: $scope });
        form4 = $controller('ts-diabetes.Utkast.Form4Controller', { $scope: $scope });
    }]));

    // intention
    it('should show extra fields when some "korkortstyp"-options are selected', function() {
        getCheckboxForKorkortstyp('C1').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('C1').selected = false;

        getCheckboxForKorkortstyp('C1E').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('C1E').selected = false;

        getCheckboxForKorkortstyp('C').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('C').selected = false;

        getCheckboxForKorkortstyp('CE').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('CE').selected = false;

        getCheckboxForKorkortstyp('D1').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('D1').selected = false;

        getCheckboxForKorkortstyp('D1E').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('D1E').selected = false;

        getCheckboxForKorkortstyp('D').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('D').selected = false;

        getCheckboxForKorkortstyp('DE').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('DE').selected = false;

        getCheckboxForKorkortstyp('TAXI').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('TAXI').selected = false;

        getCheckboxForKorkortstyp('B').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeFalsy();
        getCheckboxForKorkortstyp('B').selected = false;
    });

    // intention
    it('should reset hidden fields when some "korkortstyp"-options are deselected', function() {

        // first check the korkort - restore attic
        getCheckboxForKorkortstyp('C1').selected = true;
        $scope.$digest();

        // set some values
        $scope.viewState.intygModel.hypoglykemier.egenkontrollBlodsocker = true;
        // this is watched so we should call digest
        $scope.viewState.intygModel.hypoglykemier.allvarligForekomstVakenTid = true;
        $scope.$digest();

        $scope.viewState.intygModel.hypoglykemier.allvarligForekomstVakenTidObservationstid = '2014-10-10';
        $scope.viewState.intygModel.bedomning.lamplighetInnehaBehorighet = true;
        $scope.$digest();

        // set korkort to false, - update attic
        getCheckboxForKorkortstyp('C1').selected = false;
        $scope.$digest();

        expect($scope.viewState.intygModel.hypoglykemier.egenkontrollBlodsocker).toBeUndefined();
        expect($scope.viewState.intygModel.hypoglykemier.allvarligForekomstVakenTid).toBeUndefined();
        expect($scope.viewState.intygModel.hypoglykemier.allvarligForekomstVakenTidObservationstid).toBe(undefined);
        expect($scope.viewState.intygModel.bedomning.lamplighetInnehaBehorighet).toBeUndefined();

        // re-enable korkot - restore attic, previous values should be visible
        getCheckboxForKorkortstyp('C1').selected = true;
        $scope.$digest();

        // this one works in the live but not here.. look on monday.
        expect($scope.viewState.intygModel.hypoglykemier.egenkontrollBlodsocker).toBe(true);
        expect($scope.viewState.intygModel.hypoglykemier.allvarligForekomstVakenTid).toBe(true);
        expect($scope.viewState.intygModel.hypoglykemier.allvarligForekomstVakenTidObservationstid).toBe('2014-10-10');
        expect($scope.viewState.intygModel.bedomning.lamplighetInnehaBehorighet).toBe(true);
    });

    // Helper methods
    function getCheckboxForKorkortstyp(typ) {
        for (var i = 0; i < $scope.viewState.intygModel.intygAvser.korkortstyp.length; i++) {
            if ($scope.viewState.intygModel.intygAvser.korkortstyp[i].type === typ) {
                return $scope.viewState.intygModel.intygAvser.korkortstyp[i];
            }
        }
        return null;
    }
});
