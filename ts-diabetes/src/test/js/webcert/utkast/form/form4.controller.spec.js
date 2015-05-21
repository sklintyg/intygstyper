describe('ts-diabetes.Utkast.Form4Controller', function() {
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
        ctrl = $controller('ts-diabetes.Utkast.Form4Controller', { $scope: $scope });
        var cert = testData.cert;

        var draftModel = IntygModel._members.build();
            draftModel.update(cert);
        spyOn(viewState, 'setDraftModel');
        spyOn(viewState, 'intygModel').and.returnValue(draftModel.content);
        $scope.cert = draftModel.content;
        $scope.$digest();
    }]));

    // form 4
    it('should reset hidden fields when "form.behorighet" is set to false', function() {
        //console.log('*** 1 behoriget false');
        // because the default value is true we need to set it to false to start
        // the test process ..
        //$scope.form.behorighet = false;
        //$scope.$digest();

        //console.log('*** 2 behoriget true');
        $scope.cert.bedomning.kanInteTaStallning = false;
        $scope.$digest();
        expect($scope.cert.bedomning.kanInteTaStallning).toBeFalsy();

        angular.forEach($scope.cert.bedomning.korkortstyp, function(korkortstyp) {
            korkortstyp.selected = true;
        });

        //console.log('*** 3 behoriget false');
        $scope.cert.bedomning.kanInteTaStallning = true;
        $scope.$digest();

        expect($scope.cert.bedomning.kanInteTaStallning).toBeTruthy();
        angular.forEach($scope.cert.bedomning.korkortstyp, function(korkortstyp) {
            expect(korkortstyp.selected).toBeFalsy();
        });

        //// When reenabled the previously selected values should be remembered
        //console.log('*** 4. behoriget true');
        $scope.cert.bedomning.kanInteTaStallning = false;
        $scope.$digest();

        expect($scope.cert.bedomning.kanInteTaStallning).toBeFalsy();
        angular.forEach($scope.cert.bedomning.korkortstyp, function(korkortstyp) {
            expect(korkortstyp.selected).toBeTruthy();
        });
    });

});
