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

describe('lisjpFormFactory', function() {
    'use strict';

    var element;
    var lispFormFactory;
    var $scope;

    beforeEach(angular.mock.module('htmlTemplates', 'common', 'lisjp'));
    beforeEach(inject(['$compile', '$rootScope', 'lisjp.FormFactory', 'lisjp.Domain.IntygModel',
        function($compile, $rootScope, _lisjpFormFactory_, _lisjpIntygModel_) {
        lispFormFactory = _lisjpFormFactory_;

        $scope = $rootScope.$new();
        $scope.model = _lisjpIntygModel_._members.build().content;
        $scope.options = {
            formState:{viewState:{common:{validation:{}}}}
        };
        $scope.formFields = lispFormFactory.getFormFields();
        element =
            angular.element('<form><formly-form model="model" fields="formFields" options="options"></formly-form></form>');
        $compile(element)($scope);
        $scope.$digest();
    }]));

    var grundData = {
        'skapadAv':{'personId':'TSTNMT2321000156-1079','fullstandigtNamn':'Arnold Johansson','vardenhet':{'enhetsid':'TSTNMT2321000156-1077',
            'enhetsnamn':'NMT vg3 ve1','postadress':'NMT gata 3','postnummer':'12345','postort':'Testhult','telefonnummer':'0101112131416',
            'epost':'enhet3@webcert.invalid.se','vardgivare':{'vardgivarid':'TSTNMT2321000156-102Q','vardgivarnamn':'NMT vg3'},'arbetsplatsKod':'1234567890'}},
        'patient':{'personId':'19121212-1212','fullstandigtNamn':'Tolvan Tolvansson','fornamn':'Tolvan','efternamn':'Tolvansson',
            'postadress':'Svensson, Storgatan 1, PL 1234','postnummer':'12345','postort':'Småmåla','samordningsNummer':false},'relation':{}};

    var utkastData = {
        'grundData':grundData,'avstangningSmittskydd':false,'undersokningAvPatienten':'2016-08-01','telefonkontaktMedPatienten':'2016-08-02',
        'journaluppgifter':'2016-08-03','annatGrundForMU':'2016-08-04','annatGrundForMUBeskrivning':'Telepatisk kommunikation',
        'sysselsattning':[{'typ':'NUVARANDE_ARBETE'}],'nuvarandeArbete':'Siare','diagnoser':[{'diagnosKod':'D50','diagnosKodSystem':'ICD_10_SE',
            'diagnosBeskrivning':'Järnbristanemi'},{'diagnosKod':'G10','diagnosKodSystem':'ICD_10_SE','diagnosBeskrivning':'Huntingtons sjukdom'},
            {'diagnosKod':'T241','diagnosKodSystem':'ICD_10_SE','diagnosBeskrivning':'Brännskada av första graden på höft och nedre extremitet utom fotled och fot'}],
        'funktionsnedsattning':'Inga fynd gjordes','aktivitetsbegransning':'Har svårt att sitta och ligga.. Och stå. Får huka sig.',
        'pagaendeBehandling':'Meditering, självmedicinering','planeradBehandling':'Inga planerade åtgärder. Patienten har ingen almanacka.',
        'sjukskrivningar':[{'sjukskrivningsgrad':'HELT_NEDSATT','period':{'from':'2016-08-08','tom':'2016-08-22'}},{'sjukskrivningsgrad':'TRE_FJARDEDEL',
            'period':{'from':'2016-08-23','tom':'2016-08-24'}},{'sjukskrivningsgrad':'HALFTEN','period':{'from':'2016-08-25','tom':'2016-08-27'}},
            {'sjukskrivningsgrad':'EN_FJARDEDEL','period':{'from':'2016-08-29','tom':'2016-11-26'}}],
        'forsakringsmedicinsktBeslutsstod':'Har följt beslutstödet till punkt och pricka.','arbetstidsforlaggning':true,
        'arbetstidsforlaggningMotivering':'Har bra och dåliga dagar. Bättre att jobba 22h-24h de bra dagarna så patienten kan vila sedan.',
        'arbetsresor':true,'prognos':{'typ':'ATER_X_ANTAL_DGR','dagarTillArbete':'SEXTIO_DGR'},'arbetslivsinriktadeAtgarder':[{'typ':'ARBETSTRANING'},
            {'typ':'ARBETSANPASSNING'},{'typ':'SOKA_NYTT_ARBETE'},{'typ':'BESOK_ARBETSPLATS'},{'typ':'ERGONOMISK'},{'typ':'HJALPMEDEL'},{'typ':'KONFLIKTHANTERING'},
            {'typ':'KONTAKT_FHV'},{'typ':'OMFORDELNING'},{'typ':'OVRIGA_ATGARDER'}],'arbetslivsinriktadeAtgarderBeskrivning':'Därför.',
        'ovrigt':'Inga övriga upplysningar.','kontaktMedFk':true,'anledningTillKontakt':'Alltid roligt att prata med FK.','tillaggsfragor':[]};

    var utkastDataSmittskydd = {'grundData':grundData,'avstangningSmittskydd':true,'sysselsattning':[],'diagnoser':[{'diagnosKod':'D50',
        'diagnosKodSystem':'ICD_10_SE','diagnosBeskrivning':'Järnbristanemi'},{'diagnosKod':'G10','diagnosKodSystem':'ICD_10_SE','diagnosBeskrivning':'Huntingtons sjukdom'},
        {'diagnosKod':'T241','diagnosKodSystem':'ICD_10_SE','diagnosBeskrivning':'Brännskada av första graden på höft och nedre extremitet utom fotled och fot'}],
        'sjukskrivningar':[{'sjukskrivningsgrad':'HELT_NEDSATT','period':{'from':'2016-08-08','tom':'2016-08-22'}},{'sjukskrivningsgrad':'TRE_FJARDEDEL','period':
            {'from':'2016-08-23','tom':'2016-08-24'}},{'sjukskrivningsgrad':'HALFTEN','period':{'from':'2016-08-25','tom':'2016-08-27'}},{'sjukskrivningsgrad':
            'EN_FJARDEDEL','period':{'from':'2016-08-29','tom':'2016-11-26'}}],'prognos':{},'arbetslivsinriktadeAtgarder':[],'ovrigt':'Inga övriga upplysningar.',
        'kontaktMedFk':false,'tillaggsfragor':[]};

    it('Should clear model values if avstangningSmittskydd is selected', function() {

        // Load utkast with all fields populated
        $scope.model.update(utkastData);
        $scope.$digest();

        // Verify sendmodel includes all fields
        expect(angular.equals($scope.model.toSendModel(), utkastData)).toBeTruthy();

        /*
            avstangningSmittskydd should clear all fields except

            diagnos
            sjukskrivningar
            ovrigt
         */
        $scope.model.avstangningSmittskydd = true;
        $scope.$digest();

        expect(angular.equals($scope.model.toSendModel(), utkastDataSmittskydd)).toBeTruthy();

        // when avstangningSmittskydd is set to false again the original values should be restored from the attic
        $scope.model.avstangningSmittskydd = false;
        $scope.$digest();

        expect(angular.equals($scope.model.toSendModel(), utkastData)).toBeTruthy();
    });
});
