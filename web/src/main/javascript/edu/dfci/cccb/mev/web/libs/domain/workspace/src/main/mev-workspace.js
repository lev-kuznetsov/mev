define(["mui", "pouchdb", "mev-domain-common",
    "./model/Workspace",
    "./router/WorkspaceState",
    "./router/WorkspaceStateInit",
    "./view/list/WorkspaceListDirective",
    "./view/upload/UploadDatasetDirective"], function(ng){
    return ng.module("mev-workspace", arguments, arguments);
});