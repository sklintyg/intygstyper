angular.module('sjukersattning').controller('sjukersattning.EditCert.FormFunktionsNedsattningCtrl',
    ['$scope', '$log', 'sjukersattning.EditCertCtrl.ViewStateService', '$filter',
        function($scope, $log, viewState, $filter) {
            'use strict';
            var model = viewState.intygModel;
            $scope.model = model;
            $scope.viewState = viewState;

            $scope.funktionsnedsattningar = [
                { 'model':'funktionsnedsattningIntellektuell', 'rubrik':'DFR_8.1.RBK', 'hjalptext':'DFR_8.1.HLP' },
                { 'model':'funktionsnedsattningKommunikation', 'rubrik':'DFR_9.1.RBK', 'hjalptext':'DFR_9.1.HLP'  },
                { 'model':'funktionsnedsattningKoncentration', 'rubrik':'DFR_10.1.RBK', 'hjalptext':'DFR_10.1.HLP'  },
                { 'model':'funktionsnedsattningPsykisk', 'rubrik':'DFR_11.1.RBK', 'hjalptext':'DFR_11.1.HLP'  },
                { 'model':'funktionsnedsattningSynHorselTal', 'rubrik':'DFR_12.1.RBK', 'hjalptext':'DFR_12.1.HLP'  },
                { 'model':'funktionsnedsattningBalansKoordination', 'rubrik':'DFR_13.1.RBK', 'hjalptext':'DFR_13.1.HLP'  },
                { 'model':'funktionsnedsattningAnnan', 'rubrik':'DFR_14.1.RBK', 'hjalptext':'DFR_14.1.HLP'  }
            ];
        }]);