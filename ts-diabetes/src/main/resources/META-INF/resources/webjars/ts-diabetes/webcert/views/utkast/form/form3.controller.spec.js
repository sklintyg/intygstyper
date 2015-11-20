describe('ts-diabetes.Utkast.Form3Controller', function() {
    'use strict';

    var ManageCertView;
    var UserModel;
    var wcFocus;
    var utkastNotifyService;
    var ModelAttr;
    var IntygModel;
    var viewState;
    var anchorScrollService;

    beforeEach(angular.mock.module('common', 'ts-diabetes', function($provide) {
        ManageCertView = jasmine.createSpyObj('common.ManageCertView', [ 'load' ]);
        UserModel = {};
        wcFocus = {};
        $provide.value('common.ManageCertView', ManageCertView);
        $provide.value('common.UserModel', UserModel);
        $provide.value('common.wcFocus', wcFocus);
        $provide.value('common.utkastNotifyService', utkastNotifyService);
        $provide.value('common.UtkastViewStateService',{intyg:{}, reset: function() {}});
        $provide.value('common.DateUtilsService', {});
        $provide.value('common.anchorScrollService', anchorScrollService);
    }));

    beforeEach(angular.mock.inject([
        'common.domain.ModelAttr',
        'ts-diabetes.Domain.IntygModel',
        function( _modelAttr_, _IntygModel_) {
            ModelAttr = _modelAttr_;
            IntygModel = _IntygModel_;
        }]));

    var $scope, ctrl;

    beforeEach(angular.mock.inject(['$controller',
        '$rootScope',
        'ts-diabetes.UtkastController.ViewStateService',
        function($controller, $rootScope, _viewState_) {
        viewState = _viewState_;
        $scope = $rootScope.$new();
        ctrl = $controller('ts-diabetes.Utkast.Form3Controller', { $scope: $scope });
    }]));

    // form 3
    it('should reset hidden fields when "separatOgonlakarintyg" is set to true', function() {

        $scope.viewState.intygModel.syn.separatOgonlakarintyg = true;
        $scope.$digest();

        $scope.viewState.intygModel.syn.separatOgonlakarintyg = false;
        $scope.$digest();

        $scope.viewState.intygModel.syn.hoger.utanKorrektion = '2.0';
        $scope.viewState.intygModel.syn.hoger.medKorrektion = '2.0';
        $scope.viewState.intygModel.syn.vanster.utanKorrektion = '2.0';
        $scope.viewState.intygModel.syn.vanster.medKorrektion = '2.0';
        $scope.viewState.intygModel.syn.binokulart.utanKorrektion = '2.0';
        $scope.viewState.intygModel.syn.binokulart.medKorrektion = '2.0';
        $scope.viewState.intygModel.syn.diplopi = false;

        $scope.viewState.intygModel.syn.separatOgonlakarintyg = true;
        $scope.$digest();

        expect($scope.viewState.intygModel.syn.hoger.utanKorrektion).toBeUndefined();
        expect($scope.viewState.intygModel.syn.hoger.medKorrektion).toBeUndefined();
        expect($scope.viewState.intygModel.syn.vanster.utanKorrektion).toBeUndefined();
        expect($scope.viewState.intygModel.syn.vanster.medKorrektion).toBeUndefined();
        expect($scope.viewState.intygModel.syn.binokulart.utanKorrektion).toBeUndefined();
        expect($scope.viewState.intygModel.syn.binokulart.medKorrektion).toBeUndefined();
        expect($scope.viewState.intygModel.syn.diplopi).toBeUndefined();

        // When reenabled the previously selected values should be remembered
        $scope.viewState.intygModel.syn.separatOgonlakarintyg = false;
        $scope.$digest();
        expect($scope.viewState.intygModel.syn.hoger.utanKorrektion).toBe('2.0');
        expect($scope.viewState.intygModel.syn.hoger.utanKorrektion).toBe('2.0');
        expect($scope.viewState.intygModel.syn.vanster.utanKorrektion).toBe('2.0');
        expect($scope.viewState.intygModel.syn.vanster.medKorrektion).toBe('2.0');
        expect($scope.viewState.intygModel.syn.binokulart.utanKorrektion).toBe('2.0');
        expect($scope.viewState.intygModel.syn.binokulart.medKorrektion).toBe('2.0');
        expect($scope.viewState.intygModel.syn.diplopi).toBe(false);
    });

});
