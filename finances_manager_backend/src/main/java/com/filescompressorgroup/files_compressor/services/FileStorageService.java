package com.filescompressorgroup.files_compressor.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.filescompressorgroup.files_compressor.config.FileStorageConfig;
import com.filescompressorgroup.files_compressor.exceptions.FileStorageException;
import com.filescompressorgroup.files_compressor.exceptions.MyFileNotFoundException;
import com.filescompressorgroup.files_compressor.filecompressor.entities.Node;
import com.filescompressorgroup.files_compressor.filecompressor.util.FileEnconding;
import com.filescompressorgroup.files_compressor.models.FilePresentation;

@Service
public class FileStorageService {

	private final Path fileStorageLocation;
	private final Path compressedFileStorageLocation;
	private final Path decodedFileStorageLocation;

	@Autowired
	public FileStorageService(FileStorageConfig fileStorageConfig) {
		Path path = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();
		Path compressedPath = Paths.get("C:/FilesFinancesManagerED/CompressedFiles").toAbsolutePath().normalize();
		Path decodedPath = Paths.get("C:/FilesFinancesManagerED/DecodedFiles").toAbsolutePath().normalize();
		
		// trocando da pasta que ficam os arquivos que foram salvos para a pasta que está
		// os arquivos compactados.
		//Path path = Paths.get("C:\\FilesFinancesManagerED\\CompressedFiles")
		//		.toAbsolutePath().normalize();

		this.fileStorageLocation = path;
		this.compressedFileStorageLocation = compressedPath;
		this.decodedFileStorageLocation = decodedPath;

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception e) {
			throw new FileStorageException(
					"Could not create the directory where the uploaded files will be stored!", e);
		}
	}

	public String compress(MultipartFile file) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			// Filename..txt
			if (filename.contains("..")) {
				throw new FileStorageException(
						"Sorry! Filename contains invalid path sequence " + filename);
			}
			Path targetLocation = this.fileStorageLocation.resolve(filename);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			String compressedFilename = compressFile(filename, targetLocation);
			
			return compressedFilename;
		} catch (Exception e) {
			throw new FileStorageException(
					"Could not store file " + filename + ". Please try again!", e);
		}
	}
	
	
	public String decompress(MultipartFile file) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			// Filename..txt
			if (filename.contains("..")) {
				throw new FileStorageException(
						"Sorry! Filename contains invalid path sequence " + filename);
			}
			Path targetLocation = this.compressedFileStorageLocation.resolve(filename);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			String decompressedFilename = decompressFile(filename, targetLocation);
			
			return decompressedFilename;
		} catch (Exception e) {
			throw new FileStorageException(
					"Could not store file " + filename + ". Please try again!", e);
		}
	}
	
	
	/*public String storeFile(MultipartFile file) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			// Filename..txt
			if (filename.contains("..")) {
				throw new FileStorageException(
						"Sorry! Filename contains invalid path sequence " + filename);
			}
			Path targetLocation = this.fileStorageLocation.resolve(filename);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			compressFile(filename, targetLocation);
			
			return filename;
		} catch (Exception e) {
			throw new FileStorageException(
					"Could not store file " + filename + ". Please try again!", e);
		}
	}*/

	public Resource loadCompressedFileAsResource(String filename) {
		// arquivo quando decodificado esta com a quantidade de paginas do arquivo
		// original porém está com o conteúdo em branco, minha suspeita é que para que
		// funcione, todas as classes de Node depende, devem implementar também
		// serializable(outra ideia observar se o decoder funciona assim que usa o encoder
		// para saber aonde pode estar o problema)
		System.out.println("O filename no loadFileAsResource é: " + filename);
		try {
			Path filePath = this.compressedFileStorageLocation.resolve
					(filename).normalize();
			
			//String decodePathDestination = "C:/FilesFinancesManagerED/DecodedFiles/"
			//		+ filename;
			
			//Path decodedFilePath = this.decodedFileStorageLocation.resolve
			//		(decodePathDestination).normalize();
			
			//Node root = desearializeNodeRoot(filename);
			//FileEnconding.decodeFile(filePath.toString(), decodedFilePath.toString(), root);
			
			
			//Resource resource = new UrlResource(decodedFilePath.toUri());
			Resource resource = new UrlResource(filePath.toUri());
			System.out.println("resource é: " + resource);

			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found");
			}
		} catch (Exception e) {
			throw new MyFileNotFoundException("File not found" + filename, e);
		}
	}
	
	
	public Resource loadDecompressedFileAsResource(String filename) {
		// arquivo quando decodificado esta com a quantidade de paginas do arquivo
		// original porém está com o conteúdo em branco, minha suspeita é que para que
		// funcione, todas as classes de Node depende, devem implementar também
		// serializable(outra ideia observar se o decoder funciona assim que usa o encoder
		// para saber aonde pode estar o problema)
		System.out.println("O filename no loadFileAsResource2 é: " + filename);
		try {
			
			Path filePath2 = this.decodedFileStorageLocation.resolve
					(filename).normalize();
			
			Resource resource = new UrlResource(filePath2.toUri());
			System.out.println("O nome do arquivo descompactado é: " + resource.getFilename());

			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found");
			}
		} catch (Exception e) {
			throw new MyFileNotFoundException("File not found" + filename, e);
		}
	}


	
	public String compressFile(String filename, Path targetLocation) throws IOException {
		String encodedBinaryFilePath = "C:/FilesFinancesManagerED/CompressedFiles/";

		String onlyFilename = getFileNameOnly(filename);

		String fullFilename = onlyFilename + "_compressed.bin";
		encodedBinaryFilePath = encodedBinaryFilePath + fullFilename;
		
		System.out.println("O path completo de encodedBinaryFilePath é: " + encodedBinaryFilePath);

		Node root = FileEnconding.encodeFile(targetLocation.toString(), encodedBinaryFilePath);
		
		serializeNodeRoot(root, onlyFilename);
		
		return fullFilename;
	}
	
	
	public String decompressFile(String filename, Path targetLocation) throws Exception {
		Path encodedFilePath = this.compressedFileStorageLocation.resolve
				(filename).normalize();
		
		
		
		String decodePathDestination = "C:/FilesFinancesManagerED/DecodedFiles/"
				+ filename;
		
		Path decodedFilePath = this.decodedFileStorageLocation.resolve
				(decodePathDestination).normalize();
		
		Node root = desearializeNodeRoot(filename);
		FileEnconding.decodeFile(encodedFilePath.toString(), decodedFilePath.toString(), root);
		
		//Resource resource = new UrlResource(decodedFilePath.toUri());
		
		//serializeNodeRoot(root, onlyFilename);
		String onlyFilename = getFileNameOnly(filename);
		String fullFilename = onlyFilename + ".txt";
		
		return fullFilename;
	}
	
	
	public void serializeNodeRoot(Node root, String onlyFilename) {
        try {
        	String fullFileName = "C:/FilesFinancesManagerED/SerializedObjects/Root" 
        			+ onlyFilename + ".ser";
        	System.out.println("Path do root: " + fullFileName);
        	
            FileOutputStream fileOut = new FileOutputStream(fullFileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            
            out.writeObject(root);
            out.close();
            fileOut.close();
            
            System.out.println("Objeto serializado em " + fullFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	
	public Node desearializeNodeRoot(String onlyFilename) throws Exception {
        try {
            FileInputStream fileIn = new FileInputStream
            		("C:/FilesFinancesManagerED/SerializedObjects/Root" 
            				+ onlyFilename + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Node root = (Node) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Objeto desserializado:");
            
            return root;
        } catch (IOException | ClassNotFoundException e) {
            throw new Exception("Erro ao desserializar objeto", e);
        }
	}
	
	public String getFileNameOnly(String filename) {
		int filenameSize = filename.length();

		char pathChar = ' ';
		int dotIndex = 0;

		// encontrando o indice do ponto que separa o nome do arquivo da extensão dele
		for (int i = filenameSize - 1; i >= 0; i--) {
			pathChar = filename.charAt(i);
			if (pathChar == '.') {
				dotIndex = i;
				break;
			}
		}

		// removendo a extensão do arquivo para obter somente o nome com o .
		String onlyFilename = filename.substring(0, dotIndex);
		System.out.println("Nome do arquivo:" + onlyFilename);
		
		return onlyFilename;
	}
	
	
	public List<FilePresentation> findAll() throws Exception {
		// Path to the directory you want to list files from
		String directory = "C:/FilesFinancesManagerED/Files";

		// Create a File object representing the directory
		File folder = new File(directory);

		// Check if it's a valid directory
		if (folder.isDirectory()) {
			// List all files in the directory
			File[] files = folder.listFiles();

			// Check if the list is not empty
			if (files != null) {
				List<FilePresentation> filesPresentations = new ArrayList<>();

				for (File file : files) {
					String fileName = file.getName();

					FilePresentation filePresentation = new FilePresentation();
					filePresentation.setFileName(fileName);

					filesPresentations.add(filePresentation);
				}

				// Print the list of file names
				System.out.println("File names:");
				for (FilePresentation fileName : filesPresentations) {
					System.out.println(fileName.getFileName());
				}
				return filesPresentations;
			} else {
				System.out.println("The directory is empty.");
				throw new Exception("The directory is empty.");
			}
		} else {
			System.out.println("The specified path is not a valid directory.");
			throw new Exception("The specified path is not a valid directory.");
		}
	}
}
