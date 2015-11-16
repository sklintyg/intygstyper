describe('ts-bas.Utkast.Form5Controller', function() {
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

        ctrl = $controller('ts-bas.Utkast.Form5Controller', { $scope: $scope });
    }]));

    // --- form5
    it('should reset hidden fields when "harDiabetes" is set to false', function() {
        $scope.viewState.intygModel.diabetes.harDiabetes = true;
        $scope.$digest();

        $scope.viewState.intygModel.diabetes.diabetesTyp = 'DIABETES_TYP_1';
        $scope.viewState.intygModel.diabetes.harDiabetes = false;
        $scope.$digest();

        expect($scope.viewState.intygModel.diabetes.diabetesTyp).toBeUndefined();

        // Attic
        $scope.viewState.intygModel.diabetes.harDiabetes = true;
        $scope.$digest();

        expect($scope.viewState.intygModel.diabetes.diabetesTyp).toBe('DIABETES_TYP_1');
    });

    it('should reset hidden fields when "diabetesTyp" is not "DIABETES_TYP_2"', function() {
        $scope.viewState.intygModel.diabetes.diabetesTyp = 'DIABETES_TYP_2';
        $scope.$digest();

        $scope.viewState.intygModel.diabetes.kost = true;
        $scope.viewState.intygModel.diabetes.tabletter = true;
        $scope.viewState.intygModel.diabetes.insulin = true;
        $scope.viewState.intygModel.diabetes.diabetesTyp = 'DIABETES_TYP_1';
        $scope.$digest();

        expect($scope.viewState.intygModel.diabetes.kost).toBeUndefined();
        expect($scope.viewState.intygModel.diabetes.tabletter).toBeUndefined();
        expect($scope.viewState.intygModel.diabetes.insulin).toBeUndefined();

        // Attic
        $scope.viewState.intygModel.diabetes.diabetesTyp = 'DIABETES_TYP_2';
        $scope.$digest();

        expect($scope.viewState.intygModel.diabetes.kost).toBeTruthy();
        expect($scope.viewState.intygModel.diabetes.tabletter).toBeTruthy();
        expect($scope.viewState.intygModel.diabetes.insulin).toBeTruthy();
    });
    // --- form5
});
