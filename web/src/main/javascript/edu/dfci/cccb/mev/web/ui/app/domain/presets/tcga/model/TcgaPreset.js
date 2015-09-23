define([], function(){
	var TcgaPreset = function TcgaPreset(DS){
		return DS.defineResource({
			name: 'TcgaPreset',
			endpoint: "presets/tcga",
			idAttribute: "name"
		});
	};
	TcgaPreset.$inject=["DS"];
	return TcgaPreset;
});