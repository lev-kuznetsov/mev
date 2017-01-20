define(["lodash"], function(_){
    function mixinGetter(dimConfig){
        if(_.isString(dimConfig.field)){
            dimConfig.get = function(d){
                return d[dimConfig.field];
            };
            dimConfig.label = dimConfig.label || dimConfig.field;
        }
        else if(_.isFunction(dimConfig.field))
            dimConfig.get = dimConfig.field;
        else
            throw new Error("DimConfig - no field specified: " + JSON.stringify(dimConfig));
    }

    function mixinDimConfig(dimConfig, chartConfig){
        if(!dimConfig)
            throw new Error("dimConfig is undefined " + JSON.stringify(dimConfig));
        mixinGetter(dimConfig);
        if(chartConfig) {
            _.extend(dimConfig, {
                chartConfig: chartConfig
            })
        }
        return dimConfig;
    }

    var factory = function(){
      return mixinDimConfig;
    };
    factory.$name="mevChartDimConfig";
    factory.$provider="factory";
    factory.$inject=[];
    return factory;
})