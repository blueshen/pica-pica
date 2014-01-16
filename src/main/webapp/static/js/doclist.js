$(function () {
    $('#search1').click(function () {
        var keywords = $('#keywords1').val();
        $.get('/iSearch/search', {'keywords': keywords, 'pageNo': '1'});
    });
});