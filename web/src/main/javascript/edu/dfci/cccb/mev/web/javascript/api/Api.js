define(['jquery', 'angular'], function(jquery, angular) {
    "use strict";
    return angular.module('Mev.Api', ['ngResource'])
        .factory('API', ['$resource', '$http', function($resource, $http) {
            return {
                user : {
                    datasets : function() {
                        var q = {
                            method : 'GET',
                            url : '/dataset',
                            params : {
                                format:'json'
                            }
                        };
                        
                        return $http({
                            method : 'GET',
                            url : '/dataset',
                            params : {
                                format:'json'
                            }});
                    }
                },
                dataset : {
                    
                    selections : function(){
                        var access = $resource('/dataset/:dataset/:dimension/selection',
                                {format:'json'});
                        
                        return access;
                    },
                    data : function(){
                        var access = $resource('/dataset/:dataset/data',
                                {format:'json'});
                        
                        return access;
                    },
                    analysis : function(){
                        var access = $resource('/dataset/:dataset/analysis/:name',
                            {format:'json'},
                            {
                                'list':{
                                    method:'GET', 
                                    url:'/dataset/:dataset/analysis/'
                                },
                                'create.limma':{
                                    method:'POST', 
                                    url:'/dataset/:dataset/analyze/limma/'
                                        + ":name(dimension=:dimension"
                                        + ",experiment=:experiment"
                                        + ",control=:control)"
                                },
                                'create.hcl':{
                                    method:'Post',
                                    url:'dataset/:dataset/analyze/hcl/'
                                        +  ':name(:dimension,:metric,:algorithm)',
                                }
                            }
                        );
                        
                        return access;
                    }
                }
            };
        }]);
  
  
});