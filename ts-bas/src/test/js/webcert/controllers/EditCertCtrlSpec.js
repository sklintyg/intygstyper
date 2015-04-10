describe('ts-bas.EditCertCtrl', function() {
    'use strict';

    var ManageCertView;
    var User;
    var wcFocus;
    var intygNotifyService;

    beforeEach(angular.mock.module('ts-bas', function($provide) {
        ManageCertView = jasmine.createSpyObj('common.ManageCertView', [ 'load' ]);
        User = {};
        wcFocus = {};
        $provide.value('common.ManageCertView', ManageCertView);
        $provide.value('common.UserModel', User);
        $provide.value('common.wcFocus', wcFocus);
        $provide.value('common.intygNotifyService', intygNotifyService);
        $provide.value('common.UtkastViewStateService',{intyg:{}});
    }));

    var $scope, ctrl;

    beforeEach(angular.mock.inject(function($controller, $rootScope) {
        $scope = $rootScope.$new();
        ctrl = $controller('ts-bas.EditCertCtrl', { $scope: $scope });
        $scope.cert = {
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

        $scope.$digest();
    }));

    it('should show extra fields when some "korkortstyp"-options are selected', function() {
        getCheckboxForKorkortstyp('D1').selected = true;
        $scope.$digest();
        expect($scope.form.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('D1').selected = false;

        getCheckboxForKorkortstyp('D1E').selected = true;
        $scope.$digest();
        expect($scope.form.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('D1E').selected = false;

        getCheckboxForKorkortstyp('D').selected = true;
        $scope.$digest();
        expect($scope.form.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('D').selected = false;

        getCheckboxForKorkortstyp('DE').selected = true;
        $scope.$digest();
        expect($scope.form.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('DE').selected = false;

        getCheckboxForKorkortstyp('TAXI').selected = true;
        $scope.$digest();
        expect($scope.form.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('TAXI').selected = false;

        getCheckboxForKorkortstyp('ANNAT').selected = true;
        $scope.$digest();
        expect($scope.form.korkortd).toBeFalsy();
        getCheckboxForKorkortstyp('ANNAT').selected = false;

        getCheckboxForKorkortstyp('C1').selected = true;
        $scope.$digest();
        expect($scope.form.korkortd).toBeFalsy();
        getCheckboxForKorkortstyp('C1').selected = false;
    });

    it('should reset hidden fields when some "korkortstyp"-options are deselected', function() {
        getCheckboxForKorkortstyp('D1').selected = true;
        $scope.cert.horselBalans.svartUppfattaSamtal4Meter = true;
        $scope.cert.funktionsnedsattning.otillrackligRorelseformaga = true;
        $scope.$digest();

        getCheckboxForKorkortstyp('D1').selected = false;
        $scope.$digest();

        expect($scope.cert.horselBalans.svartUppfattaSamtal4Meter).toBeNull();
        expect($scope.cert.funktionsnedsattning.otillrackligRorelseformaga).toBeNull();

    });

    it('should reset hidden fields when "funktionsnedsattning" is set to false', function() {
        $scope.cert.funktionsnedsattning.funktionsnedsattning = true;
        $scope.cert.funktionsnedsattning.beskrivning = 'Hello';
        $scope.$digest();

        $scope.cert.funktionsnedsattning.funktionsnedsattning = false;
        $scope.$digest();

        expect($scope.cert.funktionsnedsattning.beskrivning).toBe('');
    });

    it('should reset hidden fields when "riskfaktorerStroke" is set to false', function() {
        $scope.cert.hjartKarl.riskfaktorerStroke = true;
        $scope.cert.hjartKarl.beskrivningRiskfaktorer = 'Hello';
        $scope.$digest();

        $scope.cert.hjartKarl.riskfaktorerStroke = false;
        $scope.$digest();

        expect($scope.cert.hjartKarl.beskrivningRiskfaktorer).toBe('');
    });

    it('should reset hidden fields when "harDiabetes" is set to false', function() {
        $scope.cert.diabetes.harDiabetes = true;
        $scope.cert.diabetes.diabetesTyp = 'DIABETES_TYP_1';
        $scope.$digest();

        $scope.cert.diabetes.harDiabetes = false;
        $scope.$digest();

        expect($scope.cert.diabetes.diabetesTyp).toBeNull();
    });

    it('should reset hidden fields when "diabetesTyp" is not "DIABETES_TYP_2"', function() {
        $scope.cert.diabetes.diabetesTyp = 'DIABETES_TYP_2';
        $scope.cert.diabetes.kost = true;
        $scope.cert.diabetes.tabletter = true;
        $scope.cert.diabetes.insulin = true;
        $scope.$digest();

        $scope.cert.diabetes.diabetesTyp = 'DIABETES_TYP_1';
        $scope.$digest();

        expect($scope.cert.diabetes.kost).toBeNull();
        expect($scope.cert.diabetes.tabletter).toBeNull();
        expect($scope.cert.diabetes.insulin).toBeNull();
    });

    it('should reset hidden fields when "medvetandestorning" is set to false', function() {
        $scope.cert.medvetandestorning.medvetandestorning = true;
        $scope.cert.medvetandestorning.beskrivning = 'Hello';
        $scope.$digest();

        $scope.cert.medvetandestorning.medvetandestorning = false;
        $scope.$digest();

        expect($scope.cert.medvetandestorning.beskrivning).toBe('');
    });

    it('should reset hidden fields when "teckenMissbruk" and "foremalForVardinsats" is set to false', function() {
        $scope.cert.narkotikaLakemedel.teckenMissbruk = true;
        $scope.cert.narkotikaLakemedel.foremalForVardinsats = true;
        $scope.cert.narkotikaLakemedel.provtagningBehovs = true;
        $scope.$digest();

        $scope.cert.narkotikaLakemedel.teckenMissbruk = false;
        $scope.$digest();
        expect($scope.cert.narkotikaLakemedel.provtagningBehovs).toBeTruthy();

        $scope.cert.narkotikaLakemedel.teckenMissbruk = true;
        $scope.cert.narkotikaLakemedel.foremalForVardinsats = false;
        $scope.$digest();
        expect($scope.cert.narkotikaLakemedel.provtagningBehovs).toBeTruthy();

        $scope.cert.narkotikaLakemedel.teckenMissbruk = false;
        $scope.$digest();
        expect($scope.cert.narkotikaLakemedel.provtagningBehovs).toBeNull();
    });

    it('should reset hidden fields when "lakarordineratLakemedelsbruk" is set to false', function() {
        $scope.cert.narkotikaLakemedel.lakarordineratLakemedelsbruk = true;
        $scope.cert.narkotikaLakemedel.lakemedelOchDos = 'Hello';
        $scope.$digest();

        $scope.cert.narkotikaLakemedel.lakarordineratLakemedelsbruk = false;
        $scope.$digest();

        expect($scope.cert.narkotikaLakemedel.lakemedelOchDos).toBe('');
    });

    it('should reset hidden fields when "sjukhusEllerLakarkontakt" is set to false', function() {
        $scope.cert.sjukhusvard.sjukhusEllerLakarkontakt = true;
        $scope.cert.sjukhusvard.tidpunkt = 'Förra veckan';
        $scope.cert.sjukhusvard.vardinrattning = 'Sahlgrenska';
        $scope.cert.sjukhusvard.anledning = 'Allt';
        $scope.$digest();

        $scope.cert.sjukhusvard.sjukhusEllerLakarkontakt = false;
        $scope.$digest();

        expect($scope.cert.sjukhusvard.tidpunkt).toBe('');
        expect($scope.cert.sjukhusvard.vardinrattning).toBe('');
        expect($scope.cert.sjukhusvard.anledning).toBe('');
    });

    it('should reset hidden fields when "stadigvarandeMedicinering" is set to false', function() {
        $scope.cert.medicinering.stadigvarandeMedicinering = true;
        $scope.cert.medicinering.beskrivning = 'Hello';
        $scope.$digest();

        $scope.cert.medicinering.stadigvarandeMedicinering = false;
        $scope.$digest();

        expect($scope.cert.medicinering.beskrivning).toBe('');
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
