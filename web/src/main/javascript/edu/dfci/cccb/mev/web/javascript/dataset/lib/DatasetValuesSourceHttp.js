define(['lodash'], function(_){
    return function DatasetValueSourceHttp($http, id){
        var source = {
            get: $http.get.bind(this, '/dataset/'+id+'/data/values',
                {
                    params: {format: "binary"},
                    responseType: "arraybuffer",
                    headers: {"Accept": "application/octet-stream"}
                }),
            id: id
        }
        _.assign(this, source);
    }
});