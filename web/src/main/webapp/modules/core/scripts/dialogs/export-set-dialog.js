function ExportSetDialog(isNew) { 	
    this._createDialog(isNew);    
}

ExportSetDialog.prototype._lastItem=null;


ExportSetDialog.prototype._createDialog = function(isNew) {
    var self = this;
    this.isNew=isNew;
    if(isNew)	
    	this.dialog = $(DOM.loadHTML("core", "scripts/dialogs/export-set-dialog.html"));
	else
		this.dialog = $(DOM.loadHTML("core", "scripts/dialogs/update-set-dialog.html"));
    this._elmts = DOM.bind(this.dialog);
    this._name="";
    this._description="";
    this._color="";
    this._dimension="";
    this._oldName="";
    this._oldDescription="";
    this._oldColor="";
	if(theProject.metadata.customMetadata.selectionName)
		this._oldName=theProject.metadata.customMetadata.selectionName;
	if(theProject.metadata.customMetadata.selectionDescription)
		this._oldDescription=theProject.metadata.customMetadata.selectionDescription;	
	if(theProject.metadata.customMetadata.selectionColor)
		this._oldColor=theProject.metadata.customMetadata.selectionColor;	
	if(theProject.metadata.customMetadata.dimension)
		this._dimension=theProject.metadata.customMetadata.dimension;	
    if(isNew==false){
    	this._name=this._oldName;
    	this._description=this._oldDescription;
    	this._color=this._oldColor;    	    	
    	$(this.dialog+"#set-name").text(this._name);
    }else{    	
    	this._color='#'+(0x1000000+(Math.random())*0xffffff).toString(16).substr(1,6);
    }
    this._elmts.setName[0].value=this._name;
    this._elmts.setDescription[0].value=this._description;
    this._elmts.setColor[0].value=this._color;
    
    //this._elmts.controls.find("textarea").bind("keyup change input",function() { self._scheduleUpdate(); });
    
    //this._elmts.resetButton.html($.i18n._('core-buttons')["reset-template"]);
    this._elmts.exportSetButton.html("Save");
    this._elmts.exportSetAndExitButton.html("Save and Close");
    this._elmts.cancelSetButton.html($.i18n._('core-buttons')["cancel"]);
    
    this._elmts.exportSetButton.click(function() { if(self._validate()){self._exportAjax(); self._dismiss(); }});
    this._elmts.exportSetAndExitButton.click(function() { if(self._validate()){self._exportAjax(); self._dismiss(); Refine._close(); }});
    this._elmts.cancelSetButton.click(function() { self._dismiss(); });
    /*
    this._elmts.resetButton.click(function() {
        self._fillInTemplate(self._createDefaultTemplate());
        self._updatePreview();
    });
    */
    this._elmts.setColor.colorpicker({showOn:"button"});
    this._level = DialogSystem.showDialog(this.dialog);
};

ExportSetDialog.prototype._dismiss = function() {
    DialogSystem.dismissUntil(this._level - 1);
};

ExportSetDialog.prototype._validate = function()
{
	  //var name = window.prompt("Esport set name", "open-refine-exported-set");
	  var name = this._elmts.setName[0].value.trim();	  
	  if (!name) {
		this._elmts.errorMessage.html("Name is required");
		this._elmts.setName[0].focus();
	    return false;
	  }
	  	  
	  var color = this._elmts.setColor[0].value;
	  color = $.trim(color);
	  if (!color) {
		this._elmts.errorMessage.html("Color is required");		
		this._elmts.setColor[0].focus();
		//this._elmts.setColor.colorpicker("showPalette");
		return false;
	  }
	  
	  this._name=name;
	  this._color=color;
	  this._description = this._elmts.setDescription[0].value;
	  this._description=$.trim(this._description); 	  

	  return true;
};

ExportSetDialog.prototype._exportAjax = function(){
	var postRequest = {
		    type: "POST",
		    url: "command/core/export-set",
		    data: { 
		    	"project" : theProject.id, 
		    	"name" : name,
		    	"selectionName" : this._name,
		    	"selectionDescription" : this._description,
		    	"selectionColor" : this._color,
		    	"selectionFacetLink" : Refine.getPermanentLink(),
		    	"engine" : JSON.stringify(ui.browsingEngine.getJSON())
		    	},
		    dataType: "json",
		    success: function (data) {
		      if (data && typeof data.code != 'undefined' && data.code == "ok") {
		        //alert("Set saved succesfully");		        
		        parent.OpenRefineBridge.addSelectionSet(Refine._lastItem);
		        var currentUrl = window.location.href;
		        console.log("currentUrl:"+currentUrl);
		        var newUrl = currentUrl.replace("/new/", "/"+Refine._lastItem.name+"/");
		        newUrl = newUrl.replace("/"+Refine._lastItem.oldName+"/", "/"+Refine._lastItem.name+"/");
		        console.log("newUrl:"+newUrl);
		        window.location.replace(newUrl+Refine._lastItem.properties.selectionFacetLink);
		      } else {
		        alert($.i18n._('core-index')["error-rename"]+" " + data.message);
		      }
		    }
		  };
		 
		Refine._lastItem={
			name: this._name,
			oldName: this._oldName,
			dimension: this._dimension,
			properties: {
				selectionDescription: this._description,
				selectionColor: this._color,
				selectionFacetLink: Refine.getPermanentLink(),
			}
		};
		$.ajax(postRequest);
};

ExportSetDialog.prototype._export = function() {  

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
	  .attr("name", "selectiondescription")
	  .attr("value", this._description)
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