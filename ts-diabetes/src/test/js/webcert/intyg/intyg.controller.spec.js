describe('ts-diabetes.IntygController', function() {
    'use strict';

    var certificateService, user;

    beforeEach(angular.mock.module('common', 'ts-diabetes', function($provide) {
        certificateService = {
            getCertificate : function(id, type, onSuccess, onError) {
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
        //certificateService = jasmine.createSpyObj('common.CertificateService', [ 'getCertificate' ]);
        $provide.value('common.CertificateService', certificateService);
        user = jasmine.createSpyObj('common.User', [ 'getUserContext' ]);
        $provide.value('common.User', {});
        $provide.value('webcert.ManageCertificate', {});
    }));

    var $scope, ctrl;

    beforeEach(angular.mock.inject(function($controller, $rootScope) {
        $scope = $rootScope.$new();
        ctrl = $controller('ts-diabetes.IntygController', { $scope: $scope });

        $scope.$digest();
    }));

    it('Should assemble intygavser and bedomning where selected=true and add comma as separator', function() {
        expect($scope.view.intygAvser).toBe('A, D, DE, TAXI');
        expect($scope.view.bedomning).toBe('B, BE, TRAKTOR, C, CE');
    });
});