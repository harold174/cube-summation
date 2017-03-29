package com.summation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation for summation operations
 * 
 * @author harold.murcia
 */
@Service
public class SummationImpl implements SummationService {

	/**
	 * Input file name
	 */
	public static final String INPUT_FILE_NAME = "upload-dir/input.in";

	/**
	 * Output file name
	 */
	public static final String OUTPUT_FILE_NAME = "upload-dir/output.out";

	/**
	 * Update operation name
	 */
	public static final String UPDATE_OPERATION = "UPDATE";

	/**
	 * Query operation name
	 */
	public static final String QUERY_OPERATION = "QUERY";

	/*
	 * Define the logger object for this class
	 */
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Number of test cases in the file
	 */
	private static int numTestCases;

	/**
	 * Execute the file
	 */
	public void executeFile() {

		System.out.println("Running file...");

		try(BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_NAME));
				PrintWriter writer = new PrintWriter(OUTPUT_FILE_NAME);) {

			//Load head file
			numTestCases=Integer.parseInt(br.readLine());

			//Load tests cases
			loadTestCases(br, writer);



		} catch (FileNotFoundException e) {
			
			log.error("File not found", e);

		} catch (IOException e) {

			log.error("I/0 file error", e);
		}

		System.out.println("File successfully processed");


	}

	/**
	 * Load and execute the N test cases of the input file
	 * 
	 * @param br {@link BufferedReader} file reader
	 * @param writer {@link PrintWriter} file writer
	 * @throws IOException
	 */
	private static void loadTestCases(BufferedReader br, PrintWriter writer) throws IOException{

		int sizeDimension;
		int tmpNumberOperations;
		String[] tmpInputLine;
		for (int i = 0; i < numTestCases; i++) {
			tmpInputLine = br.readLine().split(" ");
			sizeDimension = Integer.parseInt(tmpInputLine[0]);
			tmpNumberOperations = Integer.parseInt(tmpInputLine[1]);
			executeTestCases(br, writer, sizeDimension, tmpNumberOperations);
		}

	}

	/**
	 * Execute operations for each test case
	 * 
	 * @param br {@link BufferedReader} file reader
	 * @param writer {@link PrintWriter} file writer
	 * @param matrixSize {@link Integer} matrix dimension
	 * @param numberOperations {@link Integer} operations number for test case
	 * @throws IOException
	 */
	private static void executeTestCases(BufferedReader br, PrintWriter writer, int matrixSize, 
			int numberOperations) throws IOException{

		int[][][] matrix = new int[matrixSize][matrixSize][matrixSize];
		String[] tmpInputLine;
		String operation;
		for (int i = 0; i < numberOperations; i++) {
			tmpInputLine = br.readLine().split(" ");
			operation = tmpInputLine[0];
			if(operation.equals(UPDATE_OPERATION)){

				executeUpdate(matrix, Integer.parseInt(tmpInputLine[1]), Integer.parseInt(tmpInputLine[2]), 
						Integer.parseInt(tmpInputLine[3]), Integer.parseInt(tmpInputLine[4]));

			}else{

				executeQuery(writer, matrix, Integer.parseInt(tmpInputLine[1]), Integer.parseInt(tmpInputLine[2]),
						Integer.parseInt(tmpInputLine[3]), Integer.parseInt(tmpInputLine[4]), 
						Integer.parseInt(tmpInputLine[5]), Integer.parseInt(tmpInputLine[6]));
			}

		}

	}

	/**
	 * Execute query operation, calculating the sum of the value of blocks 
	 * whose x coordinate is between x1 and x2 (inclusive), 
	 * y coordinate between y1 and y2 (inclusive) and z coordinate 
	 * between z1 and z2 (inclusive).
	 * 
	 * @param writer {@link PrintWriter} file writer
	 * @param matrix {@link Integer[][][]} matrix of test case
	 * @param x1 {@link Integer} x1 coordinate
	 * @param y1 {@link Integer} y1 coordinate
	 * @param z1 {@link Integer} z1 coordinate
	 * @param x2 {@link Integer} x2 coordinate
	 * @param y2 {@link Integer} y2 coordinate
	 * @param z2 {@link Integer} z2 coordinate
	 */
	private static void executeQuery(PrintWriter writer, int[][][] matrix, int x1, int y1, int z1, int x2, int y2,
			int z2) {

		int suma = 0;
		for(int x = x1-1; x <= x2-1; x++){
			for (int y = y1-1; y <= y2-1; y++) {
				for (int z = z1-1; z <= z2-1; z++) {
					suma += matrix[x][y][z];
				}
			}
		}
		writer.println(suma);
	}


	/**
	 * Executes the update operation, updating the value of block (x,y,z) to W.
	 * @param matrix {@link Integer[][][]} matrix of test case
	 * @param x {@link Integer} x coordinate
	 * @param y {@link Integer} y coordinate
	 * @param z {@link Integer} z coordinate
	 * @param w {@link Integer} value to be set
	 */
	private static void executeUpdate(int[][][] matrix, int x, int y, int z, int w) {

		matrix[x-1][y-1][z-1] = w;

	}
}
