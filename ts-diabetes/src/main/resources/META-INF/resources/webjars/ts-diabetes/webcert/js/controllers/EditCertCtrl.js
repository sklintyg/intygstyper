angular.module('ts-diabetes').controller('ts-diabetes.EditCertCtrl',
    ['$anchorScroll', '$location', '$log', '$q', '$rootScope', '$scope', '$timeout', '$window', '$filter', 'common.ManageCertView', 'common.UserModel',
        'common.wcFocus', 'common.intygNotifyService', 'common.IntygEditViewStateService', 'common.DateUtilsService',
        function($anchorScroll, $location, $log, $q, $rootScope, $scope, $timeout, $window, $filter,
            ManageCertView, UserModel, wcFocus, intygNotifyService, viewState, dateUtils) {
            'use strict';

            /**********************************************************************************
             * Default state
             **********************************************************************************/

            viewState.intyg.typ = 'ts-diabetes';

            // Page state
            $scope.user = UserModel;
            $scope.focusFirstInput = true;
            $scope.viewState = {
                common : viewState
            };

            // Intyg state
            $scope.cert = {};
            $scope.certMeta = {
                intygId: null,
                intygType: 'ts-diabetes',
                vidarebefordrad: false
            };

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
                if ($scope.cert.grundData.skapadAv.vardenhet.postadress === undefined ||
                    $scope.cert.grundData.skapadAv.vardenhet.postnummer === undefined ||
                    $scope.cert.grundData.skapadAv.vardenhet.postort === undefined ||
                    $scope.cert.grundData.skapadAv.vardenhet.telefonnummer === undefined ||
                    $scope.cert.grundData.skapadAv.vardenhet.postadress === '' ||
                    $scope.cert.grundData.skapadAv.vardenhet.postnummer === '' ||
                    $scope.cert.grundData.skapadAv.vardenhet.postort === '' ||
                    $scope.cert.grundData.skapadAv.vardenhet.telefonnummer === '') {
                    $scope.viewState.hasInfoMissing = true;
                } else {
                    $scope.viewState.hasInfoMissing = false;
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

                if(!$scope.cert.hypoglykemier.teckenNedsattHjarnfunktion) {
                    $scope.cert.hypoglykemier.saknarFormagaKannaVarningstecken = undefined;
                    $scope.cert.hypoglykemier.allvarligForekomst = undefined;
                    $scope.cert.hypoglykemier.allvarligForekomstTrafiken = undefined;
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
                        $scope.cert.hypoglykemier.egenkontrollBlodsocker = undefined;
                        $scope.cert.hypoglykemier.allvarligForekomstVakenTid = undefined;
                        $scope.cert.bedomning.lamplighetInnehaBehorighet = undefined;
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
                        $scope.cert.hypoglykemier.saknarFormagaKannaVarningstecken = undefined;
                        $scope.cert.hypoglykemier.allvarligForekomst = undefined;
                        $scope.cert.hypoglykemier.allvarligForekomstTrafiken = undefined;
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
                intygNotifyService.forwardIntyg($scope.certMeta, $scope.viewState);
            };

            $scope.onVidarebefordradChange = function() {
                intygNotifyService.onForwardedChange($scope.certMeta, $scope.viewState);
            };

            $scope.sign = function() {
                ManageCertView.signera($scope.certMeta.intygType);
            };

            /**************************************************************************
             * Load certificate and setup form
             **************************************************************************/

                // Get the certificate draft from the server.
            ManageCertView.load($scope.certMeta.intygType, function(cert) {
                // Decorate intygspecific default data
                $scope.cert = cert.content;
                $scope.certMeta.intygId = cert.content.id;
                convertCertToForm($scope);

                if ($scope.certForm.allvarligForekomstVakenTidObservationstid.$parsers.length > 1) {
                    $scope.certForm.allvarligForekomstVakenTidObservationstid.$parsers.shift();
                }

                $scope.certForm.allvarligForekomstVakenTidObservationstid.$parsers.unshift(function (viewValue) {

                    viewValue = dateUtils.convertDateToISOString(viewValue);

                    var transformedInput;
                    if(dateUtils.isDate(viewValue)){
                        transformedInput = $filter('date')(viewValue, 'yyyy-MM-dd', 'GMT+0100');
                    }

                    if (transformedInput !== viewValue) {
                        $scope.certForm.allvarligForekomstVakenTidObservationstid.$setViewValue(transformedInput);
                        $scope.certForm.allvarligForekomstVakenTidObservationstid.$render();
                    }

                    return transformedInput;
                });

                if ($scope.certForm.allvarligForekomstVakenTidObservationstid.$formatters.length > 0) {
                    $scope.certForm.allvarligForekomstVakenTidObservationstid.$formatters.shift();
                }

                $scope.certForm.allvarligForekomstVakenTidObservationstid.$formatters.unshift(function (modelValue) {
                    if (modelValue) {
                        // convert date to iso
                        modelValue = dateUtils.convertDateToISOString(modelValue);
                    }
                    return modelValue;
                });

                viewState.isSigned = cert.status === 'SIGNED';
                viewState.intyg.isComplete = cert.status === 'SIGNED' || cert.status === 'DRAFT_COMPLETE';

                $timeout(function() {
                    wcFocus('firstInput');
                    viewState.doneLoading = true;
                }, 10);
            });

            $scope.$on('saveRequest', function($event, deferred) {
                // Mark form as saved, will be marked as not saved if saving fails.
                $scope.certForm.$setPristine();
//                $scope.cert.prepare();

                convertFormToCert();// Move into prepare later

                var intygSaveRequest = {
                    intygsId      : $scope.certMeta.intygId,
                    intygsTyp     : $scope.certMeta.intygType,
                    cert          : $scope.cert,
                    saveComplete  : $q.defer()
                };

                intygSaveRequest.saveComplete.promise.then(function(result) {

                    // save success
                    viewState.validationMessages = result.validationMessages;
                    viewState.validationMessagesGrouped = result.validationMessagesGrouped;
                    viewState.error.saveErrorMessageKey = null;

                }, function(result) {
                    // save failed
                    $scope.certForm.$setDirty();
                    viewState.error.saveErrorMessageKey = result.errorMessageKey;
                });

                deferred.resolve(intygSaveRequest);
            });


        }]);
