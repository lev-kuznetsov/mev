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
                    status: "idle",

                    uploadProgress: function(progress, event){
                        scope.$apply(function(){
                            scope.vm.percent = progress;
                            if(progress>=100)
                                scope.vm.status = "processing";
                            else
                                scope.vm.status = "uploading";
                        })
                    },
                    completed: function(event){
                        scope.vm.status="idle";
                    }
                };

                //not needed since using callback, but could be used in future
                // scope.$on("mev:dataset:uploaded", function(){
                //     scope.vm.status = "idle";
                // });
                // scope.$on("mev:dataset:imported", function(){
                //     scope.vm.status = "idle";
                // });

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
                                        DatasetResource.importZip(file, scope.vm.uploadProgress, scope.vm.completed);
                                    else
                                        DatasetResource.uploadFile(file, scope.vm.uploadProgress, scope.vm.completed);
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

