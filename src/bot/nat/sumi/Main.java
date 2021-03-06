package bot.nat.sumi;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {
	public static final String folder = "H:\\Java\\NatsumiBot2\\data\\";
	public static final String cmd = "$";	// command prefix

    public static ArrayList<Module> spec;
    public static ArrayList<Module> modules;

	public static void main(String[] arg) throws IOException, URISyntaxException, ClassNotFoundException {
        spec = SpecialModules.r();
	//	loadJAR(folder +"mc.jar");
        loadJARs();
		loadChannels();
	}

	private static void loadChannels() {
		File[] files = new File(folder +"connect/").listFiles();

		for(File f : files != null ? files : new File[0]){
			System.out.println(f.getAbsolutePath());
			Servers.addServer(new Server(new ConfigFile(f.getAbsolutePath(), ConfigFile.READ, read(f))));
		}
	}

	public static String read(File f) {
		try {
			InputStream is = new FileInputStream(f);
			java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
			String data = s.hasNext() ? s.next() : "";
			s.close();
			return data;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static boolean write(File f, String data) {
		return write(f, data, false);
	}

	public static boolean write(File f, String data, boolean append) {
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f, append)))) {
			out.print(data);
			out.close();
			return true;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/* load commands */
	public static void loadJARs() throws MalformedURLException {
        if(modules != null) {
            for (Module m : modules) {
                assert m != null;
                unload(m.jar);
            }
        }

		File[] files = new File(folder +"commands/").listFiles();
        modules = new ArrayList<>();

        assert files != null;
        for(File f : files){
			try {
				loadJAR(f.getAbsolutePath());
                Module m = getInstance(f.getName().replace(".jar", ""), f.getAbsolutePath());
                m.jar = f.getAbsolutePath();
				modules.add(m);

			} catch (IOException | ClassNotFoundException | URISyntaxException e) {
				e.printStackTrace();
			}
        }
	}

	/* get new module instance */
	private static Module getInstance(String str, String file) {
		try {
			return (Module) ClassContainer.get().getClass(file, str).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

        return null;
	}

	/* loads lang in JAR to memory for use */
	private static void loadJAR(String location) throws IOException, ClassNotFoundException, URISyntaxException {
		System.out.print("loading Classes from '" + location +"'");

        /* list of class names */
		ArrayList<String> classNames = new ArrayList<>();
        /* list of configuration files */
		ArrayList<String> configs = new ArrayList<>();

        /* get the JAR to this instance */
		ZipInputStream zip = new ZipInputStream(new FileInputStream(location));

        /* get each file in the zip */
		for(ZipEntry entry = zip.getNextEntry();entry != null;entry = zip.getNextEntry()){

            /* if this is a class */
			if(entry.getName().endsWith(".class") && !entry.isDirectory()) {

				// This ZipEntry represents a class. Now, what class does it represent?
				StringBuilder className = new StringBuilder();
				for(String part : entry.getName().split("/")) {
					if(className.length() != 0) {
						className.append(".");
					}

					className.append(part);
					if(part.endsWith(".class")) {
						className.setLength(className.length() - ".class".length());
					}
				}

                /* add final result to string array */
				classNames.add(className.toString());
            /* if its a configuration file */
			} else if(entry.getName().endsWith(".spc")){
				configs.add(entry.getName());
			}
		}

        /* add url to make sure the classes work */
		ClassContainer.get().addURL(location);

        /* load all the classes */
		for(String cls : classNames){
            /* load next class */
			ClassContainer.get().loadClass(cls);
		}

        /* load all the configurations */
		for(String cfg : configs){
            /* load next class */
			ClassContainer.get().loadConfig(location, cfg);
		}

		System.out.println(" ...Done!");
	}

	public static void unload(String jar) throws MalformedURLException {
		ArrayList<URL> url = getURLs();
		url.remove(new File(jar).toURI().toURL());
		ClassContainer.createNew(url.toArray(new URL[url.size()]));

        /* close all Closed modules */
        for(Module m : modules.toArray(new Module[modules.size()])){
            if(m.jar.equals(jar)) {
                if (m instanceof Closed) {
                    ((Closed) m).close();
                }

                modules.remove(m);
            }
        }

        for(Module m : spec.toArray(new Module[spec.size()])){
            if(m.jar != null && m.jar.equals(jar)) {
                if (m instanceof Closed) {
                    ((Closed) m).close();
                }

                spec.remove(m);
            }
        }

		/* perform garbage collection */
		System.gc();
	}

	public static void reload(String jar) throws IOException, URISyntaxException, ClassNotFoundException {
		File f = new File(jar);

        /* close all Closed modules */
		for(Module m : modules.toArray(new Module[modules.size()])){
			if(m.jar.equals(jar)) {
				if (m instanceof Closed) {
					((Closed) m).close();
				}

				modules.remove(m);
			}
		}

		for(Module m : spec.toArray(new Module[spec.size()])){
			if(m.jar != null && m.jar.equals(jar)) {
				if (m instanceof Closed) {
					((Closed) m).close();
				}

				spec.remove(m);
			}
		}

		/* perform garbage collection */
		System.gc();

		loadJAR(f.getAbsolutePath());
		Module m = getInstance(f.getName().replace(".jar", ""), f.getAbsolutePath());
		m.jar = f.getAbsolutePath();
		modules.add(m);
	}

	public static ArrayList<URL> getURLs() {
		ArrayList<URL> url = new ArrayList<>();
		Collections.addAll(url, ClassContainer.get().getURLs());
		return url;
	}
}
