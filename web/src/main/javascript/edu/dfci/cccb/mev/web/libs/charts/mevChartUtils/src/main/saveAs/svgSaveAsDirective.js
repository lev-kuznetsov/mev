define(["mui", "d3", "./svgSaveAs.less"], function(ng, d3){
    var directive = function mevSvgSaveAsDirective(mevSvgSaveAs){
        return {
            restrict: "AEC",
            scope: {
                config: "=?mevSvgSaveAs"
            },
            link: function(scope, elm, attr){
                elm.bind("click", function(){
                    var svg = ng.element("body").find(scope.config.selector || "svg");
                    mevSvgSaveAs(svg, scope.config.name || "download",
                        scope.config.selector
                            ? scope.config.selector.replace(/[ >]*[ ]*svg/g, "")
                            : undefined);
                });
            }
        };
    };
    directive.$name="mevSvgSaveAs";
    directive.$inject=["mevSvgSaveAs"];
    directive.$provider="directive";
    return directive;
});