/*
 * NewInterface.java
 *
 * Created on 13 f�vrier 2006, 16:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test;

/**
 *
 * @author SU16766
 */
public interface PersonService {
    Person findPerson(Integer id);
//    Person trouvePerson(String lastName);
    Person[] getPersons();
    
}
