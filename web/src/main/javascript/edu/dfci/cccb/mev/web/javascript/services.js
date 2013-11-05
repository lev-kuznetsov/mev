define(['angular'], function(angular){

  return angular.module('myApp.services', [])
  	.value('appVersion', '0.1')
  	.value('appName', 'MEV: Multi-Experiment Viewer');
  
});