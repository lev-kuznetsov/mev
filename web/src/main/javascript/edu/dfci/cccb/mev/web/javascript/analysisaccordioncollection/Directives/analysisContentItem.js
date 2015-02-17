(function(){

  define([], function(){

    return function(module){

        module.directive('analysisContentItem', ['$compile', function ($compile) {
		var heirarchicalTemplate = '<hierarchical-Accordion analysis="analysis" project="project"></hierarchical-Accordion>';
		var kMeansTemplate = '<k-Means-Accordion analysis="analysis" project="project"></k-Means-Accordion>';
		var anovaTemplate = '<anova-Accordion analysis="analysis" project="project"></anova-Accordion>';
		var tTestTemplate = '<t-Test-Accordion analysis="analysis" project="project"></t-Test-Accordion>';
		var limmaTemplate  = '<limma-Accordion analysis="analysis" project="project"></limma-Accordion>';
		var fTestTemplate = '<f-Test-Accordion analysis="analysis" project="project"></f-Test-Accordion>';
		var wilcoxonTestTemplate = '<wilcoxon-Test-Accordion analysis="analysis" project="project"></wilcoxon-Test-Accordion>';
		var DESeqTemplate = '<deseq-Accordion analysis="analysis" project="project"></deseq-Accordion>';
		
		
		var getTemplate = function(analysisType) {
		    var template = '';
	    
		    switch(analysisType) {
			case 'Hierarchical Clustering':
			    template = heirarchicalTemplate;
			    break;
			case 'K-means Clustering':
			    template = kMeansTemplate;
			    break;
			case 'LIMMA Differential Expression Analysis':
			    template = limmaTemplate;
			    break;
			case 'Anova Analysis':
			    template = anovaTemplate;
			    break;
			case 't-Test Analysis':
			    template = tTestTemplate;
			    break;
			case 'Fisher test':
				template = fTestTemplate;
				break;
			case 'Wilcoxon test':
				template = wilcoxonTestTemplate;
				break;
			case 'DESeq Differential Expression Analysis':
				template = DESeqTemplate;
				break
		    }
	    
		    return template;
		}
	    
		return {
		    restrict: "E",
		    rep1ace: true,
		    scope: {
			analysis : '=analysis',
			project : '=project',
		    },
		    link: function(scope, element, attrs) {
			

			element.append(getTemplate(scope.analysis.type));
		
			$compile(element.contents())(scope);
		    }
		};
        }])

        return module

    }

  })

})()
