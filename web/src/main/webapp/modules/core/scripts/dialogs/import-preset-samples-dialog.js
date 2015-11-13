function ImportPresetSamplesDialog() { 	
	
    this._createDialog();
    
}

ImportPresetSamplesDialog.prototype._lastItem=null;


ImportPresetSamplesDialog._isRowLimitMet=function(){
	return true;
	var limit=200;
	if(theProject.rowModel.filtered>limit){
		alert("Cannot import more than "+limit+" samples");
		return false;
	}
	return true;
};

ImportPresetSamplesDialog.prototype._createDialog = function() {
		
	if(!this._isRowLimitMet()){
		alert("Cannot import more than 50 samples");
		return;
	}
	
    var self = this;
    var dialog = $(DOM.loadHTML("core", "scripts/dialogs/import-preset-samples-dialog.html"));
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

ImportPresetSamplesDialog.prototype._dismiss = function() {
    DialogSystem.dismissUntil(this._level - 1);
};

ImportPresetSamplesDialog.prototype._validate = function()
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

ImportPresetSamplesDialog._exportAjax = function(onSuccess, onError){
	var postRequest = {
		    type: "POST",
		    url: "command/core/import-preset-filter-samples",
		    data: { 
		    	"project" : theProject.id, 
		    	//"import-preset" : theProject.metadata.name,
		    	"import-preset" : $.url().param('import-preset'),
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

ImportPresetSamplesDialog._exportWait = function(){
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
//	        parent.OpenRefineBridge.openDataset(Refine._lastItem);
	        var currentUrl = window.location.href;
	        console.log("currentUrl:"+currentUrl);
//	        var newUrl = "/#/dataset/"+Refine._lastItem.name+"/";
//	        /annotations/import-dataset/command/core/view-preset-sample-annotations?import-preset="+presetName+"&dimension=column";
	        var newUrl = "/annotations/import-dataset/command/core/view-preset-row-annotations?"+
	        "import-preset="+$.url().param('import-preset')
	        +"&dimension=row";
//	        +"&samples="+data.keys
//	        +"&samplesprojname="+data.samplesprojname;	        
	        console.log("newUrl:"+newUrl);
	        window.location.replace(newUrl);	        
	        
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
