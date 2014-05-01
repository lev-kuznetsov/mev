<style type="text/css">
.node circle {
  fill: #FFFFFF;
  stroke: steelblue;
  stroke-width: 1.5px;
}

.node, text {
  font: 10px sans-serif;
}

.link {
  fill: none;
  stroke: #CCCCCC;
  stroke-width: 1.5px;
}

text {
  fill: #000000;
}
</style>
<accordion-group heading="{{analysis.type}} : {{analysis.name}}" is-open="isItOpen">
	<div class="row">
      <div class="span12">
      <button class="btn btn-success pull-left" ng-click="saveImage(analysis)">
       Save Image
      </button>
      <button class="btn btn-success pull-right">
       Apply to heatmap <i class='icon-chevron-right'></i>
      </button>
      </div>
    </div>
    <br>
    <div class="row">
      <div class="span12">
        <div id="svgPlace">


		</div>
      </div>
    </div>
	<!-- 	        <div id="svgdataurl"></div> -->
	<!-- 	        <div id="pngdataurl"></div> -->
    <canvas id="canvasHclTree_{{analysis.name}}" style="display: none"></canvas>	        
</accordion-group>

