<div mev-main-menu class="collapse navbar-collapse" id="navbar-collapse-1">
	
			<ul class="nav navbar-nav navbar-left">
				
				<li class="dropdown">
			
					<a href="" class="dropdown-toggle" data-toggle="dropdown">
						CLUSTERING
					</a>
					<ul class="dropdown-menu">
						<li><a data-toggle="modal" role="button" data-target="#hierarchical">Hierarchical</a></li>
						<li><a data-toggle="modal" role="button" data-target="#kmeansclust">K-Means</a></li>
					</ul>
				</li>
				
				<li class="dropdown">
			
					<a  class="dropdown-toggle" data-toggle="dropdown">
							STATISTICS
					</a>
					<ul class="dropdown-menu">
						<li><a data-toggle="modal" role="button" data-target="#limma"">LIMMA</a></li> 
						<li><a data-toggle="modal" data-target="#tTest">t-Test</a></li>
						<li><a data-toggle="modal" data-target="#fishertest">Fisher Exact Test</a></li>
						<!--<li><a data-toggle="modal" data-target="#wilcoxon">Wilcoxon Rank Sum</a></li>-->
						<li><a data-toggle="modal" data-target="#anova">ANOVA</a></li>
					</ul>
				</li>
				
				<li class="dropdown">
			
					<a  class="dropdown-toggle" data-toggle="dropdown">
						VISUALIZATIONS
					</a>
					<ul class="dropdown-menu">
						<li><a data-target="#settingsModal"" data-toggle="modal">Heatmap Settings</a></li>
					</ul>
				</li>
				
				<!--
				
				<li class="dropdown">
			
					<a class="dropdown-toggle" data-toggle="dropdown">
						ANNOTATIONS
					</a>
					<ul class="dropdown-menu">
						<li><a href="" ng-click="showAnnotations(selection, 'row')" class="ng-scope">Row Annotations</a></li>
						<li><a href="" ng-click="showAnnotations(selection, 'column')" class="ng-scope">Column Annotations</a></li>
					</ul>
				</li>
				
				
				<li class="dropdown">
			
					<a  class="dropdown-toggle" data-toggle="dropdown">
						REDUCTION
					</a>
					<ul class="dropdown-menu">
						<li><a href=""><p class="muted">Principal Component Analysis</p></a></li>
					</ul>
				</li>
				-->
				<!--
				<li class="dropdown">
			
					<a class="dropdown-toggle" data-toggle="dropdown">
						EXPORT
					</a>
					<ul class="dropdown-menu">
						<li><a href=""><p class="muted">TSV</p></a></li>
						<li><a href=""><p class="muted">CSV</p></a></li>
						<li><a href=""><p class="muted">Drive</p></a></li>
					</ul>
				</li>
				-->
				
			</ul>			
</div>
<!-- Modals -->
<bsmodal bindid="hierarchical" func="" header="Hierarchical Clustering">
 <div class="modal-Hierarchical" heatmap-dataset="project.dataset"></div>
</bsmodal> 
  
<bsmodal bindid="kmeansclust" func="" header="K-Means/Medians Clustering">
 <div class="modal-Kmeans" heatmap-dataset="project.dataset"></div>
</bsmodal>
  
<bsmodal bindid="limma" func="" header="LIMMA">
 <div class="modal-Limma" heatmap-dataset="project.dataset"></div>
</bsmodal> 

<bsmodal bindid="fishertest" func="" header="Fisher's Exact Test">
 <div class="modal-FTest" heatmap-dataset="project.dataset"></div>
</bsmodal> 

<bsmodal bindid="wilcoxon" func="" header="Wilcoxon Rank Sum Test">
 <div class="modal-Wilcoxon" heatmap-dataset="project.dataset"></div>
</bsmodal> 

<bsmodal bindid="anova" func="" header="Analysis of Variance">
 <div class="modal-Anova" heatmap-dataset="project.dataset"></div>
</bsmodal> 

<bsmodal bindid="tTest" func="" header="t-Test Analysis">
 <div class="modal-T-Test" heatmap-dataset="project.dataset"></div>
</bsmodal> 
