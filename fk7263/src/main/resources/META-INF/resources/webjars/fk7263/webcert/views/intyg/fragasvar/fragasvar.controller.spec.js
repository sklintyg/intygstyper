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

describe('QACtrl', function() {
    'use strict';

    var $scope;
    var $rootScope;
    var fragaSvarCommonService;
    var fragaSvarService;
    var IntygService;
    var ObjectHelper;
    var pingService;

    var testCert = { 'id': 'intyg-2', 'typ': 'fk7263', 'grundData': {'signeringsdatum': '2012-12-23T21:00:00.000',
        'skapadAv': {'personId': 'hans', 'fullstandigtNamn': 'Hans Njurgren', 'vardenhet': {'enhetsid': 'dialys',
            'enhetsnamn': 'Centrum Väst Mott', 'postadress': 'Lasarettsvägen 13', 'postnummer': '721 61',
            'postort': 'Västerås', 'telefonnummer': '021-1818000', 'epost': 'centrum-vast@vardenhet.se',
            'vardgivare': {'vardgivarid': 'vastmanland', 'vardgivarnamn': 'Landstinget Västmanland'}}},
        'patient': {'personId': '19121212-1212', 'fullstandigtNamn': 'Test Testorsson', 'efternamn': 'Test Testorsson',
            'samordningsNummer': false}}, 'giltighet': {'from': '2011-01-26', 'tom': '2011-05-31'},
        'avstangningSmittskydd': false, 'diagnosKod': 'S47', 'diagnosKodsystem1': 'ICD_10_SE',
        'diagnosBeskrivning': 'Medicinskttillstånd: Klämskada på överarm',
        'sjukdomsforlopp': 'Bedömttillstånd: Patienten klämde höger överarm vid olycka i hemmet.\nProblemen har pågått en längre tid.',
        'funktionsnedsattning': 'Funktionstillstånd-Kroppsfunktion: Kraftigt nedsatt rörlighet i överarmen pga skadan.\n' +
            'Böj- och sträckförmågan är mycket dålig.\nSmärtar vid rörelse vilket ger att patienten inte kan använda armen särkilt mycket.',
        'undersokningAvPatienten': '2011-01-26', 'telefonkontaktMedPatienten': '2011-01-12', 'journaluppgifter': '2010-01-14',
        'annanReferens': '2010-01-24', 'aktivitetsbegransning': 'Funktionstillstånd-Aktivitet: Patienten bör/kan inte använda ' +
            'armen förrän skadan läkt.\nSkadan förvärras vid för tidigt påtvingad belastning.\nPatienten kan inte lyfta ' +
            'armen utan den ska hållas riktad nedåt och i fast läge så mycket som möjligt under tiden för läkning.',
        'rekommendationKontaktArbetsformedlingen': true, 'rekommendationKontaktForetagshalsovarden': true, 'rekommendationOvrigtCheck': true,
        'rekommendationOvrigt': 'När skadan förbättrats rekommenderas muskeluppbyggande sjukgymnastik',
        'atgardInomSjukvarden': 'Utreds om operation är nödvändig', 'annanAtgard': 'Patienten ansvarar för att armen hålls i stillhet',
        'rehabilitering': 'rehabiliteringGarInteAttBedoma', 'nuvarandeArbete': true,
        'nuvarandeArbetsuppgifter': 'Dirigent. Dirigerar en större orkester på deltid', 'arbetsloshet': true, 'foraldrarledighet': true,
        'nedsattMed25': {'from': '2011-04-01', 'tom': '2011-05-31'}, 'nedsattMed50': {'from': '2011-03-07', 'tom': '2011-03-31'},
        'nedsattMed75': {'from': '2011-02-14', 'tom': '2011-03-06'}, 'nedsattMed100': {'from': '2011-01-26', 'tom': '2011-02-13'},
        'arbetsformagaPrognos': 'Arbetsförmåga: Skadan har förvärrats vid varje tillfälle patienten använt armen. Måste ' +
            'hållas i total stillhet tills läkningsprocessen kommit en bit på väg. Eventuellt kan utredning visa att operation ' +
            'är nödvändig för att läka skadan.', 'prognosBedomning': 'arbetsformagaPrognosGarInteAttBedoma',
        'ressattTillArbeteAktuellt': false, 'ressattTillArbeteEjAktuellt': true, 'kontaktMedFk': true,
        'kommentar': 'Prognosen för patienten är god.\nHan kommer att kunna återgå till sitt arbete efter genomförd behandling.',
        'namnfortydligandeOchAdress': 'Hans Njurgren\nCentrum Väst Mott\nLasarettsvägen 13\n721 61 Västerås\n021-1818000' };

    // Load the webcert module and mock away everything that is not necessary.
    beforeEach(angular.mock.module('fk7263', function($provide) {
        $provide.value('common.dialogService', {});
        fragaSvarCommonService = jasmine.createSpyObj('common.fragaSvarCommonService', [ 'isUnhandled', 'fromFk', 'setVidareBefordradState' ]);
        $provide.value('common.fragaSvarCommonService', fragaSvarCommonService);
        $provide.value('common.IntygService', { isSentToTarget: function() {} });
        $provide.value('common.statService', {});
        $provide.value('common.User', {});
        $provide.value('common.UserModel', {});

        ObjectHelper = { isDefined: function() {} }; //jasmine.createSpyObj('common.ObjectHelper', [ 'isDefined']);
        $provide.value('common.ObjectHelper', ObjectHelper);
        fragaSvarService = jasmine.createSpyObj('fk7263.fragaSvarProxy',
            [ 'getQAForCertificate', 'closeAsHandled', 'closeAllAsHandled', 'saveNewQuestion', 'saveAnswer']);
        $provide.value('fk7263.fragaSvarProxy', fragaSvarService);
        $provide.value('common.IntygViewStateService', {});
        pingService = jasmine.createSpyObj('common.pingService', [ 'registerUserAction']);
        $provide.value('common.pingService', pingService);
    }));

    beforeEach(angular.mock.inject(['$controller', '$rootScope', 'common.IntygService',
        function($controller, _$rootScope_, _IntygService_) {
            $rootScope = _$rootScope_;
            $scope = $rootScope.$new();
            $controller('fk7263.QACtrl',
                { $scope: $scope, fragaSvarCommonService: fragaSvarCommonService, fragaSvarService: fragaSvarService });
            IntygService = _IntygService_;

            // arrange
            spyOn($scope, '$broadcast');

            // ----- arrange
            // spies, mocks
            spyOn(IntygService, 'isSentToTarget').and.callFake(function(/*statuses, target*/) {
                // Statuses include a SENT object below so return true.
                return true;
            });
            spyOn(ObjectHelper, 'isDefined').and.callFake(function(object) {
                return typeof object !== 'undefined' && object !== null;
            });

        }]));

    describe('#testEvents', function() {
        it('on load fragasvar with intyg', function() {

            // kick off the window change event
            $rootScope.$broadcast('fk7263.ViewCertCtrl.load', testCert, {
                isSent: true,
                isRevoked: false
            });

            // ------ act
            // promises are resolved/dispatched only on next $digest cycle
            // this will fire the event!
            $rootScope.$apply();

            // ------ assert
            expect($scope.intyg).toEqual(testCert);
            expect($scope.certProperties.isLoaded).toBe(true);
            expect($scope.certProperties.isSent).toBe(true);
            expect($scope.certProperties.isRevoked).toBe(false);
        });

        it('on load fragasvar with utkast (forced parent intyg)', function() {

            // kick off the window change event
            $rootScope.$broadcast('fk7263.ViewCertCtrl.load', testCert, {
                isSent: true,
                isRevoked: false,
                forceUseProvidedIntyg: true,
                kompletteringOnly: true
            });

            // ------ act
            // promises are resolved/dispatched only on next $digest cycle
            // this will fire the event!
            $rootScope.$apply();

            // ------ assert
            expect($scope.certProperties.isLoaded).toBe(true);
            expect($scope.certProperties.isSent).toBe(true);
            expect($scope.certProperties.isRevoked).toBe(false);
        });

        it('on load fragasvar with null', function() {

            // kick off the window change event
            $rootScope.$broadcast('fk7263.ViewCertCtrl.load', null, null);

            // ------ act
            // promises are resolved/dispatched only on next $digest cycle
            // this will fire the event!
            $rootScope.$apply();

            // ------ assert
            expect($scope.intyg).toEqual(null);
            expect($scope.certProperties.isLoaded).toBe(false);
            expect($scope.certProperties.isSent).toBe(false);
            expect($scope.certProperties.isRevoked).toBe(false);
        });

    });

    describe('#open-closed issuesFilter', function() {
        it('should return false if openIssuesFilter qa status is closed', function() {
            var qa = {
                status: 'CLOSED'
            };
            expect($scope.openIssuesFilter(qa)).toBe(false);
        });

        it('should return true if closedIssuesFilter qa status is closed', function() {
            var qa = {
                status: 'CLOSED'
            };
            expect($scope.closedIssuesFilter(qa)).toBe(true);
        });
    });

    describe('#send question', function() {
        it('should toggle the state of the new question form when toggleQuestionForm is called', function() {

            spyOn($scope, 'initQuestionForm');

            $scope.toggleQuestionForm();

            expect($scope.widgetState.newQuestionOpen).toBe(true);
            expect($scope.initQuestionForm).toHaveBeenCalled();

            // hide sent message and focus question
            expect($scope.widgetState.sentMessage).toBe(false);
            expect($scope.widgetState.focusQuestion).toBe(true);
        });

        it('should sendQuestion when "skicka fråga" is clicked', function() {

            var question = {
                chosenTopic: {
                    value: 'KONTAKT'
                },
                frageText: 'Att fråga eller inte fråga. Det är frågan.'
            };

            $scope.sendQuestion(question);

            expect(fragaSvarService.saveNewQuestion).toHaveBeenCalled();
        });
    });

});
