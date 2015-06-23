describe('ts-diabetes.Utkast.Form2Controller', function() {
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
        ctrl = $controller('ts-diabetes.Utkast.Form2Controller', { $scope: $scope });
    }]));

    it('should reset hidden fields when "teckenNedsattHjarnfunktion" is set to false', function() {
        $scope.viewState.intygModel.hypoglykemier.teckenNedsattHjarnfunktion = true;
        $scope.$digest();

        $scope.viewState.intygModel.hypoglykemier.saknarFormagaKannaVarningstecken = true;
        $scope.viewState.intygModel.hypoglykemier.allvarligForekomst = true;
        $scope.viewState.intygModel.hypoglykemier.allvarligForekomstTrafiken = true;

        $scope.viewState.intygModel.hypoglykemier.teckenNedsattHjarnfunktion = false;
        $scope.$digest();

        expect($scope.viewState.intygModel.hypoglykemier.saknarFormagaKannaVarningstecken).toBeUndefined();
        expect($scope.viewState.intygModel.hypoglykemier.allvarligForekomst).toBeUndefined();
        expect($scope.viewState.intygModel.hypoglykemier.allvarligForekomstTrafiken).toBeUndefined();

        // When reenabled the previously selected values should be remembered
        $scope.viewState.intygModel.hypoglykemier.teckenNedsattHjarnfunktion = true;
        $scope.$digest();
        expect($scope.viewState.intygModel.hypoglykemier.saknarFormagaKannaVarningstecken).toBe(true);
        expect($scope.viewState.intygModel.hypoglykemier.allvarligForekomst).toBe(true);
        expect($scope.viewState.intygModel.hypoglykemier.allvarligForekomstTrafiken).toBe(true);
    });

    it('should reset hidden fields when "allvarligForekomst" is set to false', function() {
        $scope.viewState.intygModel.hypoglykemier.allvarligForekomst = true;
        $scope.$digest();

        $scope.viewState.intygModel.hypoglykemier.allvarligForekomstBeskrivning = 'Hello';
        $scope.viewState.intygModel.hypoglykemier.allvarligForekomst = false;
        $scope.$digest();

        expect($scope.viewState.intygModel.hypoglykemier.allvarligForekomstBeskrivning).toBe(undefined);

        // When reenabled the previously selected values should be remembered
        $scope.viewState.intygModel.hypoglykemier.allvarligForekomst = true;
        $scope.$digest();
        expect($scope.viewState.intygModel.hypoglykemier.allvarligForekomstBeskrivning).toBe('Hello');
    });

    it('should reset hidden fields when "allvarligForekomstTrafiken" is set to false', function() {
        $scope.viewState.intygModel.hypoglykemier.allvarligForekomstTrafiken = true;
        $scope.$digest();

        $scope.viewState.intygModel.hypoglykemier.allvarligForekomstTrafikBeskrivning = 'Hello';
        $scope.viewState.intygModel.hypoglykemier.allvarligForekomstTrafiken = false;
        $scope.$digest();

        expect($scope.viewState.intygModel.hypoglykemier.allvarligForekomstTrafikBeskrivning).toBe(undefined);

        // When reenabled the previously selected values should be remembered
        $scope.viewState.intygModel.hypoglykemier.allvarligForekomstTrafiken = true;
        $scope.$digest();
        expect($scope.viewState.intygModel.hypoglykemier.allvarligForekomstTrafikBeskrivning).toBe('Hello');
    });

    it('should reset hidden fields when "allvarligForekomstVakenTid" is set to false', function() {
        $scope.viewState.intygModel.hypoglykemier.allvarligForekomstVakenTid = true;
        $scope.$digest();

        $scope.viewState.intygModel.hypoglykemier.allvarligForekomstVakenTidObservationstid = 'Hello';
        $scope.viewState.intygModel.hypoglykemier.allvarligForekomstVakenTid = false;
        $scope.$digest();

        expect($scope.viewState.intygModel.hypoglykemier.allvarligForekomstVakenTidObservationstid).toBe(undefined);

        // When reenabled the previously selected values should be remembered
        $scope.viewState.intygModel.hypoglykemier.allvarligForekomstVakenTid = true;
        $scope.$digest();
        expect($scope.viewState.intygModel.hypoglykemier.allvarligForekomstVakenTidObservationstid).toBe('Hello');
    });

});
