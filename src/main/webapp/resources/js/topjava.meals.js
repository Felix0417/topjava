const mealAjaxUrl = "profile/meals/";

const ctx = {
    ajaxUrl: mealAjaxUrl
};

$.ajaxSetup({
    converters: {
        "text json": function (stringData) {
            let json = JSON.parse(stringData);
            if (typeof json === 'object') {
                $(json).each(function () {
                    if (this.hasOwnProperty('dateTime')) {
                        this.dateTime = this.dateTime.slice(0, 16).replace('T', ' ');
                    }
                });
            }
            return json;
        }
    }
});


$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});

function filtering() {
    form = $('#filter')
    $.ajax({
        url: ctx.ajaxUrl + 'filter',
        type: "GET",
        data: form.serialize()
    }).done(function (data) {
        updateTableWithData(data);
    })
}

// https://ru.stackoverflow.com/questions/382620/%D0%9E%D1%87%D0%B8%D1%81%D1%82%D0%BA%D0%B0-%D1%84%D0%BE%D1%80%D0%BC%D1%8B-%D1%81-%D0%BF%D0%BE%D0%BC%D0%BE%D1%89%D1%8C%D1%8E-js
function clearFilter() {
    $('#filter').trigger('reset');
    updateTable()
}