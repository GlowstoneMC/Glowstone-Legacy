package net.glowstone.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;
import java.util.logging.Level;
import net.glowstone.GlowServer;
import static net.glowstone.GlowServer.logger;


public final class LibraryManager {
     /**
     * The server.
     */
    private final GlowServer server;
    
    /**
     * The list of libraries.
     */
    private List<String> libs;

    public LibraryManager(GlowServer server) {
        this.server = server;
        libs = server.getLibs();
        logger.log(Level.INFO, "Got libs: " + libs);

        File folder = new File("libraries");
        if (!folder.isDirectory() && !folder.mkdirs()) {
            logger.log(Level.SEVERE, "Could not create libraries directory: ", folder);
        }

        for (String lib : libs) {
            String[] libParsed = parseLib(lib);
            downloadLib(libParsed[0], libParsed[1], libParsed[2]);
        }
    }

    private String[] parseLib(String libs) {
        return libs.split(":");
    }

    private void downloadLib(String libPackage, String libName, String libVersion) {
        String libPackageURL = "";

        String libURL =  server.getRepo() + libPackage.replaceAll("\\.", "/") + "/" + libName + "/" + libVersion + "/" + libName + "-" + libVersion + ".jar";
        File libFile = new File("libraries/" + libName + "-" + libVersion + ".jar");

        try {
            if (!libFile.exists()) {
                server.getLogger().log(Level.INFO, "Downloading " + libName + "-" + libVersion + ".jar" + " from " + libURL);
                URL downloadURL = new URL(libURL);
                ReadableByteChannel byteChannel = Channels.newChannel(downloadURL.openStream());
                FileOutputStream outputStream = new FileOutputStream("libraries/" + libName + "-" + libVersion + ".jar");
                outputStream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
            }
        } catch (MalformedURLException e) {
            server.getLogger().log(Level.SEVERE, "Library URL could not be parsed: ", e);
        } catch (IOException ex) {
            server.getLogger().log(Level.SEVERE, "Library stream could not be opened: ", ex);
        }
    }
}
