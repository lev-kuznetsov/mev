define(
		[ 'angular', 'd3' ],
		function(angular, d3) {

			return angular
					.module('myApp.services', [])
					.value('appVersion', '0.1')
					.value('appName', 'MEV: Multi-Experiment Viewer')
					.factory('mainMenuBarOptions', [ function() {
						return [ {
							value : "About",
							url : "#"
						}, {
							value : "Contact",
							url : "#"
						} ];
					} ])
					.factory('alertService', [ function() {

						return {
							success: function(message, header, callback, params){
								
								alert(header + '\n'+ message);
								//Fix this later with something interesting
								
							},
							error: function(message, header, callback, params){
								
								alert(header + '\n'+ message);
								//Fix this later with something interesting
								
							}
						};

					} ])
					.factory('heatmapGenerator', [ function() {
						return null;
					} ])
					.factory('QHTTP', [ '$http', '$q', function($http, $q) {

						return function(params, callback_fn, error_fn) {
							var deferred = $q.defer();

							$http(params).success(function(data, status) {

								deferred.resolve(callback_fn(data, status));

							}).error(function(data, status) {

								deferred.reject(error_fn(data, status));

							});

							return deferred.promise;
						};

					} ])
					.factory(
							'API',
							[
									'QHTTP',
									function(QHTTP) {

										return {

											heatmap : {
												get : function(url) {
													var params = {
														method : 'GET',
														url : 'heatmap/'
																+ url
																+ '?format=json',
														format : 'json'
													};
													return QHTTP(params,
															function(d, s) {
																return d;
															}, function(d, s) {

																return d, s;
															});

												}
											},
											user : {
												datasets : {
													get : function() {

														var params = {
															method : 'GET',
															url : '/dataset?format=json'
														};
														return QHTTP(
																params,
																function(d, s) {
																	return d;
																},
																function(d, s) {

																	return d, s;
																});

													}
												}
											},
											dataset : {
												get : function(url) {

													var params = {
														method : 'GET',
														url : '/dataset/'
																+ url
																+ '/data'
																+ '?format=json'
													};

													return QHTTP(params,
															function(d, s) {
																return d;
															}, function(d, s) {

																return d, s;
															});

												}
											},
											analysis : {
												hcl : {
													create : function(q) {

														var params = {

															method : 'POST',
															url : 'analysis/hcl/new/'
																	+ q.name
																	+ '('
																	+ q.dataset
																	+ ','
																	+ q.dimension
																	+ ','
																	+ q.metric
																	+ ','
																	+ q.algorithm
																	+ ')'

														};

														return QHTTP(
																params,
																function(d, s) {
																	
																	return null // fix this to alert something interesting
																},
																function(d, s) {
																	return d, s;
																});

													}
												}
											}
										}

									} ]);

		});