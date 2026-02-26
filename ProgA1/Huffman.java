package ProgA1;

import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;

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

		int b;
		while ((b = input.read()) != -1) {
			freqTable.set(b, freqTable.get(b) + 1);
		}
		freqTable.set(256, 1); // special end-of-file symbol

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
		HeapPriorityQueue<Integer, HuffmanTreeNode> pq = new HeapPriorityQueue<>();
		for (int i = 0; i <= 256; i++) {
			if (freqTable.get(i) > 0) {
				HuffmanTreeNode node = new HuffmanTreeNode(i, freqTable.get(i), null, null);
				pq.insert(freqTable.get(i), node);
			}
		}

		while (pq.size() > 1) {
			Entry<Integer, HuffmanTreeNode> e1 = pq.removeMin();
			Entry<Integer, HuffmanTreeNode> e2 = pq.removeMin();
			int combinedFreq = e1.getValue().count + e2.getValue().count;
			HuffmanTreeNode parent = new HuffmanTreeNode(-1, combinedFreq, e1.getValue(), e2.getValue());
			pq.insert(combinedFreq, parent);
		}

		return pq.removeMin().getValue();
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

		buildEncodingTableHelper(encodingTreeRoot, "", code);

		return code;
	}

	/**
	 * Recursive helper for buildEncodingTable.
	 * Performs DFS, appending "0" for left and "1" for right.
	 */
	private void buildEncodingTableHelper(HuffmanTreeNode node, String path, ArrayList<String> code) {
		if (node.isLeaf()) {
			// Edge case: single-node tree (only one symbol) gets code "0"
			if (path.isEmpty()) path = "0";
			code.set(node.getChar(), path);
			return;
		}
		if (node.getLeft() != null)  buildEncodingTableHelper(node.getLeft(),  path + "0", code);
		if (node.getRight() != null) buildEncodingTableHelper(node.getRight(), path + "1", code);
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

		int b;
		while ((b = input.read()) != -1) {
			String code = encodingTable.get(b);
			for (int i = 0; i < code.length(); i++) {
				bitStream.writeBit(code.charAt(i) == '1' ? 1 : 0);
			}
		}
		// Write the end-of-file code
		String eofCode = encodingTable.get(256);
		for (int i = 0; i < eofCode.length(); i++) {
			bitStream.writeBit(eofCode.charAt(i) == '1' ? 1 : 0);
		}

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

		// Special case: single-leaf tree
		if (encodingTreeRoot.isLeaf()) {
			return;
		}

		HuffmanTreeNode current = encodingTreeRoot;
		while (true) {
			int bit = inputBitStream.readBit();
			if (bit == -1) break; // end of stream

			if (bit == 0) current = current.getLeft();
			else          current = current.getRight();

			if (current.isLeaf()) {
				if (current.getChar() == 256) break; // end-of-file symbol
				output.write(current.getChar());
				current = encodingTreeRoot;
			}
		}
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

		// Print input/output byte counts
		System.out.println("Number of bytes in input : " + new File(inputFileName).length());
		System.out.println("Number of bytes in output: " + new File(outputFileName).length());
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

		// Print input/output byte counts
		System.out.println("Number of bytes in input : " + new File(inputFileName).length());
		System.out.println("Number of bytes in output: " + new File(outputFileName).length());
	}

	/**
	 * Entry interface for key-value pairs in priority queue
	 */
	interface Entry<K, V> {
		K getKey();
		V getValue();
	}

	/**
	 * Simple implementation of Entry interface
	 */
	private static class EntryImpl<K, V> implements Entry<K, V> {
		private K key;
		private V value;

		public EntryImpl(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() { return key; }

		@Override
		public V getValue() { return value; }
	}

	/**
	 * HeapPriorityQueue using Java's PriorityQueue as backing structure
	 */
	private static class HeapPriorityQueue<K extends Comparable<K>, V> {
		private PriorityQueue<Entry<K, V>> heap;

		public HeapPriorityQueue() {
			heap = new PriorityQueue<>(new Comparator<Entry<K, V>>() {
				@Override
				public int compare(Entry<K, V> e1, Entry<K, V> e2) {
					return e1.getKey().compareTo(e2.getKey());
				}
			});
		}

		public void insert(K key, V value) {
			Entry<K, V> entry = new EntryImpl<>(key, value);
			heap.offer(entry);
		}

		public Entry<K, V> removeMin() {
			return heap.poll();
		}

		public int size() {
			return heap.size();
		}

		public boolean isEmpty() {
			return heap.isEmpty();
		}
	}
}