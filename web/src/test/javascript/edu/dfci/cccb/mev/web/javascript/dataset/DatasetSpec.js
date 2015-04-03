(function(){

	var deps = ['dataset/lib/DatasetClass']

	define(deps, function(Dataset){

		describe('Class: Dataset', function(){

			var dataset;

			beforeEach(function(){

				var mock_response_object = {
					'row':{
						'type':'row',
						'keys': ['gene1', 'gene2'],
						'selections':[] 
					},
					'column':{
						'type':'column',
						'keys': ['Sample1', 'Sample2', 'Sample3', 'Sample4'],
						'selections':[] 
					},
					'values':[{'row':'gene1', 'column':'Sample1', 'value':1},
					          {'row':'gene1', 'column':'Sample2', 'value':-1},
					          {'row':'gene1', 'column':'Sample3', 'value':1},
					          {'row':'gene1', 'column':'Sample4', 'value':1},
					          {'row':'gene2', 'column':'Sample1', 'value':1},
					          {'row':'gene2', 'column':'Sample2', 'value':-1},
					          {'row':'gene2', 'column':'Sample3', 'value':1},
					          {'row':'gene2', 'column':'Sample4', 'value':1}],
					'min':-1,
					'max':1,
					'avg': 0
				}

				dataset = new Dataset('mockDatasetName', mock_response_object)

			})

			it('should be defined', function(){
				expect(dataset).toBeDefined()
			})

                        describe('label indexed getters', function(){
                            describe('when given object with column property', function(){
                                describe('that is in column labels', function(){
                                    var searchValue
                                    beforeEach(function(){
                                        searchValue = {'column': 'Sample1'}
                                    })
                                    it('should return all values with matching column property',function(){
                                       var response = dataset.expression.retrieve(searchValue)
                                       expect(response).toContain({'row':'gene1', 'column':'Sample1', 'value':1})
                                       expect(response).toContain({'row':'gene2', 'column':'Sample1', 'value':1})
                                       expect(response.length).toBe(2)
                                    })
                                })
                                describe('that is not in column labels', function(){
                                    var searchValue
                                    beforeEach(function(){
                                        searchValue = {'column': 'Sample10'}
                                    })
                                    it('should return an empty list', function(){
                                       var response = dataset.expression.retrieve(searchValue)
                                       expect(response.length).toBe(0)
                                    })
                                })
                            })
                            describe('when given object with row property', function(){
                                describe('that is in row labels', function(){
                                    var searchValue
                                    beforeEach(function(){
                                        searchValue = {'row': 'gene1'}
                                    })
                                    it('should return all values with matching row property', function(){
                                       var response = dataset.expression.retrieve(searchValue)
				       expect(response).toContain({'row':'gene1', 'column':'Sample1', 'value':1})
				       expect(response).toContain({'row':'gene1', 'column':'Sample2', 'value':-1})
				       expect(response).toContain({'row':'gene1', 'column':'Sample3', 'value':1})
				       expect(response).toContain({'row':'gene1', 'column':'Sample4', 'value':1})
                                       expect(response.length).toBe(4)
                                    })
                                })
                                describe('that is not in row labels', function(){
                                    var searchValue
                                    beforeEach(function(){
                                        searchValue = {'row': 'gene10'}
                                    })
                                    it('should return an empty list', function(){
                                       var response = dataset.expression.retrieve(searchValue)
                                       expect(response.length).toBe(0)
                                    })
                                })
                            })
                            describe('when given object with row and column property', function(){
                                describe('that is in row and column labels', function(){
                                    var searchValue
                                    beforeEach(function(){
                                        searchValue = {'row': 'gene1', 'column': 'Sample2'}
                                    })
                                    it('should return a single element with matching row property', function(){
                                       var response = dataset.expression.retrieve(searchValue)
				       expect(response).toContain({'row':'gene1', 'column':'Sample2', 'value':-1})
                                       expect(response.length).toBe(1)
                                    })
                                })
                                describe('that is not not in row and column labels', function(){
                                    var searchValue
                                    beforeEach(function(){
                                        searchValue = {'row': 'gene10', 'column': 'Sample10'}
                                    })
                                    it('should return an empty list', function(){
                                       var response = dataset.expression.retrieve(searchValue)
                                       expect(response.length).toBe(0)
                                    })
                                })
                                describe('that is empty', function(){
                                    var searchValue
                                    beforeEach(function(){
                                        searchValue = {}
                                    })
                                    it('should return an empty list', function(){
                                       var response = dataset.expression.retrieve(searchValue)
                                       expect(response.length).toBe(0)
                                    })
                                })
                            })
                        })

			describe('statistics module', function(){

				describe('threshold contingency matrix function', function(){


					it('should be defined', function(){
						expect(dataset.expression.statistics().contingency).toBeDefined()
					})



					it('should produce contingency matrix when given dimension, groups,  and proper threshold', function(){

						var experiment = {
							dimension:'row',
							groups: [
							         ['Sample1', 'Sample2'],
							         ['Sample3', 'Sample4']
							         ],
							population: 'gene1',
							threshold: 0
						}

						var resp, error

						var expected = [
							{
								above:1,
								below:1
							},
							{
								above:2,
								below:0
							}

						]

						try {
							resp = dataset.expression.statistics().contingency(experiment)

						} catch (err) {
							error = err
						}

						expect(resp).toBeDefined()
						expect(resp).toEqual(expected)
						expect(error).not.toBeDefined()
					})

					it('should fail if intersection of groups is not null', function(){



						var experiment = {
								dimension:'row',
								groups: [
								         ['Sample1', 'Sample2'],
								         ['Sample3', 'Sample4', 'Sample1']
								         ],
								population: 'gene1',
								threshold: 0
							}

						var errorInstanceOfCorrectType

						try {
							resp = dataset.expression.statistics().contingency(experiment)
						} catch (error) {
							errorInstanceOfCorrectType = error instanceof TypeError
						}

						expect(errorInstanceOfCorrectType).toBeTruthy()

					})

					it('should fail if any groups element is length zero', function(){

						var experiment = {
								dimension:'row',
								groups: [[],['Sample3', 'Sample4', 'Sample1']],
								population: 'gene1',
								threshold: 0
							}

						var errorInstanceOfCorrectType

						try {
							resp = dataset.expression.statistics().contingency(experiment)
						} catch (error) {
							errorInstanceOfCorrectType = error instanceof RangeError
						}

						expect(errorInstanceOfCorrectType).toBeTruthy()

					})

					it('should fail if there are less than two group elements', function(){

						var experiment = {
								dimension:'row',
								groups: [
								         ['Sample3', 'Sample4', 'Sample1']
								         ],
								population: 'gene1',
								threshold: 0
							}

						var errorInstanceOfCorrectType

						try {
							resp = dataset.expression.statistics().contingency(experiment)
						} catch (error) {
							errorInstanceOfCorrectType = error instanceof RangeError
						}

						expect(errorInstanceOfCorrectType).toBeTruthy()
					})

					it('should fail if there are more than two group elements', function(){

						var experiment = {
								dimension:'row',
								groups: [['Sample1'],['Sample2'],['Sample3']],
								population: 'gene1',
								threshold: 0
							}

						var errorInstanceOfCorrectType

						try {
							resp = dataset.expression.statistics().contingency(experiment)
						} catch (error) {
							errorInstanceOfCorrectType = error instanceof RangeError
						}

						expect(errorInstanceOfCorrectType).toBeTruthy()
					})

					it('should fail if experiment keys are missing', function(){

						var experiment = {}

						var errorInstanceOfCorrectType

						try {
							resp = dataset.expression.statistics().contingency(experiment)
						} catch (error) {
							errorInstanceOfCorrectType = error instanceof TypeError
						}

						expect(errorInstanceOfCorrectType).toBeTruthy()
					})

				})

			})



		})


	})

})()
