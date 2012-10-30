import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Invariante: file und bands sind ungleich null. bands enthaelt keine Elemente
 * gleich null.
 */
public class Programm {
	private File file;
	private List<Band> bands;

	/**
	 * NOTE: Erzeugt ein neues Programm und laedt den letzten Zustand aus der
	 * Sicherungsdatei.
	 * 
	 * Nachbedingung: Wenn in die Datei "bands.dat" gelesen werden konnte,
	 * entspricht der Zustand der Bands dem Zustand zum Zeitpunkt der Sicherung
	 * der Datei. Wenn in die Datei nicht gelesen werden konnte, wurde eine
	 * Fehlermeldung ausgegeben.
	 */
	public Programm() {
		this(true);
	}

	/**
	 * NOTE: Erzeugt ein neues Programm
	 * 
	 * Nachbedingung fuer load == true: Wenn in die Datei "bands.dat" gelesen
	 * werden konnte, entspricht der Zustand der Bands dem Zustand zum Zeitpunkt
	 * der Sicherung der Datei. Wenn in die Datei nicht gelesen werden konnte,
	 * wurde eine Fehlermeldung ausgegeben.
	 * 
	 * @param load
	 *            true, wenn der letzte Zustand aus der Sicherung geladen werden
	 *            soll.
	 */
	public Programm(boolean load) {
		String filename = "bands.dat";
		file = new File(filename);

		if (load) {
			if (file.exists()) {
				read();
			}

			if (this.bands == null) {
				this.bands = new ArrayList<Band>();
			}
		} else {
			this.bands = new ArrayList<Band>();
		}
	}

	/**
	 * Vorbedingung: band ist ungleich null
	 * 
	 * Nachbedingung: band ist in der Bandliste enthalten
	 */
	public void addBand(Band band) {
		bands.add(band);
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist ungleich null
	 */
	public Band getBand(String name) {
		for (Band band : bands) {
			if (band.getName().equalsIgnoreCase(name)) {
				return band;
			}
		}
		return null;
	}

	/**
	 * NOTE: Speichert den aktuellen Programmzustand.
	 * 
	 * Nachbedingung: Wenn in die Datei file geschrieben werden konnte, ist der
	 * Zustand der Bands gespeichert. Wenn nicht in die Datei geschrieben werden
	 * konnte, wurde eine Fehlermeldung ausgegeben.
	 */
	public void quit() {
		ObjectOutputStream dos = null;
		try {
			dos = new ObjectOutputStream(new FileOutputStream(file));
			dos.writeObject(bands);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	/**
	 * Nachbedingung: Wenn in die Datei file gelesen werden konnte, entspricht der
	 * Zustand der Bands dem Zustand zum Zeitpunkt der Sicherung der Datei. Wenn in die Datei nicht gelesen werden
	 * konnte, wurde eine Fehlermeldung ausgegeben.
	 */
	private void read() {
		ObjectInputStream ois = null;

		try {
			ois = new ObjectInputStream(new FileInputStream(file));
			this.bands = (List<Band>) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("Ungueltige Datei " + file.getName());
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
