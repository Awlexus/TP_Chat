package logic.storage;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Maximilian Estfeller
 * @since 27.08.2017
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DataRepository implements Serializable {

    private static HashMap<String, Collection<String>> repositoryFiles = new HashMap<>();

    private String repositoryPath;


    public static Map<String, Collection<String>> getRepositoryFiles() {
        return repositoryFiles;
    }

    public static void setRepositoryFiles(String path) {
        try {
            HashMap readList = (HashMap) DataIOs.read(path);
            for (Object key : readList.keySet()) {
                repositoryFiles.put((String) key, new ArrayList<>());
                for (Object o : ((ArrayList) readList.get(key)))
                    repositoryFiles.get(key).add((String) o);
            }
        } catch (DataException e) {
            System.out.println(e.getMessage());
        } catch (ClassCastException e) {
            System.out.println("ERROR: can't resolve repositoryFiles on given path (" + path + ")" +
                    " as an HaspMap<String, ArrayList<String>>");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void printRepositoryFiles(String path) {
        try {
            DataIOs.print(path, repositoryFiles);
        } catch (DataException e) {
            System.out.println("Can't print repositoryFiles");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataRepository(String repositoryPath) {
        this.repositoryPath = repositoryPath;

        if (!repositoryFiles.containsKey(repositoryPath))
            repositoryFiles.put(repositoryPath, new ArrayList<>());
    }

    public void print(String fileName, Serializable obj) throws DataException, IOException {
        DataIOs.print(repositoryPath + "\\" + fileName, obj);

        if (!repositoryFiles.get(repositoryPath).contains(fileName))
            repositoryFiles.get(repositoryPath).add(fileName);
    }

    public Object read(String fileName) throws DataException, IOException, ClassNotFoundException {
        return DataIOs.read(repositoryPath + "\\" + fileName);
    }

    /**
     * Removes this repository from the data, and deletes all of it's files.
     */
    public void deleteRepository() {
        clearRepository();
        repositoryFiles.remove(repositoryPath);
    }

    /**
     * Deletes all Files from this repository.
     */
    public void clearRepository() {
        repositoryFiles.get(repositoryPath).removeIf(s -> {
            try {
                deleteFile(s);
            } catch (DataException e) {
                System.out.println(e.getMessage());
                return false;
            }
            return true;
        });
    }

    /**
     * Removes the given File from the data and deletes it.
     *
     * @param fileName path of the File to delete
     * @throws DataException when deletion fails
     */
    public void deleteFile(String fileName) throws DataException {
        if (!new File(repositoryPath + "\\" + fileName).delete())
            throw new DataException("Can't delete File: " + fileName);
        else
            repositoryFiles.get(repositoryPath).remove(fileName);
    }

    public static class DataException extends Exception {
        DataException(String msg) {
            super(msg);
        }

    }
}
