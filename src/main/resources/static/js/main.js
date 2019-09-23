M.AutoInit();

// M.toast({html: 'Show added to your library'})

/*

username : arnaudsm661hp
unique ID : F0QAE2DS4114JS0D

API KEY : LS941GXSKILXE963
project : potato-tv-student-project



*/
var token = "";
var cors_proxy = "https://cors.arnaud.at/";

var auth = {
    "apikey": "LS941GXSKILXE963",
    "userkey": "F0QAE2DS4114JS0D",
    "username": "arnaudsm661hp"
};


get_token();

function create_token() {
    $.ajax({
        headers: {"x-requested-with": "XMLHttpRequest"},
        url: (cors_proxy + "https://api.thetvdb.com/login"),
        type: 'POST',
        data: JSON.stringify(auth),
        contentType: 'application/json',
        dataType: "json",
        success: function (data, status) {
            set_token(data.token)
        },
        error: function (err) {

        },
    });
}

function set_token(new_token) {
    console.log("token created : " + new_token);
    token = new_token;
    localStorage.setItem("token", new_token);
    connection_callback();
}

function get_token() {
    token = localStorage.getItem("token");
    if (token !== null) {
        console.log("token loaded :" + token)
        connection_callback();
    }
    else {
        create_token();
    }
}

function connection_callback() {
    if ($(".latest-shows").length > 0)
        latest_shows();

    if ($(".show").length > 0)
        get_show("121361", display_show_page);
}


function search_show() {
    console.log("send request : " + token);
    $.ajax({
        headers: {"x-requested-with": "XMLHttpRequest"},
        url: "https://cors.arnaud.at/https://api.thetvdb.com/search/series?name=simpsons",
        type: 'GET',
        dataType: 'json',
        success: function (data, status) {
            console.log(data);
        },
        error: function (err) {

        },
        beforeSend: function (xhr, settings) { xhr.setRequestHeader('Authorization', 'Bearer ' + token); }
    });
}

function latest_shows() {
    var from_time = parseInt((new Date).getTime() / 1000 - 24 * 60 * 60); // Get shows updated in the last 24 hours
    $.ajax({
        headers: {"x-requested-with": "XMLHttpRequest"},
        url: "https://cors.arnaud.at/https://api.thetvdb.com/updated/query",
        type: 'GET',
        dataType: 'json',
        data: { 'fromTime': from_time },
        success: function (data, status) {
            display_show_cards(data.data)
        },
        error: function (err) {

        },
        beforeSend: function (xhr, settings) { xhr.setRequestHeader('Authorization', 'Bearer ' + token); }
    });
}

function get_show(show_id, callback) {
    $.ajax({
        headers: {"x-requested-with": "XMLHttpRequest"},
        url: "https://cors.arnaud.at/https://api.thetvdb.com/series/" + show_id,
        type: 'GET',
        dataType: 'json',
        success: function (data, status) {
            callback(data.data)
        },
        error: function (err) {

        },
        beforeSend: function (xhr, settings) { xhr.setRequestHeader('Authorization', 'Bearer ' + token); }
    });
}


function get_seasons(show_id, callback) {
    $.ajax({
        headers: {"x-requested-with": "XMLHttpRequest"},
        url: "https://cors.arnaud.at/https://api.thetvdb.com/series/" + show_id + "/episodes/summary",
        type: 'GET',
        dataType: 'json',
        success: function (data, status) {
            callback(data.data)
        },
        error: function (err) {

        },
        beforeSend: function (xhr, settings) { xhr.setRequestHeader('Authorization', 'Bearer ' + token); }
    });
}

function get_image(show_id, image) {
    $.ajax({
        headers: {"x-requested-with": "XMLHttpRequest"},
        url: "https://cors.arnaud.at/https://api.thetvdb.com/series/" + show_id + "/images/query",
        type: 'GET',
        dataType: 'json',
        data: { "keyType": "fanart" },
        success: function (data, status) {
            var poster_url = data.data[0].fileName;
            image.attr('src', "https://www.thetvdb.com/banners/" + poster_url)
            //console.log(image);
        },
        error: function (err) {
            image.parent().parent().parent().parent().remove();

        },
        beforeSend: function (xhr, settings) { xhr.setRequestHeader('Authorization', 'Bearer ' + token); }
    });
}


function display_show_cards(shows) {
    for (const show of shows) {
        console.log(show);
        get_show(show.id, display_show_card);
    }
}



function display_show_card(data) {
    if (!data.seriesName || !data.banner || !data.overview || data.siteRatingCount < 20) return;
    if ($(".col .card").length > 10) return;

    $(".loader").remove();
    console.log("launching show " + data.seriesName);
    var new_card = $(".col.hide").clone();
    new_card.find(".card-title").text(data.seriesName);
    new_card.find(".card-content p").text(data.overview);
    new_card.find(".card-action").attr("href", "/show.html");
    new_card.find(".card-image").attr("href", "/show.html");


    get_image(data.id, new_card.find(".card-image img").first());

    new_card.appendTo(".latest-shows .row").removeClass('hide');

}

function display_show_page(data) {
    get_image(data.id, $(".parallax-container img"));
    $(".show h1").text(data.seriesName);
    $(".show h1").data("show-id",data.id);

    $(".show .col p").text(data.overview);
    get_seasons(data.id, display_seasons);
    console.log(data);
}


function get_episode(show_id, season_id) {
    $.ajax({
        headers: {"x-requested-with": "XMLHttpRequest"},
        url: "https://cors.arnaud.at/https://api.thetvdb.com/series/" + show_id + "/episodes/query",
        type: 'GET',
        dataType: 'json',
        data: {"airedSeason":season_id},
        success: function (data, status) {
            console.log(data);
        },
        error: function (err) {
        },
        beforeSend: function (xhr, settings) { xhr.setRequestHeader('Authorization', 'Bearer ' + token); }
    });
}

function display_seasons(data) {
    console.log(data.airedSeasons);
    var seasons = data.airedSeasons.map(season => parseInt(season));
    seasons.sort();
    console.log(seasons);
    for (const season of seasons) {
        if (season == 0) continue;

        var new_season = $(".seasons .collapsible").first().clone();
        new_season.find('.collapsible-header div').text('Season '+season);
        new_season.data("season-id", "test"); // TODO
        new_season.appendTo(".seasons").removeClass('hide');
  
        get_episode($(".show h1").data("show-id"), season);
    }

    var elems = document.querySelectorAll('.collapsible');
    var instances = M.Collapsible.init(elems);
}