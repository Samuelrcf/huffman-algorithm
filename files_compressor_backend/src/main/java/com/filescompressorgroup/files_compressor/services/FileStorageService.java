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
import java.util.LinkedList;
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
		Path compressedPath = Paths.get("C:/FilesCompressorED/CompressedFiles").toAbsolutePath().normalize();
		Path decodedPath = Paths.get("C:/FilesCompressorED/DecompressedFiles").toAbsolutePath().normalize();

		this.fileStorageLocation = path;
		this.compressedFileStorageLocation = compressedPath;
		this.decodedFileStorageLocation = decodedPath;

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception e) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored!",
					e);
		}
	}

	public String compress(MultipartFile file) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			// Filename..txt
			if (filename.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + filename);
			}
			Path targetLocation = this.fileStorageLocation.resolve(filename);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			String compressedFilename = compressFile(filename, targetLocation);
			
			// deleta o arquivo que o usuário mandou, para não gastar espaço no servidor
			deleteOriginalFile(filename);

			return compressedFilename;
		} catch (Exception e) {
			throw new FileStorageException("Could not store file " + filename + ". Please try again!", e);
		}
	}

	public String decompress(MultipartFile file) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			// Filename..txt
			if (filename.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + filename);
			}
			Path targetLocation = this.compressedFileStorageLocation.resolve(filename);
			// Files.copy(file.getInputStream(), targetLocation,
			// StandardCopyOption.REPLACE_EXISTING);

			String decompressedFilename = decompressFile(filename, targetLocation);

			return decompressedFilename;
		} catch (Exception e) {
			throw new FileStorageException("Could not store file " + filename + ". Please try again!", e);
		}
	}

	public Resource loadCompressedFileAsResource(String filename) {
		System.out.println("O filename no loadFileAsResource é: " + filename);
		try {
			Path filePath = this.compressedFileStorageLocation.resolve(filename).normalize();

			// String decodePathDestination = "C:/FilesFinancesManagerED/DecodedFiles/"
			// + filename;

			// Path decodedFilePath = this.decodedFileStorageLocation.resolve
			// (decodePathDestination).normalize();

			// Node root = desearializeNodeRoot(filename);
			// FileEnconding.decodeFile(filePath.toString(), decodedFilePath.toString(),
			// root);

			// Resource resource = new UrlResource(decodedFilePath.toUri());
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
		System.out.println("O filename no loadFileAsResource2 é: " + filename);
		try {

			Path filePath2 = this.decodedFileStorageLocation.resolve(filename).normalize();

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

	
	public List<File> findAllCompressedFilesInFolder(File folder) throws Exception {
		if (folder.isDirectory()) {
			File[] files = folder.listFiles();

			if (files != null) {
				List<File> foundedFiles = new LinkedList<>();

				for (File file : files) {
					foundedFiles.add(file);
				}
				return foundedFiles;
			} else {
				throw new Exception("The directory is empty.");
			}
		} else {
			throw new Exception("The specified path is not a valid directory.");
		}
	}

	public List<FilePresentation> findAllCompressedFiles() throws Exception {
		File folder = new File(this.compressedFileStorageLocation.toString());
		
		List<File> files = findAllCompressedFilesInFolder(folder);

		List<FilePresentation> filesPresentations = new LinkedList<>();
		for (File file : files) {
			String fileName = file.getName();

			FilePresentation filePresentation = new FilePresentation();
			filePresentation.setFileName(fileName);

			filesPresentations.add(filePresentation);
		}
		
		return filesPresentations;
	}
	
	
	
	// Metodo interessante, pois ele vai ficar se chamando caso haja uma pasta com o nome do proprio arquivo
	// quando o arquivo não for um diretorio ele vai buscar esse arquivo e retornar. É uma especie de
	// recursividade
    public File findFileByFilename(File folder, String filename) throws Exception {
        List<File> files = findAllCompressedFilesInFolder(folder);

        if (files != null && folder.isDirectory()) {
            for (File file : files) {
                if (file.getName().equals(filename)) {
                    return file;
                }
                
                if (file.isDirectory()) {
                    File foundedFile = findFileByFilename(file, filename);
                    if (foundedFile != null) {
                        return foundedFile;
                    }
                }
            }
        }
        return null;
    }
	
	
	

	public boolean deleteCompressedFile(String filename) throws Exception {
		File folder = new File(this.compressedFileStorageLocation.toString());
		
		File foundedFile = findFileByFilename(folder, filename);
		
		if(foundedFile != null) {
            if (foundedFile.delete()) {
                return true;
            }
		}
		
		return false;
	}
	
	
	public boolean deleteOriginalFile(String filename) throws Exception {
		File folder = new File(this.fileStorageLocation.toString());
		
		File foundedFile = findFileByFilename(folder, filename);
		
		if(foundedFile != null) {
            if (foundedFile.delete()) {
                return true;
            }
		}
		
		return false;
	}

	public String compressFile(String filename, Path targetLocation) throws IOException {
		String encodedBinaryFilePath = "C:/FilesCompressorED/CompressedFiles/";

		String onlyFilename = getFileNameOnly(filename);

		String fullFilename = onlyFilename + "_compressed.bin";
		encodedBinaryFilePath = encodedBinaryFilePath + fullFilename;

		System.out.println("O path completo de encodedBinaryFilePath é: " + encodedBinaryFilePath);

		Node root = FileEnconding.encodeFile(targetLocation.toString(), encodedBinaryFilePath);

		serializeNodeRoot(root, onlyFilename);

		return fullFilename;
	}

	public String decompressFile(String filename, Path targetLocation) throws Exception {
		Path encodedFilePath = this.compressedFileStorageLocation.resolve(filename).normalize();

		System.out.println("filename em decompressFile: " + filename);
		String onlyFilename = filename.replace("_compressed.bin", "");
		System.out.println("onlyFilename em decompressFile: " + onlyFilename);

		// decodificando o arquivo e colocando o .txt
		String decodePathDestination = "C:/FilesCompressorED/DecompressedFiles/" + onlyFilename + ".txt";

		Path decodedFilePath = this.decodedFileStorageLocation.resolve(decodePathDestination).normalize();

		Node root = desearializeNodeRoot(onlyFilename);
		FileEnconding.decodeFile(encodedFilePath.toString(), decodedFilePath.toString(), root);

		// Resource resource = new UrlResource(decodedFilePath.toUri());

		// serializeNodeRoot(root, onlyFilename);

		// como o interesse é um .txt, acrescenta-se .txt na frente
		String fullFilename = onlyFilename + ".txt";

		return fullFilename;
	}

	public void serializeNodeRoot(Node root, String onlyFilename) {
		try {
			String fullFileName = "C:/FilesCompressorED/SerializedObjects/Root" + onlyFilename + ".ser";
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
			FileInputStream fileIn = new FileInputStream(
					"C:/FilesCompressorED/SerializedObjects/Root" + onlyFilename + ".ser");
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

		// removendo a extensão do arquivo para obter somente o nome
		String onlyFilename = filename.substring(0, dotIndex);
		System.out.println("Nome do arquivo:" + onlyFilename);

		return onlyFilename;
	}
}
