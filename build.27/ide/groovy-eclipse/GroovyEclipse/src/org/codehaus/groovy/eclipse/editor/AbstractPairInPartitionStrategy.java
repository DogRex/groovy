package org.codehaus.groovy.eclipse.editor;

/**
 * Abstract implementation with some generally useful methods.
 * 
 * @author emp
 */
public abstract class AbstractPairInPartitionStrategy implements
		IPairInPartitionStrategy {
	/**
	 * Check if the sequence contains any of the characters in the samples.
	 * 
	 * @param seq
	 * @param samples
	 * @return True if a partial match exists, else false.
	 */
	protected boolean partiallyMatchesOne(String seq, char[] samples) {
		for (int i = 0; i < samples.length; ++i) {
			if (seq.indexOf(samples[i]) != -1)
				return true;
		}
		return false;
	}

	/**
	 * Check if the sequence is equal to one of the samples.
	 * 
	 * @param seq
	 * @param samples
	 * @return
	 */
	protected boolean matchesOne(String seq, String[] samples) {
		for (int i = 0; i < samples.length; ++i) {
			if (seq.equals(samples[i]))
				return true;
		}
		return false;
	}
}
