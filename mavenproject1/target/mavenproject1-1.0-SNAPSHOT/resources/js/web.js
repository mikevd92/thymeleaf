/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var id_play = -1;
var socket = new SockJS('/mavenproject1/end');
var stompClient = Stomp.over(socket);
var root = "http://localhost:8080/mavenproject1/";

stompClient.connect({}, function (frame) {
    console.log('Connected First client: ' + frame);
    stompClient.subscribe('/channel/plays', function (message) {
        var content = JSON.parse(message.body);
        try {
            if (content.notify === 'succes') {
                refreshPlays();
                fill(id_play);
            } else if (content.notify === 'delete') {
                refreshPlays();
                if (parseInt(content.id) === id_play) {
                    hide();
                } else {
                    refresh();
                }
            }
        } catch (e) {
            console.log("Socket Error Client 1" + e);
        }
    });
});
var secondSocket = new SockJS('/mavenproject1/end');
var stompSecondClient = Stomp.over(secondSocket);
stompSecondClient.connect({}, function (frame) {
    console.log('Connected Second client: ' + frame);
    stompSecondClient.subscribe('/channel/seats', function (message) {
        var content = JSON.parse(message.body);
        console.log(content);
        try {
            if (content.notify === 'succes') {
                console.log(parseInt(content.id));
                console.log(id_play);
                if (parseInt(content.id) == id_play) {
                    refresh();
                }
            }
        } catch (e) {
            console.log("Socket Error Client 2" + e);
        }
    });
});

