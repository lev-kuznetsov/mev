define(["mui", "pouchdb", "mev-domain-common",
"./model/Workspace",
"./view/list/WorkspaceListDirective",
"./view/upload/UploadDatasetDirective"], function(ng){
    return ng.module("mev-workspace", arguments, arguments);
});