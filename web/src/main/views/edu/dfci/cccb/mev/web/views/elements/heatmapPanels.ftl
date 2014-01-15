<style type="text/css">
#partition {
	float: left;
	height: 100%;
	margin: 0px;
	width: 30px;

}

#leftPanel {
	width: 20%;
	float: left;
	box-shadow: 10px 10px 5px #888888;
	border-top:1px solid #B2B2B2;
	border-bottom:1px solid #B2B2B2;
}

#rightPanel {
	float: left;
	width: 70%;
}

div#tab {
	border-top:1px solid #B2B2B2;
	border-right:1px solid #B2B2B2;
	border-bottom:1px solid #B2B2B2;
	border-top-right-radius: 3px;
	border-bottom-right-radius: 3px;
	background: #B8E6E6;
	box-shadow: 7px 10px 5px #888888;
	width: 15px;
	height:30px;
	text-align: left;
}

div#tab i {
	margin-top:8px;
	margin-left:2px;
}

</style>
<br>





<div ng-controller="MainPanelController" class="row-fluid"> <!-- Start Column Expand Tabs -->	
	<div class="row-fluid">
	    
		<div id="leftPanel">
				<side-Navigation-Bar></side-Navigation-Bar>
		</div>
		
		<div id="partition">
			<div id='tab'>
				<i class="icon-chevron-left icon-white"></i>
			</div>
		</div>
		
		
		<div id="rightPanel">
			
				<analysis-Menu-Bar></analysis-Menu-Bar>
				<br>
				
				<div id="heatmapTabPane" class="tab-pane" expression-Panel></div>
		</div>
			
	</div>
		
</div>
