define(["mui", "d3", "save-svg-as-png"],
function(ng, d3, saveSvgAsPng){"use strict";
    var factory = function(){
        return function svgSaveAs(svgElm, name, selector) {
            saveSvgAsPng.saveSvgAsPng(svgElm.get(0), _.endsWith(name, ".png") ? name : name+".png", {
                selectorRemap: function(s) {
                    return s.replace(new RegExp("^[\[]*"+selector+"[\]]*", "g"), '');
                },
                backgroundColor: "white"
            });
        }
    };
    factory.$name="mevSvgSaveAs";
    factory.$inject=[];
    factory.$provider="factory";
    return factory;
});