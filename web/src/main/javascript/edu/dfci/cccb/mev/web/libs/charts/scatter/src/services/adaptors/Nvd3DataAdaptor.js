"use strict";
define(["lodash"], function(_){
	var service = function Nvd3DataAdaptor(){
		function forEachById(input, fn, idField, thisArg){			
			return _.forEach(input, function(item, key, collection){																				
				var bind = thisArg || this;
				var id;
				if(_.isArray(collection))
					id = item[idField];
				else if(idField)
					id = item[idField];
				else
					id = key;

				return fn.call(bind, item, id, collection);					
			});
		}

		function transform(input, x, y, idField, selections, limit){
    		
    		if(!limit) limit = Infinity;
    		if(!selections) selections = [];    		
		    
		    var groups = {};
		    var count=0;

		    forEachById(input, function(item, id){
		        if(++count > limit)
		            return false;

		        var groupAcc = {names: [], color: "grey"};
		        _.map(selections, function(selection){
		            if(_.includes(selection.keys, id)){
		                groupAcc.names.push(selection.name);
		                groupAcc.color = selection.properties.selectionColor;
		            }
		        });

		        var groupName = "none";
		        if(groupAcc.names.length===1)
		            groupName = groupAcc.names[0];
		        else if(groupAcc.names.length>1)
		            groupName = groupAcc.names.join("+");

		        var group = groups[groupName];
		        if(!group){
		            groups[groupName] = group = {};
		            group.name = group.key = groupName;                  
		            group.names = groupAcc.names;
		            if(group.names.length<2){
		                group.color = groupAcc.color;
		            }else{
		                group.color = '#'+Math.floor(Math.random()*16777215).toString(16);
		            }   
		            group.shape = 'circle';
		            group.values=[];
		        }
		                    
		        group.values.push({
		            x: item[x],
		            y: item[y],
		            size: 10,
		            id: id
		        });
		    }, idField);         
		    
		    if(Object.keys(groups).length === 1 && groups.none)
		        groups.none.name = groups.none.key = "Selection: none";

		    return _.sortBy(groups, function(group){
		        return group.name === "none"  ? -Infinity : -group.names.length;                   
		    });
		}

		this.transform=transform;
		this.forEachById = forEachById;
	};
	service.$inject=[];
	service.$name="mevNvd3DataAdaptor";
	service.$provider="service";
	return service;
});