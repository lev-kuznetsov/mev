define([], function(){
	
	return function (){
	//function to clear analysis list to undefined state for reset
    	this.analysisList.hierarchical = [];
    	this.analysisList.limma = [];
    	this.analysisList.kMeans = [];
    	this.analysisList.tTest = [];
    	this.analysisList.anova = [];
    	
	};
	
});