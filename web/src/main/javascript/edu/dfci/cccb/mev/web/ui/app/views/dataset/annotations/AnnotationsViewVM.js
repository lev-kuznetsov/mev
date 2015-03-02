define(["ng"], function(ng){
	var AnnotationsViewVM=function AnnotationsViewVM(dataset, annotations){
		this.dataset=dataset;
		this.annotations=annotations;
	};
	AnnotationsViewVM.$inject=["dataset", "annotations"];	
	return AnnotationsViewVM;
});