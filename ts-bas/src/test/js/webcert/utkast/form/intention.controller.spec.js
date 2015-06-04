describe('ts-bas.Utkast.Form17Controller', function() {
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

        ctrl = $controller('ts-bas.Utkast.IntentionController', { $scope: $scope });

            var cert = testData.cert;

        spyOn(viewState, 'setDraftModel');
        spyOn(viewState, 'intygModel').and.returnValue(cert);

        $scope.$digest();
    }]));

    // --- intention
    it('should show extra fields when some "korkortstyp"-options are selected', function() {
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

        getCheckboxForKorkortstyp('ANNAT').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeFalsy();
        getCheckboxForKorkortstyp('ANNAT').selected = false;

        getCheckboxForKorkortstyp('C1').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeFalsy();
        getCheckboxForKorkortstyp('C1').selected = false;
    });

    it('should reset hidden fields when some "korkortstyp"-options are deselected', function() {

        getCheckboxForKorkortstyp('D1').selected = true;
        $scope.$digest();

        $scope.cert.horselBalans.svartUppfattaSamtal4Meter = true;
        $scope.cert.funktionsnedsattning.otillrackligRorelseformaga = true;
        getCheckboxForKorkortstyp('D1').selected = false;
        $scope.$digest();

        expect($scope.cert.horselBalans.svartUppfattaSamtal4Meter).toBeUndefined();
        expect($scope.cert.funktionsnedsattning.otillrackligRorelseformaga).toBeUndefined();

        // Attic
        getCheckboxForKorkortstyp('D1').selected = true;
        $scope.$digest();

        expect($scope.cert.horselBalans.svartUppfattaSamtal4Meter).toBeTruthy();
        expect($scope.cert.funktionsnedsattning.otillrackligRorelseformaga).toBeTruthy();

        // Check that 2b and 3b still are selected if you select another high körkortstyp after it already is set to high.
        $scope.cert.horselBalans.svartUppfattaSamtal4Meter = true;
        $scope.cert.funktionsnedsattning.otillrackligRorelseformaga = true;
        getCheckboxForKorkortstyp('D1E').selected = true;
        $scope.$digest();

        expect($scope.cert.horselBalans.svartUppfattaSamtal4Meter).toBeTruthy();
        expect($scope.cert.funktionsnedsattning.otillrackligRorelseformaga).toBeTruthy();
    });
    // --- intention


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
