import java.io.*;
import java.util.ArrayList;

import net.datastructures.*;

/**
 * Class Huffman that provides Huffman compression encoding and decoding of files
 * @author Lucia Moura
 * updated by Bo Sun
 */
public class Huffman {

	/** Named constant for the most-significant-bit mask (binary 1000 0000). */
	private static final int MSB_MASK = 0x80; // 128

	/**
	 *
	 * Inner class Huffman Node to Store a node of Huffman Tree
	 *
	 */
	private class HuffmanTreeNode {
		private int character;          // character represented by this node (applicable to leaves)
		private int count;              // frequency for the subtree rooted at node
		private HuffmanTreeNode left;   // left/0 subtree (null if empty)
		private HuffmanTreeNode right;  // right/1 subtree (null if empty)

		public HuffmanTreeNode(int c, int ct, HuffmanTreeNode leftNode, HuffmanTreeNode rightNode) {
			character = c;
			count = ct;
			left = leftNode;
			right = rightNode;
		}

		public int getChar() { return character; }
		public Integer getCount() { return count; }
		public HuffmanTreeNode getLeft() { return left; }
		public HuffmanTreeNode getRight() { return right; }

		/** A leaf has no children. */
		public boolean isLeaf() { return left == null && right == null; }
	}

	/**
	 *
	 * Auxiliary class to write bits to an OutputStream
	 * Since files output one byte at a time, a buffer is used to group each output of 8 bits.
	 * Method close should be invoked to flush half-filled buffers by padding extra 0's.
	 */
	private class OutBitStream {
		OutputStream out;
		int buffer;
		int buffCount;

		public OutBitStream(OutputStream output) { // associates this to an OutputStream
			out = output;
			buffer = 0;
			buffCount = 0;
		}

		public void writeBit(int i) throws IOException { // write one bit to OutputStream (using byte buffer)
			buffer = buffer << 1;
			buffer = buffer + i;
			buffCount++;
			if (buffCount == 8) {
				out.write(buffer);
				buffCount = 0;
				buffer = 0;
			}
		}

		public void close() throws IOException { // close output file, flushing half-filled byte
			if (buffCount > 0) { // flush remaining bits by padding 0's
				buffer = buffer << (8 - buffCount);
				out.write(buffer);
			}
			out.close();
		}
	}

	/**
	 *
	 * Auxiliary class to read bits from a file
	 * Since we must read one byte at a time, a buffer is used to group each input of 8 bits.
	 *
	 */
	private class InBitStream {
		InputStream in;
		int buffer;    // stores a byte read from input stream
		int buffCount; // number of bits already read from buffer (0..8)

		public InBitStream(InputStream input) { // associates this to an InputStream
			in = input;
			buffer = 0;
			buffCount = 8; // forces reading a new byte on the first readBit()
		}

		public int readBit() throws IOException { // read one bit from InputStream (using byte buffer)
			if (buffCount == 8) { // current buffer has been fully read; fetch next byte
				buffCount = 0;
				buffer = in.read();
				if (buffer == -1) return -1; // indicates stream ended
			}

			// Mask selects the next bit to read: MSB_MASK >> buffCount
			int aux = MSB_MASK >> buffCount;
			buffCount++;

			return ((aux & buffer) != 0) ? 1 : 0;
		}
	}

	/**
	 * Builds a frequency table indicating the frequency of each character/byte in the input stream.
	 * @param input is a file where to get the frequency of each character/byte
	 * @return freqTable an ArrayList<Integer> such that freqTable.get(i) = number of times character i appears in file
	 *                   and such that freqTable.get(256) = 1 (adding special character representing "end-of-file")
	 * @throws IOException indicating errors reading input stream
	 */
	private ArrayList<Integer> buildFrequencyTable(InputStream input) throws IOException {
		ArrayList<Integer> freqTable = new ArrayList<Integer>(257); // declare frequency table
		for (int i = 0; i < 257; i++) freqTable.add(i, 0); // initialize all frequencies to 0

		/************ your code comes here ************/
		// IMPORTANT: Also set freqTable[256] = 1 for the EOF (end-of-file) symbol.
		/************ your code ends here ************/

		return freqTable; // return computed frequency table
	}

