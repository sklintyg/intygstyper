angular.module('fk7263').controller('fk7263.ErrorCtrl',
    function($state, $stateParams, $scope) {
        'use strict';

        // set a default if no errorCode is given in routeparams
        $scope.errorCode = $stateParams.errorCode || 'generic';
        $scope.backLink = $state.current.backLink || '#view';
        $scope.pagefocus = true;
    });
