define([
    'angular',
    'angularMocks'
], function() {
    'use strict';

    describe('dummySpec', function() {

        it('you shall not pass', function() {
            expect(true === false).toEqual(false);
        });
    });
});
