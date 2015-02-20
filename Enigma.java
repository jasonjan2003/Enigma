///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            Enigma
// Files:            Enigma.java
// Semester:         CS302 Spring 2015
//
// Author:           Jason Chan
// Email:            cchan42@wisc.edu
// CS Login:         jchan
// Lecturer's Name:  Deb Deppler
// Lab Section:      
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////
//
//                   CHECK ASSIGNMENT PAGE TO see IF PAIR-PROGRAMMING IS ALLOWED
//                   If pair programming is allowed:
//                   1. Read PAIR-PROGRAMMING policy (in cs302 policy) 
//                   2. choose a partner wisely
//                   3. REGISTER THE TEAM BEFORE YOU WORK TOGETHER 
//                      a. one partner creates the team
//                      b. the other partner must join the team
//                   4. complete this section for each program file.
//
// Pair Partner:     Alex Politowicz
// Email:            
// CS Login:         
//
//////////////////////////// 80 columns wide //////////////////////////////////
import java.util.*;

/**
 * Simulate the encryption of messages that was performed by the 
 * World War II-era German Enigma cipher machine.
 * 
 */
public class Enigma {

	// CAUTION: YOU MAY NOT DECLARE STATIC FIELDS IN class Enigma
	// All values must be passed (as described) to and from methods 
	// as this is a major objective of this program assignment.

	/**
	 * User enter some initial configuration information and is then
	 * prompted for the lines of text to be encrypted.
	 * 
	 * Each line is encrypted according to the rotor configuration.
	 * The encoded line is displayed to the user.
	 *
	 * @param args UNUSED
	 */
	public static void main( String [] args ) {

		// TODO left to the student
		
		// Welcome
		System.out.println("Willkommen auf der Enigma-Maschine");
		System.out.println("Please enter a Rotor Configuration.");
		System.out.print("This must be a list of numbers in the ");
		System.out.print("range from 0 to "+RotorConstants.ROTORS.length);
		System.out.println(", separated by spaces.");
		System.out.println("Note that rotor 0 is the identity rotor.");
		
		// Declare variables
		Scanner stdin = new Scanner(System.in);	// Create scanner object
		String input = "";
		String text = "";
		
		// take the input
		input = stdin.nextLine();
		
		// parse and check the input
		int[] rotorIndices = parseRotorIndices(input);
		
		// rotorsInUse
		int[][] rotorsInUse = setUpRotors(rotorIndices);
		
		// reflector rotor
		int[] reflectorRotor = convertRotor(RotorConstants.REFLECTOR);
		
		// rotor notches
		int[][] rotorNotches = getRotorNotches(rotorIndices);
		
		// rotorOffsets
		int[] rotorOffsets = new int[rotorIndices.length];
			
		// Give instruction. start decoding
		System.out.println("Enter lines of text to be encoded:");
		while( true){
			
			text = stdin.nextLine().toUpperCase();
			System.out.print("Encoded result: ");
			for( int k=0; k<text.length(); k++){
				char encoded = 0;
				if( text.charAt(k) <='Z' && text.charAt(k) >= 'A' ){
					encoded = encode(rotorsInUse, reflectorRotor, text.charAt(k));
					advance(rotorsInUse, rotorOffsets, rotorNotches);
				}else
					encoded = text.charAt(k);
				System.out.print(encoded);
				for( int i=0; i<rotorOffsets.length; i++){
					//System.out.println("Rotor at index "+i+":\t"+rotorOffsets[i]);
				}
				
			}
			System.out.println();
		}
		

	}

	/**
	 * Rotates (advances) a single rotor by one position.
	 * This is done by removing the first value of the array,
	 * shifting all the other values towards the beginning of the array,
	 * and placing the first value back into the array as the last value.
	 *
	 * @param rotor The rotor that must be rotated (or shifted).
	 */
	public static void rotate( int [] rotor ) {
		
		// TO.DO left to the student
		int first = rotor[0];
		
		for( int i=0; i<RotorConstants.ROTOR_LENGTH-1; i++){
			rotor[i] = rotor[i+1];
		}
		rotor[RotorConstants.ROTOR_LENGTH-1] = first;

	}

