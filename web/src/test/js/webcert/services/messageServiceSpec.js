describe('messageService', function() {
    'use strict';

    var messageService;
    var $rootScope;

    beforeEach(angular.mock.module('common'));

    beforeEach(angular.mock.inject(['common.messageService', '$rootScope',
        function(_messageService_, _$rootScope_) {

            messageService = _messageService_;
            $rootScope = _$rootScope_;
        }
    ]));

    describe('#getProperty', function() {

        beforeEach(function() {
            messageService.addResources({
                en: { correct: 'Correct', hello: 'Hello ${prefix} ${user}!', empty: '' },
                sv: { correct: 'Rätt', hello: 'Hej ${prefix} ${user}!', empty: '' }
            });
        });

        it('should return the correct value if a language is specified', function() {
            var enMessage = messageService.getProperty('correct', null, null, 'en');
            var svMessage = messageService.getProperty('correct', null, null, 'sv');

            expect(enMessage).toBe('Correct');
            expect(svMessage).toBe('Rätt');
        });

        it('should return an empty string if the message is an empty string', function() {
            var message = messageService.getProperty('empty', null, null, 'sv');

            expect(message).toBe('');
        });

        it('should return the correct value if a language is set in the root scope', function() {
            $rootScope.lang = 'sv';

            var message = messageService.getProperty('correct');

            expect(message).toBe('Rätt');
        });

        it('should return the correct value if a default language is set', function() {
            $rootScope.DEFAULT_LANG = 'sv';

            var message = messageService.getProperty('correct', null, null, null, true);

            expect(message).toBe('Rätt');
        });

        it('should return the default value if the key is not present in the resources', function() {
            var message = messageService.getProperty('emptystring', null, '', 'sv');

            expect(message).toBe('');
        });

        it('should return the missing key if the key is not present in the resources', function() {
            var message = messageService.getProperty('emptystring', null, null, 'sv');

            expect(message).toBe('[Missing "emptystring"]');
        });

        it('should return missing language if no language is set', function() {
            var message = messageService.getProperty('emptystring');

            expect(message).toBe('[Missing language]');
        });


        it('should return string with expanded string if variable available', function() {
            var message = messageService.getProperty('hello', { prefix: 'Stora', user: 'Världen' }, null, 'sv');

            expect(message).toBe('Hej Stora Världen!');
        });
    });
});
