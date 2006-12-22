Welcome to the groovy eclipse plugin.

To build it try the Ant build from inside Eclipse; letting the default goal be executed.

If this doesn't work, try the command line. Note that you'll need to specify where eclipse is installed.

e.g.

	ant -Declipse.home=/Applications/eclipse 
	
which works on my OS X box. Obviously this path will depend on where eclipse is installed.