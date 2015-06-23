describe('IntygController', function() {
    'use strict';

    var $httpBackend;
    var $scope;
    var $q;
    var $rootScope;
    var IntygProxy;
    var viewState;
    var $controller;

    beforeEach(angular.mock.module('common', function($provide) {

    }));

    // Load the webcert module and mock away everything that is not necessary.
    beforeEach(angular.mock.module('fk7263', function($provide) {
        IntygProxy = jasmine.createSpyObj('common.IntygProxy', [ 'getIntyg', 'load' ]);
        $provide.value('common.IntygProxy', IntygProxy);
        $provide.value('common.UserModel', { userContext: { lakare: false }});
    }));

    // Get references to the object we want to test from the context.

    beforeEach(angular.mock.inject(['$controller', '$rootScope', '$q', '$httpBackend',
        function( _$controller_, _$rootScope_, _$q_, _$httpBackend_) {
            $controller = _$controller_;
            $rootScope = _$rootScope_;
            $scope = $rootScope.$new();

            $q = _$q_;
            $httpBackend = _$httpBackend_;

            // arrange
            spyOn($scope, '$broadcast');
        }]));

    describe('#loadIntyg', function() {
        it('should run when controller is created', function(){

            $controller('fk7263.ViewCertCtrl' , { $scope: $scope, viewState:viewState });

            expect(IntygProxy.getIntyg).toHaveBeenCalled();
        });

    });

});