package com.github.badoualy.ui.model;


public final class ChangeLog {

    public final String version;
    public final String[] entries;

    public final int versionMajor, versionMinor, versionHotfix;

    public ChangeLog(String version, String... entries) {
        this.version = version;
        this.entries = entries;

        String[] fields = version.split(" ")[0].split("\\.");

        if (fields.length < 3)
            throw new RuntimeException("Version number has illegal format, must be $major.$minor.$hotfix but is " + version);

        versionMajor = Integer.parseInt(fields[0]);
        versionMinor = Integer.parseInt(fields[1]);
        versionHotfix = Integer.parseInt(fields[2]);
    }

    public String toHtmlString() {
        StringBuilder builder = new StringBuilder();

        builder.append("<strong>")
               .append(version)
               .append("</strong> <br>");

        for (String entry : entries)
            builder.append("- ").append(entry).append("<br>");

        return builder.toString().substring(0, builder.length());
    }
}