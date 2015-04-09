describe('ts-diabetes.EditCertCtrl', function() {
    'use strict';

    var ManageCertView;
    var UserModel;
    var wcFocus;
    var intygNotifyService;

    beforeEach(angular.mock.module('ts-diabetes', function($provide) {
        ManageCertView = jasmine.createSpyObj('common.ManageCertView', [ 'load' ]);
        UserModel = {};
        wcFocus = {};
        $provide.value('common.ManageCertView', ManageCertView);
        $provide.value('common.UserModel', UserModel);
        $provide.value('common.wcFocus', wcFocus);
        $provide.value('common.intygNotifyService', intygNotifyService);
        $provide.value('common.IntygEditViewStateService',{intyg:{}});
    }));

    var $scope, ctrl;

    beforeEach(angular.mock.inject(function($controller, $rootScope) {
        $scope = $rootScope.$new();
        ctrl = $controller('ts-diabetes.EditCertCtrl', { $scope: $scope });
        $scope.cert = {
            id: '8eab08a6-e25a-44c1-8cf4-634df3b6459a',
            typ: 'TS_DIABETES_U06_V02',
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
            intygAvser: {
                korkortstyp: [
                    { type: 'AM', selected: false },
                    { type: 'A1', selected: false },
                    { type: 'A2', selected: false },
                    { type: 'A', selected: false },
                    { type: 'B', selected: false },
                    { type: 'BE', selected: false },
                    { type: 'TRAKTOR', selected: false },
                    { type: 'C1', selected: false },
                    { type: 'C1E', selected: false },
                    { type: 'C', selected: false },
                    { type: 'CE', selected: false },
                    { type: 'D1', selected: false },
                    { type: 'D1E', selected: false },
                    { type: 'D', selected: false },
                    { type: 'DE', selected: false },
                    { type: 'TAXI', selected: false }
                ]
            },
            diabetes: {},
            hypoglykemier: {},
            syn: {},
            bedomning: {
                korkortstyp: [
                    { type: 'AM', selected: false },
                    { type: 'A1', selected: false },
                    { type: 'A2', selected: false },
                    { type: 'A', selected: false },
                    { type: 'B', selected: false },
                    { type: 'BE', selected: false },
                    { type: 'TRAKTOR', selected: false },
                    { type: 'C1', selected: false },
                    { type: 'C1E', selected: false },
                    { type: 'C', selected: false },
                    { type: 'CE', selected: false },
                    { type: 'D1', selected: false },
                    { type: 'D1E', selected: false },
                    { type: 'D', selected: false },
                    { type: 'DE', selected: false },
                    { type: 'TAXI', selected: false }
                ]
            }
        };

        $scope.$digest();
    }));

    it('should show extra fields when some "korkortstyp"-options are selected', function() {
        getCheckboxForKorkortstyp('C1').selected = true;
        $scope.$digest();
        expect($scope.form.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('C1').selected = false;

        getCheckboxForKorkortstyp('C1E').selected = true;
        $scope.$digest();
        expect($scope.form.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('C1E').selected = false;

        getCheckboxForKorkortstyp('C').selected = true;
        $scope.$digest();
        expect($scope.form.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('C').selected = false;

        getCheckboxForKorkortstyp('CE').selected = true;
        $scope.$digest();
        expect($scope.form.korkortd).toBeTruthy();
        getCheckboxForKorkortstyp('CE').selected = false;

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

        getCheckboxForKorkortstyp('B').selected = true;
        $scope.$digest();
        expect($scope.form.korkortd).toBeFalsy();
        getCheckboxForKorkortstyp('B').selected = false;
    });

    it('should reset hidden fields when some "korkortstyp"-options are deselected', function() {
        getCheckboxForKorkortstyp('C1').selected = true;
        $scope.cert.hypoglykemier.egenkontrollBlodsocker = true;
        $scope.cert.hypoglykemier.allvarligForekomstVakenTid = true;
        $scope.cert.bedomning.lamplighetInnehaBehorighet = true;
        $scope.$digest();

        getCheckboxForKorkortstyp('C1').selected = false;
        $scope.$digest();

        expect($scope.cert.hypoglykemier.egenkontrollBlodsocker).toBeUndefined();
        expect($scope.cert.hypoglykemier.allvarligForekomstVakenTid).toBeUndefined();
        expect($scope.cert.bedomning.lamplighetInnehaBehorighet).toBeUndefined();
    });

    it('should reset hidden fields when "teckenNedsattHjarnfunktion" is set to false', function() {
        $scope.cert.hypoglykemier.teckenNedsattHjarnfunktion = true;
        $scope.cert.hypoglykemier.saknarFormagaKannaVarningstecken = true;
        $scope.cert.hypoglykemier.allvarligForekomst = true;
        $scope.cert.hypoglykemier.allvarligForekomstTrafiken = true;
        $scope.$digest();

        $scope.cert.hypoglykemier.teckenNedsattHjarnfunktion = false;
        $scope.$digest();

        expect($scope.cert.hypoglykemier.saknarFormagaKannaVarningstecken).toBeUndefined();
        expect($scope.cert.hypoglykemier.allvarligForekomst).toBeUndefined();
        expect($scope.cert.hypoglykemier.allvarligForekomstTrafiken).toBeUndefined();
    });

    it('should reset hidden fields when "allvarligForekomst" is set to false', function() {
        $scope.cert.hypoglykemier.allvarligForekomst = true;
        $scope.cert.hypoglykemier.allvarligForekomstBeskrivning = 'Hello';
        $scope.$digest();

        $scope.cert.hypoglykemier.allvarligForekomst = false;
        $scope.$digest();

        expect($scope.cert.hypoglykemier.allvarligForekomstBeskrivning).toBe('');
    });

    it('should reset hidden fields when "allvarligForekomstTrafiken" is set to false', function() {
        $scope.cert.hypoglykemier.allvarligForekomstTrafiken = true;
        $scope.cert.hypoglykemier.allvarligForekomstTrafikBeskrivning = 'Hello';
        $scope.$digest();

        $scope.cert.hypoglykemier.allvarligForekomstTrafiken = false;
        $scope.$digest();

        expect($scope.cert.hypoglykemier.allvarligForekomstVakenTidObservationstid).toBe('');
    });

    it('should reset hidden fields when "allvarligForekomstVakenTid" is set to false', function() {
        $scope.cert.hypoglykemier.allvarligForekomstVakenTid = true;
        $scope.cert.hypoglykemier.allvarligForekomstVakenTidObservationstid = 'Hello';
        $scope.$digest();

        $scope.cert.hypoglykemier.allvarligForekomstVakenTid = false;
        $scope.$digest();

        expect($scope.cert.hypoglykemier.allvarligForekomstVakenTidObservationstid).toBe('');
    });

    it('should reset hidden fields when "separatOgonlakarintyg" is set to true', function() {
        $scope.cert.syn.separatOgonlakarintyg = false;
        $scope.cert.syn.hoger = { utanKorrektion: '2.0', medKorrektion: '2.0' };
        $scope.cert.syn.vanster = { utanKorrektion: '2.0', medKorrektion: '2.0' };
        $scope.cert.syn.binokulart = { utanKorrektion: '2.0', medKorrektion: '2.0' };
        $scope.cert.syn.diplopi = false;
        $scope.$digest();

        $scope.cert.syn.separatOgonlakarintyg = true;
        $scope.$digest();

        expect($scope.cert.syn.hoger).toBeUndefined();
        expect($scope.cert.syn.vanster).toBeUndefined();
        expect($scope.cert.syn.binokulart).toBeUndefined();
        expect($scope.cert.syn.diplopi).toBeUndefined();
    });

    it('should reset hidden fields when "allvarligForekomstVakenTid" is set to false', function() {
        $scope.cert.hypoglykemier.allvarligForekomstVakenTid = true;
        $scope.cert.hypoglykemier.allvarligForekomstVakenTidObservationstid = 'Hello';
        $scope.$digest();

        $scope.cert.hypoglykemier.allvarligForekomstVakenTid = false;
        $scope.$digest();

        expect($scope.cert.hypoglykemier.allvarligForekomstVakenTidObservationstid).toBe('');
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
