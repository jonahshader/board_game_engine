package jonahshader.headless;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import jonahshader.BoardApp;

/** Launches the headless application. Can be converted into a utilities project or a server application. */
public class HeadlessLauncher {
	public static void main(String[] args) {
		createApplication();
	}

	private static Application createApplication() {
		// Note: you can use a custom ApplicationListener implementation for the headless project instead of BoardApp.
		return new HeadlessApplication(new BoardApp(), getDefaultConfiguration());
	}

	private static HeadlessApplicationConfiguration getDefaultConfiguration() {
		HeadlessApplicationConfiguration configuration = new HeadlessApplicationConfiguration();
//		configuration.renderInterval = -1f; // When this value is negative, BoardApp#render() is never called.
		return configuration;
	}
}