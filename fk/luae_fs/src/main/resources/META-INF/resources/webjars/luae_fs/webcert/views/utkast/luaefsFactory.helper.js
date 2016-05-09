angular.module('luae_fs').factory('luae_fs.FormFactoryHelper', ['common.ObjectHelper', 'common.UtilsService', function(ObjectHelper, UtilsService) {
    'use strict';

    function _underlagListener(field, newValue, oldValue, scope, stopWatching) {
        var model = scope.model;
        if (newValue) {
            if (model.isInAttic(model.properties.underlag)) {
                model.restoreFromAttic(model.properties.underlag);
            }
            if (!model.underlag || model.underlag.length === 0) {
                model.underlag.push({typ: null, datum: null, hamtasFran: null});
            }
        } else {
            model.updateToAttic(model.properties.underlag);
            model.clear(model.properties.underlag);
            scope.model.underlag = [];
        }
    }

    return {
        underlagListener: _underlagListener
    };
}]);
