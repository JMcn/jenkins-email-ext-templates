<%
import java.text.DateFormat
import java.text.SimpleDateFormat
%>
<STYLE>
BODY, TABLE, TD, TH, P {
  font-family:Verdana,Helvetica,sans serif;
  font-size:11px;
  color:black;
}
h1 { color:black; }
h2 { color:black; }
h3 { color:black; }
TD.bg1 { color:white; background-color:#0000C0; font-size:120% }
TD.bg2 { color:white; background-color:#4040FF; font-size:110% }
TD.bg3 { color:white; background-color:#8080FF; }
TD.test_passed { color:blue; }
TD.test_failed { color:red; }
TD.console { font-family:Courier New; }
</STYLE>
<BODY>

<TABLE>
  <TR><TD align="right"><IMG SRC="${rooturl}<%= build.result == hudson.model.Result.SUCCESS  ? "static/e59dfe28/images/32x32/blue.gif" : "static/e59dfe28/images/32x32/red.gif" %>" />
  </TD><TD valign="center"><B style="font-size: 150%;"><%= build.result == hudson.model.Result.SUCCESS ? "TESTSRUN ${build.result}" : "TESTRUN ${build.result}" %></B></TD></TR>
  <TR><TD>Project:</TD><TD>${project.name}</TD></TR>
  <TR><TD>Describe:</TD><TD><b>${project.description}</b></TD></TR>
  <TR><TD>Version:</TD><TD><b>${build.environment.version}<b></TD></TR>
  <TR><TD>Number:</TD><TD>${build.displayName}</TD></TR>
  <TR><TD>Date of run:</TD><TD>${it.timestampString}</TD></TR>
  <TR><TD>Test duration:</TD><TD>${build.durationString}</TD></TR>
  <TR><TD>Build URL:</TD><TD><A href="${rooturl}${build.url}">${rooturl}${build.url}</A></TD></TR>
  <TR><TD>Test report:</TD><TD><A href="http://jenkins.corp.mama.cn/jenkins/job/${project.name}/${build.number}/robot/report/report.html">Open report.html</A></TD></TR>
  <TR><TD>Test log:</TD><TD><A href="http://jenkins.corp.mama.cn/jenkins/job/${project.name}/${build.number}/robot/report/log.html">Open log.html</A></TD></TR>
  <TR><TD>Console logs:</TD><TD><A href="${rooturl}${build.url}console">Console</A></TD></TR>
</TABLE>
<BR/>

<!-- Robot Framework Results -->
<%
def robotResults = false
def actions = build.actions // List<hudson.model.Action>
actions.each() { action ->
  if( action.class.simpleName.equals("RobotBuildAction") ) { // hudson.plugins.robot.RobotBuildAction
    robotResults = true %>
<p><h4>Robot Framework Results</h4></p>
<table cellspacing="0" cellpadding="4" border="1" align="left">
        <thead>
          <tr bgcolor="#F3F3F3">
            <td><b>Type</b></td>
            <td><b>Total</b></td>
            <td><b>Passed</b></td>
            <td><b>Failed</b></td>
            <td><b>Pass %</b></td>
          </tr>
        </thead>

        <tbody>

          <tr><td><b>All Tests</b></td>
            <td><%= action.result.overallTotal %></td>
            <td><%= action.result.overallPassed %></td>
            <td><%= action.result.overallFailed %></td>
            <td><%= action.overallPassPercentage %></td>
          </tr>

          <tr><td><b>Critical Tests</b></td>
            <td><%= action.result.criticalTotal %></td>
            <td><%= action.result.criticalPassed %></td>
            <td><%= action.result.criticalFailed %></td>
            <td><%= action.criticalPassPercentage %></td>
          </tr>

        </tbody>
      </table>
<br />
<br />
<br />
<br />
<br />
<br />
<table cellspacing="0" cellpadding="4" border="1" align="left">
<thead>
<tr bgcolor="#F3F3F3">
  <td><b>Test Name</b></td>
  <td><b>Status</b></td>
  <td><b>Execution Datetime</b></td>
</tr>
</thead>
<tbody>
<%  def suites = action.result.allSuites
    suites.each() { suite -> 
      def currSuite = suite
      def suiteName = currSuite.displayName
      // ignore top 2 elements in the structure as they are placeholders
      while (currSuite.parent != null && currSuite.parent.parent != null ) {
        currSuite = currSuite.parent
        suiteName = currSuite.displayName + "." + suiteName
      } %>
<tr><td colspan="3"><b><%= suiteName %></b></td></tr>
<%    DateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SS")
      def execDateTcPairs = []
      suite.caseResults.each() { tc ->
        Date execDate = format.parse(tc.starttime)
        execDateTcPairs << [execDate, tc]
      }
      // primary sort execDate, secondary displayName
      execDateTcPairs = execDateTcPairs.sort{ a,b -> a[1].displayName <=> b[1].displayName }
      execDateTcPairs = execDateTcPairs.sort{ a,b -> a[0] <=> b[0] }
      execDateTcPairs.each() {
        def execDate = it[0]
        def tc = it[1]  
        def tcresult = tc.isPassed() %>
<tr>
  <td><%= tc.displayName%></td>
  <td style="color: <%= tc.isPassed() ? "#66CC00" : "#FF3333" %>"><%= tc.isPassed() ? "PASS" : "FAIL" %></td>
  <td><%= execDate %></td>
</tr>
<%    } // tests
    } // suites %>
</tbody></table><%
  } // robot results
}
if (!robotResults) { %>
<p>No Robot Framework test results found.</p>
<%
} %>
</BODY>