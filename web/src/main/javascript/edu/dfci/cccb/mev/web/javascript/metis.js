$(function() {

    $('a[rel=tooltip]').tooltip();

    // make code pretty
    window.prettyPrint && prettyPrint();

    $('.minimize-box').on('click', function(e){
        e.preventDefault();
        var $icon = $(this).children('i');
        if($icon.hasClass('icon-chevron-down')) {
            $icon.removeClass('icon-chevron-down').addClass('icon-chevron-up');
        } else if($icon.hasClass('icon-chevron-up')) {
            $icon.removeClass('icon-chevron-up').addClass('icon-chevron-down');
        }
    });
    $('.minimize-box').on('click', function(e){
        e.preventDefault();
        var $icon = $(this).children('i');
        if($icon.hasClass('icon-minus')) {
            $icon.removeClass('icon-minus').addClass('icon-plus');
        } else if($icon.hasClass('icon-plus')) {
            $icon.removeClass('icon-plus').addClass('icon-minus');
        }
    });

    $('.close-box').click(function() {
        $(this).closest('.box').hide('slow');
    });

    $('#changeSidebarPos').on('click', function(e) {
        $('body').toggleClass('hide-sidebar');
    });
});



/*--------------------------------------------------------
 BEGIN TABLES.HTML SCRIPTS
 ---------------------------------------------------------*/
function metisTable() {

    /*----------- BEGIN action table CODE -------------------------*/
    $('#actionTable button.remove').on('click', function() {
      $(this).closest('tr').remove();
    });
    $('#actionTable button.edit').on('click', function() {
        $('#editModal').modal({
            show: true
        });
        var val1 = $(this).closest('tr').children('td').eq(1),
            val2 = $(this).closest('tr').children('td').eq(2),
            val3 = $(this).closest('tr').children('td').eq(3);
        $('#editModal #fName').val(val1.html());
        $('#editModal #lName').val(val2.html());
        $('#editModal #uName').val(val3.html());


        $('#editModal #sbmtBtn').on('click', function() {
            val1.html($('#editModal #fName').val());
            val2.html($('#editModal #lName').val());
            val3.html($('#editModal #uName').val());
        });

    });
    /*----------- END action table CODE -------------------------*/

}
