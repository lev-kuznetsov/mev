<div class="navbar static-top">
	<div class="navbar-inner">
		<ul class="nav pull-left">
		  
			<li><a href="/home"><i class="icon-list"></i> Datasets</a></li>
		    
			<li class="dropdown">
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					<i class="fa fa-caret-square-o-down"></i> Clustering <b class="caret"></b>
				</a>
				<ul class="dropdown-menu">
					<li><a data-toggle="modal" role="button" href="#hierarchical">Hierarchical</a></li>
					<li><a data-toggle="modal" role="button" class="disabled" href=""><p class="muted">K-Means/Medians</p></a></li>
				</ul>
			</li>
		    
			<li class="dropdown">
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					<i class="fa fa-caret-square-o-down"></i> Statistics <b class="caret"></b>
				</a>
				<ul class="dropdown-menu">
					<li><a data-toggle="modal" role="button" href="#limma">LIMMA</a></li> 
					<li><a href=""><p class="muted">t-Test</p></a></li>
				</ul>
			</li>
		    
			
		
		</ul>
		<ul class="nav pull-right">
		
			<li><a href=""><i class="icon-download-alt"></i> Export</a></li>
			
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