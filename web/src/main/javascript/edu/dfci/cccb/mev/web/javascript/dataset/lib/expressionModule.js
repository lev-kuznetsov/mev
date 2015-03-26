(function(){

    define([], function(){

        return {

             'retrieve': function(searchValue){
            	 
                 var self = this

                 if (searchValue['row'] && !(searchValue['column']) ){
                     //Row Case
                     return self.column.keys.map(function(col){ 
                         return self.expression.get([searchValue['row'], col])
                     })
                     .filter(function(exp){ return (typeof exp != 'undefined') })


                 } else if (searchValue['column'] && !(searchValue['row']) ){
                     //Column Case
                     return self.row.keys.map(function(row){ 
                         return self.expression.get([row, searchValue['column']])
                     })
                     .filter(function(exp){ return (typeof exp != 'undefined') })

                 } else if (searchValue['column'] && searchValue['row']) {
                     //Both Case
                     var elem = [self.expression
                        .get([searchValue['row'], searchValue['column']])]

                     return elem
                        .filter(function(exp){ return (typeof exp != 'undefined') })
                  
                 } else {
                     //Neither Case
                     return []
                 }

             }

        }

    })

})()
