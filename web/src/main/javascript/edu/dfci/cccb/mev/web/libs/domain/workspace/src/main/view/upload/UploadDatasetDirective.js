define(["./UploadDataset.tpl.html"], function(template){
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

                elInput.on(
                    "change",
                    function() {
                        var input = elInput[0],
                            files = new Array();
                        for ( var i = 0; i < input.files.length; i++) {
                            files.push(input.files[i]);
                            if (files.length == input.files.length) {
                                files.map(function(file){
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

