/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

describe('ts-diabetes.IntygController', function() {
    'use strict';

    var IntygProxy, user, $httpBackend, $rootScope, $controller;

    beforeEach(angular.mock.module('common', 'ts-diabetes', function($provide) {
        IntygProxy = {
            isSentToTarget: function() {},
            isRevoked: function() {},
            getIntyg : function(id, type, onSuccess/*, onError*/) {
                onSuccess({
                    contents: {
                        'id': '987654321',
                        'typ': 'TS_DIABETES_U06_V02',
                        'grundData': {
                            'signeringsdatum': '2013-08-12T15:57:00.000',
                                'skapadAv': {
                                'personId': 'SE0000000000-1333',
                                    'fullstandigtNamn': 'Doktor Thompson',
                                    'vardenhet': {
                                    'enhetsid': 'SE0000000000-1337',
                                        'enhetsnamn': 'Vårdenhet Väst',
                                        'postadress': 'Enhetsvägen 12',
                                        'postnummer': '54321',
                                        'postort': 'Tumba',
                                        'telefonnummer': '08-1337',
                                        'vardgivare': {
                                        'vardgivarid': 'SE0000000000-HAHAHHSAA',
                                            'vardgivarnamn': 'Vårdgivarnamn'
                                    }
                                }
                            },
                            'patient': {
                                'personId': '19121212-1212',
                                'fullstandigtNamn': 'Herr Dundersjuk',
                                'fornamn': 'Herr',
                                'efternamn': 'Dundersjuk',
                                'postadress': 'Testvägen 12',
                                'postnummer': '123456',
                                'postort': 'Testort'
                            }
                        },
                        'kommentar' :  'Kommentarer av det viktiga slaget',
                        'vardkontakt' : {
                            'typ' : '5880005',
                            'idkontroll' : 'PASS'
                        },
                        'intygAvser' : {
                            'korkortstyp' : [
                                {'type': 'AM', 'selected': false},
                                {'type': 'A1', 'selected': false},
                                {'type': 'A2', 'selected': false},
                                {'type': 'A', 'selected': true},
                                {'type': 'B', 'selected': false},
                                {'type': 'BE', 'selected': false},
                                {'type': 'TRAKTOR', 'selected': false},
                                {'type': 'C1', 'selected': false},
                                {'type': 'C1E', 'selected': false},
                                {'type': 'C', 'selected': false},
                                {'type': 'CE', 'selected': false},
                                {'type': 'D1', 'selected': false},
                                {'type': 'D1E', 'selected': false},
                                {'type': 'D', 'selected': true},
                                {'type': 'DE', 'selected': true},
                                {'type': 'TAXI', 'selected': true}
                            ]
                        },
                        'diabetes' : {
                            'diabetestyp' : 'DIABETES_TYP_2',
                                'observationsperiod' : '2012',
                                'endastKost' : true,
                                'tabletter' : true,
                                'insulin' : true,
                                'insulinBehandlingsperiod' : '2012',
                                'annanBehandlingBeskrivning' : 'Hypnos'
                        },
                        'hypoglykemier' : {
                            'kunskapOmAtgarder' : true,
                                'teckenNedsattHjarnfunktion' : true,
                                'saknarFormagaKannaVarningstecken' : true,
                                'allvarligForekomst' : true,
                                'allvarligForekomstBeskrivning' : 'Beskrivning',
                                'allvarligForekomstTrafiken' : true,
                                'allvarligForekomstTrafikBeskrivning' : 'Beskrivning',
                                'allvarligForekomstVakenTid' : true,
                                'allvarligForekomstVakenTidObservationstid' : '2012-12-12',
                                'egenkontrollBlodsocker' : true
                        },
                        'syn' : {
                            'separatOgonlakarintyg' : false,
                                'synfaltsprovningUtanAnmarkning' : true,
                                'hoger' : {
                                'utanKorrektion' : 0.0,
                                    'medKorrektion' : 0.0
                            },

                            'vanster' : {
                                'utanKorrektion' : 0.0,
                                    'medKorrektion' : 0.0
                            },
                            'binokulart' : {
                                'utanKorrektion' : 0.0,
                                    'medKorrektion' : 0.0
                            },
                            'diplopi' : true,
                                'synfaltsprovning' : true,
                                'provningOgatsRorlighet' : true
                        },
                        'bedomning' : {
                            'korkortstyp' : [
                                {'type': 'AM', 'selected': false},
                                {'type': 'A1', 'selected': false},
                                {'type': 'A2', 'selected': false},
                                {'type': 'A', 'selected': false},
                                {'type': 'B', 'selected': true},
                                {'type': 'BE', 'selected': true},
                                {'type': 'TRAKTOR', 'selected': true},
                                {'type': 'C1', 'selected': false},
                                {'type': 'C1E', 'selected': false},
                                {'type': 'C', 'selected': true},
                                {'type': 'CE', 'selected': true},
                                {'type': 'D1', 'selected': false},
                                {'type': 'D1E', 'selected': false},
                                {'type': 'D', 'selected': false},
                                {'type': 'DE', 'selected': false},
                                {'type': 'TAXI', 'selected': false}
                            ],
                            'lakareSpecialKompetens' : 'Kronologisk bastuberedning',
                            'lamplighetInnehaBehorighet' : true
                        }
                    },
                    statuses:[{type:'RECEIVED', target:'MI', timestamp:'2015-04-15T09:37:17.932'}],
                    revoked:false
                });
            }
        };
        $provide.value('common.IntygProxy', IntygProxy);
        user = jasmine.createSpyObj('common.User', [ 'getUser' ]);
        $provide.value('common.User', {});
    }));

    var $scope, ctrl;

    beforeEach(angular.mock.inject(['$controller', '$rootScope', '$httpBackend', function(_$controller_, _$rootScope_, _$httpBackend_) {
        $rootScope = _$rootScope_;
        $controller = _$controller_;
        $httpBackend = _$httpBackend_;
        $scope = $rootScope.$new();
        ctrl = $controller('ts-diabetes.IntygController', { $scope: $scope });

        $scope.$digest();
    }]));

    it('Should assemble intygavser and bedomning where selected=true and add comma as separator', function() {
        expect($scope.viewState.intygAvser).toBe('A, D, DE, TAXI');
        expect($scope.viewState.bedomning).toBe('B, BE, TRAKTOR, C, CE');
    });
});
