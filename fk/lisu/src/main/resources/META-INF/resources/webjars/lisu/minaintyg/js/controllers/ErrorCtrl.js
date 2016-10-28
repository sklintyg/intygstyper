angular.module('lisjp').controller('lisjp.ErrorCtrl',
    function($state, $stateParams, $scope) {
        'use strict';

        // set a default if no errorCode is given in stateParams
        $scope.errorCode = $stateParams.errorCode || 'generic';
        $scope.backLink = $state.current.backLink || '#view';
        $scope.pagefocus = true;
    });
