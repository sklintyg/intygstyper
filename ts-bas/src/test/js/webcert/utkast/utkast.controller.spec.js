describe('ts-bas.UtkastController', function() {
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

        ctrl = $controller('ts-bas.UtkastController', { $scope: $scope });
        var cert = {
            id: '9e5340af-015f-4942-b2ec-d9e512d09abd',
            typ: 'TS_BAS_U06_V06',
            skapadAv: {
                personid: 'IFV1239877878-104B',
                fullstandigtNamn: 'Åsa Andersson',
                vardenhet: {
                    enhetsid: 'IFV1239877878-1042',
                    enhetsnamn: 'WebCert-Enhet1',
                    postadress: 'Storgatan 1',
                    postnummer: '12345',
                    postort: 'Småmåla',
                    telefonnummer: '0101234567890',
                    vardgivare: {
                        vardgivarid: 'IFV1239877878-1041',
                        vardgivarnamn: 'WebCert-Vårdgivare1'
                    }
                }
            },
            patient: {
                personid: '20121212-1212',
                fullstandigtNamn: 'Lilltolvan Tolvansson',
                fornamn: 'Lilltolvan',
                efternamn: 'Tolvansson',
                postadress: 'Storgatan 1',
                postnummer: '12345',
                postort: 'Småmåla'
            },
            vardkontakt: {},
            intygAvser: {
                korkortstyp: [
                    { type: 'C1', selected: false },
                    { type: 'C1E', selected: false },
                    { type: 'C', selected: false },
                    { type: 'CE', selected: false },
                    { type: 'D1', selected: false },
                    { type: 'D1E', selected: false },
                    { type: 'D', selected: false },
                    { type: 'DE', selected: false },
                    { type: 'TAXI', selected: false },
                    { type: 'ANNAT', selected: false }
                ]
            },
            syn: {},
            horselBalans: {},
            funktionsnedsattning: {},
            hjartKarl: {},
            diabetes: {},
            neurologi: {},
            medvetandestorning: {},
            njurar: {},
            kognitivt: {},
            somnVakenhet: {},
            narkotikaLakemedel: {},
            psykiskt: {},
            utvecklingsstorning: {},
            sjukhusvard: {},
            medicinering: {},
            bedomning: {
                korkortstyp: [
                    { type: 'C1', selected: false },
                    { type: 'C1E', selected: false },
                    { type: 'C', selected: false },
                    { type: 'CE', selected: false },
                    { type: 'D1', selected: false },
                    { type: 'D1E', selected: false },
                    { type: 'D', selected: false },
                    { type: 'DE', selected: false },
                    { type: 'TAXI', selected: false },
                    { type: 'ANNAT', selected: false }
                ]
            }
        };
        spyOn(viewState, 'setDraftModel');
        spyOn(viewState, 'intygModel').and.returnValue(cert);

        $scope.$digest();
    }]));

    it('should show extra fields when some "korkortstyp"-options are selected', function() {
        getCheckboxForKorkortstyp('D1').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('D1').selected = false;

        getCheckboxForKorkortstyp('D1E').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('D1E').selected = false;

        getCheckboxForKorkortstyp('D').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('D').selected = false;

        getCheckboxForKorkortstyp('DE').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('DE').selected = false;

        getCheckboxForKorkortstyp('TAXI').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('TAXI').selected = false;

        getCheckboxForKorkortstyp('ANNAT').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeFalsy();
        getCheckboxForKorkortstyp('ANNAT').selected = false;

        getCheckboxForKorkortstyp('C1').selected = true;
        $scope.$digest();
        expect($scope.viewState.korkortd).toBeFalsy();
        getCheckboxForKorkortstyp('C1').selected = false;
    });

    it('should reset hidden fields when some "korkortstyp"-options are deselected', function() {

        getCheckboxForKorkortstyp('D1').selected = true;
        $scope.$digest();

        $scope.cert.horselBalans.svartUppfattaSamtal4Meter = true;
        $scope.cert.funktionsnedsattning.otillrackligRorelseformaga = true;
        getCheckboxForKorkortstyp('D1').selected = false;
        $scope.$digest();

        expect($scope.cert.horselBalans.svartUppfattaSamtal4Meter).toBeUndefined();
        expect($scope.cert.funktionsnedsattning.otillrackligRorelseformaga).toBeUndefined();

        // Attic
        getCheckboxForKorkortstyp('D1').selected = true;
        $scope.$digest();

        expect($scope.cert.horselBalans.svartUppfattaSamtal4Meter).toBeTruthy();
        expect($scope.cert.funktionsnedsattning.otillrackligRorelseformaga).toBeTruthy();
    });

    it('should reset hidden fields when "funktionsnedsattning" is set to false', function() {
        $scope.cert.funktionsnedsattning.funktionsnedsattning = true;
        $scope.$digest();

        $scope.cert.funktionsnedsattning.beskrivning = 'Hello';
        $scope.cert.funktionsnedsattning.funktionsnedsattning = false;
        $scope.$digest();

        expect($scope.cert.funktionsnedsattning.beskrivning).toBe('');

        // Attic
        $scope.cert.funktionsnedsattning.funktionsnedsattning = true;
        $scope.$digest();

        expect($scope.cert.funktionsnedsattning.beskrivning).toBe('Hello');
    });

    it('should reset hidden fields when "riskfaktorerStroke" is set to false', function() {
        $scope.cert.hjartKarl.riskfaktorerStroke = true;
        $scope.$digest();

        $scope.cert.hjartKarl.beskrivningRiskfaktorer = 'Hello';
        $scope.cert.hjartKarl.riskfaktorerStroke = false;
        $scope.$digest();

        expect($scope.cert.hjartKarl.beskrivningRiskfaktorer).toBe('');

        // Attic
        $scope.cert.hjartKarl.riskfaktorerStroke = true;
        $scope.$digest();

        expect($scope.cert.hjartKarl.beskrivningRiskfaktorer).toBe('Hello');
    });

    it('should reset hidden fields when "harDiabetes" is set to false', function() {
        $scope.cert.diabetes.harDiabetes = true;
        $scope.$digest();

        $scope.cert.diabetes.diabetesTyp = 'DIABETES_TYP_1';
        $scope.cert.diabetes.harDiabetes = false;
        $scope.$digest();

        expect($scope.cert.diabetes.diabetesTyp).toBeUndefined();

        // Attic
        $scope.cert.diabetes.harDiabetes = true;
        $scope.$digest();

        expect($scope.cert.diabetes.diabetesTyp).toBe('DIABETES_TYP_1');
    });

    it('should reset hidden fields when "diabetesTyp" is not "DIABETES_TYP_2"', function() {
        $scope.cert.diabetes.diabetesTyp = 'DIABETES_TYP_2';
        $scope.$digest();

        $scope.cert.diabetes.kost = true;
        $scope.cert.diabetes.tabletter = true;
        $scope.cert.diabetes.insulin = true;
        $scope.cert.diabetes.diabetesTyp = 'DIABETES_TYP_1';
        $scope.$digest();

        expect($scope.cert.diabetes.kost).toBeUndefined();
        expect($scope.cert.diabetes.tabletter).toBeUndefined();
        expect($scope.cert.diabetes.insulin).toBeUndefined();

        // Attic
        $scope.cert.diabetes.diabetesTyp = 'DIABETES_TYP_2';
        $scope.$digest();

        expect($scope.cert.diabetes.kost).toBeTruthy();
        expect($scope.cert.diabetes.tabletter).toBeTruthy();
        expect($scope.cert.diabetes.insulin).toBeTruthy();
    });

    it('should reset hidden fields when "medvetandestorning" is set to false', function() {
        $scope.cert.medvetandestorning.medvetandestorning = true;
        $scope.$digest();

        $scope.cert.medvetandestorning.beskrivning = 'Hello';
        $scope.cert.medvetandestorning.medvetandestorning = false;
        $scope.$digest();

        expect($scope.cert.medvetandestorning.beskrivning).toBe('');

        // Attic
        $scope.cert.medvetandestorning.medvetandestorning = true;
        $scope.$digest();

        expect($scope.cert.medvetandestorning.beskrivning).toBe('Hello');
    });

    it('should reset hidden fields when "teckenMissbruk" and "foremalForVardinsats" is set to false', function() {
        $scope.cert.narkotikaLakemedel.teckenMissbruk = true;
        $scope.cert.narkotikaLakemedel.foremalForVardinsats = true;
        $scope.$digest();

        // Set provtagning
        $scope.cert.narkotikaLakemedel.provtagningBehovs = true;

        // One true, nothing changes
        $scope.cert.narkotikaLakemedel.teckenMissbruk = false;
        $scope.$digest();

        expect($scope.cert.narkotikaLakemedel.provtagningBehovs).toBeTruthy();

        // Still one true, nothing changes
        $scope.cert.narkotikaLakemedel.teckenMissbruk = true;
        $scope.cert.narkotikaLakemedel.foremalForVardinsats = false;
        $scope.$digest();

        expect($scope.cert.narkotikaLakemedel.provtagningBehovs).toBeTruthy();

        // Both false, provtagning = true will be saved and cleared because field is invisible
        $scope.cert.narkotikaLakemedel.teckenMissbruk = false;
        $scope.$digest();

        expect($scope.cert.narkotikaLakemedel.provtagningBehovs).toBeUndefined();

        // Attic
        // One true again, provtagning = true should be restored from attic
        $scope.cert.narkotikaLakemedel.teckenMissbruk = true;
        $scope.$digest();

        expect($scope.cert.narkotikaLakemedel.provtagningBehovs).toBeTruthy();
    });

    it('should reset hidden fields when "lakarordineratLakemedelsbruk" is set to false', function() {
        $scope.cert.narkotikaLakemedel.lakarordineratLakemedelsbruk = true;
        $scope.$digest();

        $scope.cert.narkotikaLakemedel.lakemedelOchDos = 'Hello';
        $scope.cert.narkotikaLakemedel.lakarordineratLakemedelsbruk = false;
        $scope.$digest();

        expect($scope.cert.narkotikaLakemedel.lakemedelOchDos).toBe('');

        // Attic
        $scope.cert.narkotikaLakemedel.lakarordineratLakemedelsbruk = true;
        $scope.$digest();

        expect($scope.cert.narkotikaLakemedel.lakemedelOchDos).toBe('Hello');
    });

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

    it('should reset hidden fields when "stadigvarandeMedicinering" is set to false', function() {
        $scope.cert.medicinering.stadigvarandeMedicinering = true;
        $scope.$digest();

        $scope.cert.medicinering.beskrivning = 'Hello';
        $scope.cert.medicinering.stadigvarandeMedicinering = false;
        $scope.$digest();

        expect($scope.cert.medicinering.beskrivning).toBe('');

        // Attic
        $scope.cert.medicinering.stadigvarandeMedicinering = true;
        $scope.$digest();

        expect($scope.cert.medicinering.beskrivning).toBe('Hello');
    });

    // Helper methods

    function getCheckboxForKorkortstyp(typ) {
        for (var i = 0; i < $scope.cert.intygAvser.korkortstyp.length; i++) {
            if ($scope.cert.intygAvser.korkortstyp[i].type === typ) {
                return $scope.cert.intygAvser.korkortstyp[i];
            }
        }
        return null;
    }
});
