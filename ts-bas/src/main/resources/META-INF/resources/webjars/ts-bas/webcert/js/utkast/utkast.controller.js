angular.module('ts-bas').controller('ts-bas.UtkastController',
    [ '$anchorScroll', '$location', '$q', '$rootScope', '$scope', '$timeout', '$window',
        'common.ManageCertView', 'common.UserModel',
        'common.intygNotifyService', 'ts-bas.Domain.IntygModel',
        'ts-bas.UtkastController.ViewStateService',
        function($anchorScroll, $location, $q, $rootScope, $scope, $timeout, $window,
            ManageCertView, UserModel, intygNotifyService, IntygModel, viewState) {
            'use strict';

            /**********************************************************************************
             * Default state
             **********************************************************************************/

            viewState.reset();
            $scope.viewState = viewState;
            $scope.notifieringVidarebefordrad = viewState.draftModel.vidarebefordrad; // temporary hack. maybe move this to viewState?
            $scope.cert = viewState.intygModel; // Set default intyg state from model

            $scope.user = UserModel;

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
                'behorighet': 'BEDOMNING',
                'anyTrue': false
            };

            /***************************************************************************
             * Private controller support functions
             ***************************************************************************/

            function updateHSAInfoMessage() {

                // check if all info is available from HSA. If not, display the info message that someone needs to update it
                if (viewState.intygModel.grundData.skapadAv.vardenhet.postadress === undefined ||
                    viewState.intygModel.grundData.skapadAv.vardenhet.postnummer === undefined ||
                    viewState.intygModel.grundData.skapadAv.vardenhet.postort === undefined ||
                    viewState.intygModel.grundData.skapadAv.vardenhet.telefonnummer === undefined ||
                    viewState.intygModel.grundData.skapadAv.vardenhet.postadress === '' ||
                    viewState.intygModel.grundData.skapadAv.vardenhet.postnummer === '' ||
                    viewState.intygModel.grundData.skapadAv.vardenhet.postort === '' ||
                    viewState.intygModel.grundData.skapadAv.vardenhet.telefonnummer === '') {
                    viewState.intygModel.common.hsaInfoMissing = true;
                } else {
                    viewState.intygModel.common.hsaInfoMissing = false;
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

            $scope.$watch('form.behorighet', function (behorighet) {
                if (!$scope.cert.bedomning) {
                    $scope.cert.bedomning = {
                        korkortstyp: {},
                        kanInteTaStallning: false
                    };
                }

                if (behorighet === 'KANINTETASTALLNING') {
                    $scope.cert.bedomning.kanInteTaStallning = true;
                    angular.forEach($scope.cert.bedomning.korkortstyp, function (korkortstyp) {
                        korkortstyp.selected = false;
                    });
                } else if(behorighet === 'BEDOMNING') {
                    $scope.cert.bedomning.kanInteTaStallning = false;
                } else {
                    $scope.cert.bedomning.kanInteTaStallning = undefined;
                }
            });

            /****************************************************************************
             * Exposed interaction functions to view
             ****************************************************************************/

            /**
             * Handle vidarebefordra dialog
             *
             * @param cert
             */
            $scope.openMailDialog = function() {

                var certMeta = {
                    intygId: viewState.intygModel.id,
                    intygType: viewState.common.intyg.type,
                    vidarebefordrad: viewState.draftModel.vidarebefordrad
                };

                intygNotifyService.forwardIntyg(certMeta);
            };

            $scope.onVidarebefordradChange = function() {
                var certMeta = {
                    intygId: viewState.intygModel.id,
                    intygType: viewState.common.intyg.type,
                    vidarebefordrad: viewState.draftModel.vidarebefordrad
                };
                intygNotifyService.onForwardedChange(certMeta);
            };

            $scope.sign = function() {
                ManageCertView.signera(viewState.common.intyg.type);
            };

            /**************************************************************************
             * Load certificate and setup form
             **************************************************************************/

            // Get the certificate draft from the server.
            ManageCertView.load(viewState.common.intyg.type, viewState.draftModel);

            $scope.$on('saveRequest', function($event, deferred) {
                // Mark form as saved, will be marked as not saved if saving fails.
                $scope.certForm.$setPristine();
//                $scope.cert.prepare();

                var intygSaveRequest = {
                    intygsId      : viewState.intygModel.id,
                    intygsTyp     : viewState.common.intyg.type,
                    cert          : viewState.intygModel.toSendModel(),
                    saveComplete  : $q.defer()
                };

                intygSaveRequest.saveComplete.promise.then(function(result) {

                    // save success
                    viewState.common.validationMessages = result.validationMessages;
                    viewState.common.validationMessagesGrouped = result.validationMessagesGrouped;
                    viewState.common.error.saveErrorMessageKey = null;

                }, function(result) {
                    // save failed
                    $scope.certForm.$setDirty();
                    viewState.common.error.saveErrorMessageKey = result.errorMessageKey;
                });

                deferred.resolve(intygSaveRequest);
            });

        }]);
