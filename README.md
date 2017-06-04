[![N|Solid](https://api.travis-ci.org/christian-draeger/jenkins-monitor.svg?branch=master)](https://travis-ci.org/christian-draeger/jenkins-monitor)

### About
The Jenkins-Monitor is a standalone monitoring tool displaying the build status / test results of several jobs.
It's espacially intresting if you want to monitor jobs of a jenkins you are not administrating yourself or don't have permission to install plugins on your own.
It is possible to create multiple boards with a separate configuration (which jobs you want to monitor, set different display name, optical things like backgrounds, panel themes, effects, etc.) for each of them.

#### Required
* java jre (7 or higher) installed

#### Installation
* [download](https://github.com/christian-draeger/jenkins-monitor/releases) and run application
* place the [config file](https://github.com/christian-draeger/jenkins-monitor/blob/master/jenkinsMonitorConfig.json) in the same directory as the application (jar file)
  * don't change the name of the file, it has to be "jenkinsMonitorConfig.json"
* board will be available under localhost:8282/test-results.html
  * if you want to display the board from another mashine replace localhost with the ip-address of the mashine where you're executing the jar

### [Try the live example monitor](https://christian-draeger.github.io/jenkins-monitor/)
