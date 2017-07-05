$.getJSON(window.location.protocol + "//" + window.location.host + "/config", configJson);

function configJson(config) {

    $.each(config, function(i, item) {
        alert(item.PageName);
    });​

    document.addEventListener("DOMContentLoaded", function(event) {
        g1 = new JustGage({
            id: "g1",
            value: getRandomInt(0, 980),
            min: 0,
            max: 980,
            title: "Jenkins Job 1",
            label: "failing tests"
        });

        g2 = new JustGage({
            id: "g2",
            value: getRandomInt(0, 20),
            min: 0,
            max: 20,
            title: "Jenkins Job 2",
            label: "failing tests"
        });

        g3 = new JustGage({
            id: "g3",
            value: getRandomInt(0, 123),
            min: 0,
            max: 123,
            title: "Jenkins Job 3",
            label: "failing tests"
        });

        setInterval(function() {
            g1.refresh(getRandomInt(350, 980));
            g2.refresh(getRandomInt(0, 20));
            g3.refresh(getRandomInt(0, 123));
        }, 2500);
    });

}

var g1, g2, g3;

