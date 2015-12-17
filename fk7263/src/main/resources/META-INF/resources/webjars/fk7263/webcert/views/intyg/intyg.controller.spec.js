describe('IntygController', function() {
    'use strict';

    var $httpBackend;
    var $scope;
    var $q;
    var $rootScope;
    var IntygProxy;
    var viewState;
    var $controller;

    beforeEach(angular.mock.module('common', function(/*$provide*/) {

    }));

    // Load the webcert module and mock away everything that is not necessary.
    beforeEach(angular.mock.module('fk7263', function($provide) {
        IntygProxy = jasmine.createSpyObj('common.IntygProxy', [ 'getIntyg', 'load' ]);
        $provide.value('common.IntygProxy', IntygProxy);
    }));

    // Get references to the object we want to test from the context.

    beforeEach(angular.mock.inject(['$controller', '$rootScope', '$q', '$httpBackend','common.UserModel',
        function( _$controller_, _$rootScope_, _$q_, _$httpBackend_, _UserModel_) {
            $controller = _$controller_;
            $rootScope = _$rootScope_;
            $scope = $rootScope.$new();

            $q = _$q_;
            $httpBackend = _$httpBackend_;
            _UserModel_.setUser(getTestUser({LAKARE: 'Läkare'}));
            // arrange
            spyOn($scope, '$broadcast');
        }]));

    describe('#loadIntyg', function() {
        it('should run when controller is created', function(){

            $controller('fk7263.ViewCertCtrl' , { $scope: $scope, viewState:viewState });

            expect(IntygProxy.getIntyg).toHaveBeenCalled();
        });

    });

    /**
     * Created by stephenwhite on 21/09/15.
     */
    function getTestUser (role){
        return {
            'hsaId': 'eva',
            'namn': 'Eva Holgersson',
            'lakare': true,
            'forskrivarkod': '2481632',
            'authenticationScheme': 'urn:inera:webcert:fake',
            'vardgivare': [
                {
                    'id': 'vastmanland', 'namn': 'Landstinget Västmanland', 'vardenheter': [
                    {
                        'id': 'centrum-vast', 'namn': 'Vårdcentrum i Väst', 'arbetsplatskod': '0000000', 'mottagningar': [
                        {'id': 'akuten', 'namn': 'Akuten', 'arbetsplatskod': '0000000'},
                        {'id': 'dialys', 'namn': 'Dialys', 'arbetsplatskod': '0000000'}
                    ]
                    }
                ]
                },
                {
                    'id': 'ostergotland', 'namn': 'Landstinget Östergötland', 'vardenheter': [
                    {
                        'id': 'linkoping',
                        'namn': 'Linköpings Universitetssjukhus',
                        'arbetsplatskod': '0000000',
                        'mottagningar': [
                            {'id': 'lkpg-akuten', 'namn': 'Akuten', 'arbetsplatskod': '0000000'},
                            {'id': 'lkpg-ogon', 'namn': 'Ögonmottagningen', 'arbetsplatskod': '0000000'}
                        ]
                    }
                ]
                }
            ],
            'specialiseringar': ['Kirurgi', 'Oftalmologi'],
            'titel': 'Leg. Ögonläkare',
            'legitimeradeYrkesgrupper': ['Läkare'],
            'valdVardenhet': {
                'id': 'centrum-vast', 'namn': 'Vårdcentrum i Väst', 'arbetsplatskod': '0000000', 'mottagningar': [
                    {'id': 'akuten', 'namn': 'Akuten', 'arbetsplatskod': '0000000'},
                    {'id': 'dialys', 'namn': 'Dialys', 'arbetsplatskod': '0000000'}
                ]
            },
            'valdVardgivare': {
                'id': 'vastmanland', 'namn': 'Landstinget Västmanland', 'vardenheter': [
                    {
                        'id': 'centrum-vast', 'namn': 'Vårdcentrum i Väst', 'arbetsplatskod': '0000000', 'mottagningar': [
                        {'id': 'akuten', 'namn': 'Akuten', 'arbetsplatskod': '0000000'},
                        {'id': 'dialys', 'namn': 'Dialys', 'arbetsplatskod': '0000000'}
                    ]
                    }
                ]
            },
            'roles': role,
            'features': ['hanteraFragor', 'hanteraFragor.fk7263'],
            'totaltAntalVardenheter': 6,
            'origin': 'NORMAL'
        };

    }

});