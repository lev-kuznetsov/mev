define([], function(){
	
	return function (data){
    	//Adds analysis http response to correct analysisList group
		var randstr = Math.floor(Math.random()*0xFFFFFF<<0).toString(16);
        var randstr2 = Math.floor(Math.random()*0xFFFFFF<<0).toString(16);

        if (data.type == "Hierarchical Clustering") {

            data.href = "#" + randstr;
            data.parentId = randstr2;
            data.dataParent = '#' + randstr2;
            data.divId = randstr;
            this.analysisList.hierarchical
                    .push(data);

        } else if (data.type == "LIMMA Differential Expression Analysis") {

        	this.analysisList.limma
                    .push({
                        name : data.name,
                        href : "#"
                                + randstr,
                        parentId : randstr2,
                        dataParent : '#'
                                + randstr2,
                        divId : randstr,
                        datar : data
                    });
                
        } else if (data.type == "K-means Clustering"){
        	this.analysisList.kMeans.push(data);

        } else if (data.type == "Anova Analysis") {
        	this.analysisList.anova.push(data);

        } else if (data.type == "t-Test Analysis"){                                                                                                    
        	this.analysisList.tTest.push(data);

        };
        
	};
	
});