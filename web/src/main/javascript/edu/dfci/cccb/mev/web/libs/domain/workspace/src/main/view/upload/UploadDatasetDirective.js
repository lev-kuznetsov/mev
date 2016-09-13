define(["lodash", "./UploadDataset.tpl.html"], function(_, template){
    var directive = function(DatasetResource){

        return {
            restrict : 'CEA',
            template : template,
            link : function(scope, elem, attrs) {
                var elButton = elem.find(".btn");
                var elInput = elem.find("input");
                elButton.click(function() {
                    elInput.click();
                });
                
                scope.vm = {
                    isInProgress: false,
                    progress: function(progress, event){
                        scope.$apply(function(){
                            scope.vm.percent = progress;
                            if(progress>=100)
                                scope.vm.isInProgress = false;
                            else
                                scope.vm.isInProgress = true;
                        })
                    }
                };
                scope.$on("mev:dataset:upload:started", function(){
                    scope.vm.isInProgress = true;
                    scope.$apply();
                });
                scope.$on("mev:dataset:uploaded", function(){
                    scope.vm.isInProgress = false;
                    scope.$apply();
                });
                elInput.on(
                    "change",
                    function() {
                        var input = elInput[0],
                            files = new Array();
                        for ( var i = 0; i < input.files.length; i++) {
                            files.push(input.files[i]);
                            if (files.length == input.files.length) {
                                files.map(function(file){
                                    if(_.endsWith(file.name, ".zip"))
                                        DatasetResource.importZip(file);
                                    else
                                        DatasetResource.uploadFile(file);
                                });
                            }
                        }
                    });
            }
        };
    };
    directive.$name="mevDatasetUpload";
    directive.$inject=["mevDatasetRest"];
    directive.$provider="directive";
    return directive;
});

