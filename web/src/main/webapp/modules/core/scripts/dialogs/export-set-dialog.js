function ExportSetDialog() {    
    this._createDialog();    
}

ExportSetDialog.prototype._createDialog = function() {
    var self = this;
    var dialog = $(DOM.loadHTML("core", "scripts/dialogs/export-set-dialog.html"));
    this._elmts = DOM.bind(dialog);
    
    //this._elmts.controls.find("textarea").bind("keyup change input",function() { self._scheduleUpdate(); });
    
    //this._elmts.resetButton.html($.i18n._('core-buttons')["reset-template"]);
    this._elmts.exportSetButton.html($.i18n._('core-buttons')["export"]);
    this._elmts.cancelSetButton.html($.i18n._('core-buttons')["cancel"]);
    
    this._elmts.exportSetButton.click(function() { self._export(); self._dismiss(); });
    this._elmts.cancelSetButton.click(function() { self._dismiss(); });
    /*
    this._elmts.resetButton.click(function() {
        self._fillInTemplate(self._createDefaultTemplate());
        self._updatePreview();
    });
    */
    
    this._level = DialogSystem.showDialog(dialog);
};

ExportSetDialog.prototype._dismiss = function() {
    DialogSystem.dismissUntil(this._level - 1);
};

ExportSetDialog.prototype._export = function() {
	  //var name = window.prompt("Esport set name", "open-refine-exported-set");
	  var name = this._elmts.setName[0].value;	  
	  if (!name) {
		this._elmts.errorMessage.html("Name is required");
		this._elmts.setName[0].focus();
	    return;
	  }
	  
	  var description = this._elmts.setDescription[0].value;
	  description = $.trim(description);	  
	  
	  var color = this._elmts.setColor[0].value;
	  color = $.trim(color);
	  if (!color) {
		this._elmts.errorMessage.html("Color is required");		
		this._elmts.setColor[0].focus();
		return;
	}
		  
	  
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
	  .attr("name", "set-name")
	  .attr("value", name)
	  .appendTo(form);
	  
	  $('<input />')
	  .attr("name", "set-description")
	  .attr("value", description)
	  .appendTo(form);
	  
	  $('<input />')
	  .attr("name", "set-color")
	  .attr("value", color)
	  .appendTo(form);
	  
	  $('<input />')
	  .attr("name", "engine")
	  .attr("value", JSON.stringify(ui.browsingEngine.getJSON()))
	  .appendTo(form);
	  
	  document.body.appendChild(form);
	  form.submit();
	  document.body.removeChild(form);
	  
	/*
	  name = $.trim(name);
	  if (theProject.metadata.name == name || name.length === 0) {
	    return;
	  }

	  $.ajax({
	    type: "POST",
	    url: "command/core/rename-project",
	    data: { "project" : theProject.id, "name" : name },
	    dataType: "json",
	    success: function (data) {
	      if (data && typeof data.code != 'undefined' && data.code == "ok") {
	        theProject.metadata.name = name;
	        Refine.setTitle();
	      } else {
	        alert($.i18n._('core-index')["error-rename"]+" " + data.message);
	      }
	    }
	  });
	  */

}