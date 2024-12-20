/*
 * MCreator (https://mcreator.net/)
 * Copyright (C) 2012-2020, Pylo
 * Copyright (C) 2020-2021, Pylo, opensource contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * MCreator (https://mcreator.net/)
 * Copyright (C) 2020 Pylo and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package rip.sayori.rmcr;

import rip.sayori.rmcr.io.OS;
import rip.sayori.rmcr.io.UserFolderManager;
import rip.sayori.rmcr.preferences.PreferencesManager;
import rip.sayori.rmcr.ui.MCreatorApplication;
import rip.sayori.rmcr.util.DefaultExceptionHandler;
import rip.sayori.rmcr.util.LoggingOutputStream;
import rip.sayori.rmcr.util.MCreatorVersionNumber;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zone.rong.imaginebreaker.ImagineBreaker;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Launcher {

	public static MCreatorVersionNumber version;

	public static void main(String[] args) throws Throwable {
		List<String> arguments = Arrays.asList(args);

		System.setProperty("log_directory", UserFolderManager.getFileFromUserFolder("").getAbsolutePath());
		if (OS.getOS() == OS.WINDOWS && !System.getProperty("java.class.path").contains("idea_rt.jar")) {
			System.setProperty("log_disable_ansi", "true");
		} else {
			System.setProperty("log_disable_ansi", "false");
		}

		final Logger LOG = LogManager.getLogger("Launcher"); // init logger after log directory is set

		System.setErr(new PrintStream(new LoggingOutputStream(LogManager.getLogger("STDERR"), Level.ERROR), true));
		System.setOut(new PrintStream(new LoggingOutputStream(LogManager.getLogger("STDOUT"), Level.INFO), true));
		Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler());

		try {
			Properties conf = new Properties();
			conf.load(Launcher.class.getResourceAsStream("/mcreator.conf"));

			version = new MCreatorVersionNumber(conf);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}

		LOG.info("Starting MCreator {}", version);

		// print version of Java
		String java_spec_version = System.getProperty("java.specification.version");
		LOG.info("Java version: {}, specification: {}, VM name: {}", System.getProperty("java.version"),
				java_spec_version, System.getProperty("java.vm.name"));
		LOG.info("Current JAVA_HOME for running instance: {}", System.getProperty("java.home"));

		// check if the user is using proper version of Java

		// after we have libraries loaded, we load preferences
		PreferencesManager.loadPreferences();

		// set system properties from preferences
		System.setProperty("awt.useSystemAAFontSettings", PreferencesManager.PREFERENCES.ui.textAntialiasingType);
		System.setProperty("swing.aatext", Boolean.toString(PreferencesManager.PREFERENCES.ui.aatext));
		System.setProperty("sun.java2d.opengl", Boolean.toString(PreferencesManager.PREFERENCES.ui.use2DAcceleration));
		System.setProperty("sun.java2d.d3d", "false");
		System.setProperty("prism.lcdtext", "false");

		Launcher.class.getClassLoader().loadClass("javafx.embed.swing.JFXPanel");
		// if we manage to load JavaFX, we set the listener to print to the sout js messages
		com.sun.javafx.webkit.WebConsoleListener.setDefaultListener((webView, message, lineNumber, sourceId) -> {
			String[] sidparsed = sourceId.split("/");
			LOG.info("[JFX JS bridge] [{}: {}] {}", sidparsed[sidparsed.length - 1], lineNumber, message);
		});

		LOG.info("Installation path: {}", System.getProperty("user.dir"));
		LOG.info("User home of MCreator: {}", UserFolderManager.getFileFromUserFolder("/"));
		if (!UserFolderManager.createUserFolderIfNotExists()) {
			JOptionPane.showMessageDialog(null, "<html><b>MCreator failed to write to user directory!</b><br><br>"
							+ "Please make sure that the user running MCreator has permissions to read and write to the directory<br>"
							+ "in which MCreator tried to create user specific data storage. The path MCreator could not write to is:<br><br>"
							+ UserFolderManager.getFileFromUserFolder("/") + "<br>", "MCreator file system error",
					JOptionPane.WARNING_MESSAGE);
			System.exit(-3);
		}

		MCreatorApplication.createApplication(arguments);
	}

	static {
		ImagineBreaker.wipeMethodFilters();
		ImagineBreaker.wipeFieldFilters();
		ImagineBreaker.openBootModules();
	}
}
