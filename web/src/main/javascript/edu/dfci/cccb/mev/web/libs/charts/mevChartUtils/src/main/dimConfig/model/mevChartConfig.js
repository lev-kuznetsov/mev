define(["lodash"], function(_){
    var factory = function(mevChartDimConfig, mevChartColorDimConfig){
        function ChartDimConfigMixin(config, options){
            if(!config)
                throw new Error("No config provided for ChartDimConfig");

            if(options)
                _.extend(config, options);

            mevChartDimConfig(config.x, config);
            mevChartDimConfig(config.y, config);

            if(config.size)
                mevChartDimConfig(config.size, config);

            if(config.color)
                mevChartColorDimConfig(
                    mevChartDimConfig(config.color, config),
                    config.data, config.onUpdateColor);

            if(config.z){
                mevChartDimConfig(config.z, config);
                if(config.z.display==="color")
                    mevChartColorDimConfig(config.z, config.data, config.onUpdateColor);
            }


        }
        return ChartDimConfigMixin;
    };
    factory.$name="mevChartConfig";
    factory.$provider = "factory";
    factory.$inject = ["mevChartDimConfig", "mevChartColorDimConfig"];
    return factory;
});