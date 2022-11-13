const api = require("./api.js")


exports.api = async function (req, resp) {

    var response = await api.coreRouter(req.method, req.path, req.query, req.headers)

    if (response.contentType) resp.type(response.contentType)
    if (response.body)
        resp.send(response.body)
    else
        resp.send("")
}
