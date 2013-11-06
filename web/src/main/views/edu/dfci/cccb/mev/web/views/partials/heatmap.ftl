<!-- Hacks to make it fit under navbar -->
<br>
<br>
<!-- End Hacks --> 

<div class="row-fluid">
  
  <div class="span10 offset1">
    <div class="well">  
      <h3>{{heatmapId}}
        <bs-Imgbutton icon='download' title='blah' align='right'></bs-Imgbutton>
      </h3> 

      <menubar></menubar>

		<div class="row-fluid"> <!-- Start Column Expand Tabs -->
			<div class="span3" id="leftPanel">
				<div class="well">
				    <button class="btn btn-primary pull-right" ng-click="expandLeft()"><i class="icon-chevron-right"></i></button>
					Left	
				</div>
			</div>
		
			<div class="span9" id="rightPanel">
				<div class="well">
					<button class="btn btn-primary" ng-click="expandRight()"><i class="icon-chevron-left"></i></button>
				    Right
			    </div>
			</div>
		</div> <!--End column expand tabs -->

    </div>
    
  </div>
  
</div>



 <!-- Modals -->

  <bsmodal bindid="heirarchical" func="" header="Heirarchical Clustering">
    <div class="modal-Heirarchical"></div>
  </bsmodal> 