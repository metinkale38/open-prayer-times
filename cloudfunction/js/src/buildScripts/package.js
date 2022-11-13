module.exports = [{
    name: 'server',
    entry: '../../build/js/packages/open-prayer-times-core/kotlin/open-prayer-times-core.js',
    target: 'node',
    mode: 'production',
    output: {
        path: __dirname + '/../../build',
        filename: 'api.js',
        libraryTarget: 'commonjs2'
    }
}];