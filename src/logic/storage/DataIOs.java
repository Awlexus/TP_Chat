package logic.storage;

import java.io.*;
import java.nio.file.Paths;

/**
 * @author Maximilian Estfeller
 * @since 23.08.2017
 * <p>
 * This class consists exclusively of static methods that help printing and reading serialized Objects
 */
public final class DataIOs {

    private DataIOs() {
    }

    /**
     * Writes a File to the given Path
     * <p>
     * Path get validated first
     *
     * @param path to the File
     * @param data Object to print
     * @throws DataRepository.DataException validation failed, IOException
     * @see #validatePath(String)
     */
    public static void print(String path, Serializable data) throws IOException, DataRepository.DataException {
        validatePath(path);
        File file = new File(path);
        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(file));
        oos.writeObject(data);
        oos.close();
    }

    /**
     * Reads a File from the given Path
     * <p>
     * Path validation first
     *
     * @param path to the File
     * @return an Object
     * @throws DataRepository.DataException validation failed, IOException
     * @see #validatePath(String)
     */
    public static Object read(String path) throws IOException, ClassNotFoundException, DataRepository.DataException {
        validatePath(path);

        File file = new File(path);

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

        return ois.readObject();
    }

    /**
     * Method validates the path
     *
     * @param path to the File
     * @throws DataRepository.DataException not a path, or not a serialized Object
     */
    private static void validatePath(String path) throws DataRepository.DataException {
        Paths.get(path);
        if (!path.endsWith(".ser"))
            throw new DataRepository.DataException("DataFiles need to end in .ser");
    }
}
