describe('sjukersattning.Domain.IntygModel', function() {
    'use strict';

    var IntygModel;
    var ModelAttr;

    // Load the webcert module and mock away everything that is not necessary.

    beforeEach(angular.mock.module('common', 'sjukersattning', function(/*$provide*/) {
    }));

    // Get references to the object we want to test from the context.

    beforeEach(angular.mock.inject([
        'common.domain.ModelAttr',
        'sjukersattning.Domain.IntygModel',
        function( _modelAttr_, _IntygModel_) {
            ModelAttr = _modelAttr_;
            IntygModel = _IntygModel_;
        }]));

    describe('#creation and functions of sjukersättning intyg.model', function() {

        var model;

        beforeEach(function(){
            var content = {
                form2: { undersokningAvPatienten: '2015-01-01' } ,
                diagnosBeskrivning1: 'diagnosBeskrivning1' };
            model = new IntygModel();
            model.update(content);
            model.updateToAttic();
            console.log('model base', model);
        });

        it('that the model attributes are set', function(){
            angular.forEach(model.properties, function(prop) {
             //  console.log('mp: ', model.properties);

                var propertyCheck = function(model, prop){
                    var property = prop;
                    if(property instanceof ModelAttr){
                        property = property.property;
                    }
                    expect(model.hasOwnProperty(property)).toBe(true);
                };

                    if (prop instanceof Array) {
                        angular.forEach(prop, function (aprop) {
                            propertyCheck(model, aprop);
                        }, this);
                    }  else {
                        propertyCheck(model, prop);
                    }

            }, this);

        });
        it('form2 has base properties set', function() {
            var form2props = model.properties.form2;
            console.log('form2 contents underlag:  ', form2props.underlag );
           // var expectedProps = ['undersokningAvPatienten', 'journaluppgifter', 'telefonkontaktMedPatienten', 'kannedomOmPatient', 'underlag'];
            console.log('prop test', form2props.journaluppgifter);
            expect(form2props.undersokningAvPatienten).toBeDefined();
            expect(form2props.journaluppgifter).toBeDefined();
            expect(form2props.telefonkontaktMedPatienten).toBeDefined();
            expect(form2props.underlag).toBeDefined();

        });

        it('form2 has at least 1 underlag set as default', function(){
            var form2underlag = model.properties.form2.underlag;
            expect(form2underlag.defaultValue[0].id).toBe(0);
        });



        it('can update and restore from attic', function(){

            var props = ['undersokningAvPatienten','diagnosBeskrivning1'];

            // restore
            model.undersokningAvPatienten = 'new date';

            model.restoreFromAttic(props);

            expect(model.undersokningAvPatienten).toBe('2015-01-01');

            // update
            model.diagnosBeskrivning = '1999-01-01';

            model.updateToAttic(props);

            expect(model.undersokningAvPatienten).toBe('1999-01-01');

            // restore, should restore the last updated value
            model.restoreFromAttic(props);

            expect(model.undersokningAvPatienten).toBe('1999-01-01');

            // restore
            model.diagnosBeskrivning = '1994-01-01';

            model.restoreFromAttic(props);

            expect(model.diagnosBeskrivning).toBe('1999-01-01');

            expect(model.diagnosBeskrivning1).toBe('diagnosBeskrivning1');


        });

        it('can create a toSendModel', function(){
            var send = model.toSendModel();

            // check that send includes default values

            expect(send.samsjuklighet).toBe(false);

            // check values that have been set
            expect(send.undersokningAvPatienten).toBe('2015-01-01');
            expect(send.diagnosBeskrivning1).toBe('diagnosBeskrivning1');

        });


        /* from proxy
         data: Object
         content: Object
         atgarder: Array[0]
         length: 0
         __proto__: Array[0]
         diagnoser: Array[0]
         funktionsnedsattnings: Array[0]
         grundData: Object
         id: "035f5404-86ad-4b78-802f-7dc1bea0401a"
         kontaktMedFk: false
         nyBedomningDiagnos: false
         typ: "sjukersattning"
         underlag: Array[0]
         __proto__: Object
         enhetsNamn: "WebCert-Enhet1"
         status: "DRAFT_INCOMPLETE"
         vardgivareNamn: "WebCert-Vårdgivare1"
         version: 0
         vidarebefordrad: false
         __proto__: Object
         this: undefined

         */
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