angular.module('ts-bas').controller('ts-bas.EditCertCtrl',
    [ '$anchorScroll', '$location', '$scope', '$window', 'common.ManageCertView', 'common.User', 'common.wcFocus', 'common.intygNotifyService',
        function($anchorScroll, $location, $scope, $window, ManageCertView, User, wcFocus, intygNotifyService) {
            'use strict';

            /**********************************************************************************
             * Default state
             **********************************************************************************/

            // init state
            $scope.user = User;
            $scope.focusFirstInput = true;
            $scope.widgetState = {
                doneLoading: false,
                hasError: false,
                showComplete: false,
                collapsedHeader: false,
                hasInfoMissing: false
            };

            // intyg state
            $scope.cert = {};
            $scope.hasSavedThisSession = false;
            $scope.messages = [];
            $scope.isComplete = false;
            $scope.isSigned = false;
            $scope.certMeta = {
                intygId: null,
                intygType: 'ts-bas',
                vidarebefordrad: false
            };

            // form model (extends intyg model where necessary)
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

            /***************************************************************************
             * Private controller support functions
             ***************************************************************************/

            function convertCertToForm($scope) {

                // check if all info is available from HSA. If not, display the info message that someone needs to update it
                if ($scope.cert.intygMetadata.skapadAv.vardenhet.postadress === undefined ||
                    $scope.cert.intygMetadata.skapadAv.vardenhet.postnummer === undefined ||
                    $scope.cert.intygMetadata.skapadAv.vardenhet.postort === undefined ||
                    $scope.cert.intygMetadata.skapadAv.vardenhet.telefonnummer === undefined ||
                    $scope.cert.intygMetadata.skapadAv.vardenhet.postadress === '' ||
                    $scope.cert.intygMetadata.skapadAv.vardenhet.postnummer === '' ||
                    $scope.cert.intygMetadata.skapadAv.vardenhet.postort === '' ||
                    $scope.cert.intygMetadata.skapadAv.vardenhet.telefonnummer === '') {
                    $scope.widgetState.hasInfoMissing = true;
                } else {
                    $scope.widgetState.hasInfoMissing = false;
                }
            }

            /*************************************************************************
             * Ng-change and watches updating behaviour in form (try to get rid of these or at least make them consistent)
             *************************************************************************/

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

            // ---------------------------------------------------------------------------------------------------------

            // Watch changes to the form and make sure that other form elements that are dependent on the changed
            // element is updated correctly.

            $scope.$watch('cert.intygAvser.korkortstyp', function(valdaKorkortstyper) {
                if ($scope.cert.intygAvser && $scope.cert.intygAvser.korkortstyp) {
                    var targetTypes = ['D1', 'D1E', 'D', 'DE', 'TAXI'];
                    var visaKorkortd = false;
                    for (var i = 0; i < valdaKorkortstyper.length; i++) {
                        for (var j = 0; j < targetTypes.length; j++) {
                            if (valdaKorkortstyper[i].type === targetTypes[j] && valdaKorkortstyper[i].selected) {
                                visaKorkortd = true;
                                break;
                            }
                        }
                    }
                    $scope.form.korkortd = visaKorkortd;
                    if (!visaKorkortd) {
                        $scope.cert.horselBalans.svartUppfattaSamtal4Meter = null;
                        $scope.cert.funktionsnedsattning.otillrackligRorelseformaga = null;
                    }
                }
            }, true);
            $scope.$watch('cert.funktionsnedsattning.funktionsnedsattning', function(harFunktionsnedsattning) {
                if (!harFunktionsnedsattning && $scope.cert.funktionsnedsattning) {
                    $scope.cert.funktionsnedsattning.beskrivning = '';
                }
            }, true);
            $scope.$watch('cert.hjartKarl.riskfaktorerStroke', function(foreliggerRiskfaktorerStroke) {
                if (!foreliggerRiskfaktorerStroke && $scope.cert.hjartKarl) {
                    $scope.cert.hjartKarl.beskrivningRiskfaktorer = '';
                }
            }, true);
            $scope.$watch('cert.diabetes.harDiabetes', function(harDiabetes) {
                if (!harDiabetes && $scope.cert.hjartKarl) {
                    $scope.cert.diabetes.diabetesTyp = null;
                }
            }, true);
            $scope.$watch('cert.diabetes.diabetesTyp', function(diabetesTyp) {
                if (diabetesTyp !== 'DIABETES_TYP_2' && $scope.cert.diabetes) {
                    $scope.cert.diabetes.kost = null;
                    $scope.cert.diabetes.tabletter = null;
                    $scope.cert.diabetes.insulin = null;
                }
            }, true);
            $scope.$watch('cert.medvetandestorning.medvetandestorning', function(harHaftMedvetandestorning) {
                if (!harHaftMedvetandestorning && $scope.cert.hjartKarl) {
                    $scope.cert.medvetandestorning.beskrivning = '';
                }
            }, true);
            $scope.$watch('cert.narkotikaLakemedel.teckenMissbruk || cert.narkotikaLakemedel.foremalForVardinsats',
                function(behovsProvtagning) {
                    if (!behovsProvtagning && $scope.cert.narkotikaLakemedel) {
                        $scope.cert.narkotikaLakemedel.provtagningBehovs = null;
                    }
                }, true);
            $scope.$watch('cert.narkotikaLakemedel.lakarordineratLakemedelsbruk', function(anvanderOrdineradNarkotika) {
                if (!anvanderOrdineradNarkotika && $scope.cert.narkotikaLakemedel) {
                    $scope.cert.narkotikaLakemedel.lakemedelOchDos = '';
                }
            }, true);
            $scope.$watch('cert.sjukhusvard.sjukhusEllerLakarkontakt', function(vardatsPaSjukhus) {
                if (!vardatsPaSjukhus && $scope.cert.sjukhusvard) {
                    $scope.cert.sjukhusvard.tidpunkt = '';
                    $scope.cert.sjukhusvard.vardinrattning = '';
                    $scope.cert.sjukhusvard.anledning = '';
                }
            }, true);
            $scope.$watch('cert.medicinering.stadigvarandeMedicinering', function(harStadigvarandeMedicinering) {
                if (!harStadigvarandeMedicinering && $scope.cert.medicinering) {
                    $scope.cert.medicinering.beskrivning = '';
                }
            }, true);

            // ---------------------------------------------------------------------------------------------------------


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

            $scope.$watch('form.behorighet', function (uppfyllerKravForBehorighet) {
                if ($scope.cert.bedomning) {
                    $scope.cert.bedomning.kanInteTaStallning = !uppfyllerKravForBehorighet;
                    if (!uppfyllerKravForBehorighet) {
                        angular.forEach($scope.cert.bedomning.korkortstyp, function (korkortstyp) {
                            korkortstyp.selected = false;
                        });
                    }
                }
            });

            /****************************************************************************
             * Exposed interaction functions to view
             ****************************************************************************/

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

            /**
             * Action to save the certificate draft to the server.
             */
            $scope.save = function() {
                $scope.hasSavedThisSession = true;
                ManageCertView.save($scope, $scope.certMeta.intygType);
            };

            /**
             * Action to discard the certificate draft and return to WebCert again.
             */
            $scope.discard = function() {
                ManageCertView.discard($scope, $scope.certMeta.intygType);
            };

            /**
             * Action to sign the certificate draft and return to Webcert again.
             */
            $scope.sign = function() {
                ManageCertView.signera($scope, $scope.certMeta.intygType);
            };

            /**
             * Print draft
             */
            $scope.print = function() {
                ManageCertView.printDraft($scope.cert.id, $scope.certMeta.intygType);
            };

            /**
             * Handle vidarebefordra dialog
             *
             * @param cert
             */
            $scope.openMailDialog = function() {
                intygNotifyService.forwardIntyg($scope.certMeta, $scope.widgetState);
            };

            $scope.onVidarebefordradChange = function() {
                intygNotifyService.onForwardedChange($scope.certMeta, $scope.widgetState);
            };

            /**************************************************************************
             * Load certificate and setup form
             **************************************************************************/

            // Get the certificate draft from the server.
            ManageCertView.load($scope, $scope.certMeta.intygType, function(cert) {
                // Decorate intygspecific default data
                $scope.cert = cert;
                convertCertToForm($scope);
                wcFocus('firstInput');
            });

        }]);
