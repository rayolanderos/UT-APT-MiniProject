(function () {
    'use strict';
    var cache = {};

    var autoComplete = $('#input_search').autocomplete({
        source: function (request, response) {
            var term = request.term;
            console.log(term);
            if (term in cache) {
                response(cache[term]);
                return;
            }

            $.getJSON('api/search?s=' + term, request, function (data, status, xhr) {
                console.log('Data: ' + data);
                cache[term] = data;
                response(data);
            });
        }, position: {my: 'right top', at: 'right bottom+9'},
        select: function (event, ui) {

        }
    });

    autoComplete.data('ui-autocomplete')._renderItem = function (ul, item) {
        var inner_html = '<div class="list_item_container"><img src="' + item.cover_url + '" />' + item.name + '</div>';
        return $('<li></li>').attr('data-value', item.id).append(inner_html).appendTo(ul);
    };
}());