'use strict';
define(
		[ 'angular', 'angularMocks', 'common/js/wc-common' ],
		function(angular, mocks, wc_common) {

			describe(
					'dummySpec',
					function() {

						it('you shall not pass', function() {
							expect(true === false).toEqual(false);
						});

					});
		});
