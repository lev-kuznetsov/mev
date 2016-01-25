define(["mui", "./demo"], function(ng, app){
    ng.element(document).ready(function(){
        ng.bootstrap(document, [app.name]);
    });
});
