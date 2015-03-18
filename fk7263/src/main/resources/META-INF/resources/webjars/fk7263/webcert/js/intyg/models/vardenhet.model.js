angular.module('fk7263').factory(
    'fk7263.Domain.VardenhetModel',
    [ function() {
        'use strict';

        var _vardenhetModel;

        /**
         * Constructor, with class name
         */
        function VardenhetModel() {
            this.enhetsid;
            this.enhetsnamn;
            this.postadress;
            this.postnummer;
            this.postort;
            this.telefonnummer;
            this.epost;
            this.vardgivare = {
                vardgivarid: null,
                vardgivarnamn: null
            };
            this.arbetsplatsKod;
        }


        VardenhetModel.prototype.update = function(vardenhet) {
            // refresh the model data

            if(vardenhet === undefined) return;
            this.enhetsid = vardenhet.enhetsid;
            this.enhetsnamn = vardenhet.enhetsnamn;
            this.postadress = vardenhet.postadress;
            this.postnummer = vardenhet.postnummer;
            this.postort = vardenhet.postort;
            this.telefonnummer = vardenhet.telefonnummer;
            this.epost = vardenhet.epost;
            this.vardgivare = {
                vardgivarid: vardenhet.vardgivare.vardgivarid,
                vardgivarnamn: vardenhet.vardgivare.vardgivarnamn
            };
            this.arbetsplatsKod = vardenhet.arbetsplatsKod;
        }

        VardenhetModel.build = function() {
            return new VardenhetModel();
        };

        _vardenhetModel = VardenhetModel.build();
        /**
         * Return the constructor function VardenhetModel
         */
        return _vardenhetModel;

    }]);