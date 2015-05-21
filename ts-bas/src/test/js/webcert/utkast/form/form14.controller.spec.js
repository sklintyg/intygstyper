describe('ts-bas.Utkast.Form14Controller', function() {
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

        ctrl = $controller('ts-bas.Utkast.Form14Controller', { $scope: $scope });

            var cert = testData.cert;

        spyOn(viewState, 'setDraftModel');
        spyOn(viewState, 'intygModel').and.returnValue(cert);

        $scope.$digest();
    }]));

    // --- form14
    it('should reset hidden fields when "sjukhusEllerLakarkontakt" is set to false', function() {
        $scope.cert.sjukhusvard.sjukhusEllerLakarkontakt = true;
        $scope.$digest();

        $scope.cert.sjukhusvard.tidpunkt = 'Förra veckan';
        $scope.cert.sjukhusvard.vardinrattning = 'Sahlgrenska';
        $scope.cert.sjukhusvard.anledning = 'Allt';
        $scope.cert.sjukhusvard.sjukhusEllerLakarkontakt = false;
        $scope.$digest();

        expect($scope.cert.sjukhusvard.tidpunkt).toBe('');
        expect($scope.cert.sjukhusvard.vardinrattning).toBe('');
        expect($scope.cert.sjukhusvard.anledning).toBe('');

        // Attic
        $scope.cert.sjukhusvard.sjukhusEllerLakarkontakt = true;
        $scope.$digest();

        expect($scope.cert.sjukhusvard.tidpunkt).toBe('Förra veckan');
        expect($scope.cert.sjukhusvard.vardinrattning).toBe('Sahlgrenska');
        expect($scope.cert.sjukhusvard.anledning).toBe('Allt');
    });
    // --- form14
});
