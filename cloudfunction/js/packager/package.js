module.exports = [{
    name: 'server',
    entry: '../../../build/js/packages/open-prayer-times-cloudfunction-js/kotlin/open-prayer-times-cloudfunction-js.js',
    target: 'node',
    mode: 'production',
    output: {
        path: __dirname + '/dist',
        filename: 'api.js',
        libraryTarget: 'commonjs2'
    }
}];