angular.module('luae_na').controller('luae_na.ErrorCtrl',
    function($state, $stateParams, $scope) {
        'use strict';

        // set a default if no errorCode is given in stateParams
        $scope.errorCode = $stateParams.errorCode || 'generic';
        $scope.backLink = $state.current.backLink || '#view';
        $scope.pagefocus = true;
    });
