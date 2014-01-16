<style type="text/css">
.menu {
    width: 100%;
}
.menu a {
    color:#0088cc;
    text-decoration:none;
}
div.menu{
    
}
.menu ul {
    padding:0;
    /* border-top:1px solid #CCC; */
    
}
.menu ul li {
    list-style:none;
    /* border-bottom:1px solid #CCC; */ 
}
.menu ul li a {
    text-indent:6px;
    display:block;
    padding:6px;
}
.menu ul li a:hover, .menu ul li.selected a  {
    color:#0088cc;
    background-color:#d6d6d6;
}
/* nested items */
.menu ul li ul {
    display:none;
}
.menu ul li.selected ul {
    display:block;
}
.menu ul li.selected ul a {
    color:#666;
    background-color:white;
    border-left:6px solid white; 
}
.menu ul li ul li {
    border-bottom:1px solid white;
}
.menu ul li ul li:first-child {
    border-top:1px solid white;
}
.menu ul li.selected ul li.selected a, .menu ul li.selected ul li a:hover {
    color:#FFF;
    background-color:#7EB0B0;
    /*border-left:6px solid #006363; */ 
}
p.lead {
	left-margin: 10px;
}
</style>

<br>
<br>
<div class="row-fluid">
	<div class="span12">
		<div class='menu'>
			<ul>
				<li class="expandable"><a href=""><i class="icon-folder-open"></i> {{heatmapId}}</a>
					<ul>
						<!-- Clustering Side List -->
				        <li class="expandable"><a href=""><i class="icon-th"></i> Clusters</a>
							<ul>
								<li class="expandable"><a href=""><i class="icon-random"></i> Row</a>
									<ul>
										<li><a href=""><i class="icon-tasks"></i> Analysis1</a></li>
										<li><a href=""><i class="icon-tasks"></i> Analysis1</a></li>
									</ul>
								</li>
								<li class="expandable"><a href=""><i class="icon-random"></i> Column</a>
									<ul>
										<li>-- Board 1</li>
										<li>-- Board 2</li>
									</ul>
								</li>
							</ul>
						</li>
						
						<!-- Limma Side List -->
						<li class="expandable"><a href=""><i class="icon-th-list"></i> LIMMA Results</a>
							<ul>
								<li ><a href=""><i class="icon-tasks"></i> Analysis1</a></li>
								<li><a href=""><i class="icon-tasks"></i> Analysis2</a></li>
							</ul>
						</li>
						
						<!-- Selections Side List -->
				        <li class="expandable"><a href=""><i class="icon-tags"></i> Selections</a>
							<ul>
								<li class="expandable"><a href=""><i class="icon-bookmark"></i> Row</a>
									<ul>
										<li><a href=""><i class="icon-tag"></i> sdfs</a></li>
									</ul>
								</li>
								<li class="expandable"><a href=""><i class="icon-bookmark"></i> Column</a>
									<ul>
										<li><a href=""><i class="icon-tag"></i> sdfs</a></li>
									</ul>
								</li>
							</ul>
						</li>
						
						
						
					</ul>
				</li>
				<li><a href=""><i class="icon-folder-open"></i> Some Other Dataset</a>
				
				</li>
			</ul>
		</div>
	</div>
</div>
