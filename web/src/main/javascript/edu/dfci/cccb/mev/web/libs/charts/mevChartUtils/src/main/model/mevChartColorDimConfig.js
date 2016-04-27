define(["lodash"], function(_){
    function mixinColorDimConfig(colorConfig, data){
        if(!colorConfig)
            throw new Error("colorConfig is undefined " + JSON.stringify(colorConfig));
        _.extend(colorConfig, {
            colors: ["blue", "yellow"],
            min: 0,
            max: colorConfig.get(_.maxBy(data, colorConfig.get))
        });
        _.extend(colorConfig, {
            scale: d3.scale.linear().domain([colorConfig.min, colorConfig.max]).range(colorConfig.colors)
        });
        return colorConfig;
    }

    var factory = function(){
        return mixinColorDimConfig;
    };
    factory.$name="mevChartColorDimConfig";
    factory.$provider="factory";
    factory.$inject=[];
    return factory;
})