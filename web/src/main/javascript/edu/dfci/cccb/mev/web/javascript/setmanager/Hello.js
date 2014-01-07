(function(){
	
	var Hello = function(){ //Hello Class Definition
		
		return {
			sayHi: function(name){
				alert('Hi, ' + name);
			}
		};
	};
	
	return Hello;
}());