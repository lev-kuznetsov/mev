define(['angular', 'notific8'], function(angular){
	return angular.module('Mev.AlertService', [])
	.factory ('alertServiceFactory', [ function () {

            return {
              success : function (message, header, callback, params) {
                $.notific8(message, {
                  heading: header,
                  theme: 'lime',
                  life: 5000
                });
              },
              error : function (message, header, callback, params) {

                $.notific8('Issue: \n' +message, {
                  heading: header,
                  theme: 'ruby',
                  life: 5000
                });

              },
              info : function (message, header, callback, params) {
                $.notific8(message, {
                  heading: header,
                  theme: 'ebony',
                  life: 5000
                });
              }
              
            };

          } ]);
	
})