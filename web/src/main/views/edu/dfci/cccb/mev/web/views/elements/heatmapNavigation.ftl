
<div class="navbar navbar-fixed-top">

	<div class="navbar-inner">
	
		<ul class="nav pull-left">
			
			<li class="dropdown">
		
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					CLUSTERING
				</a>
				<ul class="dropdown-menu">
					<li><a data-toggle="modal" role="button" href="#hierarchical">Hierarchical</a></li>
					<li><a data-toggle="modal" role="button" class="disabled" href=""><p class="muted">K-Means/Medians</p></a></li>
				</ul>
			</li>
			
			<li class="dropdown">
		
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
						STATISTICS
				</a>
				<ul class="dropdown-menu">
					<li><a data-toggle="modal" role="button" href="#limma">LIMMA</a></li> 
					<li><a href=""><p class="muted">t-Test</p></a></li>
				</ul>
			</li>
			
			<li class="dropdown">
		
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					CLASSIFICATION
				</a>
				<ul class="dropdown-menu">
					<li><a href=""><p class="muted">Heatmap</p></a></li>
				</ul>
			</li>
			
			<li class="dropdown">
		
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					VISUALIZATIONS
				</a>
				<ul class="dropdown-menu">
					<li><a href="#settingsModal" data-toggle="modal">Heatmap Settings</a></li>
				</ul>
			</li>
			
			<li class="dropdown">
		
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					ANNOTATIONS
				</a>
				<ul class="dropdown-menu">
					<li><a href="" ng-click="showAnnotations(selection, 'row')" class="ng-scope">Row Annotations</a></li>
					<li><a href="" ng-click="showAnnotations(selection, 'column')" class="ng-scope">Column Annotations</a></li>
				</ul>
			</li>
			
			<li class="dropdown">
		
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					REDUCTION
				</a>
				<ul class="dropdown-menu">
					<li><a href=""><p class="muted">Principal Component Analysis</p></a></li>
				</ul>
			</li>
			
			<li class="dropdown">
		
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					EXPORT
				</a>
				<ul class="dropdown-menu">
					<li><a href=""><p class="muted">TSV</p></a></li>
					<li><a href=""><p class="muted">CSV</p></a></li>
					<li><a href=""><p class="muted">Drive</p></a></li>
				</ul>
			</li>
			
		</ul>
		<ul class="nav pull-right">
			<a class="brand" href="/">mev<img src="/container/images/favicon/favicon-32x32.png"/></a>
		</ul>
		
	</div>
</div>

<br>

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