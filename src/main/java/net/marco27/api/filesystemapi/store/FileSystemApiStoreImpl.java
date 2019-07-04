package net.marco27.api.filesystemapi.store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.marco27.api.filesystemapi.domain.FileStructure;
import net.marco27.api.filesystemapi.repository.FileStructureCrudRepository;

@Service
public class FileSystemApiStoreImpl implements FileSystemApiStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemApiStoreImpl.class);

    @Autowired
    protected DataSource dataSource;

    private FileStructureCrudRepository fileStructureCrudRepository;

    public FileSystemApiStoreImpl(@Autowired final FileStructureCrudRepository fileStructureCrudRepository) {
        this.fileStructureCrudRepository = fileStructureCrudRepository;
    }

    @Override
    public FileStructure findFileStructureById(final String path) {
        final Optional<FileStructure> result = this.fileStructureCrudRepository.findById(path);
        return result.orElse(null);
    }

    @Override
    public FileStructure findFileStructureByPath(final String path) {
        return this.fileStructureCrudRepository.findByPath(path);
    }

    @Override
    public FileStructure saveFileStructure(final FileStructure fileStructure) {
        return this.fileStructureCrudRepository.save(fileStructure);
    }

    @Override
    public void deleteFileStructure(final FileStructure fileStructure) {
        this.fileStructureCrudRepository.delete(fileStructure);
    }

    @Override
    public FileStructure loadFileStructure(final String path) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.dataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT PATH, EXT, IS_DIRECTORY, NAME, TIMESTAMP FROM FILE_STRUCTURE WHERE PATH = ?");
            preparedStatement.setString(1, path);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final String findPath = resultSet.getString(1);
                final String ext = resultSet.getString(2);
                final boolean isDirectory = resultSet.getBoolean(3);
                final String name = resultSet.getString(4);
                final String timestamp = resultSet.getString("TIMESTAMP");
                return new FileStructure.Builder(findPath, name, ext).isDirectory(isDirectory).withName(name).withTimestamp(timestamp).build();
            }
            resultSet.close();
        } catch (Exception e) {
            LOGGER.error("Error loadFileStructure", e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    LOGGER.error("Error close PreparedStatement", e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.error("Error close Connection", e);
                }
            }
        }
        return null;
    }

}
