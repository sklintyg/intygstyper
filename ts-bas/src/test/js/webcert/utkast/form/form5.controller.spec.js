describe('ts-bas.Utkast.Form5Controller', function() {
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

        ctrl = $controller('ts-bas.Utkast.Form5Controller', { $scope: $scope });

            var cert = testData.cert;

        spyOn(viewState, 'setDraftModel');
        spyOn(viewState, 'intygModel').and.returnValue(cert);

        $scope.$digest();
    }]));

    // --- form5
    it('should reset hidden fields when "harDiabetes" is set to false', function() {
        $scope.cert.diabetes.harDiabetes = true;
        $scope.$digest();

        $scope.cert.diabetes.diabetesTyp = 'DIABETES_TYP_1';
        $scope.cert.diabetes.harDiabetes = false;
        $scope.$digest();

        expect($scope.cert.diabetes.diabetesTyp).toBeUndefined();

        // Attic
        $scope.cert.diabetes.harDiabetes = true;
        $scope.$digest();

        expect($scope.cert.diabetes.diabetesTyp).toBe('DIABETES_TYP_1');
    });

    it('should reset hidden fields when "diabetesTyp" is not "DIABETES_TYP_2"', function() {
        $scope.cert.diabetes.diabetesTyp = 'DIABETES_TYP_2';
        $scope.$digest();

        $scope.cert.diabetes.kost = true;
        $scope.cert.diabetes.tabletter = true;
        $scope.cert.diabetes.insulin = true;
        $scope.cert.diabetes.diabetesTyp = 'DIABETES_TYP_1';
        $scope.$digest();

        expect($scope.cert.diabetes.kost).toBeUndefined();
        expect($scope.cert.diabetes.tabletter).toBeUndefined();
        expect($scope.cert.diabetes.insulin).toBeUndefined();

        // Attic
        $scope.cert.diabetes.diabetesTyp = 'DIABETES_TYP_2';
        $scope.$digest();

        expect($scope.cert.diabetes.kost).toBeTruthy();
        expect($scope.cert.diabetes.tabletter).toBeTruthy();
        expect($scope.cert.diabetes.insulin).toBeTruthy();
    });
    // --- form5
});
