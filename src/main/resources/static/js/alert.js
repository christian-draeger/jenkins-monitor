$(document).ready(function () {

    var failingJobs = false;
    var buildingJobs = false;
    var abortedJobs = false;
    var theme;
    var jobCount;

    var $configContainer = $('#boardConfig'),
        $configForm = $configContainer.find('.modal-body form'),
        $configOptions = $configForm.find('input[type="checkbox"]'),
        environment = location.search.substr(1);


    function config(mode) {
        $.each($configOptions, function (index, element) {
            var optionName = $(element).attr('name') + "-" + environment;
            if (mode === "get") {
                element.checked = localStorage.getItem(optionName) !== "false";
            } else if (mode === "set") {
                localStorage.setItem(optionName, element.checked);
            }
        });
    }

    config("get");
    $configContainer.find('.modal-footer button').on("click", function () {
        localStorage.setItem("reload-" + environment, $('input[type="text"][name="reload"]').val());
        localStorage.setItem("dangerPercentage-" + environment, $('input[type="text"][name="percent"]').val());
        localStorage.setItem("panelTheme-" + environment, $('select[name="panelTheme"] option:selected').val());
        localStorage.setItem("panelEffect-" + environment, $('select[name="panelEffect"] option:selected').val());
        localStorage.setItem("backgroundTheme-" + environment, $('select[name="background"] option:selected').val());
        config("set");

        location.reload();
    });

    $.getJSON(window.location.protocol + "//" + window.location.host + "/config", configJson);

    function configJson(config) {

        useBackgroundTheme(environment);
        columnSwitch(environment);

        if (Object.keys(config.boards).includes(environment)) {
            checkForDefaultConfigOfBoard(environment);
        }

        getConfigModalHeader(environment);
        autoRefresh();

        function getBoardNames() {
            var names = [];
            for (var key in config.boards) {
                names.push(key);
            }
            return names;
        }

        function getBoardName(index) {
            var boards = getBoardNames();
            return boards[index];
        }

        function getNumberOfJobsForBoard(numberOfBoard) {
            return config.boards[getBoardName(numberOfBoard)].jobs.length;
        }

        function getJobName(index) {
            return config.boards[getBoardName(numberOfBoard)].jobs[index].name;
        }

        function getJobMessage(index) {
            return config.boards[getBoardName(numberOfBoard)].jobs[index].message;
        }

        function getJobSub(index) {
            return config.boards[getBoardName(numberOfBoard)].jobs[index].sub;
        }

        var boards = getBoardNames();
        var boardName;

        // stick all together
        if (boards.length === 0) {
            $('h1').hide();
            $('#noParam').html("no board configurations found<br> create a board by clicking on the config symbol at the bottom left corner");
        } else {
            for (var numberOfBoard = 0; numberOfBoard < boards.length; numberOfBoard++) {
                boardName = getBoardName(numberOfBoard);

                if (location.search === "?" + boardName) {
                    $("#noParam").hide();

                    jobCount = getNumberOfJobsForBoard(numberOfBoard);
                    $("#job-count").html("jobCount: " + jobCount);

                    var jenkinsUrl = config.boards[boardName].info.jenkinsUrl;

                    getAllJobsList(jenkinsUrl);

                    theme = localStorage.getItem("panelTheme-" + environment);
                    getTestResults(numberOfBoard, jenkinsUrl, boardName);

                    var headline = config.boards[boardName].info.headline;
                    $("h1").append(headline);

                }

                else if (!Object.keys(config.boards).includes(environment)) {

                    $('#alerts').hide();
                    $('#noParam').html("you need to select a board:<br>");

                    for (var i = 0; i < boards.length; i++) {
                        $('#noParam').append("<a href='" + document.URL + "?" + getBoardName(i) + "'>" + getBoardName(i) + "</a><br>");
                    }
                    $('.config-info').hide();
                    $('h1').hide();
                }
            }
        }

        function getJobResult(jobName, message, sub, boardName, jenkinsUrl) {
            var webserviceAdress = window.location.protocol + "//" + window.location.host;
            var commonPath = webserviceAdress + "/jenkins?jenkinsUrl=" + jenkinsUrl + "&job=";
            var url = commonPath + jobName;

            $.getJSON(url, function (jenkinsData) {

                var failCount = getFailCount(jenkinsData);
                var totalCount = getTotalCount(jenkinsData);
                var resultStatus = jenkinsData.result;
                var showSuccessful = localStorage.getItem("showSuccessfulJobs-" + boardName);
                var showBuilding = localStorage.getItem("showBuildingJobs-" + boardName);
                var showAborted = localStorage.getItem("showAbortedJobs-" + boardName);

                if (((failCount === 0 && totalCount !== 0) || resultStatus === "success") && showSuccessful === "true") {
                    getAlertPanelTemplate(jenkinsData, "success", message, sub, jobName, jenkinsUrl, boardName);
                }

                else if ((failCount === 0 && resultStatus === "success") && showSuccessful === "false") {
                    showNoFailingTests();
                }

                else if (failCount > 0 || resultStatus === "failure") {
                    if (isDanger(jenkinsData)) {
                        getAlertPanelTemplate(jenkinsData, "danger", message, sub, jobName, jenkinsUrl, boardName);
                    } else {
                        getAlertPanelTemplate(jenkinsData, "warning", message, sub, jobName, jenkinsUrl, boardName);
                    }
                    failingJobs = true;
                }

                else if (resultStatus === "building" && showBuilding === "true") {
                    getAlertPanelTemplate(jenkinsData, "building", message, sub, jobName, jenkinsUrl, boardName);
                    buildingJobs = true;
                }

                else if (resultStatus === "abort" && showAborted === "true") {
                    getAlertPanelTemplate(jenkinsData, "abort", message, sub, jobName, jenkinsUrl, boardName);
                    abortedJobs = true;
                }

                else if (resultStatus === "error") {
                    getAlertPanelTemplate(jenkinsData, "info", message, " job n/a", jobName, jenkinsUrl, boardName);
                }
            });
        }

        function isDanger(jenkinsData) {

            var percentageConfig = localStorage.getItem("dangerPercentage-" + environment);

            if (getFailCount(jenkinsData) > getPercentage(percentageConfig, jenkinsData)) {
                return true;
            }

            return false;
        }

        function getTestResults(numberOfBoard, jenkinsUrl, boardName) {

            for (var i = 0; i < getNumberOfJobsForBoard(numberOfBoard); i++) {
                getJobResult(getJobName(i), getJobMessage(i), getJobSub(i), boardName, jenkinsUrl);
            }
        }
    }

    /********************************* start config modal *********************************/

    function getConfigModalHeader(boardName) {
        var headerMarkup =  "<span class='glyphicon glyphicon-cog'></span> " +
                            boardName + "-config" +
                            "<button type='button' class='close' data-dismiss='modal'>&times;</button>";
        $('#config-modal-header').html(headerMarkup);
    }

    /********************************* end config modal *********************************/

    /********************************* start alert template *********************************/

    // level can be "warning", "danger", "success" and "info"
    function getAlertPanelTemplate(data, level, message, sub, jobName, jenkinsUrl, boardName) {
        var alertClass = "alert-dashboard ";

        var panelLevel = level + theme;

        if (localStorage.getItem("showOneColumn-" + boardName) === "false") alertClass = "alert-dashboard-two-cols ";
        $("#alerts").append(
            '<a href="' + jenkinsUrl + '/job/' + jobName + '" target="_blank">' +
            '<div class="' + alertEffects(boardName) + alertClass + panelLevel + '" title="' + jobName + '">' +
            showResultBadge(data, level, boardName) + '<b><span>' + message + '</span><sub class="framework-' + level + '"><i>' + sub + '</i></sub></b>' +
            '<span class="meta-data">' + showBuildTime(data, level, boardName) + showBuildNumber(data, boardName) + '</span></span></div></a>');
    }

    function showResultBadge(data, level, boardName) {
        if (localStorage.getItem("showResultBadge-" + boardName) === "true") {
            return '<span class="alert-badge-' + level + '">' + getResultBadgeValue(data) + '</span>';
        }
        return "";
    }

    function showBuildTime(data, level, boardName) {
        if (localStorage.getItem("showTimestamp-" + boardName) === "true") {
            return '<span class="framework-' + level + ' glyphicon glyphicon-time"><span class="last-build">' + data.date + '</span>';
        }
        return "";
    }

    function showBuildNumber(data, boardName) {
        if (localStorage.getItem("showBuildNumber-" + boardName) === "true") {
            return '<span class="build-number">#' + data.number + '</span>';
        }
        return "";
    }

    function getTotalCount(data) {
        var totalCount = 0;
        if (data.hasOwnProperty('totalCount')) {
            return data.totalCount;
        }
        if (data.hasOwnProperty('passCount')) {
            totalCount = data.passCount;
        }
        if (data.hasOwnProperty('skipCount')) {
            totalCount = totalCount + data.skipCount;
        }
        if (data.hasOwnProperty('failCount')) {
            totalCount = totalCount + data.failCount;
        }

        return totalCount;

    }

    function getFailCount(data) {
        if (data.hasOwnProperty('failCount')) {
            return data.failCount;
        }
        return 0;
    }

    function getPassCount(data) {
        if (data.hasOwnProperty('passCount')) {
            return data.passCount;
        } else if (getFailCount(data) == 0 && getTotalCount(data) >= 1) {
            return getTotalCount(data)
        }
        return 0;
    }

    function getPercentage(percent, data) {
        var per = (percent / 100) * getTotalCount(data);
        return per;
    }

    function getResultBadgeValue(data) {
        var result = data.result;
        var jobHasNumbers = data.hasNumbers;
        var showSuccessVsFail = localStorage.getItem("showSuccessVsFail-" + environment);

        if (jobHasNumbers && showSuccessVsFail === "false") {
            return getFailCount(data) + '<sub>/' + getTotalCount(data) + '</sub>';
        } else if (jobHasNumbers && showSuccessVsFail === "true") {
            return getPassCount(data) + '<sup>+</sup><span class="vertical"></span>' + getFailCount(data) + '<sup>-</sup>';
        } else if (result === "success") {
            return '<span class="glyphicon glyphicon-ok"></span>';
        } else if (result === "failure") {
            return '<span class="glyphicon glyphicon-remove"></span>';
        } else if (result === "building") {
            return '<img src="/pics/gears.svg" alt="building...">';
        } else if (result === "error") {
            return '?';
        } else if (result === "abort") {
            return 'abort';
        }
    }

    function showNoFailingTests() {
        setTimeout(function () {
            if (failingJobs == false && buildingJobs == false && abortedJobs == false) {
                $('.no-fails').show();
            }
        }, 3000);
    }


    /********************************* end alert template *********************************/

    function getAllJobsList(jenkinsUrl) {
        var webserviceAdress = window.location.protocol + "//" + window.location.host;
        var endpoint = webserviceAdress + "/apiRaw?jenkinsUrl=" + jenkinsUrl;
        $.getJSON(endpoint, function (jenkinsData) {
            var jobList = jenkinsData.jobs;
        });
    }


    /********************************* start configuration *********************************/

    function columnSwitch(boardName) {

        // if localStorageKeyName is true do open else do close
        var localStorageKeyName = "showOneColumn-" + boardName;

        $('#cols').addClass(localStorage.getItem(localStorageKeyName) == "false" ? 'glyphicon-align-justify' : 'glyphicon-th-large');
        $("#alerts a div").toggleClass(localStorage.getItem(localStorageKeyName) == "false" ? 'alert-dashboard-two-cols' : 'alert-dashboard');

        setTimeout(function () {
            $("div#alerts :nth-child(even) .alert-dashboard-two-cols").show();
        }, 1000);

        // switch cols
        $('#cols').click(function () {

            $("div#alerts :nth-child(even) .alert-dashboard-two-cols").hide();
            $("div#alerts :nth-child(even) .alert-dashboard").hide();

            $("span.meta-data").hide();
            setTimeout(function () {
                $("span.meta-data").show();
            }, 400);

            var $this = $(this);

            $this.toggleClass('glyphicon-th-large glyphicon-align-justify');

            $("#alerts a div").toggleClass('alert-dashboard alert-dashboard-two-cols');

            localStorage.setItem(localStorageKeyName, localStorage.getItem(localStorageKeyName) === "false" ? "true" : "false");

            setTimeout(function () {
                $("div#alerts :nth-child(even) .alert-dashboard").show();
            }, 800);

            setTimeout(function () {
                $("div#alerts :nth-child(even) .alert-dashboard-two-cols").show();

            }, 800);

        });
    }


    function checkForDefaultConfigOfBoard(boardName) {
        var success = 'showSuccessfulJobs-' + boardName;
        var building = 'showBuildingJobs-' + boardName;
        var aborted = 'showAbortedJobs-' + boardName;
        var buildNumber = 'showBuildNumber-' + boardName;
        var successVsFail = 'showSuccessVsFail-' + boardName;
        var resultBadge = 'showResultBadge-' + boardName;
        var buildTime = 'showTimestamp-' + boardName;
        var panelEffect = 'panelEffect-' + boardName;
        var oneCol = 'showOneColumn-' + boardName;
        var reload = 'reload-' + boardName;
        var percent = 'dangerPercentage-' + boardName;
        var panelTheme = 'panelTheme-' + boardName;
        var backgroundTheme = 'backgroundTheme-' + boardName;

        if (localStorage.getItem(success) == undefined) {
            localStorage.setItem(success, true);
        }
        if (localStorage.getItem(building) == undefined) {
            localStorage.setItem(building, true);
        }
        if (localStorage.getItem(aborted) == undefined) {
            localStorage.setItem(aborted, true);
        }
        if (localStorage.getItem(buildNumber) == undefined) {
            localStorage.setItem(buildNumber, true);
        }
        if (localStorage.getItem(successVsFail) == undefined) {
            localStorage.setItem(successVsFail, false);
        }
        if (localStorage.getItem(buildTime) == undefined) {
            localStorage.setItem(buildTime, true);
        }
        if (localStorage.getItem(resultBadge) == undefined) {
            localStorage.setItem(resultBadge, true);
        }
        if (localStorage.getItem(oneCol) == undefined) {
            if (jobCount <= 6) {
                localStorage.setItem(oneCol, true);
            } else {
                localStorage.setItem(oneCol, false);
            }
        }
        if (localStorage.getItem(reload) == undefined) {
            localStorage.setItem(reload, 0);
        }
        $('input[type="text"][name="reload"]').val(localStorage.getItem("reload-" + environment));

        if (localStorage.getItem(panelTheme) == undefined) {
            localStorage.setItem(panelTheme, "-plain");
        }
        $('select[name="panelTheme"]').val(localStorage.getItem("panelTheme-" + environment));

        if (localStorage.getItem(panelEffect) == undefined) {
            localStorage.setItem(panelEffect, "fadeIn");
        }
        $('select[name="panelEffect"]').val(localStorage.getItem("panelEffect-" + environment));

        if (localStorage.getItem(percent) == undefined) {
            localStorage.setItem(percent, 40);
        }
        $('input[type="text"][name="percent"]').val(localStorage.getItem("dangerPercentage-" + environment));

        if (localStorage.getItem(backgroundTheme) == undefined) {
            localStorage.setItem(backgroundTheme, "background-chalkboard");
        }
        $('select[name="background"]').val(localStorage.getItem("backgroundTheme-" + environment));
    }

    function autoRefresh() {
        var interval = localStorage.getItem("reload-" + environment) * 60000;
        if (interval >= 60000)
            setTimeout(function () {
                location.reload();

                // $('#alerts').load(document.URL + ' #alerts');

            }, interval);
    }

    function alertEffects(boardName) {
        var effect = localStorage.getItem('panelEffect-' + boardName);
        effect = effect + " animated ";
        return effect;
    }

    function useBackgroundTheme(boardName) {
        var backgroundTheme = localStorage.getItem('backgroundTheme-' + boardName);
        $('body.dashboard').addClass(backgroundTheme);
    }

    /********************************* end configuration *********************************/
});