	/**
	 * Parses (interprets) the rotor configuration string of integer values
	 * and returns an integer array of those values.  

	 * Example: If the input string is: 
	 * <pre>"3 7 2"</pre>
	 * The method returns an int array containing three indices: { 3, 7, 2 }.
	 *
	 * <h6>Parameter Validation</h6>
	 * <blockquote>
	 * <p>If any of the specified indices is not a valid index into the
	 * {@code RotorConstants.ROTORS} array, the method prints: 
	 * <pre>   Invalid rotor. You must enter an integer between 0 and 8.</pre> 
	 * to {@code System.out} and then quits the program 
	 * by calling {@code System.exit(-1)}.
	 *
	 * <p>The same rotor may not be used twice. If the user tries to specify 
	 * the same rotor multiple more than once, this method prints:
	 * <pre>  You cannot use the same rotor twice.</pre> to {@code System.out} 
	 * and then quits the program by calling {@code System.exit(-1)}.
	 * </blockquote>
	 * 
	 * @param rotorIndicesLine Text containing rotor index values separated
	 *        by spaces.
	 * @return Array of rotor index values.
	 */
	public static int [] parseRotorIndices( String rotorIndicesLine ) {

		int[] inputs = new int[RotorConstants.ROTORS.length];
		int input_length = 0;
		int[] inputs_result = new int[1];
		
		// TO.DO left to the student
		Scanner parser = new Scanner(rotorIndicesLine);
		if( rotorIndicesLine.length() == 0){
			System.out.println("You must specify at least one rotor.");
			System.exit(-1);
		}else{
			// saves all the input and checks for invalid rotors
			while( parser.hasNextInt()){
				int nextInt = parser.nextInt();
				if( nextInt < RotorConstants.ROTORS.length 
						&& nextInt >= 0){
					inputs[input_length] = nextInt;
					input_length++;
				}else{
					System.out.print("Invalid rotor. ");
					System.out.print("You must enter an integer between 0 and ");
					System.out.println(RotorConstants.ROTORS.length-1 + ".");
					System.exit(-1);
				}
			}
			
			inputs_result = new int[input_length];
			
			//loop through input to check for duplicates
			for( int i = 0; i<input_length; i++){
				for( int j = 0; j < input_length; j++){
					if( inputs[j] == inputs[i] && i != j){
						System.out.println("You cannot use the same rotor twice.");
						System.exit(-1);
					}
				}
				inputs_result[i] = inputs[i];
			}
			
		}
		return inputs_result;
	}

	/**
	 * Creates and returns array of rotor ciphers, based on rotor indices.
	 * 
	 * The array of rotor ciphers returned is a 2D array, 
	 * where each "row" of the array represents a given rotor's rotor cipher 
	 * in integral form.
	 * 
	 * The number of rows is equal to the number of rotors in use, as
	 * specified by the length of rotorIndices parameter.
	 *
	 * @param rotorIndices The indices of the rotors to use. Each value in this
	 *        array should be a valid index into the
	 *        {@code RotorConstants.ROTORS}array.
	 *        
	 * @return The array of rotors, each of which is itself an array of ints
	 *         copied from {@code RotorConstants.ROTORS}.
	 */
	public static int [][] setUpRotors( int [] rotorIndices ) {

		// TO.DO left to the student
		int[][] result = new int[rotorIndices.length][RotorConstants.ROTOR_LENGTH];
		for( int i=0; i < rotorIndices.length; i++){
			String rotor = (RotorConstants.ROTORS)[rotorIndices[i]];
			int[] rotorConverted = convertRotor( rotor);
			for( int j=0; j<26; j++)
				result[i][j] = rotorConverted[j];
		}
		
		return result;
	}

