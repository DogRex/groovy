//  Gant -- A Groovy build tool based on scripting Ant tasks
//
//  Copyright © 2006 Russel Winder <russel@russel.org.uk>
//
//  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
//  compliance with the License. You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software distributed under the License is
//  distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
//  implied. See the License for the specific language governing permissions and limitations under the
//  License.

package org.codehaus.groovy.gant.tests

import gant.Gant

//  Commented out tests are ones that it is not certain should be supported.

/**
 *  A test to ensure that the various include mechanisms work as they should.
 *
 *  @author Russel Winder <russel@russel.org.uk>
 *  @version $Revision$ $Date$
 */
final class Include_Test extends GantTestCase {
  def toolClassName = 'ToolClass'
  def toolClassFilePath = "/tmp/${toolClassName}.groovy"
  def toolClassText =  """
class ${toolClassName} {
  def ${toolClassName} ( Binding binding ) { }
  def flob ( ) { println ( 'flobbed.' ) }
}
"""
  def toolBuildScriptBase =  """
task ( something : '' ) { ${toolClassName}.flob ( ) }
task ( 'default' : '' ) { something ( ) }
"""
  def toolBuildScriptClass =  "includeTool <<  groovyShell.evaluate ( '''${toolClassText} ; return ${toolClassName}''' )\n" + toolBuildScriptBase
  def toolBuildScriptFile =  "includeTool <<  new File ( '${toolClassFilePath}' )\n" + toolBuildScriptBase
  def toolBuildScriptString =  "includeTool <<  '''${toolClassText}'''\n" + toolBuildScriptBase
  def targetsScriptFilePath = '/tmp/targets.gant'
  def targetsScriptText =  '''
task ( flob : '' ) { println ( 'flobbed.' ) }
''' 
  def targetsClassName = 'TargetsClass'
  def targetsClassFilePath = "/tmp/${targetsClassName}.groovy"
  def targetsClassText =  """
class ${targetsClassName} {
  def ${targetsClassName} ( Binding binding ) {
    binding.task.call ( flob : '' ) { println ( 'flobbed.' ) }
  }
}
"""
  def targetsBuildScriptBase =  """
task ( something : '' ) { flob ( ) }
task ( 'default' : '' ) { something ( ) }
"""
  def targetsBuildScriptClass =  "includeTargets <<  groovyShell.evaluate ( '''${targetsScriptText} ; return ${targetsClassName}''' , ${targetsClassName} )\n" + targetsBuildScriptBase
  def targetsBuildScriptFile =  "includeTargets <<  new File ( '${targetsScriptFilePath}' )\n" + targetsBuildScriptBase
  def targetsBuildScriptString =  "includeTargets <<  '''${targetsScriptText}'''\n" + targetsBuildScriptBase
  def targetsBuildClassClass =  "includeTargets <<  groovyShell.evaluate ( '''${targetsClassText} ; return ${targetsClassName}''' )\n" + targetsBuildScriptBase
  def targetsBuildClassFile =  "includeTargets <<  new File ( '${targetsClassFilePath}' )\n" + targetsBuildScriptBase
  def targetsBuildClassString =  "includeTargets <<  '''${targetsClassText}'''\n" + targetsBuildScriptBase
  def nonExistentFilePath = '/tmp/tmp/tmp'
  Include_Test ( ) {
    ( new File ( toolClassFilePath ) ).write( toolClassText )
    ( new File ( targetsScriptFilePath ) ).write( targetsScriptText )
    ( new File ( targetsClassFilePath ) ).write( targetsClassText )
  }
  void testToolDefaultClass ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptClass ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testToolDefaultFile ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptFile ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testToolDefaultString ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptString ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testToolFlobClass ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptClass ) )
    Gant.main ( [ '-f' ,  '-'  , 'flob'] as String[] )
    assertEquals ( 'Target flob does not exist.\n' , output.toString ( ) ) 
  }
  void testToolFlobFile ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptFile ) )
    Gant.main ( [ '-f' ,  '-'  , 'flob'] as String[] )
    assertEquals ( 'Target flob does not exist.\n' , output.toString ( ) ) 
  }
  void testToolFlobString ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptString ) )
    Gant.main ( [ '-f' ,  '-'  , 'flob'] as String[] )
    assertEquals ( 'Target flob does not exist.\n' , output.toString ( ) ) 
  }
  void testToolBurbleClass ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptClass ) )
    Gant.main ( [ '-f' ,  '-'  , 'burble'] as String[] )
    assertEquals ( 'Target burble does not exist.\n' , output.toString ( ) ) 
  }
  void testToolBurbleFile ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptFile ) )
    Gant.main ( [ '-f' ,  '-'  , 'burble'] as String[] )
    assertEquals ( 'Target burble does not exist.\n' , output.toString ( ) ) 
  }
  void testToolBurbleString ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptString ) )
    Gant.main ( [ '-f' ,  '-'  , 'burble'] as String[] )
    assertEquals ( 'Target burble does not exist.\n' , output.toString ( ) ) 
  }
  void testToolSomethingClass ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptClass ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testToolSomethingFile ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptFile ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testToolSomethingString ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptString ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testToolClassNoFile ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptFile.replace ( toolClassFilePath , nonExistentFilePath ) ) )
    try { Gant.main ( [ '-f' ,  '-'  , 'flob'] as String[] ) }
    catch ( FileNotFoundException fnfe ) { return }
    fail ( 'Should have got a FileNotFoundException but didn\'t.' )
  }
  void testTargetsDefaultClassClass ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassClass ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  /*
  void testTargetsDefaultClassFile ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassFile ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testTargetsDefaultClassString ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassString ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  */
  void testTargetsFlobClassClass ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassClass ) )
    Gant.main ( [ '-f' ,  '-'  , 'flob'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  /*
  void testTargetsFlobClassFile ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassFile ) )
    Gant.main ( [ '-f' ,  '-'  , 'flob'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testTargetsFlobClassString ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassString ) )
    Gant.main ( [ '-f' ,  '-'  , 'flob'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  */
  void testTargetsBurbleClassClass ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassClass ) )
    Gant.main ( [ '-f' ,  '-'  , 'burble'] as String[] )
    assertEquals ( 'Target burble does not exist.\n' , output.toString ( ) ) 
  }
  /*
  void testTargetsBurbleClassFile ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassFile ) )
    Gant.main ( [ '-f' ,  '-'  , 'burble'] as String[] )
    assertEquals ( 'Target burble does not exist.\n' , output.toString ( ) ) 
  }
  void testTargetsBurbleClassString ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassString ) )
    Gant.main ( [ '-f' ,  '-'  , 'burble'] as String[] )
    assertEquals ( 'Target burble does not exist.\n' , output.toString ( ) ) 
  }
  */
  void testTargetsSomethingClassClass ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassClass ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  /*
  void testTargetsSomethingClassFile ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassFile ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testTargetsSomethingClassString ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassString ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  */
  void testTargetsClassNoFile ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassFile.replace ( targetsClassFilePath , nonExistentFilePath ) ) )
    try { Gant.main ( [ '-f' ,  '-'  , 'flob'] as String[] ) }
    catch ( FileNotFoundException fnfe ) { return }
    fail ( 'Should have got a FileNotFoundException but didn\'t.' )
  }
  /*
  void testTargetsDefaultScriptClass ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptClass ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  */
  void testTargetsDefaultScriptFile ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptFile ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testTargetsDefaultScriptString ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptString ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  /*
  void testTargetsFlobScriptClass ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptClass ) )
    Gant.main ( [ '-f' ,  '-'  , 'flob'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  */
  void testTargetsFlobScriptFile ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptFile ) )
    Gant.main ( [ '-f' ,  '-'  , 'flob'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testTargetsFlobScriptString ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptString ) )
    Gant.main ( [ '-f' ,  '-'  , 'flob'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  /*
  void testTargetsBurbleScriptClass ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptClass ) )
    Gant.main ( [ '-f' ,  '-'  , 'burble'] as String[] )
    assertEquals ( 'Target burble does not exist.\n' , output.toString ( ) ) 
  }
  */
  void testTargetsBurbleScriptFile ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptFile ) )
    Gant.main ( [ '-f' ,  '-'  , 'burble'] as String[] )
    assertEquals ( 'Target burble does not exist.\n' , output.toString ( ) ) 
  }
  void testTargetsBurbleScriptString ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptString ) )
    Gant.main ( [ '-f' ,  '-'  , 'burble'] as String[] )
    assertEquals ( 'Target burble does not exist.\n' , output.toString ( ) ) 
  }
  /*
  void testTargetsSomethingScriptClass ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptClass ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  */
  void testTargetsSomethingScriptFile ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptFile ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testTargetsSomethingScriptString ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptString ) )
    Gant.main ( [ '-f' ,  '-'  , 'something'] as String[] )
    assertEquals ( 'flobbed.\n' , output.toString ( ) ) 
  }
  void testTargetsScriptNoFile ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptFile.replace ( targetsScriptFilePath , '/tmp/tmp/tmp' ) ) )
    try { Gant.main ( [ '-f' ,  '-'  , 'flob'] as String[] ) }
    catch ( FileNotFoundException fnfe ) { return }
    fail ( 'Should have got a FileNotFoundException but didn\'t.' )
  }
}
