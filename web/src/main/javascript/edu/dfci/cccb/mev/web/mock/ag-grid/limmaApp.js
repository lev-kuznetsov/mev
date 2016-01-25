var module = angular.module("limmaApp", ["agGrid"]);

module.controller("limmaCtrl", function($scope, $http) {
	$scope.headers = [
          {
              'name': 'ID',
              'field': "id",
              'icon': "search"
          },
          {
              'name': 'Log-Fold-Change',
              'field': "logFoldChange",
              'icon': [">=", "<="]
          },
          {
              'name': 'Average Expression',
              'field': "averageExpression",
              'icon': [">=", "<="]
          },
          {
              'name': 'P-Value',
              'field': "pValue",
              'icon': "<=",
              'default': 0.05
          },
          {
              'name': 'Q-Value',
              'field': "qValue",
              'icon': "<="
          }
      ];
 	
     var columnDefs = $scope.headers.map(function(header){
     	header["headerName"]=header.name;
     	return header;
     });
     	
     var rowData = [
//                   {make: "Toyota", model: "Celica", price: 35000},
//                   {make: "Ford", model: "Mondeo", price: 32000},
//                   {make: "Porsche", model: "Boxter", price: 72000}
     ];	
     $scope.gridOptions = {
       columnDefs: columnDefs,
       rowData: rowData
     };
     
     $http.get("limma.json").then(function(response){
    	 $scope.gridOptions.api.setRowData(response.data.results); 
     });

});