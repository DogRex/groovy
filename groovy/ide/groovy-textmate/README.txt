README
------

This project contains the TextMate bundle files that add Groovy & Grails support to the TextMate text editor for Mac OS X. They were originally written by Graeme Rocher (Grails project lead) and are made available as is.

INSTALLATION
------------
Copy the Groovy and Grails bundle files to ~/Library/Application Support/TextMate/Bundles and start TextMate

USAGE
-----
The bundles add Groovy and GSP support to files ending with .groovy and .gsp. There is syntax highlighting and code completion with snippets. All of the completions can be found in Bundles -> Groovy and Bundles -> Grails.

It is useful to look at these menus as a lot of the Groovy API is explore-able from there.

Some useful tips:

- type "to" and hit TAB for type conversion by method call
- type "as" and hit TAB for type conversion by coercion
- type "with" and hit TAB for i/o stuff
- typing ea, eawi, eal, eaf, eab, eam and hitting TAB do things like each, eachWithIndex, eachFile and so on 
- type "static" and hit TAB for various options for statics
- type "cla" and hit TAB for class definition templates
- typing ":" and hitting TAB creates key/value hash pair   
- Use ^ H to access JavaDoc help given you have them installed
- Use ^ ENTER to create new methods

Remember after you have hit TAB you can often TAG through the code the template generates to modify each changeable value.
