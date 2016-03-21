angular.module('luse').factory('luse.FormFactoryHelper', ['common.ObjectHelper', 'common.UtilsService', function(ObjectHelper, UtilsService) {
    'use strict';

    function _annatGrundBeskrivningListener(field, newValue, oldValue, scope, stopWatching) {
        var model = scope.model;
        if (ObjectHelper.isDefined(oldValue) && !ObjectHelper.isDefined(newValue)) {
            model.updateToAttic(model.properties.annatGrundForMUBeskrivning);
            model.clear(model.properties.annatGrundForMUBeskrivning);
        } else {
            if(!UtilsService.isResetByAngular(newValue, oldValue) && model.isInAttic(model.properties.annatGrundForMUBeskrivning)){
                model.restoreFromAttic(model.properties.annatGrundForMUBeskrivning);
            }
        }
    }

    function _underlagListener(field, newValue, oldValue, scope, stopWatching) {
        var model = scope.model;
        if (newValue) {
            if(model.isInAttic(model.properties.underlag)){
                model.restoreFromAttic(model.properties.underlag);
            }
            if (!model.underlag || model.underlag.length === 0) {
                model.underlag.push({ typ: null, datum: null, hamtasFran: null });
            }
        } else {
            model.updateToAttic(model.properties.underlag);
            model.clear(model.properties.underlag);
            scope.model.underlag = [];
        }
    }

    function _anledningKontaktListener(field, newValue, oldValue, scope, stopWatching) {
        var model = scope.model;
        if (newValue === false) {
            model.updateToAttic(model.properties.anledningTillKontakt);
            model.clear(model.properties.anledningTillKontakt);
        } else {
            if(model.isInAttic(model.properties.anledningTillKontakt)){
                model.restoreFromAttic(model.properties.anledningTillKontakt);
            }
        }
    }

    return {
        annatGrundBeskrivningListener: _annatGrundBeskrivningListener,
        underlagListener: _underlagListener,
        anledningKontaktListener: _anledningKontaktListener
    };
}]);
