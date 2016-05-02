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

describe('ts-bas.Utkast.Form17Controller', function() {
    'use strict';

    var ManageCertView;
    var User;
    var wcFocus;
    var utkastNotifyService;
    var anchorScrollService;


    beforeEach(angular.mock.module('common','ts-bas', function($provide) {
        ManageCertView = jasmine.createSpyObj('common.ManageCertView', [ 'load' ]);
        User = {};
        wcFocus = {};
        $provide.value('common.ManageCertView', ManageCertView);
        $provide.value('common.UserModel', User);
        $provide.value('common.wcFocus', wcFocus);
        $provide.value('common.utkastNotifyService', utkastNotifyService);
        $provide.value('common.anchorScrollService', anchorScrollService);
    }));

    var $scope, ctrl;

    beforeEach(angular.mock.inject([
        '$controller',
        '$rootScope',
        function($controller, $rootScope) {
        $scope = $rootScope.$new();

        ctrl = $controller('ts-bas.Utkast.IntentionController', { $scope: $scope });
    }]));

    // --- intention
    it('should show extra fields when some "korkortstyp"-options are selected', function() {
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

        getCheckboxForKorkortstyp('ANNAT').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeFalsy();
        getCheckboxForKorkortstyp('ANNAT').selected = false;

        getCheckboxForKorkortstyp('C1').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeFalsy();
        getCheckboxForKorkortstyp('C1').selected = false;
    });

    it('should reset hidden fields when some "korkortstyp"-options are deselected', function() {

        getCheckboxForKorkortstyp('D1').selected = true;
        $scope.$digest();

        $scope.viewState.intygModel.horselBalans.svartUppfattaSamtal4Meter = true;
        $scope.viewState.intygModel.funktionsnedsattning.otillrackligRorelseformaga = true;
        getCheckboxForKorkortstyp('D1').selected = false;
        $scope.$digest();

        expect($scope.viewState.intygModel.horselBalans.svartUppfattaSamtal4Meter).toBeUndefined();
        expect($scope.viewState.intygModel.funktionsnedsattning.otillrackligRorelseformaga).toBeUndefined();

        // Attic
        getCheckboxForKorkortstyp('D1').selected = true;
        $scope.$digest();

        expect($scope.viewState.intygModel.horselBalans.svartUppfattaSamtal4Meter).toBeTruthy();
        expect($scope.viewState.intygModel.funktionsnedsattning.otillrackligRorelseformaga).toBeTruthy();

        // Check that 2b and 3b still are selected if you select another high körkortstyp after it already is set to high.
        $scope.viewState.intygModel.horselBalans.svartUppfattaSamtal4Meter = true;
        $scope.viewState.intygModel.funktionsnedsattning.otillrackligRorelseformaga = true;
        getCheckboxForKorkortstyp('D1E').selected = true;
        $scope.$digest();

        expect($scope.viewState.intygModel.horselBalans.svartUppfattaSamtal4Meter).toBeTruthy();
        expect($scope.viewState.intygModel.funktionsnedsattning.otillrackligRorelseformaga).toBeTruthy();
    });
    // --- intention


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
