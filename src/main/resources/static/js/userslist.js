$(document).on('hidden.bs.modal', "", function () {
    var form = $(this).find('form');
    form[0].reset();
});


$(document).ready(function () {

    var currentdate = new Date();
    var datetime = "Last Sync: " + currentdate.getDate() + "/"
        + (currentdate.getMonth()+1)  + "/"
        + currentdate.getFullYear() + " @ "
        + currentdate.getHours() + ":"
        + currentdate.getMinutes() + ":"
        + currentdate.getSeconds();

    console.log("DOCK RELOADED!!!!!"+datetime);
    $.getJSON('http://localhost:8080/admin/api/list', function (json) {
        var tr = [];
        for (var i = 0; i < json.length; i++) {
            tr.push('<tr>');
            tr.push('<td>' + json[i].id + '</td>');
            // tr.push('<td>' + json[i].rolesDescription() + '</td>');
            tr.push('<td>' + json[i].rolesDescription + '</td>');
            tr.push('<td>' + json[i].ssoId + '</td>');
            tr.push('<td>' + json[i].password + '</td>');
            tr.push('<td>' + json[i].email + '</td>');
            tr.push('<td><button  data-id=' + json[i].id + ' class=\'userinfo\'>EDIT!!!</button>&nbsp;&nbsp;<button class=\'delete\' id=' + json[i].ssoId + '>Delete</button></td>');
            tr.push('</tr>');
        }
        $('table').append($(tr.join('')));
    });

    $(document).delegate('.userinfo', 'click', function (event) {
        event.preventDefault();

        var userid = $(this).data('id');
        console.log("userid = " + userid);

        $.getJSON('http://localhost:8080/admin/api/edit?id=' + userid, function (json) {
            console.log("json = " + json);

            console.log(JSON.stringify(json));


            $(".edit_invisibleId").val(json.id);
            $(".edit_visibleId_class").val(json.id);
            $(".edit_email_class").val(json.email);
            $(".edit_ssoId_class").val(json.ssoId);

            $('#empModal').modal('show');

            $("#"+json.userProfiles[0].id).prop("checked", true);
            $("#"+json.userProfiles[1].id).prop("checked", true);
            $("#"+json.userProfiles[2].id).prop("checked", true);
        });
    });

});

$(document).delegate('.delete', 'click', function() {
    if (confirm('Do you really want to delete record?')) {
        var ssoId = $(this).attr('id');
        var parent = $(this).parent().parent();
        $.ajax({
            type: "DELETE",
            url: "http://localhost:8080/admin/api/delete/" + ssoId,
            cache: false,
            success: function() {
                parent.fadeOut('slow', function() {
                    $(this).remove();
                });
                //location.reload(true)
            },
            error: function() {
                $('#err').html('<span style=\'color:red; font-weight: bold; font-size: 30px;\'>Error deleting record').fadeIn().fadeOut(4000, function() {
                    $(this).remove();
                });
            }
        });
    }
});

function addErrorToHtml(err) {
    $("#err").html( "<span style='color: red'>"+err.responseJSON.message+"</span>" );

}

$(document).delegate('#addNew', 'click', function(event) {
    event.preventDefault();

    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: "http://localhost:8080/admin/api/save",
        data: JSON.stringify(getJsonData(false, '#ssoIdNew', '#ssoIdNew','#emailNew', '#passwordNew','#new1', '#new2', '#new3')),
        cache: false,
        success: function(result) {
            $("#msg").html( "<span style='color: green'>User edit successfully</span>" );
            window.setTimeout(function(){location.reload()},1000)
        },
        error: function(err) {
            addErrorToHtml(err);
        }
    });
});

function getJsonData( needId, id_selector, email_selector , sso_selector,password_selector, roleId1_name, roleId2_name, roleId3_name) {

    var arrOfRoles = [];
    if ($(roleId1_name).is(":checked"))
    {
        arrOfRoles.push({"id":1,"type":"USER"});
    }

    if ($(roleId2_name).is(":checked"))
    {

        arrOfRoles.push({"id":2,"type":"ADMIN"});
    }

    if ($(roleId3_name).is(":checked"))
    {
        arrOfRoles.push({"id":3,"type":"DBA"});
    }

    var json = {  'email':$(email_selector).val(), 'ssoId':$(sso_selector).val(), 'password':$(password_selector).val(), "userProfiles": arrOfRoles};
    if (needId) {
        json['id'] = $(id_selector).val();
    }

    console.log(json);
    return json;
}

$(document).delegate('.SaveAndClose', 'click', function(event) {
    event.preventDefault();

    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: "http://localhost:8080/admin/api/save",
        data: JSON.stringify(getJsonData(true, '.edit_invisibleId',
            '.edit_email_class',
            '.edit_ssoId_class',
            '.edit_password_class',
            '#1', '#2', '#3') ),
        cache: false,
        success: function(result) {
            $("#msg").html( "<span style='color: green'>User edit successfully</span>" );
            window.setTimeout(function(){location.reload()},1000)
        },
        error: function(err) {
            addErrorToHtml(err);
        }
    });
});


$(function () {
    var path = window.location.pathname;
    path = path.replace(/\/$/, "");
    path = decodeURIComponent(path);

    $(".nav-link").each(function () {
        var href = $(this).attr('href');
        if (path.substring(0, href.length) === href) {
            $(this).addClass('active');
        }
    });
});
