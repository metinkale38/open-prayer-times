
var api = require("./api.js")

exports.api = async function(req,resp){
    console.error(req.originalUrl)
    await api.doRequest(req, resp)
}