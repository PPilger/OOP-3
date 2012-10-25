import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author Christian Kletzander
 * 
 */
public class Song implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private Zeitraum zeitraum;
	
	//Liste darf nicht NULL sein, oder Elemente die NULL sind enthalten
	private List<Variante> varianten;
	 
	/**
	 * Parameter duerfen nicht NULL sein
	 * Legt neuen Song an
	 * @param name
	 * @param zeitraum 
	 * @param varianten Liste darf nicht NULL sein, oder Elemente die NULL sind enthalten
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
	 * 
	 */
	public static class ZeitpunktSelektor implements Selector<Song> {
		private Date zeitpunkt;

		//Parameter darf nicht NULL sein
		public ZeitpunktSelektor(Date zeitpunkt) {
			this.zeitpunkt = zeitpunkt;
		}

		@Override
		//Parameter darf nicht NULL sein
		public boolean select(Song item) {
			return item.zeitraum.inZeitraum(zeitpunkt);
		}

	}

	/**
	 * Abgleich mit Namen, ignoreCase
	 * @author Koegler Alexander
	 * 
	 */
	public static class NameSelektor implements Selector<Song> {

		private String name;

		//Parameter darf nicht NULL sein
		public NameSelektor(String name) {
			this.name = name;
		}

		@Override
		//Parameter darf nicht NULL sein
		public boolean select(Song item) {
			return item.name.compareToIgnoreCase(name) == 0;
		}
	}
}
