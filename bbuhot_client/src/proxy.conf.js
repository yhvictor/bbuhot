const DISCUZ_HOST = "165.227.17.140"
const BACKEND_HOST = "165.227.17.140"

const PROXY_CONFIG = {
    "/api": {
        "target": "http://" + DISCUZ_HOST + "/",
        "secure": false,
    },
    "/assets": {
        "target": "http://localhost:4200/v2/",
        "secure": false,
    },
    "/": {
        "target": "http://" + BACKEND_HOST + "/",
        "secure": false,
    },
    "changeOrigin": true,
    "logLevel": "debug"
}

module.exports = PROXY_CONFIG;