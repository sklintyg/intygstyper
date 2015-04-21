angular.module('ts-diabetes').controller('ts-diabetes.UtkastController',
    ['$anchorScroll', '$location', '$log', '$q', '$rootScope', '$scope', '$timeout', '$window', 'common.ManageCertView', 'common.UserModel',
        'common.intygNotifyService', 'ts-diabetes.Domain.IntygModel', 'ts-diabetes.UtkastController.ViewStateService', 'common.DateUtilsService',
        function($anchorScroll, $location, $log, $q, $rootScope, $scope, $timeout, $window, ManageCertView, UserModel,
                 intygNotifyService, IntygModel, viewState, dateUtils) {
            'use strict';

            /**********************************************************************************
             * Default state
             **********************************************************************************/

            viewState.reset();
            $scope.viewState = viewState;
            $scope.notifieringVidarebefordrad = viewState.draftModel.vidarebefordrad; // temporary hack. maybe move this to viewState?
            $scope.cert = viewState.intygModel; // keep cert as a shortcut to viewState.intyModel?

            // Page state
            $scope.user = UserModel;
            $scope.focusFirstInput = true; // TODO: to common viewstate
            $scope.tomorrowDate = moment().subtract(1, 'days').format('YYYY-MM-DD'); // TODO: to viewstate

            // Intyg state
            $scope.tomorrowDate = moment().format('YYYY-MM-DD');

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
                if (viewState.intygModel.grundData.skapadAv.vardenhet.postadress === undefined ||
                    viewState.intygModel.grundData.skapadAv.vardenhet.postnummer === undefined ||
                    viewState.intygModel.grundData.skapadAv.vardenhet.postort === undefined ||
                    viewState.intygModel.grundData.skapadAv.vardenhet.telefonnummer === undefined ||
                    viewState.intygModel.grundData.skapadAv.vardenhet.postadress === '' ||
                    viewState.intygModel.grundData.skapadAv.vardenhet.postnummer === '' ||
                    viewState.intygModel.grundData.skapadAv.vardenhet.postort === '' ||
                    viewState.intygModel.grundData.skapadAv.vardenhet.telefonnummer === '') {
                    viewState.common.hsaInfoMissing = true;
                } else {
                    viewState.common.hsaInfoMissing = false;
                }
            }

            /**
             * Convert form data to internal model
             */
            function convertFormToCert() {

                // 2g. if entered date is valid, convert it to string so backend validation is happy.
                // otherwise leave it as an invalid Date so backend sends back a validation error
                if($scope.certForm && $scope.certForm.allvarligForekomstVakenTidObservationstid){
                    viewState.intygModel.hypoglykemier.allvarligForekomstVakenTidObservationstid =
                        $scope.certForm.allvarligForekomstVakenTidObservationstid.$viewValue;
                }

                if(!viewState.intygModel.hypoglykemier.teckenNedsattHjarnfunktion) {
                    viewState.intygModel.hypoglykemier.saknarFormagaKannaVarningstecken = undefined;
                    viewState.intygModel.hypoglykemier.allvarligForekomst = undefined;
                    viewState.intygModel.hypoglykemier.allvarligForekomstTrafiken = undefined;
                }
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
                        $scope.cert.updateToAttic('hypoglykemier.egenkontrollBlodsocker');
                        $scope.cert.hypoglykemier.egenkontrollBlodsocker = undefined;
                        $scope.cert.hypoglykemier.allvarligForekomstVakenTid = undefined;
                        $scope.cert.bedomning.lamplighetInnehaBehorighet = undefined;
                    } else {
                        $scope.cert.restoreFromAttic('hypoglykemier.egenkontrollBlodsocker');
                    }
                }
            }, true);
            $scope.$watch('cert.diabetes.insulin', function(behandlasMedInsulin) {
                if (!behandlasMedInsulin && $scope.cert.diabetes) {
                    $scope.cert.updateToAttic('diabetes.insulinBehandlingsperiod');
                    $scope.cert.diabetes.insulinBehandlingsperiod = null;

                } else {
                    $scope.cert.restoreFromAttic('diabetes.insulinBehandlingsperiod');
                }
            }, true);
            $scope.$watch('cert.hypoglykemier.teckenNedsattHjarnfunktion',
                function(forekommerTeckenNedsattHjarnfunktion) {
                    if (!forekommerTeckenNedsattHjarnfunktion && $scope.cert.hypoglykemier) {
                        $scope.cert.hypoglykemier.saknarFormagaKannaVarningstecken = undefined;
                        $scope.cert.hypoglykemier.allvarligForekomst = undefined;
                        $scope.cert.hypoglykemier.allvarligForekomstTrafiken = undefined;
                    }
                }, true);
            $scope.$watch('cert.hypoglykemier.allvarligForekomst', function(haftAllvarligForekomst) {
                if (!haftAllvarligForekomst && $scope.cert.hypoglykemier) {
                    $scope.cert.hypoglykemier.allvarligForekomstBeskrivning = undefined;
                }
            }, true);
            $scope.$watch('cert.hypoglykemier.allvarligForekomstTrafiken', function(haftAllvarligForekomstTrafiken) {
                if (!haftAllvarligForekomstTrafiken && $scope.cert.hypoglykemier) {
                    $scope.cert.hypoglykemier.allvarligForekomstTrafikBeskrivning = undefined;
                }
            }, true);

            // hypoglykemier.allvarligForekomstVakenTidObservationstid
            var addDateParser = function(form){
                if(form && form.allvarligForekomstVakenTidObservationstid){
                    var formElement = form.allvarligForekomstVakenTidObservationstid;
                    formElement.$parsers.push(function(viewValue) {
                        $scope.cert.hypoglykemier.allvarligForekomstVakenTidObservationstid = formElement.$viewValue;
                        return viewValue;
                    });
                }
            };
            $scope.$watch('cert.hypoglykemier.allvarligForekomstVakenTid', function(haftAllvarligForekomstVakenTid) {
                if (!haftAllvarligForekomstVakenTid && $scope.cert.hypoglykemier) {
                    $scope.cert.hypoglykemier.allvarligForekomstVakenTidObservationstid = undefined;
                }
            }, true);
            // ---

            $scope.$watch('cert.syn.separatOgonlakarintyg', function(separatOgonlakarintyg) {
                if (separatOgonlakarintyg && $scope.cert.syn) {
                    viewState.intygModel.clear('syn.synfaltsprovningUtanAnmarkning');
                    viewState.intygModel.clear('syn.hoger');
                    viewState.intygModel.clear('syn.vanster');
                    viewState.intygModel.clear('syn.binokulart');
                    viewState.intygModel.clear('syn.diplopi');
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

                var utkastNotifyRequest = {
                    intygId : viewState.intygModel.id,
                    intygType: viewState.common.intyg.type,
                    vidarebefordrad: viewState.draftModel.vidarebefordrad
                };
                intygNotifyService.forwardIntyg(utkastNotifyRequest);
            };

            $scope.onVidarebefordradChange = function() {
                var utkastNotifyRequest = {
                    intygId : viewState.intygModel.id,
                    intygType: viewState.common.intyg.type,
                    vidarebefordrad: viewState.draftModel.vidarebefordrad
                };
                intygNotifyService.onForwardedChange(utkastNotifyRequest);
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

                //convertFormToCert();// Move into prepare later

                var intygSaveRequest = {
                    intygsId : viewState.intygModel.id,
                    intygsTyp : viewState.common.intyg.type,
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

            $scope.$watch('viewState.common.doneLoading', function(doneLoading){
                if(doneLoading === true){
                    addDateParser($scope.certForm);
                }
            });



        }]);
