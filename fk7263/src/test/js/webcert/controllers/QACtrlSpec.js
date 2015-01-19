describe('QACtrl', function() {
    'use strict';

    var $httpBackend;
    var featureService;
    var $scope;
    var $q;
    var $rootScope;
    var fragaSvarCommonService;
    var fragaSvarService;

    // Load the webcert module and mock away everything that is not necessary.
    beforeEach(angular.mock.module('fk7263', function($provide) {
        featureService = {
            features:{
                HANTERA_INTYGSUTKAST: 'hanteraIntygsutkast'
            },
            isFeatureActive: jasmine.createSpy('isFeatureActive')
        };
        $provide.value('common.dialogService', {});
        fragaSvarCommonService = jasmine.createSpyObj('common.fragaSvarCommonService', [ 'isUnhandled' ]);
        $provide.value('common.fragaSvarCommonService',fragaSvarCommonService);
        $provide.value('common.ManageCertView',{});
        $provide.value('common.statService', {});
        $provide.value('common.User', {});
        fragaSvarService = jasmine.createSpyObj('cfk7263.fragaSvarService', [ 'getQAForCertificate', 'closeAsHandled' ]);
        $provide.value('fk7263.fragaSvarService',fragaSvarService);
    }));

    // Get references to the object we want to test from the context.
    /*beforeEach(angular.mock.inject(['test.fragaSvarService', '$controller', '$rootScope',
        function(_test_, $controller, _$rootScope) {
            underTest=_test_;
            $rootScope = _$rootScope;
            $scope = $rootScope.$new();
            $controller('test.TestCtrl', { $scope: $scope });
        }]));*/

    beforeEach(angular.mock.inject(['$controller', '$rootScope', '$q', '$httpBackend',
        function( $controller, _$rootScope_, _$q_, _$httpBackend_) {
            $rootScope = _$rootScope_;
            $scope = $rootScope.$new();
            $controller('fk7263.QACtrl' , { $scope: $scope, fragaSvarCommonService : fragaSvarCommonService, fragaSvarService:fragaSvarService });
            $q = _$q_;
            $httpBackend = _$httpBackend_;

            // arrange
            spyOn($scope, '$broadcast');

        }]));

    describe('#testEvents', function() {
        it('on fk7263.ViewCertCtrl.load', function(){

            // ----- arrange
            // spies, mocks

            // kick off the window change event
            var revoked;
            var statuses;
            var cert;
            //$rootScope.$broadcast('fk7263.ViewCertCtrl.load', revoked, statuses, cert);

            // ------ act
            // promises are resolved/dispatched only on next $digest cycle
            // this will fire the event!
            //$rootScope.$apply();

            // ------ assert
            // dialog should be opened
            // expects
            // TODO: the actual test!
            expect(true).toBe(true);
        });

        it('on fk7263.ViewCertCtrl.fail', function(){

            // ----- arrange
            // spies, mocks

            // kick off the window change event
            var revoked;
            var statuses;
            var cert;
            //$rootScope.$broadcast('fk7263.ViewCertCtrl.fail', revoked, statuses, cert);

            // ------ act
            // promises are resolved/dispatched only on next $digest cycle
            // this will fire the event!
            //$rootScope.$apply();

            // ------ assert
            // dialog should be opened
            // expects
            // TODO: the actual test!
            expect(true).toBe(true);
        });

    });

    describe('#hasUnhandledQas', function() {
        it('has no UnhandledQas', function(){

            $scope.qaList = [];
            expect($scope.hasUnhandledQas()).toBeFalsy();

        });

        it('has UnhandledQas', function(){
            // ----- arrange
            // in arrange we setup our spies with expected return values
            var qaAnswered = {status: 'ANSWERED'};
            $scope.qaList = [qaAnswered];
            fragaSvarCommonService.isUnhandled.andReturn(true);

            // ----- act
            var hasUnhandled = $scope.hasUnhandledQas();

            // ----- assert
            expect(fragaSvarCommonService.isUnhandled).toHaveBeenCalledWith(qaAnswered);

            expect(hasUnhandled).toBeTruthy();

        });

    });

    describe('#updateAllAsHandled', function() {
        it('has no UnhandledQas so shouldnt update qas', function(){
            // ----- arrange
            $scope.qaList = [];

            // ----- act
            $scope.updateAllAsHandled();

            // ----- assert
            expect($scope.hasUnhandledQas()).toBeFalsy();
            expect(fragaSvarCommonService.isUnhandled).not.toHaveBeenCalled();
        });

        it('has UnhandledQas so should update qas', function(){
            // ----- arrange
            var qaAnswered = {status: 'ANSWERED'};
            $scope.qaList = [qaAnswered];
            fragaSvarCommonService.isUnhandled.andReturn(true);

            // ----- act
            $scope.updateAllAsHandled();

            // ----- assert
            expect($scope.hasUnhandledQas()).toBeTruthy();
            expect(fragaSvarCommonService.isUnhandled).toHaveBeenCalledWith(qaAnswered);
            expect(fragaSvarService.closeAsHandled).toHaveBeenCalled();
        });

    });

});