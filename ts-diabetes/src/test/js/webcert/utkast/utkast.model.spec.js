describe('ts-diabetes.domain.IntygModel', function() {
    'use strict';

    var IntygModel;
    var ModelAttr;

    // Load the webcert module and mock away everything that is not necessary.

    beforeEach(angular.mock.module('common', 'fk7263', function($provide) {

    }));

    // Get references to the object we want to test from the context.

    beforeEach(angular.mock.inject([
        'common.domain.ModelAttr',
        'fk7263.domain.IntygModel',
        function(_modelAttr_, _IntygModel_) {
            ModelAttr = _modelAttr_;
            IntygModel = _IntygModel_;
        }]));
});