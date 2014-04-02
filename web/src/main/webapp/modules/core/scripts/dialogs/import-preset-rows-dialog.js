function ImportPresetRowsDialog() { 	
	
    this._createDialog();
    
}

ImportPresetRowsDialog.prototype._lastItem=null;


ImportPresetRowsDialog.prototype._createDialog = function() {
		
	if(theProject.rowModel.filtered>1000){
		alert("Cannot import more than 1000 rows");
		return;
	}
	
    var self = this;
    var dialog = $(DOM.loadHTML("core", "scripts/dialogs/import-preset-rows-dialog.html"));
    this._elmts = DOM.bind(dialog);
    this._name="";
    
    this._dimension="";
    if(theProject.metadata.customMetadata){
    	if(theProject.metadata.customMetadata.selectionName)
    		this._name=theProject.metadata.customMetadata.selectionName;
    	if(theProject.metadata.customMetadata.dimension)
    		this._dimension=theProject.metadata.customMetadata.dimension;
    	
    }
    this._elmts.setName[0].value=this._name;
    
    //this._elmts.controls.find("textarea").bind("keyup change input",function() { self._scheduleUpdate(); });
    
    //this._elmts.resetButton.html($.i18n._('core-buttons')["reset-template"]);
    this._elmts.exportSetButton.html("Save");
    this._elmts.exportSetAndExitButton.html("Save and Close");
    this._elmts.cancelSetButton.html($.i18n._('core-buttons')["cancel"]);
    
    this._elmts.exportSetButton.click(function() { if(self._validate()){self._exportWait(); self._dismiss(); }});    
    this._elmts.cancelSetButton.click(function() { self._dismiss(); });
    /*
    this._elmts.resetButton.click(function() {
        self._fillInTemplate(self._createDefaultTemplate());
        self._updatePreview();
    });
    */
    this._level = DialogSystem.showDialog(dialog);
};

ImportPresetRowsDialog.prototype._dismiss = function() {
    DialogSystem.dismissUntil(this._level - 1);
};

ImportPresetRowsDialog.prototype._validate = function()
{
	  //var name = window.prompt("Esport set name", "open-refine-exported-set");
	  var name = this._elmts.setName[0].value.trim();	  
	  if (!name) {
		this._elmts.errorMessage.html("Name is required");
		this._elmts.setName[0].focus();
	    return false;
	  }

	  this._name=name; 	  

	  return true;
};

ImportPresetRowsDialog.prototype._exportAjax = function(onSuccess, onError){
	var postRequest = {
		    type: "POST",
		    url: "command/core/import-preset-dataset",
		    data: { 
		    	"project" : theProject.id, 
		    	//"import-preset" : theProject.metadata.name,
		    	"import-preset" : $.url().param('import-preset'),
		    	"samples" : $.url().param('samples'),
		    	"samplesprojname" : $.url().param('samplesprojname'),
		    	"newDatasetName" : this._name,		    	
		    	"engine" : JSON.stringify(ui.browsingEngine.getJSON())
		    	},
		    dataType: "json",
		    success: function(data){
		    	onSuccess(data);
		    },
		    error: function(data){
		    	onError(data);		    	
		    },
		    complete: function(data){		    	}
		  };
		 
		Refine._lastItem={
			name: this._name,			
		};
		
		$.ajax(postRequest);
};

ImportPresetRowsDialog.prototype._exportWait = function(){
	  var done = false;
	  var dismissBusy = null;
	  Refine.setAjaxInProgress();

	  var closeWait = function(){
		  Refine.clearAjaxInProgress();
		  done = true;
		  if (dismissBusy) {
		      dismissBusy();
		  }		  
	  };
	  var showWait = function(){
		  window.setTimeout(function() {
			  if (!done) {
			    dismissBusy = DialogSystem.showBusy();
			 }
		  }, 500);  
	  }
	  var onSucess=function(data) {	    
		closeWait();		
	    if (data && typeof data.code != 'undefined' && data.code == "ok") {
	        //alert("Set saved succesfully");		        
	        parent.OpenRefineBridge.openDataset(Refine._lastItem);
	        var currentUrl = window.location.href;
	        console.log("currentUrl:"+currentUrl);
//	        var newUrl = "/#/dataset/"+Refine._lastItem.name+"/";
	        var newUrl = "/annotations/import-dataset/command/core/view-preset-row-annotations?"+
	        "import-preset="+$.url().param('import-preset')+"&dimension=row&samples="+data.keys;	        
	        console.log("newUrl:"+newUrl);
//	        window.location.replace(newUrl);
	      } else {
	        alert($.i18n._("Error while importing dataset:" + data.message));
	      }	    
	  };
	  var onError=function(data) {
		  closeWait();
		  alert($.i18n._("Error while importing dataset:" + data.message));
	  };
	  
	  //do
	  showWait();
	  this._exportAjax(onSucess, onError);
	  
};

ImportPresetRowsDialog.prototype._export = function() {  

	  var form = document.createElement("form");	  
	  $(form)
	  .css("display", "block")
	  .attr("method", "post")
	  .attr("action", "command/core/export-set/");	  	  
	  
	  $('<input />')
	  .attr("name", "project")
	  .attr("value", theProject.id)
	  .appendTo(form);
	  
	  $('<input />')
	  .attr("name", "selectionName")
	  .attr("value", this._name)
	  .appendTo(form);
	  	  
	  $('<input />')
	  .attr("name", "selectioncolor")
	  .attr("value", this._color)
	  .appendTo(form);
	  
	  $('<input />')
	  .attr("name", "engine")
	  .attr("value", JSON.stringify(ui.browsingEngine.getJSON()))
	  .appendTo(form);
	  
	  
	  document.body.appendChild(form);
	  form.submit();
	  document.body.removeChild(form);
	  
}