	/**
	 * Creates and returns a 2D array containing the notch positions for 
	 * each rotor being used.
	 * 
	 * <p>Each "row" of the array represents the notch positions of a single
	 * rotor. A rotor may have more than one notch position, so each "row"
	 * will contain one or more integers. There will be multiple rows, if
	 * multiple rotors are in use.</p> 
	 * 
	 * <p>Note that this array may be jagged, since different rotors have 
	 * different numbers of notch positions.</p>
	 *
	 * <p> <pre>
	 *     Example:
	 *     Input: [2, 1, 3]
	 *     Output: a 2D Array that would look something like this:
	 *            [[Array of notch positions of Rotor II],
	 *             [Array of notch positions of Rotor I] ,
	 *             [Array of notch positions of Rotor III]]</pre>
	 *
	 * @param rotorIndices Indices of the rotors. Each value in this
	 *        array should be a valid index into the
	 *        {@code RotorConstants.ROTORS} array.
	 *        
	 * @return An array containing the notch positions for each selected rotor.
	 */
	public static int [][] getRotorNotches( int[] rotorIndices ) {

		// TO.DO left to the student
		int rotorIndices_length = rotorIndices.length;
		int[][] notch_list = new int[rotorIndices_length][];
		for( int i = 0; i<rotorIndices_length; i++){
			notch_list[i] = RotorConstants.NOTCHES[rotorIndices[i]];
		}
		return notch_list;
	}

	/**
	 * Converts a rotor cipher from its textual representation into an integer-
	 * based rotor cipher. Each int would be in the range [0, 25], representing
	 * the alphabetical index of the corresponding character.
	 *
	 * <p>The mapping of letters to integers works as follows: <br />
	 * Each letter should be converted into its alphabetical index.
	 * That is, 'A' maps to 0, 'B' maps to 1, etc. until 'Z', which maps to 25.
	 *
	 * <p><pre>
	 * Example:
	 * Input String: EKMFLGDQVZNTOWYHXUSPAIBRCJ
	 * Output Array: [4 10 12 5 11 6 3 16 21 25 13 19 14 22 24 7 23 20 18 15 0
	 *                8 1 17 2 9]</pre>
	 *
	 * @param rotorText Text representation of the rotor. This will be like
	 *        the Strings in {@code RotorConstants.ROTORS}; that is, it will be
	 *        a String containing all 26 upper-case letters.
	 *        
	 * @return array of values between 0 and 25, inclusive that represents the
	 *        integer index value of each character.
	 */
	public static int [] convertRotor( String rotorText ) {

		// returning variable
		int[] mapped_result = new int[RotorConstants.ROTOR_LENGTH];
		
		for( int i = 0; i < RotorConstants.ROTOR_LENGTH; i++){
			mapped_result[i] = (rotorText.charAt(i) - 65);
		}
		return mapped_result;
	}

	/**
	 * Encodes a single uppercase character according to the current
	 * state of the Enigma encoding rotors.
	 *
	 * <p>To do this:
	 * <ol><li>Convert input character to its alphabetical index, 
	 * e.g. 'A' would be 0, 'B' would be 1, etc.</li>
	 * <li>Run the letter through the rotors in forward order.</li>
	 * </ol>
	 * 
	 * <p>To "run character through rotors", use your converted-letter as the
	 * index into the desired row of the rotors array. 
	 * Then, apply the reflector, and run the letter through the rotors again, 
	 * but in reverse. Encoding in reverse not only implies that the 
	 * rotor-order is to be reversed. It also means that each cipher
	 * is applied in reverse.</p>
	 * 
	 * An example:
	 * <pre>
	 *      Cipher (input):     EKMFLGDQVZNTOWYHXUSPAIBRCJ
	 *      Alphabet (output):  ABCDEFGHIJKLMNOPQRSTUVWXYZ
	 * </pre>
	 * While encoding in reverse, 'E' would get encoded as 'A', 'K' as 'B',
	 * etc. (In the forward direction, 'E' would get encoded as 'L')
	 *
	 * Finally, convert your letter integer index value back to a 
	 * traditional character value.
	 *
	 * @param rotors Current configuration of rotor ciphers, each in
	 *         integral rotor cipher form. Each "row" of this array represents
	 *         a rotor cipher.
	 *         
	 * @param reflector The special reflector rotor in integral rotor cipher
	 *         form.
	 *         
	 * @param input The character to be encoded. Must be an upper-case 
	 * letter. DO NOT CONVERT TO UPPERCASE HERE.
	 * 
	 * @return The result of encoding the input character. ALL encoded 
	 *         characters are upper-case.
	 */
	public static char encode( int [][] rotors, int [] reflector, char input ) {

		// TO.DO left to the student
		int input_index = input - 65;
		char output =  0;
		
		// forward encoding
		for( int i = 0; i < rotors.length; i++){
			//System.out.print(input_index+"->");
			input_index = rotors[i][input_index];
			//System.out.print(input_index+", ");
		}
		
		// add reflector
		//System.out.print(input_index+"->");
		input_index = reflector[input_index];
		//System.out.print(input_index+", ");
		
		// reverse encoding
		for( int i = rotors.length; i >0; i--){
			//System.out.print(input_index+"->");
			//input_index = rotors[i-1][input_index];
			for( int j=0; j<RotorConstants.ROTOR_LENGTH; j++){
				if( input_index == rotors[i-1][j]){
					input_index = j;
					break;
				}
			}
			//System.out.println(input_index+", ");
		}
		
		output = (char) (input_index + 65);
		
		return output;

	}

