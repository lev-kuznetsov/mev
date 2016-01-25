define(["lodash"], function(_){
	"use strict";
	return function(ng){		
		var _self= {};
		_self.providerMap = {
				service: "service",
				factory: "factory",
				directive: "directive",
				provider: "provider",
				controller: "controller",
				value: "value",
				constant: "constant",
				animation: "animation"
		};
		
		function MevError(msg){
			this.msg = msg;
		}
		
		_self.inferProvider = function inferProvider(func){
			if(typeof func !== "function")
				throw new MevError("Compnent with name '"+func.name+"' is not a function, but is "+typeof func);
			
			if(!func.$name)
				throw new MevError("Compnent name not specified for func "+func.name);
			
			var provider = func.provider;
			if(provider){
				if(!_self.providerMap[func.provider.toLowerCase()]){
					throw new MevError("Invalid angular provider '"+func.provider+"'");
				}
				return provider.toLowerCase();
			}
			
			for (var curProvider in _self.providerMap) {
				if (_self.providerMap.hasOwnProperty(curProvider)) {
					if(func.$name.toLowerCase().indexOf(curProvider) > -1)
						provider = curProvider;
				}
			}
			
			if(!provider)
				throw new MevError("Could not infer provider for component: " + JSON.stringify(func));
			
			func.provider = provider.toLowerCase();
			return func.provider;
		};
		
		_self.selectNgModules = function(deps){
			return deps.filter(function(dep){
				//all strings should be considered module names
				if(dep instanceof String || typeof dep === "string")
					return true;
				
				//if has the require array and a provider function -> assume AngularJS module
				try{					
					if(Array.isArray(dep.requires) && typeof dep.provider === "function")
						return true;
				}catch(e){
					console.error("invalid dep", e);
					throw e;
				}
				
			}).map(function(dep){
				//return names only
				//all strings should be considered module names
				if(dep instanceof String || typeof dep === "string")
					return dep;
				else
					return dep.name;
			});		
		};
		
		_self.formatComponentName = function(func){
			var name = func.$name;
			
			//if $name ends with the "provider" suffix remove the suffix
			//i.e. DashboardController -> Dashboard		 
			if(name.toLowerCase().indexOf(func.provider, this.length - func.provider.length) !== -1){			
				name = name.substring(0, name.length - func.provider.length);
			}
			
			//make sure directive names start with a lower case letter
			//otherwise camel case conversion to html tag name gets messed up in angular
			if(func.provider === "directive")
				name = name.substr(0, 1).toLowerCase() + name.substr(1);
			
			func.$name = name;
			return name;
		};
		
		_self.register = function(func){
			var provider = _self.inferProvider(func);
			
			var name = _self.formatComponentName(func);
			return this[provider](name, func);
		};
		
		_self.registerAll = function(components){
			var result;
			for(var i=0; i<components.length; i++){
				try{						
					result = this.mevRegister(components[i]);	
					console.debug("registerAll - regitered", components[i].$name);
				}catch(e){
					if(e instanceof MevError)
						console.debug("registerAll - skipping component ", e);
					else
						throw e;
				}
			}					
			return result ? result : this;		
			
		};
		
		_self._module = ng.module;
		ng.module = function(name, deps, args){
			//we are retrieving an existing module,
			//passthrough to the angular method
			if(typeof deps === "undefined")
				return _self._module(name);
			
			var modules = [];
			
			if(Array.isArray(deps)){
				//deps is an array [] they are most likely a list of explicit dependencies, like normal angular
				modules = modules.concat( _self.selectNgModules(deps));
			}else if(typeof deps.length !== "undefined"){
				//deps is an object wiht "length" property it is and requirejs argumetn array, 
				//so find angular js modules in that list
				deps = Array.prototype.slice.call(deps);
				modules = modules.concat(_self.selectNgModules(deps));
			};
			
			if(args){
				//if "args" is passed in, most likely caller is using "deps" for explicit dependencies and args for requirejs
				var aArgs = Array.prototype.slice.call(args);
				modules = modules.concat(_self.selectNgModules(aArgs));
			}
			modules = _.uniq(modules);
			var module = _self._module(name, modules);
			console.info("registered module " + module.name + " with " + modules.length + " dependencies ", modules);
			module.mevRegister =_self.register.bind(module);
			module.mevRegisterAll = _self.registerAll.bind(module);
			if(args){
				//if the third parameter is supplied, 
				//attempt to identify mev components and register them
				module.mevRegisterAll(args);
			}
			return module;
			
		};
		
		
		
	}
});
