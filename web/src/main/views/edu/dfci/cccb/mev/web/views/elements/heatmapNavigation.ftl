
<div class="navbar">

	<div class="navbar-inner">
	
		<a class="brand" href="/"><img src="/container/images/favicon/favicon-32x32.png"/></span> : {{heatmapId}}</a>
		
		<ul class="nav pull-right">
			
			<li class="dropdown">
		
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					Clustering
				</a>
				<ul class="dropdown-menu">
					<li><a data-toggle="modal" role="button" href="#hierarchical">Hierarchical</a></li>
					<li><a data-toggle="modal" role="button" class="disabled" href=""><p class="muted">K-Means/Medians</p></a></li>
				</ul>
			</li>
			
			<li class="dropdown">
		
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
						Statistics
				</a>
				<ul class="dropdown-menu">
					<li><a data-toggle="modal" role="button" href="#limma">LIMMA</a></li> 
					<li><a href=""><p class="muted">t-Test</p></a></li>
				</ul>
			</li>
			
			<li class="dropdown">
		
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					Classification 
				</a>
				<ul class="dropdown-menu">
					<li><a href=""><p class="muted">Heatmap</p></a></li>
				</ul>
			</li>
			
			<li class="dropdown">
		
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					Visualization
				</a>
				<ul class="dropdown-menu">
					<li><a href="#settingsModal" data-toggle="modal">Heatmap Settings</a></li>
				</ul>
			</li>
			
			<li class="dropdown">
		
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					Annotations
				</a>
				<ul class="dropdown-menu">
					<li><a href="" ng-click="showAnnotations(selection, 'row')" class="ng-scope">Row Annotations</a></li>
					<li><a href="" ng-click="showAnnotations(selection, 'column')" class="ng-scope">Column Annotations</a></li>
				</ul>
			</li>
			
			<li class="dropdown">
		
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					Data Reduction 
				</a>
				<ul class="dropdown-menu">
					<li><a href=""><p class="muted">Principal Component Analysis</p></a></li>
				</ul>
			</li>
			
			<li class="dropdown">
		
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					Export
				</a>
				<ul class="dropdown-menu">
					<li><a href=""><p class="muted">TSV</p></a></li>
					<li><a href=""><p class="muted">CSV</p></a></li>
					<li><a href=""><p class="muted">Drive</p></a></li>
				</ul>
			</li>
			
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