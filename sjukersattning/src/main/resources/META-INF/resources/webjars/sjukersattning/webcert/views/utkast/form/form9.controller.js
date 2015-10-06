angular.module('sjukersattning').controller('sjukersattning.EditCert.Form9Ctrl',
    ['$scope', '$log', 'sjukersattning.EditCertCtrl.ViewStateService', 'common.UtilsService',
        'common.DateUtilsService',
        function($scope, $log, viewState, utils, dateUtils) {
            'use strict';
            var model = viewState.intygModel;
            $scope.model = model;

            $scope.viewState = viewState;

        }
    ]
);
