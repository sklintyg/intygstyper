describe('ts-bas.Utkast.Form3Controller', function() {
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
        'ts-bas.UtkastController.ViewStateService',
        function($controller, $rootScope) {
        $scope = $rootScope.$new();

        ctrl = $controller('ts-bas.Utkast.Form3Controller', { $scope: $scope });
    }]));

    // --- form3
    it('should reset hidden fields when "funktionsnedsattning" is set to false', function() {
        $scope.viewState.intygModel.funktionsnedsattning.funktionsnedsattning = true;
        $scope.$digest();

        $scope.viewState.intygModel.funktionsnedsattning.beskrivning = 'Hello';
        $scope.viewState.intygModel.funktionsnedsattning.funktionsnedsattning = false;
        $scope.$digest();

        expect($scope.viewState.intygModel.funktionsnedsattning.beskrivning).toBe('');

        // Attic
        $scope.viewState.intygModel.funktionsnedsattning.funktionsnedsattning = true;
        $scope.$digest();

        expect($scope.viewState.intygModel.funktionsnedsattning.beskrivning).toBe('Hello');
    });
    // --- form3
});
