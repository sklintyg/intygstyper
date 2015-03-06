angular.module('fk7263').controller('fk7263.ErrorCtrl',
    function($route, $stateParams, $scope) {
        'use strict';

        // set a default if no errorCode is given in stateParams
        $scope.errorCode = $stateParams.errorCode || 'generic';
        $scope.backLink = $route.current.backLink || '#view';
        $scope.pagefocus = true;
    });
