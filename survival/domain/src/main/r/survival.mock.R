survivalAnalysis <- function (data) {
	baseline <- function(ds){
	  for(i in 1:length(ds)){
	    if(class(ds[1,i]) == "factor"){
	      ds[1,i] <- levels(ds[1,i])[1]
	    } else {
	      ds[1,i] <- 0
	    }
	  }
	  ds
	}	
	prepare_plot_data <- function(so){
	  data <- cbind(so$time, -log(so$surv))
	  colnames(data) <- c("time", "haz")
	  data <- apply(data, 1, as.list)
	  data  
	}
		
	#Set trt as factor so they are treated as such
	data$input$group <- as.factor(data$input$group)
	attach(data$input)
	cobj <- survival::coxph(survival::Surv(time, status) ~ group, data=data$input)
	
	control <- data$input[which(group==1),]
	experiment <- data$input[which(group==2),]
	
	covariate_selector <- data$input[1,]
	covariate_selector <- baseline(covariate_selector)
	covariate_selector$group <- as.factor(1)
	so_control <- survival::survfit(cobj, newdata=covariate_selector)
	covariate_selector$group <- as.factor(2)
	so_experiment <- survival::survfit(cobj, newdata=covariate_selector)
	
	detach()
	
	summ <- summary(cobj)
	output <- list(n=summ$n, n_event=summ$nevent, coef=summ$coefficients[[1]], exp_coef=summ$coefficients[[2]], se_coef=summ$coefficients[[3]], z=summ$coefficients[[4]], pValue=summ$coefficients[[5]], ci_lower=summ$conf.int[[3]], ci_upper=summ$conf.int[[4]], logrank=list(score=summ$sctest[[1]], pValue=summ$sctest[[3]]), plot=list(control=prepare_plot_data(so_control), experiment=prepare_plot_data(so_experiment)))
	
}

#json <- '{"name":"surv1","datasetName":"brca","experiment":{"name":"exp","properties":{"selectionColor":"#18d239","selectionDescription":""},"keys":["TCGA-A8-A09I-01A-22R-A034-07","TCGA-A2-A0SW-01A-11R-A084-07","TCGA-BH-A18R-11A-42R-A12D-07","TCGA-AO-A03R-01A-21R-A034-07","TCGA-AO-A0JL-01A-11R-A056-07","TCGA-E2-A14O-01A-31R-A115-07","TCGA-BH-A1ET-01A-11R-A137-07","TCGA-B6-A0X7-01A-11R-A10J-07"],"type":"column"},"experimentName":"exp","control":{"name":"ctr","properties":{"selectionColor":"#9c22eb","selectionDescription":""},"keys":["TCGA-AR-A1AT-01A-11R-A12P-07","TCGA-BH-A18R-01A-11R-A12D-07","TCGA-BH-A1ET-11B-23R-A137-07","TCGA-E2-A109-01A-11R-A10J-07","TCGA-AO-A0JI-01A-21R-A056-07"],"type":"column"},"controlName":"ctr","input":[{"key":"TCGA-AO-A03R-01A-21R-A034-07","days_to_death":null,"days_to_last_followup":1707,"vital_status":"Alive","group":2,"status":0,"time":1707},{"key":"TCGA-AO-A0JI-01A-21R-A056-07","days_to_death":null,"days_to_last_followup":1172,"vital_status":"Alive","group":1,"status":0,"time":1172},{"key":"TCGA-E2-A109-01A-11R-A10J-07","days_to_death":null,"days_to_last_followup":1172,"vital_status":"Alive","group":1,"status":0,"time":1172},{"key":"TCGA-BH-A1ET-01A-11R-A137-07","days_to_death":2520,"days_to_last_followup":null,"vital_status":"Dead","group":2,"status":1,"time":2520},{"key":"TCGA-A2-A0SW-01A-11R-A084-07","days_to_death":1364,"days_to_last_followup":null,"vital_status":"Dead","group":2,"status":1,"time":1364},{"key":"TCGA-AO-A0JL-01A-11R-A056-07","days_to_death":null,"days_to_last_followup":1319,"vital_status":"Alive","group":2,"status":0,"time":1319},{"key":"TCGA-BH-A18R-11A-42R-A12D-07","days_to_death":1141,"days_to_last_followup":null,"vital_status":"Dead","group":2,"status":1,"time":1141},{"key":"TCGA-BH-A1ET-11B-23R-A137-07","days_to_death":2520,"days_to_last_followup":null,"vital_status":"Dead","group":1,"status":1,"time":2520},{"key":"TCGA-A8-A09I-01A-22R-A034-07","days_to_death":null,"days_to_last_followup":1006,"vital_status":"Alive","group":2,"status":0,"time":1006},{"key":"TCGA-E2-A14O-01A-31R-A115-07","days_to_death":null,"days_to_last_followup":1172,"vital_status":"Alive","group":2,"status":0,"time":1172},{"key":"TCGA-AR-A1AT-01A-11R-A12P-07","days_to_death":1272,"days_to_last_followup":null,"vital_status":"Dead","group":1,"status":1,"time":1272},{"key":"TCGA-BH-A18R-01A-11R-A12D-07","days_to_death":1141,"days_to_last_followup":null,"vital_status":"Dead","group":1,"status":1,"time":1141},{"key":"TCGA-B6-A0X7-01A-11R-A10J-07","days_to_death":1791,"days_to_last_followup":null,"vital_status":"Dead","group":2,"status":1,"time":1791}]}'
json <- '{"name":"survival1","datasetName":"survival_test","experimentName":"eee","experiment":{"name":"eee","properties":{},"keys":["TCGA-A7-A0D9-11A-53R-A089-07","TCGA-A7-A13F-01A-11R-A12P-07","TCGA-A7-A0CG-01A-12R-A056-07","TCGA-A7-A0CJ-01A-21R-A00Z-07"]},"controlName":"ccc","control":{"name":"ccc","properties":{},"keys":["TCGA-A7-A0DC-01A-11R-A00Z-07","TCGA-A7-A0CE-01A-11R-A00Z-07","TCGA-A7-A0CD-01A-11R-A00Z-07","TCGA-A7-A13E-11A-61R-A12P-07"]},"input":[{"key":"id1","time":1000,"status":0,"group":1,"days_to_death":1000,"days_to_last_followup":null,"vital_status":"alive"},{"key":"id2","time":1000,"status":0,"group":0,"days_to_death":1000,"days_to_last_followup":null,"vital_status":"alive"},{"key":"id3","time":1000,"status":0,"group":0,"days_to_death":1000,"days_to_last_followup":null,"vital_status":"alive"}]}'
data <- jsonlite::fromJSON(json)
output <- survivalAnalysis(data)
output_json <- jsonlite::toJSON(output, auto_unbox=TRUE)

