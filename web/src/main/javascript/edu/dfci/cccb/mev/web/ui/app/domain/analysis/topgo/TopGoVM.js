define([], function(){
	return function TopGoVM(){
		this.species = ["human", "mouse"];
		this.goType = ["BP", "CC", "MF"];
		this.testType = ['fisher'];
		this.pAdjust = ["fdr", "hochberg", "bonferroni", "BH", "fdr", "none"];
		this.nodeSize = 100;
	}
});