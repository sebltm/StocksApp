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
        if (isStringNullOrEmpty(filename)) return default_filename.get();
        filename = filename.startsWith("/") ? filename.substring(1) : filename;
        filename = filename.endsWith("/") ? filename.substring(0, filename.length() - 1) : filename;
        return filename;
    }

    private int validateQuality(int quality) {
        int default_quality = 50;
        return quality < 0 || quality > 100 ? default_quality : quality;
    }

    private String validateFileDescription(String fileDescription) {
        String default_file_description = "MPAndroidChart-Library Save";
        return isStringNullOrEmpty(fileDescription) ? default_file_description : fileDescription;
    }

    private String validateSubFolderPath(String subFolderPath) {
        String default_subFolderPath = "";
        return isStringNullOrEmpty(subFolderPath) ? default_subFolderPath : subFolderPath;
    }

    private CompressFormat validateCompressFormat(CompressFormat compressFormat) {
        return compressFormat == null ? default_format : compressFormat;
    }

    private void validateInputs() {
        filename = validateFilename(filename);
        fileDescription = validateFileDescription(fileDescription);
        subFolderPath = validateSubFolderPath(subFolderPath);
        compressFormat = validateCompressFormat(compressFormat);
        quality = validateQuality(quality);
    }

    public String getFilename() {
        return filename;
    }

    public String getFileDescription() {
        return fileDescription;
    }

    public String getSubFolderPath() {
        return subFolderPath;
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

