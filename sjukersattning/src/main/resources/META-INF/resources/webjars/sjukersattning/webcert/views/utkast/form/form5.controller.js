angular.module('sjukersattning').controller('sjukersattning.EditCert.Form5Ctrl',
    ['$scope', '$log', 'sjukersattning.EditCertCtrl.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            var model = viewState.intygModel;
            $scope.model = model;
            $scope.viewState = viewState;

            function onPageLoad(){

               // alert('funktionsnedsattningar length' + model.funktionsnedsattningar.length);
                if(model.funktionsnedsattningar.length < 1){ // do nothing let viewState options list dictate data
                    $scope.funktionsnedsattningOptions = viewState.funktionsnedsattningOptions;
                } else { // set data first, then update from backend on relevant data
                    $scope.funktionsnedsattningOptions = viewState.funktionsnedsattningOptions; // 1. load
                    for(var i = 0; i < model.funktionsnedsattningar.length; i++) { // 2. update
                        var current = model.funktionsnedsattningar[i];
                        if($scope.funktionsnedsattningOptions.id === current.id) {
                            $scope.funktionsnedsattningOptions.text = current.text; // set text if match
                        }
                    }
                }
            }

           /* $scope.$watch('viewState.common.doneLoading', function(newVal, oldVal) {
                if (newVal === oldVal) {
                    return;
                }

                // only do this once the page is loaded and changes come from the gui!
                if (viewState.common.doneLoading) {
                    // Remove defaults not applicable when smittskydd is active
                    if (newVal === true) {
                        model.updateToAttic(model.properties.form5);
                        model.clear(model.properties.form5);
                    } else {
                        model.restoreFromAttic(model.properties.form5);
                    }
                }
            }); */

            $scope.setFocus = function(id, state){
                var element = window.document.getElementById(id);
                if(element) {
                    //var initialClasses = element.className;
                    if (!state) {
                        element.blur();
                    } else{
                        element.focus();
                        // set model here



                        //model.funktionsnedsattning.push
                    }


                }
            };

            $scope.setFunktionsNedsattningar = function(id, text){

                    var match = model.funktionsnedsattningar[id];

                    if( model.funktionsnedsattningar[id] === undefined ) {
                         var obj = { 'funktionsomrade': id, 'beskrivning' : text };
                         model.funktionsnedsattningar.push(obj);
                    } else if (match) {
                         match.funktionsomrade = id;
                         match.beskrivning = text;
                    }
            };

            onPageLoad();
        }]);