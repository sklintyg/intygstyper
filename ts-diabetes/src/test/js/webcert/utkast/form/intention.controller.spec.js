describe('ts-diabetes.Utkast.IntentionController', function() {
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

    var $scope, ctrl, form2, form4;

    beforeEach(angular.mock.inject(['$controller',
        '$rootScope',
        'ts-diabetes.UtkastController.ViewStateService',
        function($controller, $rootScope, _viewState_) {
        viewState = _viewState_;
        $scope = $rootScope.$new();
        ctrl = $controller('ts-diabetes.Utkast.IntentionController', { $scope: $scope });
            form2 = $controller('ts-diabetes.Utkast.Form2Controller', { $scope: $scope });
            form4 = $controller('ts-diabetes.Utkast.Form4Controller', { $scope: $scope });
        var cert = testData.cert;

        var draftModel = IntygModel._members.build();
            draftModel.update(cert);
        spyOn(viewState, 'setDraftModel');
        spyOn(viewState, 'intygModel').and.returnValue(draftModel.content);
        $scope.cert = draftModel.content;
        $scope.$digest();
    }]));

    // intention
    it('should show extra fields when some "korkortstyp"-options are selected', function() {
        getCheckboxForKorkortstyp('C1').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('C1').selected = false;

        getCheckboxForKorkortstyp('C1E').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('C1E').selected = false;

        getCheckboxForKorkortstyp('C').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('C').selected = false;

        getCheckboxForKorkortstyp('CE').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('CE').selected = false;

        getCheckboxForKorkortstyp('D1').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('D1').selected = false;

        getCheckboxForKorkortstyp('D1E').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('D1E').selected = false;

        getCheckboxForKorkortstyp('D').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('D').selected = false;

        getCheckboxForKorkortstyp('DE').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('DE').selected = false;

        getCheckboxForKorkortstyp('TAXI').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('TAXI').selected = false;

        getCheckboxForKorkortstyp('B').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeFalsy();
        getCheckboxForKorkortstyp('B').selected = false;
    });

    // intention
    it('should reset hidden fields when some "korkortstyp"-options are deselected', function() {

        // first check the korkort - restore attic
        getCheckboxForKorkortstyp('C1').selected = true;
        $scope.$digest();

        // set some values
        $scope.cert.hypoglykemier.egenkontrollBlodsocker = true;
        // this is watched so we should call digest
        $scope.cert.hypoglykemier.allvarligForekomstVakenTid = true;
        $scope.$digest();

        $scope.cert.hypoglykemier.allvarligForekomstVakenTidObservationstid = '2014-10-10';
        $scope.cert.bedomning.lamplighetInnehaBehorighet = true;
        $scope.$digest();

        // set korkort to false, - update attic
        getCheckboxForKorkortstyp('C1').selected = false;
        $scope.$digest();

        expect($scope.cert.hypoglykemier.egenkontrollBlodsocker).toBeUndefined();
        expect($scope.cert.hypoglykemier.allvarligForekomstVakenTid).toBeUndefined();
        expect($scope.cert.hypoglykemier.allvarligForekomstVakenTidObservationstid).toBe(undefined);
        expect($scope.cert.bedomning.lamplighetInnehaBehorighet).toBeUndefined();

        // re-enable korkot - restore attic, previous values should be visible
        getCheckboxForKorkortstyp('C1').selected = true;
        $scope.$digest();

        // this one works in the live but not here.. look on monday.
        expect($scope.cert.hypoglykemier.egenkontrollBlodsocker).toBe(true);
        expect($scope.cert.hypoglykemier.allvarligForekomstVakenTid).toBe(true);
        expect($scope.cert.hypoglykemier.allvarligForekomstVakenTidObservationstid).toBe('2014-10-10');
        expect($scope.cert.bedomning.lamplighetInnehaBehorighet).toBe(true);
    });

    // Helper methods
    function getCheckboxForKorkortstyp(typ) {
        for (var i = 0; i < $scope.cert.intygAvser.korkortstyp.length; i++) {
            if ($scope.cert.intygAvser.korkortstyp[i].type === typ) {
                return $scope.cert.intygAvser.korkortstyp[i];
            }
        }
        return null;
    }
});
