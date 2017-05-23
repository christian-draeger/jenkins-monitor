var g1, g2, g3;

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
        value: getRandomInt(0, 980),
        min: 0,
        max: 980,
        title: "Jenkins Job 2",
        label: "failing tests"
    });

    g3 = new JustGage({
        id: "g3",
        value: getRandomInt(0, 980),
        min: 0,
        max: 980,
        title: "Jenkins Job 3",
        label: "failing tests"
    });

    setInterval(function() {
        g1.refresh(getRandomInt(350, 980));
        g2.refresh(getRandomInt(0, 49));
        g3.refresh(getRandomInt(101, 200));
    }, 2500);
});