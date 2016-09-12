define(["lodash"], function(_){
    var run = function($rootScope, $window, mevGaTracker, $state, $stateParams){
        $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams){
            mevGaTracker.pageView("view")
        });

        $rootScope.$on("openRefine:loadedAnnotations", function(event, project){
            var page = _.get(project, "metadata.name");
            if(page)
                mevGaTracker.pageView(page);
        });

        function parseEvent(event, label, value){
            var re = /(mev|mui)(:dataset|:workspace)/g;
            var result= re.exec(event.name)
            if(!result){
                console.warning("Failed to parse GA event", event, label);
                return {
                    category: event.name,
                    action: "fired",
                    label: label,
                    value: value
                }
            }else{
                var action = event.name.substring(re.lastIndex);
                if(action.charAt(0)===':')
                    action = action.substring(1);
                return {
                    category: result[0],
                    action: action,
                    label: label,
                    value: value
                }
            }
        }
        function appendAnalysisInfo(event){
            var analysis = $state.$current.path[$state.$current.path.length-1].locals.globals.analysis || {};
            if(analysis.params && analysis.params.analysisType)
                event.category += ":analysis:"+analysis.params.analysisType;
            return event;
        }
        $rootScope.$on("mev:dataset:activated", function(event, dataset){
            mevGaTracker.event(parseEvent(event, dataset.id));
        });
        $rootScope.$on("mev:dataset:exported", function(event, dataset){
            mevGaTracker.event(parseEvent(event, dataset.id));
        });
        $rootScope.$on("mev:dataset:imported", function(event, file){
           mevGaTracker.event(parseEvent(event, file.name));
        });
        $rootScope.$on("mev:dataset:uploaded", function(event, file){
            mevGaTracker.event(parseEvent(event, file.name));
        });
        $rootScope.$on("mui:dataset:selections:added", function(event, dimension){
            mevGaTracker.event(appendAnalysisInfo(parseEvent(event, dimension)));
        });
        $rootScope.$on("mui:dataset:selections:deleted", function(event, dimension){
            mevGaTracker.event(appendAnalysisInfo(parseEvent(event, dimension)));
        })

        $rootScope.$on("mev:workspace:imports:geo:search:started", function(event, keywords){
            mevGaTracker.event(parseEvent(event, keywords));
        });
        $rootScope.$on("mev:workspace:imports:geo:search:notfound", function(event, keywords){
            mevGaTracker.event(parseEvent(event, keywords));
        });
        $rootScope.$on("mev:workspace:imports:geo:search:found", function(event, keywords, count){
            mevGaTracker.event(parseEvent(event, keywords, count));
        });

        function analysisEvent(type, status){
            return {
                category: "mev:dataset:analysis",
                action: type,
                label: type+":"+status
            }
        }
        $rootScope.$on("event:analysis:start", function(type, name, data){
            mevGaTracker.event(analysisEvent(type, "start"));
        });
        $rootScope.$on("event:analysis:success", function(type, name, data){
            mevGaTracker.event(analysisEvent(type, "success"));
        });
        $rootScope.$on("event:analysis:failure", function(type, name, data){
            mevGaTracker.event(analysisEvent(type, "failure"));
        });
        // $rootScope.$on("mui:modal:shown", function(event, id, header){
        //     var page = id;
        //     mevGaTracker.pageView(page);
        // });
    };
    run.$inject=["$rootScope", "$window", "mevGaTracker", "$state", "$stateParams"];
    run.$provider="run";
    return run;
});