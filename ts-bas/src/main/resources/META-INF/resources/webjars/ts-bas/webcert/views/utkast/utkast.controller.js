angular.module('ts-bas').controller('ts-bas.UtkastController',
    [ '$anchorScroll', '$location', '$q', '$rootScope', '$scope', '$timeout', '$window',
        'common.UtkastService', 'common.UserModel',
        'ts-bas.Domain.IntygModel',
        'ts-bas.UtkastController.ViewStateService', 'common.anchorScrollService',
        function($anchorScroll, $location, $q, $rootScope, $scope, $timeout, $window,
            UtkastService, UserModel, IntygModel, viewState, anchorScrollService) {
            'use strict';

            /**********************************************************************************
             * Default state
             **********************************************************************************/

            $scope.viewState = viewState.reset();
            $scope.notifieringVidarebefordrad = viewState.draftModel.vidarebefordrad; // temporary hack. let view take from viewstate
            $scope.user = UserModel.user;

            /***************************************************************************
             * Private controller support functions
             ***************************************************************************/


            /*************************************************************************
             * Ng-change and watches updating behaviour in form (try to get rid of these or at least make them consistent)
             *************************************************************************/

            $scope.$watch('viewState.intygModel', function() {
                viewState.updateKravYtterligareUnderlag();
            }, true);


            /****************************************************************************
             * Exposed interaction functions to view
             ****************************************************************************/

            $scope.scrollTo = function(message) {
                anchorScrollService.scrollTo('anchor.' + message);
            };

            /**************************************************************************
             * Load certificate and setup form
             **************************************************************************/

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

            // Get the certificate draft from the server.
            UtkastService.load(viewState);

        }]);
