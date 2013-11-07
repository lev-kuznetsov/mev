<div class="navbar">
  <div class="navbar-inner">

    <ul class="nav">



      <li><a href="">Visualization</a></li>
      
      <li class="divider-vertical"></li>



        <li class="dropdown">
          <a href="" class="dropdown-toggle" data-toggle="dropdown">
            Clustering
            <b class="caret"></b>
          </a>
          <ul class="dropdown-menu">
            <li><a data-toggle="modal" role="button" href="#hierarchical">Hierarchical</a></li>
            <li><a data-toggle="modal" role="button" href="#kmeansclust">K-Means/Medians</a></li>
          </ul>
        </li>



      <li class="divider-vertical"></li>
      
      
      
        <li class="dropdown">
          <a href="" class="dropdown-toggle" data-toggle="dropdown">
            Statistics
            <b class="caret"></b>
          </a>
          <ul class="dropdown-menu">
            <li><a data-toggle="modal" role="button" href="#limma">LIMMA</a></li>
            <li><a href="#">t-Test</a></li>
          </ul>
        </li>
        
      <li class="divider-vertical"></li>
      
      
      
        <li class="dropdown">
          <a href="" class="dropdown-toggle" data-toggle="dropdown">
            Data Reduction
            <b class="caret"></b>
          </a>
          <ul class="dropdown-menu">
            <li><a href="#">PCA</a></li>
          </ul>
        </li>
        
        
        
      <li class="divider-vertical"></li>
      
      
      
        <li class="dropdown">
          <a href="" class="dropdown-toggle" data-toggle="dropdown">
            Meta Analysis
            <b class="caret"></b>
          </a>
          <ul class="dropdown-menu">
            <li><a href="#">GSEA</a></li>
          </ul>
        </li>
        
        

    </ul> <!-- End Nav Link List -->
    
  </div> <!-- End Nav Inner -->
</div> <!-- End Navbar -->

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