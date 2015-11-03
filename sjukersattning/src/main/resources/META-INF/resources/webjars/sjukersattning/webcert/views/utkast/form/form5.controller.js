angular.module('sjukersattning').controller('sjukersattning.EditCert.Form5Ctrl',
    ['$scope', '$log', 'sjukersattning.EditCertCtrl.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            var model = viewState.intygModel;
            $scope.model = model;
            $scope.viewState = viewState;
            $scope.funktionsnedsattning = [];

            function onPageLoad(){
                $scope.funktionsnedsattningOptions = viewState.funktionsnedsattningOptions;
            }

            $scope.setFocus = function(id, state){
                var element = window.document.getElementById(id);
              //  $log.info('elem:' + element);

                if(element) {
                    var initialClasses = element.className;
                    if (!state) {
                        element.blur();

                    } else{
                        element.focus();
                        // set model here

                    }

                }
            };

            onPageLoad();
            $scope.logModel = function(){
                console.log('dd' + JSON.stringify($scope.funktionsnedsattningOptions));
            }

         //  console.log('dd' + JSON.stringify($scope.funktionsnedsattningOptions));

        }]);