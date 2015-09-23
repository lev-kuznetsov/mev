(function(){
		
		if(!Function.prototype.bind){			
			Function.prototype.bind=function(){
				var fn=this;
				var context=Array.prototype.slice.call(arguments, 0, 1);
				var args=Array.prototype.slice.call(arguments, 1);
				return function(){
					var moreArgs = Array.prototype.slice.call(arguments, 0);
					var finalArgs = args.concat(moreArgs);
					return fn.apply(context, finalArgs);
				};
			};
		}
		
		if ( typeof ( console ) !== 'undefined' && console != null) {
				console.log("Log exists!");
		}else {
			console = {
				log: function () {},
				info: function () {},
				warn: function () {},
				error: function () {},
				debug: function () {}
			};
		}
		
})();