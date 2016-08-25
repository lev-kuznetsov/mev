define(["mui", "pouchdb", "mev-domain-common", "mev-dataset", "mev-annotations", "angular-ui-router", "ui-router-extras",
    "./model/Workspace",
    "./router/WorkspaceState",
    "./router/WorkspaceStateInit",
    "./view/list/WorkspaceListDirective",
    "./view/upload/UploadDatasetDirective"], function(ng){
    return ng.module("mev-workspace", ["ct.ui.router.extras"], arguments);
});