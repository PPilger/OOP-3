import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author Christian Kletzander
 * Vorbedingung: Liste darf nicht NULL sein, oder Elemente die NULL sind enthalten
 * Invariante: liefert keine NULL Werte zurueck
 */
public class Song implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private Zeitraum zeitraum;
	private List<Variante> varianten;
	 
	/** 
	 * Legt neuen Song an
	 * @param name
	 * @param zeitraum 
	 * @param varianten Liste darf nicht NULL sein, oder Elemente die NULL sind enthalten
	 * Vorbedingung: Parameter duerfen nicht NULL sein
	 */
	public Song(String name, Zeitraum zeitraum, List<Variante> varianten) {
		this.name = name;
		this.zeitraum = zeitraum;
		this.varianten = varianten;
	}
	
	public List<Variante> getVarianten() {
		return varianten;
	}

	public String toString() {
		return name;
	}

	public String toDetailString() {
		return toString() + " " + zeitraum + " " + varianten;
	}

	/**
	 * Abgleich ob Datum im Zeitraum enthalten ist
	 * @author Koegler Alexander
	 * Vorbedingung: Parameter duerfen nicht NULL sein
	 */
	public static class ZeitpunktSelektor implements Selector<Song> {
		private Date zeitpunkt;

		public ZeitpunktSelektor(Date zeitpunkt) {
			this.zeitpunkt = zeitpunkt;
		}

		@Override
		public boolean select(Song item) {
			return item.zeitraum.inZeitraum(zeitpunkt);
		}

	}

	/**
	 * Abgleich mit Namen, ignoreCase
	 * @author Koegler Alexander
	 * 	 
	 * Vorbedingung: Parameter duerfen nicht NULL sein
	 */
	public static class NameSelektor implements Selector<Song> {

		private String name;
		public NameSelektor(String name) {
			this.name = name;
		}

		@Override
		public boolean select(Song item) {
			return item.name.compareToIgnoreCase(name) == 0;
		}
	}
}
