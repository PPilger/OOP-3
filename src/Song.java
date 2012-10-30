import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Invariante: name, zeitraum und varianten sind ungleich null. varianten
 * enthaelt keine Elemente gleich null.
 * 
 * @author Christian Kletzander
 */
public class Song implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private Zeitraum zeitraum;
	private List<Variante> varianten;

	/**
	 * NOTE: Legt neuen Song an
	 * 
	 * Vorbedingung: name, zeitraum und varianten sind ungleich null. varianten
	 * enthaelt keine Elemente gleich null.
	 * 
	 * @param name
	 * @param zeitraum
	 * @param varianten
	 *            Liste darf nicht NULL sein, oder Elemente die NULL sind
	 *            enthalten
	 */
	public Song(String name, Zeitraum zeitraum, List<Variante> varianten) {
		this.name = name;
		this.zeitraum = zeitraum;
		this.varianten = varianten;
	}

	/**
	 * Nachbedinung: der Rueckgabewert ist ungleich null.
	 */
	public List<Variante> getVarianten() {
		return varianten;
	}

	/**
	 * Nachbedinung: der Rueckgabewert ist ungleich null.
	 */
	public String toString() {
		return name;
	}

	/**
	 * Nachbedinung: der Rueckgabewert ist ungleich null.
	 */
	public String toDetailString() {
		return toString() + " " + zeitraum + " " + varianten;
	}

	/**
	 * NOTE: Abgleich ob Datum im Zeitraum enthalten ist
	 * 
	 * Invariante: zeitpunkt ist ungleich null
	 * 
	 * @author Koegler Alexander
	 */
	public static class ZeitpunktSelektor implements Selector<Song> {
		private Date zeitpunkt;

		/**
		 * Vorbedingung: zeitpunkt ist ungleich null
		 */
		public ZeitpunktSelektor(Date zeitpunkt) {
			this.zeitpunkt = zeitpunkt;
		}

		@Override
		/**
		 * Vorbedingung: item ist ungleich null
		 */
		public boolean select(Song item) {
			return item.zeitraum.inZeitraum(zeitpunkt);
		}

	}

	/**
	 * NOTE: Abgleich mit Namen, ignoreCase
	 * 
	 * @author Koegler Alexander
	 */
	public static class NameSelektor implements Selector<Song> {

		private String name;

		public NameSelektor(String name) {
			this.name = name;
		}

		@Override
		/**
		 * Vorbedingung: item ist ungleich null
		 */
		public boolean select(Song item) {
			return item.name.compareToIgnoreCase(name) == 0;
		}
	}
}
