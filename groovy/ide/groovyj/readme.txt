This file contains information for users and developers:
* current version
* recent changes
* todo
* done
* how to install
* how to setup environment for developing
* implemented features (= what to test before committing)
----
h1. current version

The current version is maintained in the file /build.number.
The version of the installed plugin is in the /META-INF/plugin.xml.
The automatic build cares for putting it there.

We currently work against IntelliJ IDEA, EAP build #3235 (Irida).
In descriptions below this version is called 'XXXX'.

h1. recent changes

Recent changes are to be viewed via cvs diff.
The diff will show new 'todo's and what was 'done'.

h1. todo

* fix: find out how to use the diagnostic logger
* ref: remove dupl. meta-inf/plugin.xml in groovj root and /src/etc, adapt build.xml
* doc: add some words on how to use automatic build
* ref: remove duplication from groovy.idesupport.idea and org.codehaus.groovy.intellij
* ref: get the unit tests running in a 'plain vanilla' setup
* new: make the Groovy runner write his output to the 'Messages' view.
* fix: use a Groovy Lexer/Parser (or equivalent) in place of the one for JavaScript
* fix: adapt the visitors to make PSI nodes from the AST

h1. done

[dierk, 4 Mar 05] first step into logging for getting better info what happens when
[dierk, 2 Mar 05] starting this document
[dierk, 2 Mar 05] introducing Groovy to the Custom Language API

h1. how to install

When using the "interactive development" just hit Build/Make and the plugin will be
installed in the sandbox (see below). Hit 'ant allow.interactive.plugin.dev'.
Hit groovyj run configuration (see below) for testing.

Otherwise call 'ant deploy.irida'. Adapt paths in /project.properties first.

h1. how to setup environment for developing

For interactive development, I made best experiences with the following
setup:
* download and install Idea Version XXXX
* make a copy of that installation called Idea-XXXX-sandbox
  (this is where the plugin will live in while developing)
* copy your ~/.IntelliJIdea/ dir to some safe place and remove all plugins in the copy
* adapt seetings in Idea-XXXX-sandbox/bin/idea.properties to point to that copy
  (this is to protect your settings, otherwise a malfunctioned plugin can do harm to it)
* download from
  http://www.intellij.net/eap/products/idea/redirect.jsp?filename=ideaXXXX-dev.zip
  and install the devkit.
* make a (global) library that points to the openapi sources and javadoc in that download.
  add the irida.jar from Idea-XXXX-sandbox/lib to it.
* open /groovyj.ipr within Idea. If all runs fine, ok. If not, set up the project as follows:
  * New Project / Plugin Module
  * as JDK choose your IDEAs home as IDEA sdk (this option comes with the devkit)
  * Adapt settings in Project Settings / Paths / PlugIn Development
  * Classpath settings as usual: everything below /lib and your idea library
  * Run / Edit Configurations / Plugin (uncheck 'Make Module before...')
* For trying the new Plugin you best use the Plugin Run Configuration rather than closing
  idea and doing so per Idea-XXXX-sandbox/bin starter. The Sandbox is safer to use, easier to
  shutdown, and possible to debug.
* For logging support add the following to IDEA/bin/log.xml just before the <root> element:
  <category name="groovy.idesupport.idea" additivity="true">
	<priority value="DEBUG"/>
	<appender-ref ref="CONSOLE-DEBUG"/>
  </category>


h1. implemented features (= what to test before committing)

* start with plugin
  : idea should show no warnings
  : 'Groovy' is visible in the menu bar but has no menu items, yet
* goto Project Settings / File Types
  : Groovy must be in the list and the groovy 'G' icon. Registered Extensions are shown.
* open /src/etc/check/simple.groovy
  : 'G' icon appears in Project View beneath the file name
  : 'G' icon appears in Editor Tab beneath the file name
  : no warnings are shown
* unselect / reselect the file in the Project View
  : only when selected 'G' icon appears in Toolbar and Groovy Menu offers feature to run the file
* use Toolbar icon, Menu, and shortcut (Ctrl-Alt-G R) to start the file.
  : no warnings
* open /src/etc/check/working.groovy
  : no parser warnings
  : highlighting is to be in the same style as for Java (comments, keywords, literals...)
  : select braces to see the matched brace
  : code folding is indicated in the gutter area, use it to collapse/expand,
    javadoc is folded by default, ellipsis is correctly shown when folded, tooltip shows contents when folded
* open /src/etc/check/bad.groovy
  : parser shows warnings as red, snake underline and in the gutter with tooltip
