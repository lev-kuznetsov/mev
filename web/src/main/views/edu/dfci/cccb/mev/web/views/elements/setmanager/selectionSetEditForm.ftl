<form class="form-horizontal">
<table width="100%">
	<tbody>
		<tr>
			<td><span>*Name:</span>
				<div>
					<input bind="setName" type="text" ng-model="selectedItemTmp.name" name="set-name"
						style="width: 100%;" class="param" datatype="string">
				</div></td>
		</tr>
		<tr>
			<td><span>Description:</span>
				<div>
					<textarea bind="setDescription" class="code"
						style="height: 5em; width: 100%" ng-model="selectedItemTmp.properties.selectionDescription"></textarea>
				</div></td>
		</tr>
		<tr>
			<td>
				<div style="width: 128px;">
					<div style="width: 132px;">
						<input style="width: 100px;" bind="setColor" id="mycolor"
							class="cpDiv colorPicker evo-cp0" ng-model="selectedItemTmp.properties.selectionColor">
						<div class="evo-pointer evo-colorind"
							style="background-color: #a4f1db"></div>
					</div>
				</div>
			</td>
		</tr>
	</tbody>
</table>
<button class="btn btn-success btn-block" ng-click="saveItem(selectedItemTmp)" data-dismiss="modal" aria-hidden="true">Save</button>
<button class="btn btn-success btn-block" data-dismiss="modal" aria-hidden="true">Cancel</button>
</form> 
		
