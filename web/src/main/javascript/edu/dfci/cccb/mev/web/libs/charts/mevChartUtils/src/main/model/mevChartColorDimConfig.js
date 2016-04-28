define(["lodash"], function(_){
    function mixinColorDimConfig(colorConfig, data, onUpdateColor){
        if(!colorConfig)
            throw new Error("colorConfig is undefined " + JSON.stringify(colorConfig));
        _.extend(colorConfig, {
            colors: ["blue", "yellow"],
            min: 0,
            max: colorConfig.get(_.maxBy(data, colorConfig.get)),
            updateColor: function(color){
                this.colors = color.range;
                this.scale = d3.scale.linear().domain([colorConfig.min, colorConfig.max]).range(colorConfig.colors)
                if(onUpdateColor)
                    onUpdateColor();
            }
        });
        _.extend(colorConfig, {
            scale: d3.scale.linear().domain([colorConfig.min, colorConfig.max]).range(colorConfig.colors)
        });
        return colorConfig;
    }

    var factory = function(){
        return mixinColorDimConfig;
    };
    factory.$name="mevChartColorDimConfig";32
    factory.$provider="factory";
    factory.$inject=[];
    return factory;
})