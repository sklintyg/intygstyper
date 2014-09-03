angular.module('ts-bas').controller('ts-bas.EditCertCtrl',
    [ '$anchorScroll', '$location', '$scope', '$window', 'common.CertificateService', 'common.ManageCertView',
        'common.User',
        function($anchorScroll, $location, $scope, $window, CertificateService, ManageCertView, User) {
            'use strict';

            $scope.cert = {};

            $scope.messages = [];
            $scope.isComplete = false;
            $scope.isSigned = false;
            $scope.hasSavedThisSession = false;
            $scope.user = User;

            // init state
            $scope.widgetState = {
                doneLoading: false,
                hasError: false,
                showComplete: false,
                collapsedHeader: false
            };

            $scope.toggleHeader = function() {
                $scope.widgetState.collapsedHeader = !$scope.widgetState.collapsedHeader;
            };

            $scope.toggleShowComplete = function() {
                $scope.widgetState.showComplete = !$scope.widgetState.showComplete;
                if ($scope.widgetState.showComplete) {
                    $scope.save();
                    var old = $location.hash();
                    $location.hash('top');
                    $anchorScroll();
                    // reset to old to keep any additional routing logic from kicking in
                    $location.hash(old);
                }
            };

            $scope.form = {
                'identity': [
                    {label: 'ID-kort *', id: 'ID_KORT'},
                    {label: 'Företagskort eller tjänstekort **', id: 'FORETAG_ELLER_TJANSTEKORT'},
                    {label: 'Svenskt körkort', id: 'KORKORT'},
                    {label: 'Personlig kännedom', id: 'PERS_KANNEDOM'},
                    {label: 'Försäkran enligt 18 kap. 4§ ***', id: 'FORSAKRAN_KAP18'},
                    {label: 'Pass ****', id: 'PASS'}
                ],
                'korkortd': false,
                'behorighet': true,
                'anyTrue': false
            };

            // This is not so pretty, but necessary?
            $scope.$watch('cert', function() {
                if (!$scope.cert ||
                    (!$scope.cert.syn && !$scope.cert.horselBalans && !$scope.cert.funktionsnedsattning &&
                        !$scope.cert.hjartKarl && !$scope.cert.diabetes && !$scope.cert.neurologi &&
                        !$scope.cert.medvetandestorning && !$scope.cert.njurar && !$scope.cert.kognitivt &&
                        !$scope.cert.somnVakenhet && !$scope.cert.narkotikaLakemedel && !$scope.cert.psykiskt &&
                        !$scope.cert.utvecklingsstorning && !$scope.cert.sjukhusvard && !$scope.cert.medicinering)) {
                    $scope.form.anyTrue = false;
                } else if ($scope.cert.syn.synfaltsdefekter === true || $scope.cert.syn.nattblindhet === true ||
                    $scope.cert.syn.progressivOgonsjukdom === true || $scope.cert.syn.diplopi === true ||
                    $scope.cert.syn.nystagmus === true || $scope.cert.horselBalans.balansrubbningar === true ||
                    $scope.cert.horselBalans.svartUppfattaSamtal4Meter === true ||
                    $scope.cert.funktionsnedsattning.funktionsnedsattning === true ||
                    $scope.cert.funktionsnedsattning.otillrackligRorelseformaga === true ||
                    $scope.cert.hjartKarl.hjartKarlSjukdom === true ||
                    $scope.cert.hjartKarl.hjarnskadaEfterTrauma === true ||
                    $scope.cert.hjartKarl.riskfaktorerStroke === true ||
                    $scope.cert.diabetes.harDiabetes === true || $scope.cert.neurologi.neurologiskSjukdom === true ||
                    $scope.cert.medvetandestorning.medvetandestorning === true ||
                    $scope.cert.njurar.nedsattNjurfunktion === true ||
                    $scope.cert.kognitivt.sviktandeKognitivFunktion === true ||
                    $scope.cert.somnVakenhet.teckenSomnstorningar === true ||
                    $scope.cert.narkotikaLakemedel.teckenMissbruk === true ||
                    $scope.cert.narkotikaLakemedel.foremalForVardinsats === true ||
                    $scope.cert.narkotikaLakemedel.provtagningBehovs === true ||
                    $scope.cert.narkotikaLakemedel.lakarordineratLakemedelsbruk ||
                    $scope.cert.psykiskt.psykiskSjukdom === true ||
                    $scope.cert.utvecklingsstorning.psykiskUtvecklingsstorning === true ||
                    $scope.cert.utvecklingsstorning.harSyndrom === true ||
                    $scope.cert.sjukhusvard.sjukhusEllerLakarkontakt === true ||
                    $scope.cert.medicinering.stadigvarandeMedicinering === true) {
                    $scope.form.anyTrue = true;
                } else {
                    $scope.form.anyTrue = false;
                }
            }, true);

            $scope.testerror = false;

            // Input limit handling
            $scope.inputLimits = {
                'funktionsnedsattning': 180,
                'beskrivningRiskfaktorer': 180,
                'medvetandestorning': 180,
                'lakemedelOchDos': 180,
                'medicinering': 180,
                'kommentar': 500,
                'lakareSpecialKompetens': 130,
                'sjukhusvardtidpunkt': 49,
                'sjukhusvardvardinrattning': 45,
                'sjukhusvardanledning': 63
            };

            $scope.$watch('cert.intygAvser.korkortstyp', function(newValue) {
                if (!$scope.cert || !$scope.cert.intygAvser || !$scope.cert.intygAvser.korkortstyp) {
                    return;
                }
                $scope.form.korkortd = false;
                for (var i = 4; i < $scope.cert.intygAvser.korkortstyp.length; i++) {
                    if (newValue[i].selected) {
                        $scope.form.korkortd = true;
                        break;
                    }
                }
            }, true);

            //Make a printable list of Befattningar (which as of yet consists of un-readable codes...)
            $scope.befattningar = '';
            $scope.$watch('user.userContext.befattningar', function(befattningar) {
                if (befattningar === undefined) {
                    return;
                }
                $scope.befattningar = befattningar;
                //                    var result = '';
                //                    for (var i = 0; i < befattningar.length; i++) {
                //                        if (i < befattningar.length-1) {
                //                            result += befattningar[i] + (', ');
                //                        } else {
                //                            result += befattningar[i];
                //                        }
                //                    }
                //                    $scope.befattningar = result;
            }, true);

            //Make a printable list of Specialiteter
            $scope.specialiteter = '';
            $scope.$watch('user.userContext.specialiseringar', function(specialiteter) {
                if (specialiteter === undefined) {
                    return;
                }
                var result = '';
                for (var i = 0; i < specialiteter.length; i++) {
                    if (i < specialiteter.length - 1) {
                        result += specialiteter[i] + (', ');
                    } else {
                        result += specialiteter[i];
                    }
                }
                $scope.specialiteter = result;
            }, true);

            // Get the certificate draft from the server.
            $scope.cert = {};
            ManageCertView.load($scope);

            /**
             * Action to save the certificate draft to the server.
             */
            $scope.save = function() {
                $scope.hasSavedThisSession = true;
                ManageCertView.save($scope);
            };

            /**
             * Action to discard the certificate draft and return to WebCert again.
             */
            $scope.discard = function() {
                ManageCertView.discard($scope);
            };

            /**
             * Action to sign the certificate draft and return to Webcert again.
             */
            $scope.sign = function() {
                ManageCertView.signera($scope, 'ts-bas');
            };

            /**
             * Print draft
             */
            $scope.print = function() {
                ManageCertView.printDraft($scope.cert.id);
            };
        }]);
