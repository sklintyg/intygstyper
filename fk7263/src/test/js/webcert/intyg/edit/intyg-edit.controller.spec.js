describe('EditCertCtrl', function() {
    'use strict';

    var $httpBackend;
    var featureService;
    var $scope;
    var $q;
    var $rootScope;
    var _manageCertView;
    var viewState;
    var intygModel;

    beforeEach(angular.mock.module('common', function($provide) {

    }));

    // Load the webcert module and mock away everything that is not necessary.
    beforeEach(angular.mock.module('fk7263', function($provide) {
        featureService = {
            features:{
                HANTERA_INTYGSUTKAST: 'hanteraIntygsutkast'
            },
            isFeatureActive: jasmine.createSpy('isFeatureActive')
        };
        $provide.value('common.CertificateService',{});

        _manageCertView = jasmine.createSpyObj('common.ManageCertView', [ 'save',  'discard', 'signera', 'printDraft', 'load' ]);
        $provide.value('common.ManageCertView',_manageCertView);
        $provide.value('common.User', {});
        $provide.value('common.UserModel', {});
        $provide.value('common.wcFocus', {});
        $provide.value('common.intygNotifyService', {});
        $provide.value('common.diagnosService', {});
        $provide.value('common.DateUtilsService', {});
        $provide.value('common.UtilsService', {});
        $provide.value('common.anchorScrollService', {});
    }));

    // Get references to the object we want to test from the context.

    beforeEach(angular.mock.inject(['$controller', '$rootScope', '$q', '$httpBackend',
        function( $controller, _$rootScope_, _$q_, _$httpBackend_) {
            $rootScope = _$rootScope_;
            $scope = $rootScope.$new();

            $controller('fk7263.EditCertCtrl' , { $scope: $scope, viewState:viewState });
            $q = _$q_;
            $httpBackend = _$httpBackend_;

            // arrange
            spyOn($scope, '$broadcast');
        }]));

    describe('#testEvents', function() {
        it('on some event', function(){

            // ----- arrange
            // spies, mocks

            // kick off the window change event
            //$rootScope.$broadcast('fk7263.ViewCertCtrl.load');

            // ------ act
            // promises are resolved/dispatched only on next $digest cycle
            $rootScope.$apply();

            // ------ assert
            // dialog should be opened
            // expects

            expect(true).toBe(true);
        });

    });

    describe('#testScopeFunctions', function() {
        it('some scope function', function(){
            // ----- arrange
            // in arrange we setup our spies with expected return values


            // ----- act
            // call the function

            // ----- assert
            // expects

            expect(true).toBe(true);
        });

    });

});