angular.module('fk7263').factory('fk7263.domain.SkapadAvModel',
    ['fk7263.domain.VardenhetModel',
        function(vardenhetModel) {
            'use strict';

            var _skapadAv;

            /**
             * Constructor, with class name
             */
            function SkapadAv() {
                this.personId = undefined;
                this.fullstandigtNamn = undefined;
                this.forskrivarKod = undefined;
                this.vardenhet = vardenhetModel;
            }

            SkapadAv.prototype.update = function(skapadAv) {
                // refresh the model data
                if(skapadAv === undefined){
                    return;
                }
                this.personId = skapadAv.personId;
                this.fullstandigtNamn = skapadAv.fullstandigtNamn;
                this.forskrivarKod = skapadAv.forskrivarkod;
                this.vardenhet.update(skapadAv.vardenhet);
            };

            SkapadAv.build = function() {
                return new SkapadAv();
            };

            _skapadAv = SkapadAv.build();
            /**
             * Return the constructor function SkapadAv
             */
            return _skapadAv;

        }]);