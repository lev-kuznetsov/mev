<style type="text/css">
.menu {
    width:200px;
}
.menu a {
    color:#666;
    text-decoration:none;
}
.menu ul {
    padding:0;
    border-top:1px solid #CCC;
}
.menu ul li {
    list-style:none;
    border-bottom:1px solid #CCC; 
}
.menu ul li a {
    text-indent:6px;
    display:block;
    padding:6px;
}
.menu ul li a:hover, .menu ul li.selected a  {
    color:#FFF;
    background-color:#006363;
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
    border-left:6px solid #006363; 
}
</style>

<script type="text/javascript">
	$(".menu").click(function(e) {
        // unhighlight the previous menu selection
        $(".menu .selected").removeClass("selected"); 
        // highlight the selected item & its parents
        $(e.target).closest("li").addClass("selected").parent().parent().addClass("selected");
    });
</script>