	/**
	 * Advances the rotors.  (Always advances rotor at index 0. May also
	 * advance other rotors depending upon notches that are reached.)
	 *
	 * <ol><li> Advancement takes place, starting at rotor at index 0. </li>
	 * <li>The 0th rotor is rotated</li>
	 * <li>Update the corresponding offset in <tt>rotorOffsets.</tt></li>
	 * <li>Check to see if the current offset matches a notch
	 * (meaning that a notch has been reached). </li>
	 * <li>If a notch has been reached:
	 * <ol><li>The next rotor must also be advanced</li>
	 *     <li>And have its rotorOffset updated.</li>
	 *     <li>And if a notch is reached, the next rotor is advanced.</li>
	 *     </ol>
	 * <li>Otherwise, notch was not reached, so no further rotors are advanced.</li>
	 * </ol>
	 *
	 * <p>Advancement halts when a <tt>rotorOffset</tt> is updated and
	 * it does not reach a notch for that rotor.</p>
	 * 
 	 * Note: The reflector never advances, it always stays stationary.
	 *
	 * @param rotors The array of rotor ciphers in their current configuration.
	 *         The rotor at index 0 is the first rotor to be considered for
	 *         advancement. It will always rotate exactly once. The remaining
	 *         rotors may advance when notches on earlier rotors are reach.
	 *         Later rotors will not not advance as often unless there is a 
	 *         notch at each index. (Tip: We try such a configuration during 
	 *         testing). The data in this array will be updated to show the 
	 *         rotors' new positions.
	 *         
	 * @param rotorOffsets The current offsets by which the rotors have been
	 *        rotated. These keep track of how far each rotor has rotated since
	 *        the beginning of the program. The offset at index i will
	 *        correspond to rotor at index 0 of rotors. e.g. offset 0 pertains
	 *        to the 0th rotor cipher in rotors.
	 *        Will be updated in-place to reflect the new offsets after
	 *        advancing.  The offsets are compared to notches to know when
	 *        next rotor must also be advanced.
	 *        
	 * @param rotorNotches The positions of the notches on each of the rotors.
	 *        Each row of this array represents the notches of a certain rotor.
	 *        The ith row will correspond to the notches of the ith rotor
	 *        cipher in rotors.  Only when a rotor advances to its notched 
	 *        position does the next rotor in the chain advance.
	 */
	public static void advance( int [][] rotors, int [] rotorOffsets,
			int [][] rotorNotches)
	{
		// TODO left to the student

		checkrotors:
		for( int i=0; i<rotors.length; i++ ){
			rotate(rotors[i]);
			if(rotorOffsets[i] == RotorConstants.ROTOR_LENGTH-1)
				rotorOffsets[i] = 0;
			else
				rotorOffsets[i]++;
			boolean reached_notch = false;
			for( int j=0; j<rotorNotches[i].length; j++){
				if( rotorOffsets[i] == rotorNotches[i][j]){
					reached_notch = true;
				}
			}
			if( !reached_notch)
				break checkrotors;
		}
		

		/*
		 * Hint: call rotate() to rotate a rotor. 
		 * Check offset against notches: if a notch is reached,
		 * advance the next rotor and continue until advance and
		 * offset does not indicated that a notch has been reached.
		 */

	}

} // end of Enigma class
