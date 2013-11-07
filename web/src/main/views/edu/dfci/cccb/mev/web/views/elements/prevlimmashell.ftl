<div class="accordion-group">

  <div class="accordion-heading">
    <a class="accordion-toggle" data-toggle="collapse" data-parent="#{{parentid}}" href="#{{bindid}}">
      {{header}} 
    </a>
    
  </div>
  
  <div id="{{bindid}}" class="accordion-body collapse">
    <div class="accordion-inner">
      
      <bs-Imgbutton  icon='download' title='blah' align='left'></bs-Imgbutton>
      
      <bs-table data="{{data}}"></bs-table>

    </div> <!--Accordion Inner End -->
  </div> <!--Accordion Collapse End -->

</div> <!--Accordion Group End -->