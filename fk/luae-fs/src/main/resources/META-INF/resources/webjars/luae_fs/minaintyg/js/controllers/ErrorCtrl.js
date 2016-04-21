angular.module('luae_fs').controller('luae_fs.ErrorCtrl',
    function($state, $stateParams, $scope) {
        'use strict';

        // set a default if no errorCode is given in stateParams
        $scope.errorCode = $stateParams.errorCode || 'generic';
        $scope.backLink = $state.current.backLink || '#view';
        $scope.pagefocus = true;
    });
