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

describe('ts-bas.Utkast.Form11Controller', function() {
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
        'ts-bas.Domain.IntygModel',
        'ts-bas.UtkastController.ViewStateService',
        function($controller, $rootScope) {
        $scope = $rootScope.$new();

        ctrl = $controller('ts-bas.Utkast.Form11Controller', { $scope: $scope });
    }]));

    // --- form11
    it('should reset hidden fields when "teckenMissbruk" and "foremalForVardinsats" is set to false', function() {
        $scope.viewState.intygModel.narkotikaLakemedel.teckenMissbruk = true;
        $scope.viewState.intygModel.narkotikaLakemedel.foremalForVardinsats = true;
        $scope.$digest();

        // Set provtagning
        $scope.viewState.intygModel.narkotikaLakemedel.provtagningBehovs = true;

        // One true, nothing changes
        $scope.viewState.intygModel.narkotikaLakemedel.teckenMissbruk = false;
        $scope.$digest();

        expect($scope.viewState.intygModel.narkotikaLakemedel.provtagningBehovs).toBeTruthy();

        // Still one true, nothing changes
        $scope.viewState.intygModel.narkotikaLakemedel.teckenMissbruk = true;
        $scope.viewState.intygModel.narkotikaLakemedel.foremalForVardinsats = false;
        $scope.$digest();

        expect($scope.viewState.intygModel.narkotikaLakemedel.provtagningBehovs).toBeTruthy();

        // Both false, provtagning = true will be saved and cleared because field is invisible
        $scope.viewState.intygModel.narkotikaLakemedel.teckenMissbruk = false;
        $scope.$digest();

        expect($scope.viewState.intygModel.narkotikaLakemedel.provtagningBehovs).toBeUndefined();

        // Attic
        // One true again, provtagning = true should be restored from attic
        $scope.viewState.intygModel.narkotikaLakemedel.teckenMissbruk = true;
        $scope.$digest();

        expect($scope.viewState.intygModel.narkotikaLakemedel.provtagningBehovs).toBeTruthy();
    });

    it('should reset hidden fields when "lakarordineratLakemedelsbruk" is set to false', function() {
        $scope.viewState.intygModel.narkotikaLakemedel.lakarordineratLakemedelsbruk = true;
        $scope.$digest();

        $scope.viewState.intygModel.narkotikaLakemedel.lakemedelOchDos = 'Hello';
        $scope.viewState.intygModel.narkotikaLakemedel.lakarordineratLakemedelsbruk = false;
        $scope.$digest();

        expect($scope.viewState.intygModel.narkotikaLakemedel.lakemedelOchDos).toBe('');

        // Attic
        $scope.viewState.intygModel.narkotikaLakemedel.lakarordineratLakemedelsbruk = true;
        $scope.$digest();

        expect($scope.viewState.intygModel.narkotikaLakemedel.lakemedelOchDos).toBe('Hello');
    });
    // --- form11
});
