({
    baseUrl: '../resources/META-INF/resources',
    dir: '../../../target/requirebuild',
    skipDirOptimize: true,
    keepBuildDir: true,
    modules: [
        {
            name: 'webjars/ts-bas/minaintyg/js/module.min',
            create: true,
            include: [],
            exclude: [
                'angular', 'angularCookies', 'angularRoute', 'angularSanitize', 'angularSwedish', 'angularUiBootstrap'
            ]
        },
        {
            name: 'webjars/ts-bas/webcert/js/module.min',
            create: true,
            include: [
                'webjars/ts-bas/webcert/js/module.js'
            ],
            exclude: [
                'angular', 'angularCookies', 'angularRoute', 'angularSanitize', 'angularSwedish', 'angularUiBootstrap',
                'webjars/common/webcert/js/directives',
                'webjars/common/webcert/js/filters',
                'webjars/common/webcert/js/messages',
                'webjars/common/webcert/js/services'
            ]
        }
    ],
    optimize: 'uglify2',
    uglify2: {
        mangle: false
    },
    paths: {

        angular: '../../../../../../target/webjardependencies/angularjs/angular',
        angularCookies: '../../../../../../target/webjardependencies/angularjs/angular-cookies',
        angularRoute: '../../../../../../target/webjardependencies/angularjs/angular-route.min',
        angularSanitize: '../../../../../../target/webjardependencies/angularjs/angular-sanitize.min',
        angularSwedish: '../../../../../../target/webjardependencies/angularjs/1.2.14/angular-locale_sv-se',
        angularUiBootstrap: '../../../../../../target/webjardependencies/angular-ui-bootstrap/ui-bootstrap-tpls',

        text: '../../../../../../target/webjardependencies/requirejs-text/text'
    },
    onBuildWrite: function (moduleName, path, contents) {
        return contents.replace(/\/web\/webjars/g, 'webjars');
    }
})
