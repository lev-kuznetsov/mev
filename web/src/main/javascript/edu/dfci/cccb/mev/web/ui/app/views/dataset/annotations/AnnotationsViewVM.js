define(["ng"], function(ng){
	var AnnotationsViewVM=function AnnotationsViewVM(dataset, annotations){
		this.dataset=dataset;
		this.annotations=annotations;
	};
	AnnotationsViewVM.$name="AnnotationsViewVM";
	AnnotationsViewVM.$inject=["dataset", "annotations"];
	AnnotationsViewVM.$provider="controller";
	return AnnotationsViewVM;
});