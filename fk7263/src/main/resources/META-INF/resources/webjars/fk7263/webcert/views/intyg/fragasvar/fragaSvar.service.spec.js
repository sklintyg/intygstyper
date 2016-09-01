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

describe('FragaSvarService', function() {
    'use strict';

    var $httpBackend;
    var $rootScope;
    var fragaSvarService;

    // Testdata
    var testNewQuestionResponse = {'internReferens':13,'frageStallare':'WC','amne':'ARBETSTIDSFORLAGGNING',
        'frageText':'fdhfdh','frageSigneringsDatum':'2015-02-23T10:14:49.013','frageSkickadDatum':'2015-02-23T10:14:49.013',
        'senasteHandelse':'2015-02-23T10:14:49.013','vardAktorHsaId':'eva','vardAktorNamn':'Eva Holgersson',
        'intygsReferens':{'intygsId':'intyg-2','intygsTyp':'fk7263','patientNamn':'Test Testorsson',
            'signeringsDatum':'2012-12-23T21:00:00.000','patientId':{'patientIdRoot':'1.2.752.129.2.1.3.1',
                'patientIdExtension':'19121212-1212'}},'vardperson':{'hsaId':'hans','namn':'Hans Njurgren','enhetsId':'dialys',
            'enhetsnamn':'Centrum Väst Mott','postadress':'Lasarettsvägen 13','postnummer':'721 61','postort':'Västerås',
            'telefonnummer':'021-1818000','epost':'centrum-vast@vardenhet.se','vardgivarId':'vastmanland',
            'vardgivarnamn':'Landstinget Västmanland'},'status':'PENDING_EXTERNAL_ACTION','vidarebefordrad':false,
        'senasteHandelseDatum':'2015-02-23T10:14:49.013'};
    var testAnswerResponse = {'internReferens':12,'externReferens':'FK-5003-revoked','frageStallare':'FK','amne':'OVRIGT',
        'frageText':'Detta är ett konstigt ärende. vad menar Hr Doktor egentligen?','frageSigneringsDatum':'2012-12-23T21:00:00.000',
        'frageSkickadDatum':'2013-09-09T00:00:00.000','svarsText':'srhrshs','svarSigneringsDatum':'2015-02-23T13:11:15.790',
        'svarSkickadDatum':'2015-02-23T13:11:15.790','senasteHandelse':'2015-02-23T13:11:15.790','vardAktorHsaId':'eva',
        'vardAktorNamn':'Eva Holgersson','intygsReferens':{'intygsId':'intyg-4-revoked','intygsTyp':'fk7263',
            'patientNamn':'Test Testorsson','signeringsDatum':'2011-01-26T00:00:00.000',
            'patientId':{'patientIdRoot':'1.2.752.129.2.1.3.1','patientIdExtension':'19121212-1212'}},
        'vardperson':{'hsaId':'eva','namn':'Eva Rättare','forskrivarKod':'1234567','enhetsId':'centrum-vast',
            'arbetsplatsKod':'123456789011','enhetsnamn':'Kir mott','postadress':'Lasarettsvägen 13','postnummer':'85150',
            'postort':'Sundsvall','telefonnummer':'060-1818000','vardgivarId':'Landstinget Norrland',
            'vardgivarnamn':'Landstinget Norrland'},'status':'CLOSED','vidarebefordrad':false,
        'senasteHandelseDatum':'2015-02-23T13:11:15.790'};

    beforeEach(module('fk7263', function($provide) {
        $provide.value('common.User', {});
    }));

    beforeEach(angular.mock.inject(['$controller', '$rootScope', '$httpBackend', 'fk7263.fragaSvarProxy',
        function($controller, _$rootScope_, _$httpBackend_, _fragaSvarService_) {

            $httpBackend = _$httpBackend_;
            $rootScope = _$rootScope_;
            fragaSvarService = _fragaSvarService_;
        }]));

    describe('#saveNewQuestion', function() {
        it('should call onSuccess callback when called', function() {

            var onSuccess = jasmine.createSpy('onSuccess');
            var onError = jasmine.createSpy('onError');

            var intygsTyp = 'fk7263';
            var intygsId = 'intyg-1';
            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + intygsId;
            $httpBackend.expectPOST(restPath).respond(200, testNewQuestionResponse);

            var question = {
                chosenTopic: {
                    value: 'KONTAKT'
                },
                frageText: 'Att fråga eller inte fråga. Det är frågan.'
            };

            fragaSvarService.saveNewQuestion(intygsId, intygsTyp, question, onSuccess, onError);
            $httpBackend.flush();

            expect(onSuccess).toHaveBeenCalled();
        });
    });

    describe('#saveAnswer', function() {
        it('should call onSuccess callback when saveAnswer is called', function() {

            var onSuccess = jasmine.createSpy('onSuccess');
            var onError = jasmine.createSpy('onError');

            var intygsTyp = 'fk7263';
            var intygsId = 'intyg-1';
            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + intygsId + '/besvara';
            $httpBackend.expectPUT(restPath).respond(200, testAnswerResponse);

            var answer = {
                internReferens: 'intyg-1',
                svarsText: 'Att svara eller inte svara. Det är frågan.'
            };

            fragaSvarService.saveAnswer(answer, intygsTyp, onSuccess, onError);
            $httpBackend.flush();

            expect(onSuccess).toHaveBeenCalled();
        });
    });

});
