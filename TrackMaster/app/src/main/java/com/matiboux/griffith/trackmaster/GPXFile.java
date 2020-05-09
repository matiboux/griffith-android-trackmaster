package com.matiboux.griffith.trackmaster;

import android.location.Location;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GPXFile {

    private File file;

    public GPXFile(File file) {
        this.file = file;
    }

    public String getName() {
        return file.getName();
    }

    public void createFile() {
        try {
            // Create the parent directories for the file if necessary
            file.getParentFile().mkdirs();

            // Create the file
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Write the basic content for the GPX file
            bufferedWriter.write(NEW_FILE_CONTENT);

            // Flush & Close the file
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addEntry(@NonNull Location location) {
        StringBuilder fileContent = new StringBuilder();

        // Try to open & read the file
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Read the file one line at a time
            String line;
            while ((line = bufferedReader.readLine()) != null)
                fileContent.append(line).append("\n");

            // Close the file
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String newContent = fileContent.toString().replace(NEW_ENTRIES_ANCHOR,
                ENTRY_TEMPLATE.replace("{LAT}", String.valueOf(location.getLatitude()))
                        .replace("{LON}", String.valueOf(location.getLongitude()))
                        .replace("{ELE}", String.valueOf(location.getAltitude()))
                        .replace("{TIME}", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                                .format(new Date(location.getTime()))) + NEW_ENTRIES_ANCHOR);

        // Try to open & update the file
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Write the updated content to the GPX file
            bufferedWriter.write(newContent);

            // Flush & Close the file
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GPXData getData() {
        StringBuilder fileContent = new StringBuilder();

        // Try to open & read the file
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Read the file one line at a time
            String line;
            while ((line = bufferedReader.readLine()) != null)
                fileContent.append(line).append("\n");

            // Close the file
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get the array of entries as they are in the file
        // The first element in the array in the file header and does not contain any entry information
        String[] rawEntries = fileContent.toString().split("<trkpt");

        // Compile the pattern to be used
        Pattern pattern = Pattern.compile(".*lat=\"(.*)\".*lon=\"(.*)\".*<ele>(.*)</ele>.*<time>(.*)</time>.*",
                Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

        // Parse each entry in the array
        ArrayList<GPXEntry> gpxEntries = new ArrayList<>();
        for (int i = 1; i < rawEntries.length; i++) {
            Matcher matcher = pattern.matcher(rawEntries[i]);

            if (matcher.matches()) {
                long time = -1;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                try {
                    Date date = format.parse(Objects.requireNonNull(matcher.group(4)));
                    if (date != null) time = date.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                GPXEntry gpxEntry = new GPXEntry(
                        Double.parseDouble(Objects.requireNonNull(matcher.group(1))),
                        Double.parseDouble(Objects.requireNonNull(matcher.group(2))),
                        Double.parseDouble(Objects.requireNonNull(matcher.group(3))),
                        time);
                gpxEntries.add(gpxEntry);
            }
        }

        return new GPXData(gpxEntries);
    }

    // *** Static

    public static List<String> getFileList(File dir) {
        ArrayList<String> filenameList = new ArrayList<>();
        walkdir(dir, filenameList, "");
        return filenameList;
    }

    private static void walkdir(File dir, List<String> filenameList, String prefix) {
        File[] listFile = dir.listFiles();

        if (listFile != null)
            for (File file : listFile)
                if (file.isDirectory()) // If directory, walk recursively
                    walkdir(file, filenameList, prefix + file.getName() + "/");
                else // If file, add it to the list
                    filenameList.add(prefix + file.getName());
    }

    // *** Templates

    private final String NEW_ENTRIES_ANCHOR = "\t\t\t<!-- New entries can be inserted here -->\n";
    private final String NEW_FILE_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n\n" +
            "<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" xmlns:gpxx=\"http://www.garmin.com/xmlschemas/GpxExtensions/v3\" xmlns:gpxtpx=\"http://www.garmin.com/xmlschemas/TrackPointExtension/v1\" creator=\"TrackMaster\" version=\"1.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd\">\n" +
            "\t<trk>\n" +
            "\t\t<name>TrackMaster GPX File</name>\n" +
            "\t\t<trkseg>\n" +
            NEW_ENTRIES_ANCHOR +
            "\t\t</trkseg>\n" +
            "\t</trk>\n" +
            "</gpx>\n";
    private final String ENTRY_TEMPLATE = "\t\t\t<trkpt lat=\"{LAT}\" lon=\"{LON}\">\n" +
            "\t\t\t\t<ele>{ELE}</ele>\n" +
            "\t\t\t\t<time>{TIME}</time>\n" +
            "\t\t\t</trkpt>\n";
}
