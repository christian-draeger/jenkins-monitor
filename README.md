[![N|Solid](https://api.travis-ci.org/christian-draeger/jenkins-monitor.svg?branch=master)](https://travis-ci.org/christian-draeger/jenkins-monitor)

### About
The Jenkins-Monitor is a standalone monitoring tool to display the build status / the test results of several jobs.
This is espacially intresting if want to monitor jobs of a jenkins you are not administtrating yourself and don't have permission to install plugins on your own because the Jenkins-Monitor will communicate with the jenkins API.
It is possible to create multiple boards with a separate configuration (which jobs you want to monitor, set different display name, optical things like backgrounds, panel themes, effects, etc.) for each of them.

#### Required
* java jre (7 or higher) installed

#### Installation
* [download](https://github.com/christian-draeger/jenkins-monitor/releases) and run application
* board will be available under ${ip.of.mashine.app.is.installed.on}:8282/test-results.html

### [Try the live example monitor](https://christian-draeger.github.io/jenkins-monitor/)
