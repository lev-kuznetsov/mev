define(["mui", "d3", "blueimp-canvas-to-blob", "browser-filesaver", "save-svg-as-png"],
function(ng, d3, canvasToBlob, fileSaver, saveSvgAsPng){"use strict";
    var factory = function(){
        return function svgSaveAs(svgElm, name) {
            saveSvgAsPng.saveSvgAsPng(svgElm.get(0), name);
            return;

            console.debug("svg", svg);
            var svg = d3.select(svgElm[0]);
            var html = svg
                .attr("version", 1.1)
                .attr("xmlns", "http://www.w3.org/2000/svg")
                .node().parentNode.innerHTML;

            var imgsrc = 'data:image/svg+xml;base64,' + btoa(html);

            var canvas = ng.element("body").find("canvas")[0];
            if(!canvas) {
                canvas = ng.element("<canvas style='display:none'></canvas>")[0];
                ng.element("body").eq(0).append(canvas);
            }

            console.debug("canvas", canvas);
            var context = canvas.getContext("2d");
            canvas.width = svg.attr('width');
            canvas.height = svg.attr('height');

            var image = new Image;
            image.src = imgsrc;
            image.onload = function () {

                canvas.style.opacity = 1;
                context.beginPath();
                context.rect(0, 0, canvas.width, canvas.height);
                context.fillStyle = "#FFFFFF";
                context.fill();

                context.drawImage(image, 0, 0);

                canvas.toBlob(function (blob) {
                    saveAs(blob, name + ".png");
                });
            };

        }

    }
    factory.$name="mevSvgSaveAs";
    factory.$inject=[];
    factory.$provider="factory";
    return factory;
});