function refreshPlays() {
    $.ajax({
        type: "POST",
        url: root + "play/list/refresh",
        data: "",
        headers: {
            'Accept': 'text/html',
            'Content-Type': 'text/html'
        },
        success: function (response) {
            // we have the response
            if (response.indexOf('Exception:') === 0)
                alert(response);
            else {
                $('#playDiv').html(response);
                normFill();
            }
        }, error: function (e) {
            console.log('Error Ajax: ' + e);
        }
    });
}
function refresh() {
    var id = window.id_play;
    if (id !== -1) {
        $.ajax({
            type: "PUT",
            url: root + "seat/list/id/" + id + "",
            data: "",
            headers: {
                'Accept': 'text/html',
                'Content-Type': 'text/html'
            },
            success: function (response) {
                // we have the response
                if (response.indexOf('Exception:') === 0)
                    alert(response);
                else {
                    $('#seatHeader').css("visibility", "visible");
                    $('#overDiv').css("visibility", "visible");
                    $('#placeDiv').html(response);
                }
            }, error: function (e) {
                console.log('Error Refresh: ' + e);
            }
        });
    }
}
function disconnect() {
    stompClient.disconnect();
    stompSecondClient.disconnect();
    console.log("Disconnected");
}
function normFill() {
    var selector = '#';
    selector = selector.concat(id_play);
    $('#idPlay').val($(selector).find("td").eq(0).html());
    $('#playName').val($(selector).find("td").eq(1).html());
    $('#startDate').val($(selector).find("td").eq(2).html());
    $('#startTime').val($(selector).find("td").eq(3).html());
    $('#endTime').val($(selector).find("td").eq(4).html());
    $('#ticketPrice').val($(selector).find("td").eq(5).html());
}
function fill(id) {
    var selector = '#';
    selector = selector.concat(id);
    $('#idPlay').val($(selector).find("td").eq(0).html());
    $('#playName').val($(selector).find("td").eq(1).html());
    $('#startDate').val($(selector).find("td").eq(2).html());
    $('#startTime').val($(selector).find("td").eq(3).html());
    $('#endTime').val($(selector).find("td").eq(4).html());
    $('#ticketPrice').val($(selector).find("td").eq(5).html());
    window.id_play = id;
    if ($('#placeDiv').length > 0) {
        $.ajax({
            type: "PUT",
            url: root + "seat/list/id/" + id + "",
            data: "",
            headers: {
                'Accept': 'text/html',
                'Content-Type': 'text/html'
            },
            success: function (response) {
                // we have the response
                if (response.indexOf('Exception:') === 0)
                    alert(response);
                else {
                    $('#seatHeader').css("visibility", "visible");
                    $('#overDiv').css("visibility", "visible");
                    $('#placeDiv').html(response);
                }
            }, error: function (e) {
                console.log('Error Fill: ' + e);
            }
        });
    }
}
function unfill() {
    $('#idPlay').val('');
    $('#playName').val('');
    $('#startDate').val('');
    $('#startTime').val('');
    $('#endTime').val('');
    $('#ticketPrice').val('');
    $('#errorDiv').html('');
}
function filter() {
    // get the form values
    var playName;
    var startDate;
    var startTime;
    var endTime;
    var ticketPrice;
    var idPlay = $('#idPlay').val();
    if (idPlay === null)
        idPlay = -1;
    var playName = $('#playName').val();
    var startDate = $('#startDate').val();
    var startTime = $('#startTime').val();
    var endTime = $('#endTime').val();
    var ticketPrice = $('#ticketPrice').val();
    var json = "{\"idPlay\":";
    json = json.concat("\"");
    json = json.concat(idPlay);
    json = json.concat("\"");
    if (playName !== null && playName !== "") {
        json = json.concat(",");
        json = json.concat("\"playName\":");
        json = json.concat("\"");
        json = json.concat(playName);
        json = json.concat("\"");
    }
    if (startDate !== null && startDate !== "") {
        json = json.concat(",");
        json = json.concat("\"startDate\":");
        json = json.concat("\"");
        json = json.concat(startDate);
        json = json.concat("\"");
    }
    if (startTime !== null && startTime !== "") {
        json = json.concat(",");
        json = json.concat("\"startTime\":");
        json = json.concat("\"");
        json = json.concat(startTime);
        json = json.concat("\"");
    }
    if (endTime !== null && endTime !== "") {
        json = json.concat(",");
        json = json.concat("\"endTime\":");
        json = json.concat("\"");
        json = json.concat(endTime);
        json = json.concat("\"");
    }
    if (ticketPrice !== null && ticketPrice !== "") {
        json = json.concat(",");
        json = json.concat("\"ticketPrice\":");
        json = json.concat("\"");
        json = json.concat(ticketPrice);
        json = json.concat("\"");
    }
    json = json.concat("}");
    $.ajax({
        type: "POST",
        url: root + "play/list/filter",
        data: json,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: function (response) {
            // we have the response
            if (response.status === "error") {
                if (response.result.indexOf('Exception:') === 0)
                    alert(response.result);
                else if (response.result === 'No such elements!')
                    alert(response.result);
                else
                    $('#errorDiv').html(response.result);
            } else {
                $('#playDiv').html(response.result);
                //if(idPlay===id_play)
                var table = document.getElementById('playTable');
                if (table !== null) {
                    var nrRows = table.rows.length;
                    if (nrRows > 2)
                        hide();
                }
                //unfill();
            }
        }, error: function (e) {
            console.log('Error Filter: ' + e);
        }
    });
}
function add() {
    // get the form values
    var playName = $('#playName').val();
    var startDate = $('#startDate').val();
    var startTime = $('#startTime').val();
    var endTime = $('#endTime').val();
    var ticketPrice = $('#ticketPrice').val();
    var json = {"playName": playName, "startDate": startDate, "startTime": startTime, "endTime": endTime, "ticketPrice": ticketPrice};
    $.ajax({
        type: "POST",
        url: root + "play/list",
        data: JSON.stringify(json),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: function (response) {
            // we have the response
            if (response.status === "error") {
                if (response.result.indexOf('Exception:') === 0) {
                    alert(response);
                } else {
                    $('#errorDiv').html(response.result);
                }
            }

            //unfill();
        }, error: function (e) {
            console.log('Error Add: ' + e);
        }
    });
    //stompAddClient.send("/manager/add",{},JSON.stringify(json));
}

