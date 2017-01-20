"use strict";
define(["mui", "./BsModal.tpl.html"], function(ng, template){
    function directive($rootScope){
    	 return {
            restrict : 'E',
            scope : {

                bindid : '@',
                header : '@',
                test : '@',
                func : '&',
                headerHtml : '=',
                hideClose : '@'
            },
            transclude : true,
            template : template,
            compile: function(tElem, tAttrs){                                    	
            	return {
            		post: function(scope, elem, attrs, ctrl){
            			var rootElement = ng.element("body > ui-view:first-child");
            			
            			if(rootElement.length===0)
            				rootElement = ng.element("body > ng-view:first-child");
            			if(rootElement.length > 1)
            				rootElement = rootElement[0];

        			
        				var exists = rootElement.children("[bindid='"+scope.bindid+"']");                                    				
        				if(exists.length>0){                                    					                                    				
        					//we already have this modal, remove to avoid duplicates                                     					
        					exists.html('').remove();
        					console.debug("BSMODAL remove", attrs.bindid, rootElement.children("[bindid='"+scope.bindid+"']").length);
        				}
        				console.debug("BSMODAL appaned", attrs.bindid);
    					rootElement.append(elem);

                        var elemLabel = elem.find("#myModalLabel");
                        if(scope.headerHtml){
                            elemLabel.html(scope.headerHtml);
                        }
                        //not making use of this event right now, comment it
                        // var elemModal = elem.find(".modal");
                        // elem.on('shown.bs.modal', function () {
                        //     scope.$apply(function(){
                        //         $rootScope.$broadcast("mui:modal:shown", scope.bindid, scope.header);
                        //     });
                        // });
            		}
            	};
            }
        };
    }
    directive.$inject=["$rootScope"];
    directive.$name="mevBsModalDirective";
    return directive;

});