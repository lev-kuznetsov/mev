define(["q", "./AnnotationSetMeta"], function($q, AnnotationSetMeta){
	function AnnotationSet(raw){
		var self=this;
		var _raw=raw;		
		
		//public properties
		self.meta=new AnnotationSetMeta(raw.meta);			
		self.data=raw.data;
		self.name=self.meta.name;
		
	};
	return AnnotationSet;
});