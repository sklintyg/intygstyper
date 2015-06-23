angular.module('ts-bas').controller('ts-bas.Utkast.Form1Controller',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;


        }]);