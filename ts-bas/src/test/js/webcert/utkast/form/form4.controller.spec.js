describe('ts-bas.Utkast.Form4Controller', function() {
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

        ctrl = $controller('ts-bas.Utkast.Form4Controller', { $scope: $scope });
    }]));

    // --- form4
    it('should reset hidden fields when "riskfaktorerStroke" is set to false', function() {
        $scope.viewState.intygModel.hjartKarl.riskfaktorerStroke = true;
        $scope.$digest();

        $scope.viewState.intygModel.hjartKarl.beskrivningRiskfaktorer = 'Hello';
        $scope.viewState.intygModel.hjartKarl.riskfaktorerStroke = false;
        $scope.$digest();

        expect($scope.viewState.intygModel.hjartKarl.beskrivningRiskfaktorer).toBe('');

        // Attic
        $scope.viewState.intygModel.hjartKarl.riskfaktorerStroke = true;
        $scope.$digest();

        expect($scope.viewState.intygModel.hjartKarl.beskrivningRiskfaktorer).toBe('Hello');
    });
    // --- form4
});
