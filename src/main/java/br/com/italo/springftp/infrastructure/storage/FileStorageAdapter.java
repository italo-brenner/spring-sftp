package br.com.italo.springftp.infrastructure.storage;

import br.com.italo.springftp.core.port.outgoing.FileStorage;
import br.com.italo.springftp.infrastructure.storage.config.StorageProperties;
import br.com.italo.springftp.infrastructure.storage.exception.StorageException;
import br.com.italo.springftp.infrastructure.storage.exception.StorageFileNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.sftp.client.SftpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.stream.Stream;

@Slf4j
@Service
public class FileStorageAdapter implements FileStorage {

    private final Path rootLocation;

    @Autowired
    private SessionFactory<SftpClient.DirEntry> sftpSessionFactory;

    @Autowired
    public FileStorageAdapter(StorageProperties properties) {
            if (properties.getLocation().trim().length() == 0) {
                throw new StorageException("File upload location can not be Empty.");
            }

            this.rootLocation = Paths.get(properties.getLocation());
            if (Files.notExists(this.rootLocation)) {
                FileAttribute<?> attrs = PosixFilePermissions.asFileAttribute(
                        PosixFilePermissions.fromString("rwxr-x---"));

                try {
                    // Cria o diretório com permissões
                    Files.createDirectory(this.rootLocation, attrs);
                } catch (IOException e) {
                    throw new StorageException("Failed create folder.", e);
                }
            }
    }

    @Override
    public void store(MultipartFile file) {
        log.info("Entrou no método store(MultipartFile file)");
        try {
            try (Session<SftpClient.DirEntry> session = sftpSessionFactory.getSession()) {
                session.write(file.getInputStream(), "upload/" + file.getOriginalFilename());
                log.info("Upload concluído: {}", file.getOriginalFilename());
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}