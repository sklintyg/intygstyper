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
