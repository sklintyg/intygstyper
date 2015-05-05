angular.module('ts-bas').controller('ts-bas.UtkastController',
    [ '$anchorScroll', '$location', '$q', '$rootScope', '$scope', '$timeout', '$window',
        'common.ManageCertView', 'common.UserModel',
        'common.intygNotifyService', 'ts-bas.Domain.IntygModel',
        'ts-bas.UtkastController.ViewStateService', 'common.anchorScrollService',
        function($anchorScroll, $location, $q, $rootScope, $scope, $timeout, $window,
            ManageCertView, UserModel, intygNotifyService, IntygModel, viewState, anchorScrollService) {
            'use strict';

            /**********************************************************************************
             * Default state
             **********************************************************************************/

            // TODO: make all this one line: $scope.viewState = viewState.reset();
            viewState.reset();
            $scope.viewState = viewState;
            $scope.notifieringVidarebefordrad = viewState.draftModel.vidarebefordrad; // temporary hack. let view take from viewstate
            $scope.cert = viewState.intygModel; // TODO: remove cert completely

            $scope.user = UserModel;

            /***************************************************************************
             * Private controller support functions
             ***************************************************************************/
            function isHighlevelKorkortChecked(valdaKorkortstyper) {
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
                return visaKorkortd;
            }

            /*************************************************************************
             * Ng-change and watches updating behaviour in form (try to get rid of these or at least make them consistent)
             *************************************************************************/

            $scope.$watch('cert', function() {
                viewState.updateKravYtterligareUnderlag();
            }, true);

            // ---------------------------------------------------------------------------------------------------------

            // Watch changes to the form and make sure that other form elements that are dependent on the changed
            // element is updated correctly.

            $scope.$watch('cert.intygAvser.korkortstyp', function(valdaKorkortstyper) {
                if ($scope.cert.intygAvser && $scope.cert.intygAvser.korkortstyp) {
                    var prev = $scope.viewState.korkortd;
                    $scope.viewState.korkortd = isHighlevelKorkortChecked(valdaKorkortstyper);
                    if ($scope.viewState.korkortd && $scope.viewState.korkortd !== prev) {
                        $scope.cert.restoreFromAttic('horselBalans.svartUppfattaSamtal4Meter');
                        $scope.cert.restoreFromAttic('funktionsnedsattning.otillrackligRorelseformaga');
                    } else {
                        $scope.cert.updateToAttic('horselBalans.svartUppfattaSamtal4Meter');
                        $scope.cert.updateToAttic('funktionsnedsattning.otillrackligRorelseformaga');
                        $scope.cert.horselBalans.svartUppfattaSamtal4Meter = undefined;
                        $scope.cert.funktionsnedsattning.otillrackligRorelseformaga = undefined;
                    }
                }
            }, true);
            $scope.$watch('cert.funktionsnedsattning.funktionsnedsattning', function(harFunktionsnedsattning) {
                if (!harFunktionsnedsattning && $scope.cert.funktionsnedsattning) {
                    $scope.cert.updateToAttic('funktionsnedsattning.beskrivning');
                    $scope.cert.funktionsnedsattning.beskrivning = '';
                } else {
                    $scope.cert.restoreFromAttic('funktionsnedsattning.beskrivning');
                }
            }, true);
            $scope.$watch('cert.hjartKarl.riskfaktorerStroke', function(foreliggerRiskfaktorerStroke) {
                if (!foreliggerRiskfaktorerStroke && $scope.cert.hjartKarl) {
                    $scope.cert.updateToAttic('hjartKarl.beskrivningRiskfaktorer');
                    $scope.cert.hjartKarl.beskrivningRiskfaktorer = '';
                } else {
                    $scope.cert.restoreFromAttic('hjartKarl.beskrivningRiskfaktorer');
                }
            }, true);
            $scope.$watch('cert.diabetes.harDiabetes', function(harDiabetes) {
                if (!harDiabetes && $scope.cert.hjartKarl) {
                    $scope.cert.updateToAttic('diabetes.diabetesTyp');
                    $scope.cert.diabetes.diabetesTyp = undefined;
                } else {
                    $scope.cert.restoreFromAttic('diabetes.diabetesTyp');
                }
            }, true);
            $scope.$watch('cert.diabetes.diabetesTyp', function(diabetesTyp) {
                if (diabetesTyp !== 'DIABETES_TYP_2' && $scope.cert.diabetes) {
                    $scope.cert.updateToAttic('diabetes.kost');
                    $scope.cert.updateToAttic('diabetes.tabletter');
                    $scope.cert.updateToAttic('diabetes.insulin');
                    $scope.cert.diabetes.kost = undefined;
                    $scope.cert.diabetes.tabletter = undefined;
                    $scope.cert.diabetes.insulin = undefined;
                } else {
                    $scope.cert.restoreFromAttic('diabetes.kost');
                    $scope.cert.restoreFromAttic('diabetes.tabletter');
                    $scope.cert.restoreFromAttic('diabetes.insulin');
                }
            }, true);
            $scope.$watch('cert.medvetandestorning.medvetandestorning', function(harHaftMedvetandestorning) {
                if (!harHaftMedvetandestorning && $scope.cert.hjartKarl) {
                    $scope.cert.updateToAttic('medvetandestorning.beskrivning');
                    $scope.cert.medvetandestorning.beskrivning = '';
                } else {
                    $scope.cert.restoreFromAttic('medvetandestorning.beskrivning');
                }
            }, true);

            $scope.$watch('cert.narkotikaLakemedel.teckenMissbruk || cert.narkotikaLakemedel.foremalForVardinsats',
                function(shown) {
                    if (shown) {
                        $scope.cert.restoreFromAttic('narkotikaLakemedel.provtagningBehovs');
                    } else {
                        $scope.cert.updateToAttic('narkotikaLakemedel.provtagningBehovs');
                        $scope.cert.narkotikaLakemedel.provtagningBehovs = undefined;
                    }
                }, true);

            $scope.$watch('cert.narkotikaLakemedel.lakarordineratLakemedelsbruk', function(anvanderOrdineradNarkotika) {
                if (!anvanderOrdineradNarkotika && $scope.cert.narkotikaLakemedel) {
                    $scope.cert.updateToAttic('narkotikaLakemedel.lakemedelOchDos');
                    $scope.cert.narkotikaLakemedel.lakemedelOchDos = '';
                } else {
                    $scope.cert.restoreFromAttic('narkotikaLakemedel.lakemedelOchDos');
                }
            }, true);
            $scope.$watch('cert.sjukhusvard.sjukhusEllerLakarkontakt', function(vardatsPaSjukhus) {
                if (!vardatsPaSjukhus && $scope.cert.sjukhusvard) {
                    $scope.cert.updateToAttic('sjukhusvard.tidpunkt');
                    $scope.cert.updateToAttic('sjukhusvard.vardinrattning');
                    $scope.cert.updateToAttic('sjukhusvard.anledning');
                    $scope.cert.sjukhusvard.tidpunkt = '';
                    $scope.cert.sjukhusvard.vardinrattning = '';
                    $scope.cert.sjukhusvard.anledning = '';
                } else {
                    $scope.cert.restoreFromAttic('sjukhusvard.tidpunkt');
                    $scope.cert.restoreFromAttic('sjukhusvard.vardinrattning');
                    $scope.cert.restoreFromAttic('sjukhusvard.anledning');
                }
            }, true);
            $scope.$watch('cert.medicinering.stadigvarandeMedicinering', function(harStadigvarandeMedicinering) {
                if (!harStadigvarandeMedicinering && $scope.cert.medicinering) {
                    $scope.cert.updateToAttic('medicinering.beskrivning');
                    $scope.cert.medicinering.beskrivning = '';
                } else {
                    $scope.cert.restoreFromAttic('medicinering.beskrivning');
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

            $scope.$watch('viewState.behorighet', function (behorighet) {
                if (!$scope.cert.bedomning) {
                    $scope.cert.bedomning = {
                        korkortstyp: {},
                        kanInteTaStallning: false
                    };
                }

                if (behorighet === 'KANINTETASTALLNING') {
                    $scope.cert.bedomning.kanInteTaStallning = true;
                    $scope.cert.updateToAttic('bedomning.korkortstyp');
                    $scope.cert.clear('bedomning.korkortstyp');
                } else if(behorighet === 'BEDOMNING') {
                    $scope.cert.restoreFromAttic('bedomning.korkortstyp');
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
                ManageCertView.signera(viewState.common.intyg.type, viewState.draftModel.version);
            };

            $scope.scrollTo = function(message) {
                anchorScrollService.scrollTo('anchor.' + message);
            };

            /**************************************************************************
             * Load certificate and setup form
             **************************************************************************/

            // Get the certificate draft from the server.
            ManageCertView.load(viewState);

            $scope.$on('saveRequest', function($event, saveDefered) {
                $scope.certForm.$setPristine();
                var intygState = {
                    viewState     : viewState,
                    formFail : function(){
                        $scope.certForm.$setDirty();
                    }
                };
                saveDefered.resolve(intygState);
            });

            $scope.$on('$destroy', function() {
                if(!$scope.certForm.$dirty){
                    $scope.destroyList();
                }
            });

            $scope.destroyList = function(){
                viewState.clearModel();
            };

        }]);
