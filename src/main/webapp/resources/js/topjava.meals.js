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
    $.ajax({
        url: ctx.ajaxUrl + 'filter',
        type: "POST",
        data: {
            startDate: $('#startDate').val(),
            endDate: $('#endDate').val(),
            startTime: $('#startTime').val(),
            endTime: $('#endTime').val()
        },
    }).done(function (data) {
        ctx.datatableApi.clear().rows.add(data).draw();
    })
}

function clearFilter() {
    $('#startDate').val('')
    $('#startTime').val('')
    $('#endDate').val('')
    $('#endTime').val('')
}