package com.tycoon177.mineabound;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.tycoon177.mineabound.utils.TexturePack;

public class MineaboundLauncher extends Game {

	public static final boolean IS_DEBUG_RENDERING = true;
	public static final Color PLAYER_BOUNDING_BOX_COLOR = new Color(1, 0, 1, 1);
	public static final Color BLOCK_BOUNDING_BOX_COLOR = Color.BLACK;
	public static final Color ENTITY_BOUNDING_BOX_COLOR = Color.RED;
	public static FileHandle texturePacksDirectory;

	public static Preferences preferences;

	@Override
	public void create() {
		createFilePaths();
		setScreen(new MainMenu());
	}

	private void createFilePaths() {
		FileHandle handle = Gdx.files.external(".mineabound");
		texturePacksDirectory = handle.child("texture packs");
		texturePacksDirectory.file().mkdirs();
		try {
			downloadDefaultAssets();
			unpackAssets();
			System.out.println("Here");
		} catch (IOException e) {
			System.out.println("Failed to setup the assets!");
		}
	}

	private void unpackAssets() {
		// buffer for read and write data to file
		byte[] buffer = new byte[2048];

		try {
			FileInputStream fInput = new FileInputStream("default.zip");
			ZipInputStream zipInput = new ZipInputStream(fInput);

			ZipEntry entry = zipInput.getNextEntry();

			while (entry != null) {
				String entryName = entry.getName();
				TexturePack.defaultTexturepack.file().mkdirs();
				File file = new File(TexturePack.defaultTexturepack.file() + File.separator + entryName);

				// System.out.println("Unzip file " + entryName + " to " + file.getAbsolutePath());

				// create the directories of the zip directory
				if (entry.isDirectory()) {
					File newDir = new File(file.getAbsolutePath());
					if (!newDir.exists()) {
						boolean success = newDir.mkdirs();
						if (success == false) {
							System.out.println("Problem creating Folder");
						}
					}
				}
				else {
					FileOutputStream fOutput = new FileOutputStream(file);
					int count = 0;
					while ((count = zipInput.read(buffer)) > 0) {
						// write 'count' bytes to the file output stream
						fOutput.write(buffer, 0, count);
					}
					fOutput.close();
				}
				// close ZipEntry and take the next one
				zipInput.closeEntry();
				entry = zipInput.getNextEntry();
			}

			// close the last ZipEntry
			zipInput.closeEntry();

			zipInput.close();
			fInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void downloadDefaultAssets() throws IOException {
		File f = new File("default.zip");
		if (f.exists())
			return;
		URL website = new URL("https://www.dropbox.com/s/it4cxlwsmd43ruj/default.zip?dl=1");
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream("default.zip");
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.flush();
		fos.close();
	}

}
