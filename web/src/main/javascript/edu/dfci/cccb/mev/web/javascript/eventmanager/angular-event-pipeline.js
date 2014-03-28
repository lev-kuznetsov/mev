define([angular], function(angular){
    
    //src: https://github.com/thebigredgeek/angular-event-pipeline/
    
    'use strict';

        angular
          .module('Mev.events')
          .provider('Mev.events.eventPipeline',[
        
            function(){

              var publicMembers = {},
                  privateMembers = {};
        
        
        
              privateMembers.eventHash = {};
        
              privateMembers.listenerExecutionDelay = 0;
        
              privateMembers.counter = 0;
        
              privateMembers.pipelineFactory = function(room, $timeout){
                var ret = false;
        
                if(!privateMembers.eventHash[room]){
                  throw new Error('\''+room+'\' is not defined');
                }
                else{
                  ret = {
                    list: function(){
                      var i,
                          list = [];
        
                      for(i in privateMembers.eventHash[room]){
                        if(privateMembers.eventHash[room].hasOwnProperty(i)){
                          list.push(i);
                        }
                      }
        
                      return list;
                    },
                    exists: function(name){
                      return privateMembers.eventHash[room][name] !== undefined;
                    },
                    on: function(name, filter, listener){
                      var hash,
                          self = this;
        
                      listener = listener || filter;
        
                      if(privateMembers.eventHash[room][name]){
                        hash = privateMembers.generateListenerHash();
                        privateMembers.eventHash[room][name][hash] = (function(listener){ //wrap so we can delete without deleting the function that is passed
                          return function(data){
                            var pass = true,
                                i;
                            if(typeof filter !== typeof listener){
                              for(i in filter){
                                pass = (filter[i] === data[i]);
                              }
                            }
        
                            if(pass){
                              listener.apply(self, arguments);
                            }
                          };
                        }(listener));
                      }
                      else{
                        throw new Error('\''+room+'.'+name+'\' not found in event registry');
                      }
                      return hash;
                    },
                    once: function(name, filter, listener){
                      var hash,
                          self = this;
        
                      listener = listener || filter;
                      if(privateMembers.eventHash[room][name]){
                        hash = privateMembers.generateListenerHash();
                        privateMembers.eventHash[room][name][hash] = (function(listener, hash){ //wrap so we can delete without deleting the function that is passed
                          return function(data){
                            var pass = true,
                                i;
                            if(typeof filter !== typeof listener){
                              for(i in filter){
                                if(filter.hasOwnProperty(i)){
                                  pass = (filter[i] === data[i]);
                                }
                              }
                            }
                            if(pass){
                              listener.apply(self, arguments);
                              self.off(name,hash);
                            }
                          };
                        }(listener, hash));
                      }
                      else{
                        throw new Error('\''+room+'.'+name+'\' not found in event registry');
                      }
                      return hash;
                    },
                    off: function(name, hash){
                      var i;
                      if(privateMembers.eventHash[room][name]){
                        if(hash){
                          if(privateMembers.eventHash[room][name][hash]){
                            delete privateMembers.eventHash[room][name][hash];
                          }
                        }
                        else{
                          for(i in privateMembers.eventHash[room][name]){
                            if(privateMembers.eventHash[room][name].hasOwnProperty(i)){
                              delete privateMembers.eventHash[room][name][i];
                            }
                          }
                        }
                      }
                      else{
                        throw new Error('\''+room+'.'+name+'\' not found in event registry');
                      }
                    },
                    emit: function(name, data){
                      //console.log('emitting event', name);
                      if(privateMembers.eventHash[room][name]){
                        $timeout(function(){
                          privateMembers.execute(room, name, data);
                        },privateMembers.listenerExecutionDelay);
                      }
                      else{
                        throw new Error('\''+room+'.'+name+'\' not found in event registry');
                      }
                    }
                  };
                }
                return ret;
              };
        
              privateMembers.generateListenerHash = function(){
                privateMembers.counter = (privateMembers.counter === 9007199254740992) ? 0 : privateMembers.counter; //handle overflow
                return (privateMembers.counter++) + '';
              };
        
        
              privateMembers.execute = function(room, name, data){
                var hash;
                for(hash in privateMembers.eventHash[room][name]){
                  if(privateMembers.eventHash[room][name].hasOwnProperty(hash)){
                    privateMembers.eventHash[room][name][hash](data);
                  }
                }
              };
        
              publicMembers.exists = function(room, name){
                var ret = false;
        
                if(privateMembers.eventHash[room] && !name){
                  ret = true;
                }
                else if(privateMembers.eventHash[room] && privateMembers.eventHash[room][name]){
                  ret = true;
                }
        
                return ret;
              };
        
              publicMembers.registerRoom = function(room){
                if(!privateMembers.eventHash[room]){
                  privateMembers.eventHash[room] = {};
                }
                return this;
              };
        
              publicMembers.unregisterRoom = function(room){
                if(privateMembers.eventHash[room]){
                  delete privateMembers.eventHash[room];
                }
                else{
                  throw new Error('\''+room+'\' is not defined');
                }
                return this;
              };
        
              publicMembers.registerEvent = function(room, name){
                if(privateMembers.eventHash[room]){
                  if(!privateMembers.eventHash[room][name]){
                    privateMembers.eventHash[room][name] = {};
                  }
                }
                else{
                  throw new Error('\''+room+'\' is not defined');
                }
                return this;
              };
        
              publicMembers.unregisterEvent = function(room, name){
                if(privateMembers.eventHash[room]){
                  if(privateMembers.eventHash[room][name]){
                    delete privateMembers.eventHash[room][name];
                  }
                  else{
                    throw new Error('\''+room+'.'+name+'\' is not defined');
                  }
                }
                else{
                  throw new Error('\''+room+'\' is not defined');
                }
                return this;
              };
        
        
        
              publicMembers.$get = [
                '$timeout',
        
                function($timeout){
                  return {
                    list: function(){
                      var i,
                          list = [];
        
                      for(i in privateMembers.eventHash){
                        if(privateMembers.eventHash.hasOwnProperty(i)){
                          list.push(i);
                        }
                      }
        
                      return list;
                    },
                    exists: function(room){
                      return privateMembers.eventHash[room] !== undefined;
                    },
                    room: function(room){
                      return privateMembers.pipelineFactory(room, $timeout);
                    }
                  };
                }
              ];
        
        
        
              return publicMembers;
        
            }
        
        
          ])
          .run([
        
            '$rootScope',
            '$timeout',
        
            function($rootScope, $timeout){
        
        
              $rootScope.$safeApply = function(fn){
                  var self = this;
        
                if(typeof fn === 'function'){
        
                  if( self.$$phase === '$apply' ||
                      self.$$phase === '$digest' ||
                      self.$root.$$phase === '$apply' ||
                      self.$root.$$phase === '$digest') {
        
                          fn();
                  }
                  else {
                        self.$apply(fn);
                  }
                }
              };
        
              $rootScope.$construct = function(members){
                var self = this;
                $timeout(function(){
                  self.$safeApply(function(){
                    var i;
                    for(i in members){
                      if(members.hasOwnProperty(i)){
                        self[i] = members[i];
                      }
                    }
                  });
                });
              };
        
              $rootScope.$destruct = function(members){
                var self = this;
                $timeout(function(){
                  self.$safeApply(function(){
                    var i;
                    for(i in members){
                      if(members.hasOwnProperty(i)){
                        self[i] = members[i] = null;
                      }
                    }
                  });
                });
              };
        
        
            }
          ]);
})