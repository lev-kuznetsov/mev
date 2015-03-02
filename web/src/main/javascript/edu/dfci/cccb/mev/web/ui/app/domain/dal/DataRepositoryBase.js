define(["classjs"], function(Class){
	
	var DataRepositoryBase = Class.extend({
		
		constructor: function($resource, serviceBaseUri){
			this.$resource=$resource;
			this.serviceBaseUri=serviceBaseUri;
			this._source=null;
		},		
		rootUrl: function(){
			return this.serviceBaseUrl;
		},
		source: function(){			
			console.error("No endpoint at the base url"+this.rootUrl)
			return this._source;
		} 
	});
	DataRepositoryBase.$inject=["$resource", "serviceBaseUri"];
});