function reserve(id, availability) {
    // get the form values
    var name = $('#user').html();
    var id_place = id;
    var available = availability;
    $.ajax({
        type: "PUT",
        url: root + "seat/list/id/" + id_place + "/availability/" + available + "/name/" + name,
        data: "",
        headers: {
            'Accept': 'text/html',
            'Content-Type': 'text/html'
        },
        success: function (response) {
            // we have the response
            if (response.indexOf('Exception:') === 0)
                alert(response);
            else if (response === 'Seat already reserved!')
                alert(response);
        }, error: function (e) {
            console.log('Error Reserve: ' + e);
        }
    });
    //var json = {"idPlace": id_place, "availability": available, "name": name};
    //stompResClient.send("/user/reserve",{},JSON.stringify(json));
}
function cancel(id, availability) {
    // get the form values
    var name = $('#user').html();
    var id_place = id;
    var available = availability;
    $.ajax({
        type: "PUT",
        url: root + "seat/list/cancel/id/" + id_place + "/availability/" + available + "/name/" + name,
        data: "",
        headers: {
            'Accept': 'text/html',
            'Content-Type': 'text/html'
        },
        success: function (response) {
            // we have the response
            if (response.indexOf('Exception:') === 0)
                alert(response);
            else if (response === 'Not your seat!')
                alert(response);
            else if (response === 'Seat not reserved!')
                alert(response);
        }, error: function (e) {
            console.log('Error Cancel: ' + e);
        }
    });
    //var json = {"idPlace": id_place, "availability": available, "name": name};
    //stompCanClient.send("/user/cancel",{},JSON.stringify(json));
}
function hide() {
    // get the form values
    $.ajax({
        type: "POST",
        url: root + "seat/list/hide",
        data: "",
        headers: {
            'Accept': 'text/html',
            'Content-Type': 'text/html'
        },
        success: function (response) {
            // we have the response
            if (response.indexOf('Exception:') === 0)
                alert(response);
            else {
                $('#seatHeader').css("visibility", "hidden");
                $('#overDiv').css("visibility", "hidden");
                $('#placeDiv').html(response);
            }
        }, error: function (e) {
            console.log('Error hide: ' + e);
        }
    });
}
function edit() {
    // get the form values
    var id = window.id_play;
    // $('#idPlay').val(id);
    var playName = $('#playName').val();
    var startDate = $('#startDate').val();
    var startTime = $('#startTime').val();
    var endTime = $('#endTime').val();
    var ticketPrice = $('#ticketPrice').val();
    var json = "{\"idPlay\":";
    json = json.concat("\"");
    json = json.concat(id);
    json = json.concat("\"");
    if (playName !== "") {
        json = json.concat(",");
        json = json.concat("\"playName\":");
        json = json.concat("\"");
        json = json.concat(playName);
        json = json.concat("\"");
    }
    if (startDate !== "") {
        json = json.concat(",");
        json = json.concat("\"startDate\":");
        json = json.concat("\"");
        json = json.concat(startDate);
        json = json.concat("\"");
    }
    if (startTime !== "") {
        json = json.concat(",");
        json = json.concat("\"startTime\":");
        json = json.concat("\"");
        json = json.concat(startTime);
        json = json.concat("\"");
    }
    if (endTime !== "") {
        json = json.concat(",");
        json = json.concat("\"endTime\":");
        json = json.concat("\"");
        json = json.concat(endTime);
        json = json.concat("\"");
    }
    if (ticketPrice !== "") {
        json = json.concat(",");
        json = json.concat("\"ticketPrice\":");
        json = json.concat("\"");
        json = json.concat(ticketPrice);
        json = json.concat("\"");
    }
    json = json.concat("}");
    $.ajax({
        type: "PUT",
        url: root + "play/list",
        data: json,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: function (response) {
            // we have the response
            if (response.status === "error") {
                if (response.result.indexOf('Exception:') === 0)
                    alert(response);
                else
                    $('#errorDiv').html(response.result);
            }
            // unfill();
        }, error: function (e) {
            console.log('Error Filter: ' + e);
        }
    });
    //stompEditClient.send("/manager/edit",{},JSON.stringify(json)); 
}
function rem() {
    // get the form values
    var id = window.id_play;
    //$('#idPlay').val(id);
    var json = {"idPlay": id};
    $.ajax({
        type: "DELETE",
        url: root + "play/list",
        data: JSON.stringify(json),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: function (response) {
            // we have the response
            if (response.status === "error") {
                if (response.indexOf('Exception:') === 0)
                    alert(response);
                else
                    $('#errorDiv').html(response.result);
            }
            unfill();
        }, error: function (e) {
            console.log('Error Remove: ' + e);
        }
    });
    //stompRemClient.send("/manager/rem",{},JSON.stringify(json));
}
function undo() {
    // get the form values
    $.ajax({
        type: "POST",
        url: root + "play/list/undo",
        data: "",
        headers: {
            'Accept': 'text/html',
            'Content-Type': 'text/html'
        },
        success: function (response) {
            // we have the response
            if (response.indexOf('Exception:') === 0)
                alert(response);
            else
                $('#playDiv').html(response);
            //unfill();
        }, error: function (e) {
            console.log('Error Undo: ' + e);
        }
    });
}
function signOut() {
    disconnect();
    var url = root + "logout";
    $(location).attr('href', url);
}
