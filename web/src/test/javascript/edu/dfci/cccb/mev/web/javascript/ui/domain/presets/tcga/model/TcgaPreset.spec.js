//define(["ng", "appjs", "ngmocks", "angularData"], function(ng){
define(["ng", "lodash", "appjs", "ngMocks"], function(ng, _){
	'use strict'
	describe("Spec TcgaPreset Model", function(){
				
		beforeEach(module("ngMock"));		
//		beforeEach(module("js-data"));
		beforeEach(module("mui.domain"));
		beforeEach(module("mui.domain.presets.tcga"));
		
		var $httpBackend; 
		beforeEach(inject(function( _$httpBackend_){					
			$httpBackend=_$httpBackend_;			
		}));
		
		var $http, DS, TcgaPreset;
		beforeEach(inject(function(_$http_, _DS_, _TcgaPreset_){
			$http = _$http_;
			DS = _DS_;
			TcgaPreset =_TcgaPreset_;
		}));
		
		var mockResponse = function(method, url, data) {
	        var data = [{"name":"OV.HT_HG-U133A.Level_2.tsv","disease":"OV","diseaseName":"Ovarian serous cystadenocarcinoma","platform":"HT_HG-U133A","platformName":"Affymetrix HT Human Genome U133 Array Plate Set","dataLevel":"Level_2"},{"name":"KIRC.AgilentG4502A_07_3.Level_3.tsv","disease":"KIRC","diseaseName":"Kidney renal clear cell carcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"COAD.AgilentG4502A_07_3.Level_3.tsv","disease":"COAD","diseaseName":"Colon adenocarcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"LUSC.HT_HG-U133A.Level_2.tsv","disease":"LUSC","diseaseName":"Lung squamous cell carcinoma","platform":"HT_HG-U133A","platformName":"Affymetrix HT Human Genome U133 Array Plate Set","dataLevel":"Level_2"},{"name":"GBM.AgilentG4502A_07_2.Level_3.tsv","disease":"GBM","diseaseName":"Glioblastoma multiforme","platform":"AgilentG4502A_07_2","platformName":"Agilent 244K Custom Gene Expression G4502A-07-2","dataLevel":"Level_3"},{"name":"UCEC.AgilentG4502A_07_3.Level_3.tsv","disease":"UCEC","diseaseName":"Uterine Corpus Endometrial Carcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"LUSC.AgilentG4502A_07_3.Level_2.tsv","disease":"LUSC","diseaseName":"Lung squamous cell carcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_2"},{"name":"KIRP.AgilentG4502A_07_3.Level_3.tsv","disease":"KIRP","diseaseName":"Kidney renal papillary cell carcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"LUSC.HT_HG-U133A.Level_3.tsv","disease":"LUSC","diseaseName":"Lung squamous cell carcinoma","platform":"HT_HG-U133A","platformName":"Affymetrix HT Human Genome U133 Array Plate Set","dataLevel":"Level_3"},{"name":"OV.AgilentG4502A_07_2.Level_3.tsv","disease":"OV","diseaseName":"Ovarian serous cystadenocarcinoma","platform":"AgilentG4502A_07_2","platformName":"Agilent 244K Custom Gene Expression G4502A-07-2","dataLevel":"Level_3"},{"name":"OV.AgilentG4502A_07_3.Level_3.tsv","disease":"OV","diseaseName":"Ovarian serous cystadenocarcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"BRCA.AgilentG4502A_07_3.Level_3.tsv","disease":"BRCA","diseaseName":"Breast invasive carcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"BRCA.IlluminaHiSeq_RNASeq.Level_3.Expression-Gene.RPKM.2.tsv","disease":"BRCA","diseaseName":"Breast invasive carcinoma","platform":"IlluminaHiSeq_RNASeq","platformName":"Illumina HiSeq 2000 RNA Sequencing (RPKM)","dataLevel":"Level_3"},{"name":"BRCA.IlluminaHiSeq_RNASeq.Level_3.Expression-Gene.counts.tsv","disease":"BRCA","diseaseName":"Breast invasive carcinoma","platform":"IlluminaHiSeq_RNASeq","platformName":"Illumina HiSeq 2000 RNA Sequencing (Counts)","dataLevel":"Level_3"},{"name":"OV.HT_HG-U133A.Level_3.tsv","disease":"OV","diseaseName":"Ovarian serous cystadenocarcinoma","platform":"HT_HG-U133A","platformName":"Affymetrix HT Human Genome U133 Array Plate Set","dataLevel":"Level_3"},{"name":"OV.AgilentG4502A_07_2.Level_2.tsv","disease":"OV","diseaseName":"Ovarian serous cystadenocarcinoma","platform":"AgilentG4502A_07_2","platformName":"Agilent 244K Custom Gene Expression G4502A-07-2","dataLevel":"Level_2"},{"name":"LGG.AgilentG4502A_07_3.Level_3.tsv","disease":"LGG","diseaseName":"Brain Lower Grade Glioma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"READ.AgilentG4502A_07_3.Level_3.tsv","disease":"READ","diseaseName":"Rectum adenocarcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"LUAD.AgilentG4502A_07_3.Level_3.tsv","disease":"LUAD","diseaseName":"Lung adenocarcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"LUSC.AgilentG4502A_07_3.Level_3.tsv","disease":"LUSC","diseaseName":"Lung squamous cell carcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"GBM.HT_HG-U133A.Level_3.tsv","disease":"GBM","diseaseName":"Glioblastoma multiforme","platform":"HT_HG-U133A","platformName":"Affymetrix HT Human Genome U133 Array Plate Set","dataLevel":"Level_3"},{"name":"GBM.HT_HG-U133A.Level_2.tsv","disease":"GBM","diseaseName":"Glioblastoma multiforme","platform":"HT_HG-U133A","platformName":"Affymetrix HT Human Genome U133 Array Plate Set","dataLevel":"Level_2"}];
	        return [200, data, {}];
	    };
		
		it("mui.domain.presets.tcgs - test mock data directly with $http", function(){
			
			$httpBackend.expectGET("/presets/tcga?format=data").respond(mockResponse);
			$http.get("/presets/tcga?format=data").then(function(response){
				console.debug("values:", response.data);
				expect(response.data.length).toBe(22);
			});
			$httpBackend.flush();
		});
		
		it("should return js-data array of TcgaPresets", function(){

			$httpBackend.whenGET('/presets/tcga?format=json').respond(mockResponse);
			
			TcgaPreset.findAll().then(function (tcgaPresets) {
				console.debug("tcgaPresets:", tcgaPresets);
				expect(tcgaPresets.length).toBe(22);
			});
			$httpBackend.flush();
			
			var tcgaOV = TcgaPreset.get("OV.HT_HG-U133A.Level_2.tsv");
			expect(tcgaOV.name).toBe("OV.HT_HG-U133A.Level_2.tsv");
		});
		
		//this does not work as the server does not support getting a single preset
		xit("should fetch single TcgaPreset from server", function(){
			var tcgaOV = TcgaPreset.find("OV.HT_HG-U133A.Level_2.tsv");
			expect(tcgaOV.name).toBe("OV.HT_HG-U133A.Level_2.tsv");
		});
		
		//so we must fetch all items into the store before getting one
		it("should get single TcgaPreset from cache", function(){
			$httpBackend.whenGET('/presets/tcga?format=json').respond(function(method, url, data) {
		        var data = [{"name":"OV.HT_HG-U133A.Level_2.tsv","disease":"OV","diseaseName":"Ovarian serous cystadenocarcinoma","platform":"HT_HG-U133A","platformName":"Affymetrix HT Human Genome U133 Array Plate Set","dataLevel":"Level_2"},{"name":"KIRC.AgilentG4502A_07_3.Level_3.tsv","disease":"KIRC","diseaseName":"Kidney renal clear cell carcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"COAD.AgilentG4502A_07_3.Level_3.tsv","disease":"COAD","diseaseName":"Colon adenocarcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"LUSC.HT_HG-U133A.Level_2.tsv","disease":"LUSC","diseaseName":"Lung squamous cell carcinoma","platform":"HT_HG-U133A","platformName":"Affymetrix HT Human Genome U133 Array Plate Set","dataLevel":"Level_2"},{"name":"GBM.AgilentG4502A_07_2.Level_3.tsv","disease":"GBM","diseaseName":"Glioblastoma multiforme","platform":"AgilentG4502A_07_2","platformName":"Agilent 244K Custom Gene Expression G4502A-07-2","dataLevel":"Level_3"},{"name":"UCEC.AgilentG4502A_07_3.Level_3.tsv","disease":"UCEC","diseaseName":"Uterine Corpus Endometrial Carcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"LUSC.AgilentG4502A_07_3.Level_2.tsv","disease":"LUSC","diseaseName":"Lung squamous cell carcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_2"},{"name":"KIRP.AgilentG4502A_07_3.Level_3.tsv","disease":"KIRP","diseaseName":"Kidney renal papillary cell carcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"LUSC.HT_HG-U133A.Level_3.tsv","disease":"LUSC","diseaseName":"Lung squamous cell carcinoma","platform":"HT_HG-U133A","platformName":"Affymetrix HT Human Genome U133 Array Plate Set","dataLevel":"Level_3"},{"name":"OV.AgilentG4502A_07_2.Level_3.tsv","disease":"OV","diseaseName":"Ovarian serous cystadenocarcinoma","platform":"AgilentG4502A_07_2","platformName":"Agilent 244K Custom Gene Expression G4502A-07-2","dataLevel":"Level_3"},{"name":"OV.AgilentG4502A_07_3.Level_3.tsv","disease":"OV","diseaseName":"Ovarian serous cystadenocarcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"BRCA.AgilentG4502A_07_3.Level_3.tsv","disease":"BRCA","diseaseName":"Breast invasive carcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"BRCA.IlluminaHiSeq_RNASeq.Level_3.Expression-Gene.RPKM.2.tsv","disease":"BRCA","diseaseName":"Breast invasive carcinoma","platform":"IlluminaHiSeq_RNASeq","platformName":"Illumina HiSeq 2000 RNA Sequencing (RPKM)","dataLevel":"Level_3"},{"name":"BRCA.IlluminaHiSeq_RNASeq.Level_3.Expression-Gene.counts.tsv","disease":"BRCA","diseaseName":"Breast invasive carcinoma","platform":"IlluminaHiSeq_RNASeq","platformName":"Illumina HiSeq 2000 RNA Sequencing (Counts)","dataLevel":"Level_3"},{"name":"OV.HT_HG-U133A.Level_3.tsv","disease":"OV","diseaseName":"Ovarian serous cystadenocarcinoma","platform":"HT_HG-U133A","platformName":"Affymetrix HT Human Genome U133 Array Plate Set","dataLevel":"Level_3"},{"name":"OV.AgilentG4502A_07_2.Level_2.tsv","disease":"OV","diseaseName":"Ovarian serous cystadenocarcinoma","platform":"AgilentG4502A_07_2","platformName":"Agilent 244K Custom Gene Expression G4502A-07-2","dataLevel":"Level_2"},{"name":"LGG.AgilentG4502A_07_3.Level_3.tsv","disease":"LGG","diseaseName":"Brain Lower Grade Glioma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"READ.AgilentG4502A_07_3.Level_3.tsv","disease":"READ","diseaseName":"Rectum adenocarcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"LUAD.AgilentG4502A_07_3.Level_3.tsv","disease":"LUAD","diseaseName":"Lung adenocarcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"LUSC.AgilentG4502A_07_3.Level_3.tsv","disease":"LUSC","diseaseName":"Lung squamous cell carcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"GBM.HT_HG-U133A.Level_3.tsv","disease":"GBM","diseaseName":"Glioblastoma multiforme","platform":"HT_HG-U133A","platformName":"Affymetrix HT Human Genome U133 Array Plate Set","dataLevel":"Level_3"},{"name":"GBM.HT_HG-U133A.Level_2.tsv","disease":"GBM","diseaseName":"Glioblastoma multiforme","platform":"HT_HG-U133A","platformName":"Affymetrix HT Human Genome U133 Array Plate Set","dataLevel":"Level_2"}];
		        return [200, data, {}];
		    });
			TcgaPreset.findAll();
			$httpBackend.flush();
			
			var tcgaOV = TcgaPreset.get("OV.HT_HG-U133A.Level_2.tsv");
			expect(tcgaOV.name).toBe("OV.HT_HG-U133A.Level_2.tsv");
		});

		//so we must fetch all items into the store before getting one
		it("should get single TcgaPreset from cache", function(){
			$httpBackend.whenGET('/presets/tcga?format=json').respond(function(method, url, data) {
		        var data = [{"name":"OV.HT_HG-U133A.Level_2.tsv","disease":"OV","diseaseName":"Ovarian serous cystadenocarcinoma","platform":"HT_HG-U133A","platformName":"Affymetrix HT Human Genome U133 Array Plate Set","dataLevel":"Level_2"},{"name":"KIRC.AgilentG4502A_07_3.Level_3.tsv","disease":"KIRC","diseaseName":"Kidney renal clear cell carcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"COAD.AgilentG4502A_07_3.Level_3.tsv","disease":"COAD","diseaseName":"Colon adenocarcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"LUSC.HT_HG-U133A.Level_2.tsv","disease":"LUSC","diseaseName":"Lung squamous cell carcinoma","platform":"HT_HG-U133A","platformName":"Affymetrix HT Human Genome U133 Array Plate Set","dataLevel":"Level_2"},{"name":"GBM.AgilentG4502A_07_2.Level_3.tsv","disease":"GBM","diseaseName":"Glioblastoma multiforme","platform":"AgilentG4502A_07_2","platformName":"Agilent 244K Custom Gene Expression G4502A-07-2","dataLevel":"Level_3"},{"name":"UCEC.AgilentG4502A_07_3.Level_3.tsv","disease":"UCEC","diseaseName":"Uterine Corpus Endometrial Carcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"LUSC.AgilentG4502A_07_3.Level_2.tsv","disease":"LUSC","diseaseName":"Lung squamous cell carcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_2"},{"name":"KIRP.AgilentG4502A_07_3.Level_3.tsv","disease":"KIRP","diseaseName":"Kidney renal papillary cell carcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"LUSC.HT_HG-U133A.Level_3.tsv","disease":"LUSC","diseaseName":"Lung squamous cell carcinoma","platform":"HT_HG-U133A","platformName":"Affymetrix HT Human Genome U133 Array Plate Set","dataLevel":"Level_3"},{"name":"OV.AgilentG4502A_07_2.Level_3.tsv","disease":"OV","diseaseName":"Ovarian serous cystadenocarcinoma","platform":"AgilentG4502A_07_2","platformName":"Agilent 244K Custom Gene Expression G4502A-07-2","dataLevel":"Level_3"},{"name":"OV.AgilentG4502A_07_3.Level_3.tsv","disease":"OV","diseaseName":"Ovarian serous cystadenocarcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"BRCA.AgilentG4502A_07_3.Level_3.tsv","disease":"BRCA","diseaseName":"Breast invasive carcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"BRCA.IlluminaHiSeq_RNASeq.Level_3.Expression-Gene.RPKM.2.tsv","disease":"BRCA","diseaseName":"Breast invasive carcinoma","platform":"IlluminaHiSeq_RNASeq","platformName":"Illumina HiSeq 2000 RNA Sequencing (RPKM)","dataLevel":"Level_3"},{"name":"BRCA.IlluminaHiSeq_RNASeq.Level_3.Expression-Gene.counts.tsv","disease":"BRCA","diseaseName":"Breast invasive carcinoma","platform":"IlluminaHiSeq_RNASeq","platformName":"Illumina HiSeq 2000 RNA Sequencing (Counts)","dataLevel":"Level_3"},{"name":"OV.HT_HG-U133A.Level_3.tsv","disease":"OV","diseaseName":"Ovarian serous cystadenocarcinoma","platform":"HT_HG-U133A","platformName":"Affymetrix HT Human Genome U133 Array Plate Set","dataLevel":"Level_3"},{"name":"OV.AgilentG4502A_07_2.Level_2.tsv","disease":"OV","diseaseName":"Ovarian serous cystadenocarcinoma","platform":"AgilentG4502A_07_2","platformName":"Agilent 244K Custom Gene Expression G4502A-07-2","dataLevel":"Level_2"},{"name":"LGG.AgilentG4502A_07_3.Level_3.tsv","disease":"LGG","diseaseName":"Brain Lower Grade Glioma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"READ.AgilentG4502A_07_3.Level_3.tsv","disease":"READ","diseaseName":"Rectum adenocarcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"LUAD.AgilentG4502A_07_3.Level_3.tsv","disease":"LUAD","diseaseName":"Lung adenocarcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"LUSC.AgilentG4502A_07_3.Level_3.tsv","disease":"LUSC","diseaseName":"Lung squamous cell carcinoma","platform":"AgilentG4502A_07_3","platformName":"Agilent 244K Custom Gene Expression G4502A-07-3","dataLevel":"Level_3"},{"name":"GBM.HT_HG-U133A.Level_3.tsv","disease":"GBM","diseaseName":"Glioblastoma multiforme","platform":"HT_HG-U133A","platformName":"Affymetrix HT Human Genome U133 Array Plate Set","dataLevel":"Level_3"},{"name":"GBM.HT_HG-U133A.Level_2.tsv","disease":"GBM","diseaseName":"Glioblastoma multiforme","platform":"HT_HG-U133A","platformName":"Affymetrix HT Human Genome U133 Array Plate Set","dataLevel":"Level_2"}];
		        return [200, data, {}];
		    });		
			TcgaPreset.findAll();
			$httpBackend.flush();
			
			$httpBackend.expectGET(/\/presets\/tcga\/[a-zA-Z0-9_\-\.]*\?format=json/).respond(function(method, url, data) {
		        var data = {"name":"OV.HT_HG-U133A.Level_2.tsv","disease":"OV","diseaseName":"Ovarian serous cystadenocarcinoma","platform":"HT_HG-U133A","platformName":"Affymetrix HT Human Genome U133 Array Plate Set","dataLevel":"Level_2"};
		        return [200, data, {}];
		    });		
			
			TcgaPreset.find("OV.HT_HG-U133A.Level_2.tsv", {bypassCache: true}).then(function(tcgaOV){				
				expect(tcgaOV.name).toBe("OV.HT_HG-U133A.Level_2.tsv");
			});
			$httpBackend.flush();
			
		});
		
		afterEach(function() {
			DS.clear();
			$httpBackend.verifyNoOutstandingExpectation();
			$httpBackend.verifyNoOutstandingRequest();
		});
//		it("should create a simple workspace", function(){
//			
//			var Project = DS.defineResource({
//				name: 'project',			
//				baseUrl: 'api',
//				endpoint: '/project',
//				idAttribute: "id",					
//				relations: [{
//			        hasMany: {
//			            'dataset': {
//			                localField: 'dataset',
//			                foreignKey: 'projectId'
//			            }
//			        }
//			    }]
//			});
//						
//			var Dataset = DS.defineResource({
//				name: "dataset",
//				idAttribute: "id",
//				relations: [{
//					belongsTo: {
//						project: {
//							parent: true,
//							localField: "project",
//							localKey: "projectId"
//						}
//					}
//				}]
//				
//			});
//			var project1, dataset1;
//			Project.find(1).then(function(data){
//				project1=data;
//				expect(project1.name).toBe("project1");
//				
//				console.debug("project1", data);
//			});
//			
//			Dataset.find(1).then(function(data){
//				dataset1=data;
//				expect(dataset1.name).toBe("dataset 1");				
//				console.debug("datast 1", dataset1, project1.dataset);
//			});
//			
////			DS.linkAll("project").then(function(){
////				console.debug("dataset1.project", dataset1.project);
////			});
//			
//			$httpBackend.expectGET("api/project/1");
////			$httpBackend.expectGET("api/project/1/dataset/1");
//			$httpBackend.flush(2);
//			
//			
//		});
		
	});
});