	/**
	 * Create Huffman tree using the given frequency table; requires a heap priority queue to run in O(n log n)
	 * where n is the number of characters with nonzero frequency.
	 * @param freqTable the frequency table for characters 0..255 plus 256 = "end-of-file"
	 * @return root of the Huffman tree built by this method
	 */
	private HuffmanTreeNode buildEncodingTree(ArrayList<Integer> freqTable) {

		// Creates Huffman tree using a priority queue based on frequency at the root.

		/************ your code comes here ************/

		return null; // dummy return value so code compiles
	}

	/**
	 *
	 * @param encodingTreeRoot input parameter storing the root of the Huffman tree
	 * @return an ArrayList<String> of length 257 where code.get(i) returns a String of 0-1 corresponding to each character
	 *         in a Huffman tree; code.get(i) returns null if i is not a leaf of the Huffman tree.
	 */
	private ArrayList<String> buildEncodingTable(HuffmanTreeNode encodingTreeRoot) {
		ArrayList<String> code = new ArrayList<String>(257);
		for (int i = 0; i < 257; i++) code.add(i, null);

		/************ your code comes here ************/

		return code;
	}

	/**
	 * Encodes an input using encoding table that stores the Huffman code for each character.
	 * @param input input file to be encoded
	 * @param encodingTable table containing the Huffman code for each character
	 * @param output output file where the encoded bits will be written
	 * @throws IOException indicates I/O errors
	 */
	private void encodeData(InputStream input, ArrayList<String> encodingTable, OutputStream output) throws IOException {
		OutBitStream bitStream = new OutBitStream(output); // uses bitStream to output bit by bit

		/************ your code comes here ************/

		bitStream.close(); // flush remaining bits (with padding) and close
	}

	/**
	 * Decodes an encoded input using encoding tree, writing decoded file to output.
	 * @param input stream where header has already been read from
	 * @param encodingTreeRoot root of the Huffman tree
	 * @param output output stream where decoded bytes will be written
	 * @throws IOException indicates I/O errors
	 */
	private void decodeData(ObjectInputStream input, HuffmanTreeNode encodingTreeRoot, FileOutputStream output) throws IOException {

		InBitStream inputBitStream = new InBitStream(input); // reads bits from file

		/************ your code comes here ************/

	}

	/**
	 * Method that implements Huffman encoding on plain input into encoded output.
	 * @param inputFileName file to be encoded (compressed)
	 * @param outputFileName Huffman encoded output file corresponding to input
	 * @throws IOException indicates problems with input/output streams
	 */
	public void encode(String inputFileName, String outputFileName) throws IOException {
		System.out.println("\nEncoding " + inputFileName + " " + outputFileName);

		// Use try-with-resources to ensure all streams are closed properly.
		try (FileInputStream input = new FileInputStream(inputFileName);
			 FileInputStream copyinput = new FileInputStream(inputFileName); // read input twice
			 FileOutputStream out = new FileOutputStream(outputFileName);
			 ObjectOutputStream codedOutput = new ObjectOutputStream(out)) { // writes objects to file

			ArrayList<Integer> freqTable = buildFrequencyTable(input); // build frequencies from input
			HuffmanTreeNode root = buildEncodingTree(freqTable);       // build Huffman tree
			ArrayList<String> codes = buildEncodingTable(root);        // build codes for each character

			codedOutput.writeObject(freqTable); // write header with frequency table
			encodeData(copyinput, codes, codedOutput); // write encoded data bits
		}
	}

	/**
	 * Method that implements Huffman decoding on encoded input into a plain output.
	 * @param inputFileName file encoded (compressed) via encode()
	 * @param outputFileName output where the decoded file should be written
	 * @throws IOException indicates problems with input/output streams
	 * @throws ClassNotFoundException handles case where the file does not contain correct object at header
	 */
	public void decode(String inputFileName, String outputFileName) throws IOException, ClassNotFoundException {
		System.out.println("\nDecoding " + inputFileName + " " + outputFileName);

		// Use try-with-resources to ensure all streams are closed properly.
		try (FileInputStream in = new FileInputStream(inputFileName);
			 ObjectInputStream codedInput = new ObjectInputStream(in);
			 FileOutputStream output = new FileOutputStream(outputFileName)) {

			// Suppress unchecked cast warning.
			@SuppressWarnings("unchecked")
			ArrayList<Integer> freqTable = (ArrayList<Integer>) codedInput.readObject(); // read header frequency table

			HuffmanTreeNode root = buildEncodingTree(freqTable);
			decodeData(codedInput, root, output);
		}
	}
}