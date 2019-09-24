var userId = 1;

M.AutoInit();



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
        headers: { "x-requested-with": "XMLHttpRequest" },
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

function init_tooltips() {
    var elems = document.querySelectorAll('.tooltipped');
    var instances = M.Tooltip.init(elems);
}

function connection_callback() {
    if ($(".latest-shows").length > 0)
        latest_shows();

    if ($(".user-shows").length > 0)
        user_shows();


    if ($(".show").length > 0 && $("h1").attr("data-show-id"))
        get_show($("h1").attr("data-show-id"), display_show_page);
}


function search_show() {
    console.log("send request : " + token);
    $.ajax({
        headers: { "x-requested-with": "XMLHttpRequest" },
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

function timeConvert(time) {
    if (time < 24 * 60)
        return parseInt(time / 60 % 24) + ' hours';
    return parseInt(time / 24 / 60) + " days and " + parseInt(time / 60 % 24) + ' hours';
}

function show_time_spent(minutes) {
    console.log(minutes);
    var formatted_time = timeConvert(minutes);
    $(".user-spent .card-title").html("You spent <b>" + formatted_time + "</b> of your life watching TV Shows.");

    if (minutes < 300)
        $(".user-spent p").text("That's not a lot. Watch more shows !");
    else if (minutes < 60 * 24 * 3)
        $(".user-spent p").text("That's a decent amout !");
    else
        $(".user-spent p").text("You watched way too many shows, get some medical help.");

    $(".user-spent").removeClass('hide');
}

function user_shows() {

    get_time_spent(show_time_spent);

    var from_time = parseInt((new Date).getTime() / 1000 - 24 * 60 * 60); // Get shows updated in the last 24 hours
    $.ajax({
        headers: { "x-requested-with": "XMLHttpRequest" },
        url: "/" + userId + "/library",
        type: 'GET',
        success: function (data, status) {
            user_shows = data.split(",");
            for (const show of user_shows) {
                console.log(show);
                get_show(show, display_show_card);
            }
        },
        error: function (err) {

        },
    });
}



function latest_shows() {
    var from_time = parseInt((new Date).getTime() / 1000 - 24 * 60 * 60); // Get shows updated in the last 24 hours
    $.ajax({
        headers: { "x-requested-with": "XMLHttpRequest" },
        url: "https://cors.arnaud.at/https://api.thetvdb.com/updated/query",
        type: 'GET',
        dataType: 'json',
        data: { 'fromTime': from_time },
        success: function (data, status) {
            display_show_cards(data.data);
        },
        error: function (err) {

        },
        beforeSend: function (xhr, settings) { xhr.setRequestHeader('Authorization', 'Bearer ' + token); }
    });
}

function get_show(show_id, callback) {
    $.ajax({
        headers: { "x-requested-with": "XMLHttpRequest" },
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
        headers: { "x-requested-with": "XMLHttpRequest" },
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
        headers: { "x-requested-with": "XMLHttpRequest" },
        url: "https://cors.arnaud.at/https://api.thetvdb.com/series/" + show_id + "/images/query",
        type: 'GET',
        dataType: 'json',
        data: { "keyType": "fanart" },
        success: function (data, status) {
            var poster_url = data.data[0].fileName;
            image.attr('src', "https://www.thetvdb.com/banners/" + poster_url)
        },
        error: function (err) {
            image.parent().parent().parent().parent().remove();

        },
        beforeSend: function (xhr, settings) { xhr.setRequestHeader('Authorization', 'Bearer ' + token); }
    });
}


function display_show_cards(shows) {
    for (const show of shows) {
        get_show(show.id, display_show_card);
    }
}



function display_show_card(data) {
    if (!data.seriesName || !data.banner || !data.overview || data.siteRatingCount < 20) return;
    if ($(".col .card").length > 10) return;

    $(".loader").remove();
    console.log("launching show " + data.seriesName);
    var new_card = $(".col.hide").clone();
    var url = "/show/" + data.id;
    new_card.find(".card-image").attr("data-show-id", data.id);
    new_card.find(".card-title").text(data.seriesName);
    new_card.find(".card-content p").text(data.overview);
    new_card.find(".card-action a").attr("href", url);
    new_card.find(".card-image a").first().attr("href", url);



    get_image(data.id, new_card.find(".card-image img").first());

    new_card.appendTo(".latest-shows .row,.user-shows .row").removeClass('hide');

}

function display_show_page(data) {
    get_image(data.id, $(".parallax-container img"));
    $(".show h1").text(data.seriesName);
    $(".show h1").attr("data-show-id", data.id);
    $(".show h1").attr("data-show-runtime", data.runtime);
    document.title = data.seriesName;

    $(".show .col p").text(data.overview);
    get_seasons(data.id, display_seasons);
}


function get_season(show_id, season_id, new_season_div) {
    $.ajax({
        headers: { "x-requested-with": "XMLHttpRequest" },
        url: "https://cors.arnaud.at/https://api.thetvdb.com/series/" + show_id + "/episodes/query",
        type: 'GET',
        dataType: 'json',
        data: { "airedSeason": season_id },
        success: function (data, status) {
            var season_body = new_season_div.find(".collapsible-body");
            var episode_div = season_body.find(".episode");
            var episodes = data.data;

            episodes.sort(function (a, b) { return parseInt(a.airedEpisodeNumber) - parseInt(b.airedEpisodeNumber) });

            for (let episode of episodes) {
                if (episode.episodeName === null) continue;
                var new_episode = episode_div.first().clone();

                var name = "<b>S" + episode.airedSeason + "E" + episode.airedEpisodeNumber + "</b> - " + episode.episodeName;
                if (episode.imdbId.length > 0) name = "<b>S" + episode.airedSeason + "E" + episode.airedEpisodeNumber + "</b> - <a href='https://www.imdb.com/title/" + episode.imdbId + "'  target='_blank'>" + episode.episodeName + "</a>";

                if (episode.siteRating > 0) name += "<i> - " + episode.siteRating + "/10</i>";

                new_episode.find('div').html(name);
                new_episode.attr('data-episode-number', episode.airedEpisodeNumber)
                new_episode.removeClass('hide').appendTo(season_body);

                var showId = $(".show h1").attr('data-show-id');
                var durationMin = $(".show h1").attr("data-show-runtime");

                is_seen(new Episode(showId, episode.airedSeason, episode.airedEpisodeNumber, userId, durationMin), $(new_episode).find(".unseen"));
                init_tooltips();
            }
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
    seasons.sort(function (a, b) { return parseInt(a) - parseInt(b) });


    for (const season of seasons) {
        if (season == 0) continue;

        var new_season_div = $(".seasons .collapsible").first().clone();
        new_season_div.find('.collapsible-header div').html('<b>Season ' + season + "</b>");
        new_season_div.find('.collapsible-header').attr("data-season-number", season);
        new_season_div.appendTo(".seasons").removeClass('hide');

        get_season($(".show h1").data("show-id"), season, new_season_div);
    }

    var elems = document.querySelectorAll('.collapsible');
    var instances = M.Collapsible.init(elems);
}

//get_time_spent(console.log);


function get_time_spent(callback) {
    $.ajax({
        headers: { "x-requested-with": "XMLHttpRequest" },
        url: "/timeSpent",
        type: 'GET',
        dataType: 'json',
        data: { "userId": userId },
        success: function (data, status) {

            callback(data);
        },
        error: function (err) {

        },
    });
}


function watched(button) {
    var watched = button.parentElement;
    var unseen = $(button).hasClass('unseen');
    var watch_list = [];


    if ($(watched).hasClass('season')) {
        var showId = $(".show h1").attr('data-show-id');
        var seasonId = $(watched).attr("data-season-number");
        var durationMin = $(".show h1").attr("data-show-runtime");

        $(watched).parent().find(".episode:not(.hide)").each(function () {
            console.log(this);
            var episodeId = $(this).attr('data-episode-number');
            watch_list.push(new Episode(showId, seasonId, episodeId, userId, durationMin));
        });
        if (unseen) {
            $("a.btn-floating", watched.parentElement).removeClass("unseen");
            M.toast({ html: 'Season added to your library' });
        } else {
            $("a.btn-floating", watched.parentElement).addClass("unseen");
            M.toast({ html: 'Season removed from your library' });
        }

    } else if ($(watched).hasClass('episode')) {
        var showId = $(".show h1").attr('data-show-id');
        var seasonId = $(button).parent().parent().parent().children().first().attr("data-season-number");
        var durationMin = $(".show h1").attr("data-show-runtime");
        var episodeId = $(watched).attr('data-episode-number');
        watch_list.push(new Episode(showId, seasonId, episodeId, userId, durationMin))
        console.log(watch_list);
        $(button).toggleClass("unseen");
        if (unseen) {
            M.toast({ html: 'Episode added to your library' });
        } else {
            M.toast({ html: 'Episode removed from your library' });
        }
    }

    for (var i = 0; i < watch_list.length; i++) {
        view_episode(watch_list[i], unseen);
    }
}


function Episode(showId, seasonId, episodeId, userId, durationMin) {
    this.showId = showId;
    this.seasonId = seasonId;
    this.episodeId = episodeId;
    this.userId = userId;
    this.durationMin = durationMin;
}

//view_episode(new Episode(1, 1, 1, 1, 1), false);

function view_episode(episode, unseen) {
    if (unseen) {
        $.ajax({
            headers: { "x-requested-with": "XMLHttpRequest" },
            url: "/addEpisode",
            type: 'POST',
            dataType: 'json',
            data: {
                "showId": episode.showId,
                "seasonId": episode.seasonId,
                "episodeId": episode.episodeId,
                "userId": episode.userId,
                "durationMin": episode.durationMin
            },
            success: function (data, status) {
                console.log(data);
            },
            error: function (err) {

            },
        });
    } else {
        $.ajax({
            headers: { "x-requested-with": "XMLHttpRequest" },
            url: "/removeEpisode",
            type: 'POST',
            dataType: 'json',
            data: {
                "showId": episode.showId,
                "seasonId": episode.seasonId,
                "episodeId": episode.episodeId,
                "userId": episode.userId,
                "durationMin": episode.durationMin
            },
            success: function (data, status) {
                console.log(data);
            },
            error: function (err) {

            },
        });
    }
}

function is_seen(episode, button) {
    console.log(episode);
    $.ajax({
        url: "/isSeen",
        type: 'GET',
        data: {
            "showId": episode.showId,
            "seasonId": episode.seasonId,
            "episodeId": episode.episodeId,
            "userId": episode.userId,
            "durationMin": episode.durationMin
        },
        success: function (data, status) {
            if (data == "1") {
                button.removeClass("unseen");
                check_season_complete($(button).parent().parent().parent().children().first());
            }
        },
        error: function (err) {

        },
    });
}

function check_season_complete(season_div) {
    console.log(season_div);
    if (season_div.parent().find(".episode:not(.hide) .unseen").length == 0){
        season_div.find(".unseen").removeClass("unseen");
    }
}
