define([], function(){
	var SigGenesFactory = function(){
		return function SigGenesFactory(n, genes, values, headers){		
			var _self = this;
			this.genes = genes;
			this.values = values;			
			if(Array.isArray(headers)){				
				this.headers = headers;
			}else{
				this.headers = [{
	                'name': 'ID',
	                'field': "geneId",
	                'icon': "search"
	            },{
	                'name': 'Deviation',
	                'field': "value",
	                'icon' : n>0 ? ">=" : "<="
	            }];
				if(typeof headers === "string"){
					this.headers[1].name = headers;
				}
			}
			
			function formatData(genes, values){
				return genes.map(function(gene, i){
					return {
						geneId: gene,
						value: values[i]
					};
				});
			};			
			function getN(n){
				if(n>0)
					return formatData(genes.slice(0, n), values.slice(0, n));
				else
					return formatData(genes.slice(genes.length + n), values.slice(values.length + n));
			}
			return {
				keys: genes.slice(0, 19),
				data: getN(n),
				headers: self.headers
			};
		};
	};
	
	SigGenesFactory.$name = "SigGenesFactory";
	SigGenesFactory.$inject=[];
	return SigGenesFactory;
});