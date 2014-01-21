<style type="text/css">

div#analysisnav div.dropdown {
	float: left;
}


button.analysisnav {
	height: 50px;
	border-radius: 4px;
	background: #bcf;

}



</style>

<div class="row-fluid" id="analysisnav">
	<div class="dropdown">
		
		<a href="#" class="dropdown-toggle" data-toggle="dropdown">
			<button class="analysisnav">
				<img src="/container/style/img/clustering.png" height="42" width="42"> Clustering 
			</button>
		</a>
		<ul class="dropdown-menu">
			<li><a data-toggle="modal" role="button" href="#hierarchical">Hierarchical</a></li>
			<li><a data-toggle="modal" role="button" class="disabled" href=""><p class="muted">K-Means/Medians</p></a></li>
		</ul>
	</div>
	
	<div class="dropdown">
		
		<a href="#" class="dropdown-toggle" data-toggle="dropdown">
			<button class="analysisnav">
				<img src="/container/style/img/Statistics.png" height="42" width="42"> Statistics 
			</button>
		</a>
		<ul class="dropdown-menu">
			<li><a data-toggle="modal" role="button" href="#limma">LIMMA</a></li> 
			<li><a href=""><p class="muted">t-Test</p></a></li>
		</ul>
	</div>
	
	<div class="dropdown">
		
		<a href="#" class="dropdown-toggle" data-toggle="dropdown">
			<button class="analysisnav">
				<img src="/container/style/img/classification.png" height="42" width="42"> Classification 
			</button>
		</a>
		<ul class="dropdown-menu">
			<li><a href=""><p class="muted">Heatmap</p></a></li>
		</ul>
	</div>
	
	<div class="dropdown">
		
		<a href="#" class="dropdown-toggle" data-toggle="dropdown">
			<button class="analysisnav">
				<img src="/container/style/img/visualization.png" height="42" width="42"> Visualization 
			</button>
		</a>
		<ul class="dropdown-menu">
			<li><a href=""></a></li>
		</ul>
	</div>
	
	<div class="dropdown">
		
		<a href="#" class="dropdown-toggle" data-toggle="dropdown">
			<button class="analysisnav">
				<img src="/container/style/img/DataReduction.png" height="42" width="42"> Data Reduction 
			</button>
		</a>
		<ul class="dropdown-menu">
			<li><a href=""><p class="muted">Principal Component Analysis</p></a></li>
		</ul>
	</div>
	
	<div class="dropdown">
		
		<a href="#" class="dropdown-toggle" data-toggle="dropdown">
			<button class="analysisnav">
				<img src="/container/style/img/DataReduction.png" height="42" width="42"> Export 
			</button>
		</a>
		<ul class="dropdown-menu">
			<li><a href=""><p class="muted">TSV</p></a></li>
			<li><a href=""><p class="muted">CSV</p></a></li>
			<li><a href=""><p class="muted">Drive</p></a></li>
		</ul>
	</div>
	
	
</div>


<!-- Modals -->
<bsmodal bindid="hierarchical" func="" header="Hierarchical Clustering">
 <div class="modal-Hierarchical"></div>
</bsmodal> 
  
<bsmodal bindid="kmeansclust" func="" header="K-Means/Medians Clustering">
 <div class="modal-Kmeans"></div>
</bsmodal>
  
<bsmodal bindid="limma" func="" header="LIMMA">
 <div class="modal-Limma"></div>
</bsmodal> 