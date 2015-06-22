angular.module('fk7263').controller('fk7263.EditCert.Form8bCtrl',
    ['$scope', '$log', 'fk7263.EditCertCtrl.ViewStateService',
        'common.DateRangeService',
        function($scope, $log, viewState, DateRangeService) {
            'use strict';
            // private vars
            //var _dateRangeGroups = DateRangeGroupsService.build(_$scope);


            // scope
            $scope.model = viewState.intygModel;
            $scope.viewState = viewState;

            // 1. onload
            // 2. on check change
            // 3. on manual change.

            var _dateRangeService = DateRangeService.FromTos.build(['nedsattMed25','nedsattMed50','nedsattMed75','nedsattMed100']);


            // 8b. Arbetsförmåga date management
            $scope.field8b = {
                nedsattMed25 : _dateRangeService['nedsattMed25'],
                nedsattMed50 : _dateRangeService['nedsattMed50'],
                nedsattMed75 : _dateRangeService['nedsattMed75'],
                nedsattMed100 : _dateRangeService['nedsattMed100'],
                onChangeWorkStateCheck : function(nedsattModelName) {
                    $log.debug('------------------------ onChangeWorkStateCheck');

                    _dateRangeService[nedsattModelName].check();

                    if (!$scope.field8b[nedsattModelName].workState) {
                        viewState.intygModel[nedsattModelName + 'Beskrivning'] = undefined;
                    }

                },
                info : _dateRangeService
            };

            $scope.info = _dateRangeService;


            $scope.$watch('field8b.nedsattMed25.workState', function(newVal, oldVal) {
                $log.debug('workstate : newVal:' + newVal + ', oldVal:' + oldVal);
            });

            var doneLoading = false;
            $scope.$watch('viewState.common.doneLoading', function(newVal, oldVal) {
                if(newVal === oldVal || doneLoading){
                    return;
                }
                if (newVal) {
                    doneLoading = true;
                    _dateRangeService.linkFormAndModel($scope.form8b, viewState.intygModel);
                }
            });

        }]);