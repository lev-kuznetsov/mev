define(["lodash"], function(_){
    var defaultColor = {
        label: "Blue,Yellow",
        range: ["blue", "yellow"]
    };
    var factory = function($rootScope){

        function mixinColorDimConfig(colorConfig, data, onUpdateColor){
            function updateColor(color){
                this.colors = color;
                defaultColor = color;
                this.scale = d3.scale.linear().domain([colorConfig.min, colorConfig.max]).range(colorConfig.colors.range)
                if(onUpdateColor)
                    onUpdateColor();
            };
            if(!colorConfig)
                throw new Error("colorConfig is undefined " + JSON.stringify(colorConfig));
            _.extend(colorConfig, {
                sync: true,
                min: 0,
                max: data.length>0 ? colorConfig.get(_.maxBy(data, colorConfig.get)) : 0,
                updateColor: function(color){
                    updateColor.call(this, color);
                    $rootScope.$broadcast("mui:charts:color:updated", this, this.colors);
                }.bind(colorConfig),
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
            colorConfig.colors = defaultColor || colorConfig.colorOptions.available[0];
            _.extend(colorConfig, {
                scale: d3.scale.linear().domain([colorConfig.min, colorConfig.max]).range(colorConfig.colors.range)
            });
            $rootScope.$on("mui:charts:color:updated", function($evnet, source, color){
                if(colorConfig.sync && source!==this)
                    updateColor.call(this, color);
            }.bind(colorConfig));
            return colorConfig;
        }
        return mixinColorDimConfig;
    };
    factory.$name="mevChartColorDimConfig";
    factory.$provider="factory";
    factory.$inject=["$rootScope"];
    return factory;
})