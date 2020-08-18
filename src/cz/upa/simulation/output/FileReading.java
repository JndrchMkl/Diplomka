package cz.upa.simulation.output;

import cz.upa.simulation.domain.Settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import static cz.upa.simulation.utils.StringUtils.s;

public class FileReading {

    public static BufferedReader readActualBuildOutput() {
        String PATH = "";
        String id = s((Settings.ACTUAL_ID_BUILD));
        String fileName = PATH.concat(id).concat(".txt");
        String directoryName = PATH.concat("output");
        File file = new File(directoryName + "/" + fileName);
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            return br;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
