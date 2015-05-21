describe('ts-bas.Utkast.Form15Controller', function() {
    'use strict';

    var ManageCertView;
    var User;
    var wcFocus;
    var utkastNotifyService;
    var viewState;
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
        function($controller, $rootScope, _viewState_) {
        $scope = $rootScope.$new();
        viewState = _viewState_;

        ctrl = $controller('ts-bas.Utkast.Form15Controller', { $scope: $scope });

            var cert = testData.cert;

        spyOn(viewState, 'setDraftModel');
        spyOn(viewState, 'intygModel').and.returnValue(cert);

        $scope.$digest();
    }]));

    // --- form15
    it('should reset hidden fields when "stadigvarandeMedicinering" is set to false', function() {
        $scope.cert.medicinering.stadigvarandeMedicinering = true;
        $scope.$digest();

        $scope.cert.medicinering.beskrivning = 'Hello';
        $scope.cert.medicinering.stadigvarandeMedicinering = false;
        $scope.$digest();

        expect($scope.cert.medicinering.beskrivning).toBe('');

        // Attic
        $scope.cert.medicinering.stadigvarandeMedicinering = true;
        $scope.$digest();

        expect($scope.cert.medicinering.beskrivning).toBe('Hello');
    });
    // --- form15
});
