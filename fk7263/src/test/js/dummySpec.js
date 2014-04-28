'use strict';
define(
		[ 'angular', 'angularMocks', 'fk7263/webcert/js/directives' ],
		function(angular, mocks, directives) {

			describe(
					'dummySpec',
					function() {

						it('you shall not pass', function() {
							expect(true === false).toEqual(false);
						});

					});
		});
