describe('ts-bas.Utkast.Form11Controller', function() {
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

        ctrl = $controller('ts-bas.Utkast.Form11Controller', { $scope: $scope });

            var cert = testData.cert;

        spyOn(viewState, 'setDraftModel');
        spyOn(viewState, 'intygModel').and.returnValue(cert);

        $scope.$digest();
    }]));

    // --- form11
    it('should reset hidden fields when "teckenMissbruk" and "foremalForVardinsats" is set to false', function() {
        $scope.cert.narkotikaLakemedel.teckenMissbruk = true;
        $scope.cert.narkotikaLakemedel.foremalForVardinsats = true;
        $scope.$digest();

        // Set provtagning
        $scope.cert.narkotikaLakemedel.provtagningBehovs = true;

        // One true, nothing changes
        $scope.cert.narkotikaLakemedel.teckenMissbruk = false;
        $scope.$digest();

        expect($scope.cert.narkotikaLakemedel.provtagningBehovs).toBeTruthy();

        // Still one true, nothing changes
        $scope.cert.narkotikaLakemedel.teckenMissbruk = true;
        $scope.cert.narkotikaLakemedel.foremalForVardinsats = false;
        $scope.$digest();

        expect($scope.cert.narkotikaLakemedel.provtagningBehovs).toBeTruthy();

        // Both false, provtagning = true will be saved and cleared because field is invisible
        $scope.cert.narkotikaLakemedel.teckenMissbruk = false;
        $scope.$digest();

        expect($scope.cert.narkotikaLakemedel.provtagningBehovs).toBeUndefined();

        // Attic
        // One true again, provtagning = true should be restored from attic
        $scope.cert.narkotikaLakemedel.teckenMissbruk = true;
        $scope.$digest();

        expect($scope.cert.narkotikaLakemedel.provtagningBehovs).toBeTruthy();
    });

    it('should reset hidden fields when "lakarordineratLakemedelsbruk" is set to false', function() {
        $scope.cert.narkotikaLakemedel.lakarordineratLakemedelsbruk = true;
        $scope.$digest();

        $scope.cert.narkotikaLakemedel.lakemedelOchDos = 'Hello';
        $scope.cert.narkotikaLakemedel.lakarordineratLakemedelsbruk = false;
        $scope.$digest();

        expect($scope.cert.narkotikaLakemedel.lakemedelOchDos).toBe('');

        // Attic
        $scope.cert.narkotikaLakemedel.lakarordineratLakemedelsbruk = true;
        $scope.$digest();

        expect($scope.cert.narkotikaLakemedel.lakemedelOchDos).toBe('Hello');
    });
    // --- form11
});
