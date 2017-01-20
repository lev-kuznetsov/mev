define(["lodash"], function(_){
	var TcgaPreset = function TcgaPreset(DS){
		return DS.defineResource({
			name: 'TcgaPreset',
			endpoint: "presets/tcga",
			idAttribute: "name",
			computed: {
				dataLevelNorm: ['dataLevel', function (dataLevel) {
					return _.isNaN(parseInt(dataLevel))
						? dataLevel
						: "Level_"+dataLevel;
				}],
			}
		});
	};
	TcgaPreset.$inject=["DS"];
	TcgaPreset.$name="TcgaPreset";
	TcgaPreset.$provider="factory";
	return TcgaPreset;
});