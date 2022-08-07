package fileCopying;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * This class is intended for copying all the files
 * with a specific extension in a directory (including the directory's
 * sub-directories) to another directory.
 * Caution: No warnings will be generated upon replacing files
 * @author SalmaKD
 */
public class FileExtractor {
	String extension = null; // extension of source files
	File sourceDir = null; // File object representing the source directory
	File destinationDir = null; // File object representing the destination directory
	int totalFilesCopied = 0; // total number of files copied into the destination directory

	/**
	 * This constructor assigns values to host object's fields if valid.
	 * @param extension extension of the files in the source directory
	 * @param sourceDirPath pathname of the source directory 
	 * @param destinationDirPath pathname of the destination directory
	 */
	FileExtractor(
			String extension, String sourceDirPath, String destinationDirPath)
	{
		// validate and store values in current object's fields

		// check if the extension parameter starts with "."
		if (extension.charAt(0) != '.')
			throw new IllegalArgumentException();
		this.extension = extension;

		this.sourceDir =  new File(sourceDirPath);
		this.destinationDir = new File(destinationDirPath);
		// check if both directories are valid
		if (!(isDirValid(this.sourceDir) && isDirValid(destinationDir)))
			throw new IllegalArgumentException();

		// If all the requirements are satisfied, 
		// start copying the content of the source directory
		copyFiles(sourceDir);
	}
	
	/**
	 * This method checks if its single parameter represents
	 * an existing directory or not.
	 * @param dir file that represents the directory
	 * @return true if the file is an existing directory; false otherwise
	 */
	private boolean isDirValid(File dir) 
	{
		return dir.exists() && dir.isDirectory();
	}

	/**
	 * This method: 
	 * 1. copies a requested file to the destination directory OR
	 * 2. goes through all the content of a directory and performs the first step if applicable.
	 * This process is made possible through recursive calls.
	 * @param DirORFile name of either a file or directory
	 */
	public void copyFiles(File dirORfile)
	{
		// check if the dirORfile represents a file or directory
		if (dirORfile.isDirectory() == true) {
			// get a list of directory contents
			File[] dirContent = dirORfile.listFiles();
			for(int i =0; i< dirContent.length; i++)
				// call the current method on each item in the list
				copyFiles(dirContent[i]);
		}
		else {
			// copy the file to the destination directory 
			// if the parameter represents a file
			copyFileWithTheRightExtension(dirORfile);
		}
	}

	/**
	 * This method copies the parameter file to the destination directory only if 
	 * its extension matches the requested extension.
	 * @param file file that is to be copied to the destination directory
	 */
	private void copyFileWithTheRightExtension(File file)
	{
		// make sure the extension matches the requested extension
		String fileName = file.getName();
		int firstIndexOfSuffix = fileName.indexOf('.');
		if (firstIndexOfSuffix != -1 && fileName.substring(firstIndexOfSuffix).equals(this.extension)) {
			// if so, copy the file
			File duplicateFile = new File(this.destinationDir.getAbsolutePath() + "\\" + fileName);
			try {
				copyFile(file, duplicateFile);
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
	
	/**
	 * This method copies the content of a file to another file.
	 * If the destination file already exists, its content 
	 * will be overridden (no warnings will be generated).
	 * @param sourceFile the file whose content is to be copied
	 * @param destinationFile the file to which the source file content will be written to 
	 * @throws Exception thrown if any of the reading or writing operations fail.
	 */ 
	private void copyFile(File sourceFile, File destinationFile) throws Exception
	{
		// get the source file's input stream
		FileInputStream sourceInput = new FileInputStream(sourceFile);
		// get the destination file's output stream
		FileOutputStream destinationOutput = new FileOutputStream(destinationFile);
		
		// read from the source file and write to the destination file
		int nextByte;
		while ( (nextByte = sourceInput.read()) != -1) 
			destinationOutput.write(nextByte);
		
		// close all streams
		sourceInput.close();
		destinationOutput.close();
		
		// if the execution ever reaches this line, it is considered that
		// the copying operation is complete and so the number of files copied so far
		// must be incremented by 1
		this.totalFilesCopied++;
	}

	/**
	 * This method reports the final status of the program after 
	 * all the operations are complete. If no files are copied to the destination
	 * directory, it is counted as a failed operation.
	 */
	public void getFinalStatus() 
	{
		if (this.totalFilesCopied != 0)
			System.out.println("Operation Successful.\nTotal Number of Files Copied: " + this.totalFilesCopied);
		else
			System.out.println("Operation Failed");
	}
	
	/**
	 * This unit test tests the program for bugs. 
	 */
	public static void unitTest()
	{
		FileExtractor dir = new FileExtractor(".class", "C:\\Users\\khoda\\Desktop\\lib", "C:\\Users\\khoda\\Desktop\\lib2");
		dir.getFinalStatus();

	}
}