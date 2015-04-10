angular.module('ts-diabetes').factory('ts-diabetes.Domain.KorkortstypModel',
    [ function() {
        'use strict';

        // private
        var _korkortstypModel;


        /**
         * Constructor, with class name
         */
        function KorkortstypModel() {
        }

        // override the original update method
        KorkortstypModel.prototype.update = function(content) {
            // refresh the model data

            if (content !== undefined) {
                this.id = content.id;
                this.typ = content.typ;

                this.grundData.update(content.grundData);
            }
        };

        KorkortstypModel.prototype.prepare = function() {
        };

        _korkortstypModel = new KorkortstypModel();

        /**
         * Return the constructor function KorkortstypModel
         */
        return _korkortstypModel;

    }]);