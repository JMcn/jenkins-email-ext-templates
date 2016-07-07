# jenkins-email-ext-templates
## Use in Jenkins
1. Have your Jenkins administrator place the script inside `$JENKINS_HOME/email-templates`
2. Use the Jelly token with the template parameter equal to your script filename without the .jelly extension. For example, if the script filename is `testReport_fail.jelly`, the email content would look like this `${JELLY_SCRIPT,template="testReport_fail"}`

## Reference
* https://wiki.jenkins-ci.org/display/JENKINS/Email-ext+plugin
* https://github.com/jenkinsci/email-ext-plugin/tree/master/src/main/resources/hudson/plugins/emailext/templates