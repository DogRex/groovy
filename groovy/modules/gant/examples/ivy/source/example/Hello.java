package example ;

import org.apache.commons.lang.WordUtils ;

public class Hello {
  public static void main ( final String[] args ) {
    String  message = "hello ivy !" ;
    System.out.println ( "Standard message : " + message ) ;
    System.out.println ( "Capitalized by " + WordUtils.class.getName ( )  + " : " + WordUtils.capitalizeFully ( message ) ) ;
  }
}
