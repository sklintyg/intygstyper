angular.module('ts-diabetes').controller('ts-diabetes.EditCertCtrl',
    ['$anchorScroll', '$location', '$log', '$scope', '$window', 'common.ManageCertView', 'common.UserModel',
        'common.wcFocus', 'common.intygNotifyService',
        function($anchorScroll, $location, $log, $scope, $window, ManageCertView, UserModel, wcFocus, intygNotifyService) {
            'use strict';

            /**********************************************************************************
             * Default state
             **********************************************************************************/

                // Page state
            $scope.user = UserModel;
            $scope.focusFirstInput = true;
            $scope.widgetState = {
                doneLoading: false,
                hasError: false,
                showComplete: false,
                collapsedHeader: false,
                hsaInfoMissing: false
            };

            // Intyg state
            $scope.cert = {};
            $scope.messages = [];
            $scope.isComplete = false;
            $scope.isSigned = false;
            $scope.hasSavedThisSession = false;
            $scope.certMeta = {
                intygId: null,
                intygType: 'ts-diabetes',
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
                'korkorttypselected': false,
                'behorighet': true
            };

            $scope.testerror = false;

            /******************************************************************************************
             * Private support functions
             ******************************************************************************************/

            function convertCertToForm($scope) {

                // check if all info is available from HSA. If not, display the info message that someone needs to update it
                if ($scope.cert.grundData.skapadAv.vardenhet.postadress === undefined ||
                    $scope.cert.grundData.skapadAv.vardenhet.postnummer === undefined ||
                    $scope.cert.grundData.skapadAv.vardenhet.postort === undefined ||
                    $scope.cert.grundData.skapadAv.vardenhet.telefonnummer === undefined ||
                    $scope.cert.grundData.skapadAv.vardenhet.postadress === '' ||
                    $scope.cert.grundData.skapadAv.vardenhet.postnummer === '' ||
                    $scope.cert.grundData.skapadAv.vardenhet.postort === '' ||
                    $scope.cert.grundData.skapadAv.vardenhet.telefonnummer === '') {
                    $scope.widgetState.hasInfoMissing = true;
                } else {
                    $scope.widgetState.hasInfoMissing = false;
                }
            }

            /**
             * Convert form data to internal model
             */
            function convertFormToCert() {

                // 2g. if entered date is valid, convert it to string so backend validation is happy.
                // otherwise leave it as an invalid Date so backend sends back a validation error
                $scope.cert.hypoglykemier.allvarligForekomstVakenTidObservationstid =
                    $scope.certForm.allvarligForekomstVakenTidObservationstid.$viewValue;
            }

            /******************************************************************************************
             * Watches
             ******************************************************************************************/

                // Watch changes to the form and make sure that other form elements that are dependent on the changed
                // element is updated correctly.

            $scope.$watch('cert.intygAvser.korkortstyp', function(valdaKorkortstyper) {
                if ($scope.cert.intygAvser && $scope.cert.intygAvser.korkortstyp) {
                    var targetTypes = ['C1', 'C1E', 'C', 'CE', 'D1', 'D1E', 'D', 'DE', 'TAXI'];
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
                        $scope.cert.hypoglykemier.egenkontrollBlodsocker = null;
                        $scope.cert.hypoglykemier.allvarligForekomstVakenTid = null;
                        $scope.cert.bedomning.lamplighetInnehaBehorighet = null;
                    }
                }
            }, true);
            $scope.$watch('cert.diabetes.insulin', function(behandlasMedInsulin) {
                if (!behandlasMedInsulin && $scope.cert.diabetes) {
                    $scope.cert.diabetes.insulinBehandlingsperiod = null;
                }
            }, true);
            $scope.$watch('cert.hypoglykemier.teckenNedsattHjarnfunktion',
                function(forekommerTeckenNedsattHjarnfunktion) {
                    if (!forekommerTeckenNedsattHjarnfunktion && $scope.cert.hypoglykemier) {
                        $scope.cert.hypoglykemier.saknarFormagaKannaVarningstecken = null;
                        $scope.cert.hypoglykemier.allvarligForekomst = null;
                        $scope.cert.hypoglykemier.allvarligForekomstTrafiken = null;
                    }
                }, true);
            $scope.$watch('cert.hypoglykemier.allvarligForekomst', function(haftAllvarligForekomst) {
                if (!haftAllvarligForekomst && $scope.cert.hypoglykemier) {
                    $scope.cert.hypoglykemier.allvarligForekomstBeskrivning = '';
                }
            }, true);
            $scope.$watch('cert.hypoglykemier.allvarligForekomstTrafiken', function(haftAllvarligForekomstTrafiken) {
                if (!haftAllvarligForekomstTrafiken && $scope.cert.hypoglykemier) {
                    $scope.cert.hypoglykemier.allvarligForekomstTrafikBeskrivning = '';
                }
            }, true);
            $scope.$watch('cert.hypoglykemier.allvarligForekomstVakenTid', function(haftAllvarligForekomstVakenTid) {
                if (!haftAllvarligForekomstVakenTid && $scope.cert.hypoglykemier) {
                    $scope.cert.hypoglykemier.allvarligForekomstVakenTidObservationstid = '';
                }
            }, true);
            $scope.$watch('cert.syn.separatOgonlakarintyg', function(separatOgonlakarintyg) {
                if (separatOgonlakarintyg && $scope.cert.syn) {
                    $scope.cert.syn.synfaltsprovningUtanAnmarkning = undefined;
                    $scope.cert.syn.hoger = undefined;
                    $scope.cert.syn.vanster = undefined;
                    $scope.cert.syn.binokulart = undefined;
                    $scope.cert.syn.diplopi = undefined;
                }
            }, true);
            $scope.$watch('form.behorighet', function(uppfyllerKravForBehorighet) {
                if ($scope.cert.bedomning) {
                    $scope.cert.bedomning.kanInteTaStallning = !uppfyllerKravForBehorighet;
                    if (!uppfyllerKravForBehorighet) {
                        angular.forEach($scope.cert.bedomning.korkortstyp, function(korkortstyp) {
                            korkortstyp.selected = false;
                        });
                    }
                }
            });

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

            /******************************************************************************************
             * Exposed interaction
             ******************************************************************************************/

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
            ManageCertView.load($scope.certMeta.intygType, function(cert) {
                // Decorate intygspecific default data
                $scope.cert = cert;
                convertCertToForm($scope);
                wcFocus('firstInput');
            });

            $scope.$on('convertFormToCert', convertFormToCert);

        }]);
