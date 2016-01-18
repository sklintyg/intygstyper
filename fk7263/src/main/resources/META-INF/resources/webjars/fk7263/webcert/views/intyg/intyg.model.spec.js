/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

describe('fk7263.domain.IntygModel', function() {
    'use strict';

    var IntygModel;
    var ModelAttr;

    // Load the webcert module and mock away everything that is not necessary.

    beforeEach(angular.mock.module('common', 'fk7263', function(/*$provide*/) {
    }));

    // Get references to the object we want to test from the context.

    beforeEach(angular.mock.inject([
        'common.domain.ModelAttr',
        'fk7263.Domain.IntygModel',
        function( _modelAttr_, _IntygModel_) {
            ModelAttr = _modelAttr_;
            IntygModel = _IntygModel_;
        }]));

    describe('#creation and functions of intyg.model', function() {

        var model;

        beforeEach(function(){
            var content = {diagnosBeskrivning:'diagnosBeskrivning', diagnosBeskrivning1:'diagnosBeskrivning1' };
            model = new IntygModel();
            model.update(content);
            model.updateToAttic();
        });

        it('that the model attributes are set', function(){
            angular.forEach(model.properties, function(prop) {
                var propertyCheck = function(model, prop){
                    var property = prop;
                    if(property instanceof ModelAttr){
                        property = property.property;
                    }
                    expect(model.hasOwnProperty(property)).toBe(true);
                };
                if (prop instanceof Array) {
                    angular.forEach(prop, function(aprop) {
                        propertyCheck(model, aprop);
                    }, this );
                } else {
                    propertyCheck(model, prop);
                }
            }, this);

        });


        it('can update and restore from attic', function(){

            var props = ['diagnosBeskrivning','diagnosBeskrivning1'];

            // restore
            model.diagnosBeskrivning = 'new value';

            model.restoreFromAttic(props);

            expect(model.diagnosBeskrivning).toBe('diagnosBeskrivning');

            // update
            model.diagnosBeskrivning = 'new value';

            model.updateToAttic(props);

            expect(model.diagnosBeskrivning).toBe('new value');

            // restore, should restore the last updated value
            model.restoreFromAttic(props);

            expect(model.diagnosBeskrivning).toBe('new value');

            // restore
            model.diagnosBeskrivning = 'another new value';

            model.restoreFromAttic(props);

            expect(model.diagnosBeskrivning).toBe('new value');

            expect(model.diagnosBeskrivning1).toBe('diagnosBeskrivning1');


        });

        it('can create a toSendModel', function(){
            var send = model.toSendModel();

            // check that send includes default values
            expect(send.avstangningSmittskydd).toBe(false);
            expect(send.samsjuklighet).toBe(false);

            // check values that have been set
            expect(send.diagnosBeskrivning).toBe('diagnosBeskrivning');
            expect(send.diagnosBeskrivning1).toBe('diagnosBeskrivning1');

        });

        it('form2', function(){
            var content = {
                diagnosBeskrivning:'diagnosBeskrivning',
                diagnosBeskrivning1:'diagnosBeskrivning1',
                diagnosBeskrivning2:'diagnosBeskrivning2',
                diagnosBeskrivning3:'diagnosBeskrivning3',
                diagnosKod:'diagnosKod',
                diagnosKod2:'diagnosKod2',
                diagnosKod3:'diagnosKod3',
                diagnosKodsystem1: 'diagnosKodsystem1',
                diagnosKodsystem2: 'diagnosKodsystem2',
                diagnosKodsystem3: 'diagnosKodsystem3',
                samsjuklighet: true};
            model.update(content);

            var send = model.toSendModel();

            // check that send includes default values
            expect(send.samsjuklighet).toBe(true);

            // check values that have been set
            expect(send.diagnosBeskrivning).toBe('diagnosBeskrivning');
            expect(send.diagnosBeskrivning1).toBe('diagnosBeskrivning1');
            expect(send.diagnosBeskrivning2).toBe('diagnosBeskrivning2');
            expect(send.diagnosBeskrivning3).toBe('diagnosBeskrivning3');
            expect(send.diagnosKod).toBe('diagnosKod');
            expect(send.diagnosKod2).toBe('diagnosKod2');
            expect(send.diagnosKod3).toBe('diagnosKod3');
            expect(send.diagnosKodsystem1).toBe('diagnosKodsystem1');
            expect(send.diagnosKodsystem2).toBe('diagnosKodsystem2');
            expect(send.diagnosKodsystem3).toBe('diagnosKodsystem3');

        });

    });



});
