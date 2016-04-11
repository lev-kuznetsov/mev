define([], function(){
	
	//Constructor :: ?Object -> Params
	// Optional object is input object with default params if want to change
	return function (params) {
		
		var self = this;
		
		this.cell =  {height:15, width:15, padding: 0};

		this.panel = {
			side:{
				width: 150
			},
			top : {
				height: 150
			}
		};
		
		this.labels = {
			column:{
				height:80
			},
			row: {
				width: 50
			}
		};
		
		this.selections = {
			column:{
				height: 80
			},
			row:{
				width:80
			}
		};
		
		this.colors = {
			high: "yellow",
			mid: "black",
			low: "blue",
			group: "Blue,Black,Yellow"
		};
		
		if (params){
			
			properties = Object.getOwnPropertyNames(params);
			
			properties.map(function(prop){
				self[prop] = params[prop]
			});
			
		};
		
		
	};
	
});