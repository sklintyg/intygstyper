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

describe('ts-bas.Utkast.Form15Controller', function() {
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

        ctrl = $controller('ts-bas.Utkast.Form15Controller', { $scope: $scope });
    }]));

    // --- form15
    it('should reset hidden fields when "stadigvarandeMedicinering" is set to false', function() {
        $scope.viewState.intygModel.medicinering.stadigvarandeMedicinering = true;
        $scope.$digest();

        $scope.viewState.intygModel.medicinering.beskrivning = 'Hello';
        $scope.viewState.intygModel.medicinering.stadigvarandeMedicinering = false;
        $scope.$digest();

        expect($scope.viewState.intygModel.medicinering.beskrivning).toBe('');

        // Attic
        $scope.viewState.intygModel.medicinering.stadigvarandeMedicinering = true;
        $scope.$digest();

        expect($scope.viewState.intygModel.medicinering.beskrivning).toBe('Hello');
    });
    // --- form15
});
