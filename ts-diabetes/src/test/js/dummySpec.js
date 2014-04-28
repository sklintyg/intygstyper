'use strict';
define(
		[ 'angular', 'angularMocks', 'ts-diabetes/webcert/js/directives' ],
		function(angular, mocks, directives) {

			describe(
					'dummySpec',
					function() {

						it('you shall not pass', function() {
							expect(true === false).toEqual(false);
						});

					});
		});
