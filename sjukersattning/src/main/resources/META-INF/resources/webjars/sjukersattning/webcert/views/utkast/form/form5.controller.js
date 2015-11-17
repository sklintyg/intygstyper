angular.module('sjukersattning').controller('sjukersattning.EditCert.Form5Ctrl',
    ['$scope', '$log', 'sjukersattning.EditCertCtrl.ViewStateService', '$filter',
        function($scope, $log, viewState, $filter) {
            'use strict';
            var model = viewState.intygModel;
            $scope.model = model;
            $scope.viewState = viewState;

            $scope.selectedNedsattningar = [
                { 'id': 1, selected: false, text : null } ,
                { 'id': 2, selected: false, text : null },
                { 'id': 3, selected: false, text : null },
                { 'id': 4, selected: false, text : null },
                { 'id': 5, selected: false, text : null },
                { 'id': 6, selected: false, text : null },
                { 'id': 7, selected: false, text : null },
                { 'id': 8, selected: false, text : null }
            ];

            function onPageLoad(){

               // alert('funktionsnedsattningar length' + model.funktionsnedsattningar.length);
                if(model.funktionsnedsattningar.length < 1){ // do nothing let viewState options list dictate data
                    clearViewModel();
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

            $scope.setFocus = function(id, state ,text){
                var element = window.document.getElementById(id);
                if(element) {
                    //var initialClasses = element.className;
                    var toUpdate = $scope.selectedNedsattningar[id];
                    if (!state) {
                        element.blur();

                        if (toUpdate) {
                            toUpdate.selected = false;
                            toUpdate.text = text;
                            // set attic?
                        }

                    } else{
                        element.focus();
                        toUpdate.selected= true;
                        toUpdate.text = text;
                        // restore attic?
                    }
                }
            };

            $scope.updateViewModelNedsattningar = function(id, text) {
                var toUpdate = $scope.selectedNedsattningar[id];
                if (!toUpdate) {
                   return;
                } else{
                    toUpdate.text = text;

                        setFunktionsNedsattningar(id,text);


                }
            }

            function setFunktionsNedsattningar(funktionsomrade, beskrivning){
                    //var match = model.funktionsnedsattningar[id];
                    var match = $filter('filter')(model.funktionsnedsattningar, {funktionsomrade:funktionsomrade})[0];
                    console.log('match', match);

                    if( !match ) {
                         var obj = { 'funktionsomrade': funktionsomrade, 'beskrivning' : beskrivning };
                         model.funktionsnedsattningar.push(obj);
                        console.log('model.funktionsnedsattningar new*: ' + JSON.stringify(model.funktionsnedsattningar));
                    } else {
                         match.funktionsomrade = funktionsomrade;
                         match.beskrivning = beskrivning;
                        console.log('model.funktionsnedsattningar old*: ' + JSON.stringify(model.funktionsnedsattningar));
                    }
                    //console.log('model.funktionsnedsattningar: ' + JSON.stringify(model.funktionsnedsattningar));
            }

            function clearViewModel(){
                for(var i = 0; i < viewState.funktionsnedsattningOptions.length; i++){
                    viewState.funktionsnedsattningOptions[i].text = '';
                    viewState.funktionsnedsattningOptions[i].selected = false;
                }
            }

            onPageLoad();
        }]);