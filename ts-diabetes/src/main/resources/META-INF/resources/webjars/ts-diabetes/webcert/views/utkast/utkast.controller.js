angular.module('ts-diabetes').controller('ts-diabetes.UtkastController',
    ['$location', '$log', '$q', '$rootScope', '$scope', '$timeout', '$window', 'common.ManageCertView', 'common.UserModel',
        'common.intygNotifyService', 'ts-diabetes.Domain.IntygModel', 'ts-diabetes.UtkastController.ViewStateService', 'common.DateUtilsService',
        'common.anchorScrollService',
        function($location, $log, $q, $rootScope, $scope, $timeout, $window, ManageCertView, UserModel,
                 intygNotifyService, IntygModel, viewState, dateUtils, anchorScrollService) {
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

            /******************************************************************************************
             * Private support functions
             ******************************************************************************************/

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
                    $scope.viewState.korkortd = visaKorkortd;
                    if (visaKorkortd) {
                        $scope.cert.restoreFromAttic('hypoglykemier.egenkontrollBlodsocker');
                        $scope.cert.restoreFromAttic('hypoglykemier.allvarligForekomstVakenTid');
                        $scope.cert.restoreFromAttic('hypoglykemier.allvarligForekomstVakenTidObservationstid');
                        $scope.cert.restoreFromAttic('bedomning.lamplighetInnehaBehorighet');
                    } else {
                        $scope.cert.updateToAttic('hypoglykemier.egenkontrollBlodsocker');
                        $scope.cert.updateToAttic('hypoglykemier.allvarligForekomstVakenTid');
                        $scope.cert.updateToAttic('hypoglykemier.allvarligForekomstVakenTidObservationstid');
                        $scope.cert.updateToAttic('bedomning.lamplighetInnehaBehorighet');
                        $scope.cert.hypoglykemier.egenkontrollBlodsocker = undefined;
                        $scope.cert.hypoglykemier.allvarligForekomstVakenTid = undefined;
                        $scope.cert.bedomning.lamplighetInnehaBehorighet = undefined;
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
                        $scope.cert.updateToAttic('hypoglykemier.saknarFormagaKannaVarningstecken');
                        $scope.cert.updateToAttic('hypoglykemier.allvarligForekomst');
                        $scope.cert.updateToAttic('hypoglykemier.allvarligForekomstTrafiken');

                        $scope.cert.hypoglykemier.saknarFormagaKannaVarningstecken = undefined;
                        $scope.cert.hypoglykemier.allvarligForekomst = undefined;
                        $scope.cert.hypoglykemier.allvarligForekomstTrafiken = undefined;
                    } else {
                        $scope.cert.restoreFromAttic('hypoglykemier.saknarFormagaKannaVarningstecken');
                        $scope.cert.restoreFromAttic('hypoglykemier.allvarligForekomst');
                        $scope.cert.restoreFromAttic('hypoglykemier.allvarligForekomstTrafiken');
                    }
                }, true);
            $scope.$watch('cert.hypoglykemier.allvarligForekomst', function(haftAllvarligForekomst) {
                if (!haftAllvarligForekomst && $scope.cert.hypoglykemier) {
                    $scope.cert.updateToAttic('hypoglykemier.allvarligForekomstBeskrivning');
                    $scope.cert.hypoglykemier.allvarligForekomstBeskrivning = undefined;
                } else {
                    $scope.cert.restoreFromAttic('hypoglykemier.allvarligForekomstBeskrivning');
                }
            }, true);
            $scope.$watch('cert.hypoglykemier.allvarligForekomstTrafiken', function(haftAllvarligForekomstTrafiken) {
                if (!haftAllvarligForekomstTrafiken && $scope.cert.hypoglykemier) {
                    $scope.cert.updateToAttic('hypoglykemier.allvarligForekomstTrafikBeskrivning');
                    $scope.cert.hypoglykemier.allvarligForekomstTrafikBeskrivning = undefined;
                } else {
                    $scope.cert.restoreFromAttic('hypoglykemier.allvarligForekomstTrafikBeskrivning');
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
                    $scope.cert.updateToAttic('hypoglykemier.allvarligForekomstVakenTidObservationstid');
                    $scope.cert.hypoglykemier.allvarligForekomstVakenTidObservationstid = undefined;
                } else {
                    $scope.cert.restoreFromAttic('hypoglykemier.allvarligForekomstVakenTidObservationstid');
                }
            }, true);
            // ---

            $scope.$watch('cert.syn.separatOgonlakarintyg', function(separatOgonlakarintyg) {
                if(separatOgonlakarintyg !== undefined) {
                    if (separatOgonlakarintyg && $scope.cert.syn) {
                        $scope.cert.updateToAttic('syn.synfaltsprovningUtanAnmarkning');
                        $scope.cert.updateToAttic('syn.hoger');
                        $scope.cert.updateToAttic('syn.vanster');
                        $scope.cert.updateToAttic('syn.binokulart');
                        $scope.cert.updateToAttic('syn.diplopi');

                        $scope.cert.clear('syn.synfaltsprovningUtanAnmarkning');
                        $scope.cert.clear('syn.hoger');
                        $scope.cert.clear('syn.vanster');
                        $scope.cert.clear('syn.binokulart');
                        $scope.cert.clear('syn.diplopi');
                    } else {
                        $scope.cert.restoreFromAttic('syn.synfaltsprovningUtanAnmarkning');
                        $scope.cert.restoreFromAttic('syn.hoger');
                        $scope.cert.restoreFromAttic('syn.vanster');
                        $scope.cert.restoreFromAttic('syn.binokulart');
                        $scope.cert.restoreFromAttic('syn.diplopi');
                    }
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

            $scope.$watch('cert.bedomning.kanInteTaStallning', function (kanInteTaStallning) {
                if (kanInteTaStallning) {
                    $scope.cert.updateToAttic('bedomning.korkortstyp');
                    $scope.cert.clear('bedomning.korkortstyp');
                } else {
                    $scope.cert.restoreFromAttic('bedomning.korkortstyp');
                }
            });

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
                ManageCertView.signera(viewState.common.intyg.type, viewState.draftModel.version).then(
                    function(result) {
                        if (result.newVersion) {
                            viewState.draftModel.version = result.newVersion;
                        }
                    }
                );
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

            $scope.$on('intyg.loaded', function(){
                if($scope.certForm && $scope.certForm.allvarligForekomstVakenTidObservationstid){
                    var formElement = $scope.certForm.allvarligForekomstVakenTidObservationstid;
                    dateUtils.addDateParserFormatter(formElement);
                }
            });
        }]);
