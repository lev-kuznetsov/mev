define(["./views.dataset.selectionSets.edit.tpl.html", "angular-color-picker", "angular-color-picker/angular-color-picker.css"], function(template){

    var directive = function(){
        return {
            restrict: "AEC",
            replace: true,
            template: template
        }
    };
    directive.$name="SelectionSetEdit";
    directive.$provider="directive";
    return directive;

});