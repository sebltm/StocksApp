package com.example.Team8.utils;

import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

import com.github.mikephil.charting.charts.LineChart;

import java.util.Date;
import java.util.function.Supplier;

public class GraphImageExporter {

    private static final String default_full_path = String.format("%s/DCIM/", Environment.getExternalStorageDirectory());
    private final CompressFormat default_format = CompressFormat.JPEG;
    private final Supplier<String> default_filename = () -> DateTimeHelper.toEpoch(new Date());

    private final LineChart lineChart;
    private String filename;
    private String fileDescription;
    private String subFolderPath = "";
    private int quality = -1;
    private CompressFormat compressFormat;

    public GraphImageExporter(LineChart lineChart) {
        this.lineChart = lineChart;
    }

    public GraphImageExporter setFilename(Stock stock, AnalysisType analysisType, String from, String to) {
        filename = String.format(
                "%1$s_%2$s_%3$s_%4$s",
                stock.getSymbol().toLowerCase(),
                analysisType.toString().toLowerCase(),
                from, to
        );
        return this;
    }

    public GraphImageExporter setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public GraphImageExporter setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
        return this;
    }

    public GraphImageExporter setSubFolderPath(String subFolderPath) {
        this.subFolderPath = subFolderPath;
        return this;
    }

    public GraphImageExporter setCompressFormat(CompressFormat compressFormat) {
        this.compressFormat = compressFormat;
        return this;
    }

    public GraphImageExporter setQuality(int quality) {
        this.quality = quality;
        return this;
    }

    private boolean isStringNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    private String validateFilename(String filename) {
        if (isStringNullOrEmpty(filename)) {
            filename = default_filename.get();
            return filename;
        }
        filename = filename.startsWith("/") ? filename.substring(1) : filename;
        filename = filename.endsWith("/") ? filename.substring(0, filename.length() - 1) : filename;
        return filename;
    }

    private void validateInputs() {
        int default_quality = 50;
        String default_subFolderPath = "";
        String default_file_description = "MPAndroidChart-Library Save";

        if (quality < 0 || quality > 100) quality = default_quality;

        filename = validateFilename(filename);

        if (isStringNullOrEmpty(fileDescription)) fileDescription = default_file_description;

        if (isStringNullOrEmpty(subFolderPath)) subFolderPath = default_subFolderPath;

        if (compressFormat == null) compressFormat = default_format;
    }

    public String getFilename() {
        return filename;
    }

    public String getSubFolderPath() {
        return subFolderPath;
    }

    public String getFileDescription() {
        return fileDescription;
    }

    public CompressFormat getCompressFormat() {
        return compressFormat;
    }

    public int getQuality() {
        return quality;
    }

    public String getFileSavedPath() {
        return String.format("%s%s", default_full_path, subFolderPath);
    }

    public String getExtension() {
        if (compressFormat == null) return "";
        switch (compressFormat) {
            case JPEG:
                return ".jpg";
            case PNG:
                return ".png";
            default:
                return String.format(".%s", compressFormat.toString().toLowerCase());
        }
    }

    public boolean export() {
        validateInputs();
        return lineChart.saveToGallery(
                filename,
                subFolderPath,
                fileDescription,
                compressFormat,
                quality
        );
    }
}

