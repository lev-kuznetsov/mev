define(["lodash"], function(_){
    function mixinColorDimConfig(colorConfig, data, onUpdateColor){
        if(!colorConfig)
            throw new Error("colorConfig is undefined " + JSON.stringify(colorConfig));
        _.extend(colorConfig, {
            min: 0,
            max: colorConfig.get(_.maxBy(data, colorConfig.get)),
            updateColor: function(color){
                this.colors = color;
                this.scale = d3.scale.linear().domain([colorConfig.min, colorConfig.max]).range(colorConfig.colors.range)
                if(onUpdateColor)
                    onUpdateColor();
            },
            colorOptions: {
                available: [
                    {
                        label: "Blue,Yellow",
                        range: ["blue", "yellow"]
                    },
                    {
                        label: "Red,Green",
                        range: ['red', 'green'],
                    },
                    {
                        label: "Red,Blue",
                        range: ['red', 'blue']
                    }
                ],
                find: function(color){
                    if(_.isArray(color.range) && color.label){
                        return color;
                    } else if(_.isArray(color))
                        return _.find(this.available, function(item){
                            return _.isEqual(color, item.range);
                        });
                    else
                        return _.find(this.available, function(item){
                            return color===item.label;
                        });
                }
            }
        });
        colorConfig.colors = colorConfig.colorOptions.available[0];
        _.extend(colorConfig, {
            scale: d3.scale.linear().domain([colorConfig.min, colorConfig.max]).range(colorConfig.colors.range)
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