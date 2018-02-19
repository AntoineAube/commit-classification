package fr.polytech.reace.researchers.driller.studies;

import org.json.JSONObject;
import org.repodriller.RepoDrillerException;
import org.repodriller.persistence.PersistenceMechanism;

import java.io.FileOutputStream;
import java.io.PrintStream;

public class JSONFile implements PersistenceMechanism {

    private PrintStream stream;
    private boolean isOpen = false;
    private String[] labels;
    private int contentSize = 0;

    public JSONFile(String location, String... labels) {
        open(location);

        this.labels = labels;

        stream.print('[');
        stream.flush();
    }

    @Override
    public void write(Object... objects) {
        if (!isOpen) {
            throw new RepoDrillerException("Writing to a closed JSON file.");
        }

        JSONObject saved = new JSONObject();
        for (int i = 0; i < labels.length; i++) {
            saved.put(labels[i], objects[i]);
        }

        String stringSaved = saved.toString();

        if (contentSize != 0) {
            stringSaved = ", " + stringSaved;
        }

        contentSize++;

        stream.print(stringSaved);
        stream.flush();
    }

    @Override
    public void close() {
        if (isOpen) {
            stream.print(']');
            stream.flush();

            stream.close();
            isOpen = false;
        }
    }

    private void open(String fileName) {
        try {
            stream = new PrintStream(new FileOutputStream(fileName, false));
            isOpen = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
