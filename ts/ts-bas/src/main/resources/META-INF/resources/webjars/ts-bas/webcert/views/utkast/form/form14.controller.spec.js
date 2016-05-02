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

describe('ts-bas.Utkast.Form14Controller', function() {
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

        ctrl = $controller('ts-bas.Utkast.Form14Controller', { $scope: $scope });
    }]));

    // --- form14
    it('should reset hidden fields when "sjukhusEllerLakarkontakt" is set to false', function() {
        $scope.viewState.intygModel.sjukhusvard.sjukhusEllerLakarkontakt = true;
        $scope.$digest();

        $scope.viewState.intygModel.sjukhusvard.tidpunkt = 'Förra veckan';
        $scope.viewState.intygModel.sjukhusvard.vardinrattning = 'Sahlgrenska';
        $scope.viewState.intygModel.sjukhusvard.anledning = 'Allt';
        $scope.viewState.intygModel.sjukhusvard.sjukhusEllerLakarkontakt = false;
        $scope.$digest();

        expect($scope.viewState.intygModel.sjukhusvard.tidpunkt).toBe('');
        expect($scope.viewState.intygModel.sjukhusvard.vardinrattning).toBe('');
        expect($scope.viewState.intygModel.sjukhusvard.anledning).toBe('');

        // Attic
        $scope.viewState.intygModel.sjukhusvard.sjukhusEllerLakarkontakt = true;
        $scope.$digest();

        expect($scope.viewState.intygModel.sjukhusvard.tidpunkt).toBe('Förra veckan');
        expect($scope.viewState.intygModel.sjukhusvard.vardinrattning).toBe('Sahlgrenska');
        expect($scope.viewState.intygModel.sjukhusvard.anledning).toBe('Allt');
    });
    // --- form14
});
