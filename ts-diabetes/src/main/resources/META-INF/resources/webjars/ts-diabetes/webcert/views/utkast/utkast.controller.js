angular.module('ts-diabetes').controller('ts-diabetes.UtkastController',
    ['$location', '$log', '$q', '$rootScope', '$scope', '$timeout', '$window', 'common.UtkastService', 'common.UserModel',
        'ts-diabetes.Domain.IntygModel', 'ts-diabetes.UtkastController.ViewStateService', 'common.DateUtilsService',
        'common.anchorScrollService',
        function($location, $log, $q, $rootScope, $scope, $timeout, $window, UtkastService, UserModel,
            IntygModel, viewState, dateUtils, anchorScrollService) {
            'use strict';

            /**********************************************************************************
             * Default state
             **********************************************************************************/

            viewState.reset();
            $scope.viewState = viewState;
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

            // ---------------------------------------------------------------------------------------------------------


            /******************************************************************************************
             * Exposed interaction
             ******************************************************************************************/

            $scope.scrollTo = function(message) {
                anchorScrollService.scrollTo('anchor.' + message);
            };

            /**************************************************************************
             * Load certificate and setup form
             **************************************************************************/

            // Get the certificate draft from the server.
            UtkastService.load(viewState);

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
