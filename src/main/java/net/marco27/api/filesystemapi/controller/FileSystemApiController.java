package net.marco27.api.filesystemapi.controller;

import static net.marco27.api.base.ApiConstants.SLASH;
import static org.springframework.http.HttpStatus.CREATED;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import net.marco27.api.base.domain.JsonError;
import net.marco27.api.base.domain.JsonSuccess;
import net.marco27.api.filesystemapi.domain.FileStructure;
import net.marco27.api.filesystemapi.domain.PathFileToPrint;
import net.marco27.api.filesystemapi.service.FileSystemApiService;
import net.marco27.api.filesystemapi.store.FileSystemApiStore;
import net.marco27.api.filesystemapi.validation.model.ValidationResult;
import net.marco27.api.filesystemapi.validation.service.ValidationService;

/** The main use case for the API is to read the filesystem */
@RestController
@RequestMapping
public class FileSystemApiController {

    @Autowired
    private FileSystemApiService fileSystemApiService;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private FileSystemApiStore fileSystemApiStore;

    @GetMapping("/printPathToFile/{pathToPrint}/{fileToPrint}")
    public ResponseEntity<PathFileToPrint> printPathToFile(@Valid @PathVariable final String pathToPrint,
            @Valid @PathVariable final String fileToPrint) {
        final PathFileToPrint pathFileToPrint = new PathFileToPrint.Builder(pathToPrint, fileToPrint).build();
        final ValidationResult validationResult = validationService.validateInput(pathFileToPrint);
        if (validationResult.isValid()) {
            final PathFileToPrint result = fileSystemApiService.printPathToFile(pathFileToPrint.getPathToPrint(),
                    pathFileToPrint.getFileToPrint());
            return new ResponseEntity<>(result, CREATED);
        } else {
            return new ResponseEntity(new JsonError().addErrorMessage(validationResult.getErrorMessage()),
                    validationResult.getHttpStatus());
        }
    }

    /** The input path parameter from a request cannot start with SLASH, but absolute paths are used
     *
     * @param path to validate
     * @return a valid path */
    @Valid
    private String validatePath(@PathVariable @Valid final String path) {
        return StringUtils.startsWith(path, SLASH) ? path : SLASH.concat(path);
    }

    @GetMapping("/findFileStructureOracleById/{path}")
    public ResponseEntity<FileStructure> findFileStructureOracleById(@Valid @PathVariable final String path) {
        final FileStructure result = fileSystemApiStore.findFileStructureById(validatePath(path));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/findFileStructureOracleByPath/{path}")
    public ResponseEntity<FileStructure> findFileStructureOracleByPath(@Valid @PathVariable final String path) {
        FileStructure result = fileSystemApiStore.findFileStructureByPath(validatePath(path));
        if (result == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/deleteFileStructureOracle/{path}")
    public ResponseEntity<JsonSuccess> deleteFileStructureOracle(@Valid @PathVariable final String path) {
        FileStructure fileStructure = fileSystemApiStore.findFileStructureByPath(validatePath(path));
        if (fileStructure != null) {
            fileSystemApiStore.deleteFileStructure(fileStructure);
            return ResponseEntity.ok(new JsonSuccess());
        }
        return ResponseEntity.ok(new JsonSuccess(String.format("path not found %s", path)));
    }

    @PostMapping("/saveFileStructureOracle/{path}")
    public ResponseEntity<FileStructure> saveFileStructureOracle(@Valid @PathVariable final String path) {
        final String validPath = validatePath(path);
        FileStructure result = fileSystemApiStore.findFileStructureById(validatePath(validPath));
        if (result == null) {
            result = fileSystemApiService.createFileStructure(validPath);
            if (result != null) {
                result = fileSystemApiStore.saveFileStructure(result);
            }
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/loadFileStructureOracle/{path}")
    public ResponseEntity<FileStructure> loadFileStructureOracle(@Valid @PathVariable final String path) {
        final String validPath = validatePath(path);
        FileStructure result = fileSystemApiStore.loadFileStructure(validatePath(validPath));
        return ResponseEntity.ok(result);
    }

}
