describe('ts-diabetes.Utkast.Form1Controller', function() {
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
        ctrl = $controller('ts-diabetes.Utkast.Form1Controller', { $scope: $scope });
        var cert = testData.cert;

        var draftModel = IntygModel._members.build();
            draftModel.update(cert);
        spyOn(viewState, 'setDraftModel');
        spyOn(viewState, 'intygModel').and.returnValue(draftModel.content);
        $scope.cert = draftModel.content;
        $scope.$digest();
    }]));
    
    it('should reset hidden fields when "diabetes.insulin" is set to false', function() {
        $scope.cert.diabetes.insulin = true;
        $scope.$digest();

        $scope.cert.diabetes.insulinBehandlingsperiod = '2014-10-10';
        $scope.cert.diabetes.insulin = false;
        $scope.$digest();

        expect($scope.cert.diabetes.insulinBehandlingsperiod).toBeNull();

        // When reenabled the previously selected values should be remembered
        $scope.cert.diabetes.insulin = true;
        $scope.$digest();
        expect($scope.cert.diabetes.insulinBehandlingsperiod).toBe('2014-10-10');
    });

    it('should reset hidden fields when "diabetes.insulin" is set to false', function() {
        $scope.cert.diabetes.insulin = true;
        $scope.cert.diabetes.insulinBehandlingsperiod = '2014-10-10';
        $scope.$digest();

        $scope.cert.diabetes.insulin = false;
        $scope.$digest();

        expect($scope.cert.diabetes.insulinBehandlingsperiod).toBeNull();
    });

});
