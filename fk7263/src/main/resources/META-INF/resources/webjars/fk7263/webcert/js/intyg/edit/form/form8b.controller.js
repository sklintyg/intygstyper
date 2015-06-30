angular.module('fk7263').controller('fk7263.EditCert.Form8bCtrl',
    ['$scope', '$timeout', '$log', 'fk7263.EditCertCtrl.ViewStateService',
        'common.DateRangeService',
        function($scope, $timeout, $log, viewState, DateRangeService) {
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
            $log.debug('--- start doneloading ---');
            $scope.$watch('viewState.common.doneLoading', function(newVal, oldVal) {
                $log.debug('--- watch called ---');
                if(doneLoading){
                    $log.debug('--- bailing out. local doneLoading already set. ---');
                    return;
                }
                $log.debug('--- doneLoading not set. ---');
                if (newVal) {
                    $log.debug('--- setting doneLoading. ---');
                    doneLoading = true;
                    _dateRangeService.linkFormAndModel($scope.form8b, viewState.intygModel);
                }
            });
            $log.debug('--- watch loaded ---');

        }]);