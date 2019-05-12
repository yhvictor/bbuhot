const PROXY_CONFIG = {
    '/api': {
        target: 'http://165.227.17.140/',
    },
    '/': {
        target: 'http://165.227.17.140/',
    },
    secure: false,
    changeOrigin: true,
    logLevel: 'debug'
}

module.exports = PROXY_CONFIG;
