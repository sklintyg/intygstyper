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
        var cert = testData.cert;

        var draftModel = IntygModel._members.build();
            draftModel.update(cert);
        spyOn(viewState, 'setDraftModel');
        spyOn(viewState, 'intygModel').and.returnValue(draftModel.content);
        $scope.cert = draftModel.content;
        $scope.$digest();
    }]));

    it('should reset hidden fields when "teckenNedsattHjarnfunktion" is set to false', function() {
        $scope.cert.hypoglykemier.teckenNedsattHjarnfunktion = true;
        $scope.$digest();

        $scope.cert.hypoglykemier.saknarFormagaKannaVarningstecken = true;
        $scope.cert.hypoglykemier.allvarligForekomst = true;
        $scope.cert.hypoglykemier.allvarligForekomstTrafiken = true;

        $scope.cert.hypoglykemier.teckenNedsattHjarnfunktion = false;
        $scope.$digest();

        expect($scope.cert.hypoglykemier.saknarFormagaKannaVarningstecken).toBeUndefined();
        expect($scope.cert.hypoglykemier.allvarligForekomst).toBeUndefined();
        expect($scope.cert.hypoglykemier.allvarligForekomstTrafiken).toBeUndefined();

        // When reenabled the previously selected values should be remembered
        $scope.cert.hypoglykemier.teckenNedsattHjarnfunktion = true;
        $scope.$digest();
        expect($scope.cert.hypoglykemier.saknarFormagaKannaVarningstecken).toBe(true);
        expect($scope.cert.hypoglykemier.allvarligForekomst).toBe(true);
        expect($scope.cert.hypoglykemier.allvarligForekomstTrafiken).toBe(true);
    });

    it('should reset hidden fields when "allvarligForekomst" is set to false', function() {
        $scope.cert.hypoglykemier.allvarligForekomst = true;
        $scope.$digest();

        $scope.cert.hypoglykemier.allvarligForekomstBeskrivning = 'Hello';
        $scope.cert.hypoglykemier.allvarligForekomst = false;
        $scope.$digest();

        expect($scope.cert.hypoglykemier.allvarligForekomstBeskrivning).toBe(undefined);

        // When reenabled the previously selected values should be remembered
        $scope.cert.hypoglykemier.allvarligForekomst = true;
        $scope.$digest();
        expect($scope.cert.hypoglykemier.allvarligForekomstBeskrivning).toBe('Hello');
    });

    it('should reset hidden fields when "allvarligForekomstTrafiken" is set to false', function() {
        $scope.cert.hypoglykemier.allvarligForekomstTrafiken = true;
        $scope.$digest();

        $scope.cert.hypoglykemier.allvarligForekomstTrafikBeskrivning = 'Hello';
        $scope.cert.hypoglykemier.allvarligForekomstTrafiken = false;
        $scope.$digest();

        expect($scope.cert.hypoglykemier.allvarligForekomstTrafikBeskrivning).toBe(undefined);

        // When reenabled the previously selected values should be remembered
        $scope.cert.hypoglykemier.allvarligForekomstTrafiken = true;
        $scope.$digest();
        expect($scope.cert.hypoglykemier.allvarligForekomstTrafikBeskrivning).toBe('Hello');
    });

    it('should reset hidden fields when "allvarligForekomstVakenTid" is set to false', function() {
        $scope.cert.hypoglykemier.allvarligForekomstVakenTid = true;
        $scope.$digest();

        $scope.cert.hypoglykemier.allvarligForekomstVakenTidObservationstid = 'Hello';
        $scope.cert.hypoglykemier.allvarligForekomstVakenTid = false;
        $scope.$digest();

        expect($scope.cert.hypoglykemier.allvarligForekomstVakenTidObservationstid).toBe(undefined);

        // When reenabled the previously selected values should be remembered
        $scope.cert.hypoglykemier.allvarligForekomstVakenTid = true;
        $scope.$digest();
        expect($scope.cert.hypoglykemier.allvarligForekomstVakenTidObservationstid).toBe('Hello');
    });

});
