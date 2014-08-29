angular.module('fk7263').controller('fk7263.ErrorCtrl',
    function($route, $routeParams, $scope) {
        'use strict';

        // set a default if no errorCode is given in routeparams
        $scope.errorCode = $routeParams.errorCode || 'generic';
        $scope.backLink = $route.current.backLink || '#view';
        $scope.pagefocus = true;
    });
