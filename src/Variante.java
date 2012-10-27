import java.io.Serializable;

/**
 * Repraesentiert eine Variante eines Songs.
 * 
 * @author Peter Pilgerstorfer
 *  Invariante: keine NULL Werte werden zurueck gegeben
 */
public class Variante implements Serializable {
	private static final long serialVersionUID = 1L;

	private String bezeichnung;
	private int laenge;

	/**
	 * 
	 * @param bezeichnung
	 * @param laenge sollte >= 0 sein
	 * Vorbedingung: Parameter duerfen nicht NULL sein
	 */
	public Variante(String bezeichnung, int laenge) {
		this.bezeichnung = bezeichnung;
		this.laenge = laenge;
	}

	public String toString() {
		return String.format("%s: %d:%02d", bezeichnung, laenge / 60,
				laenge % 60);
	}

	/**
	 * 
	 * @author VHD
	 * Selektiert Elemente mit gleicher Bezeichnung, ignoreCase
	 * Vorbedingung: Parameter duerfen nicht NULL sein
	 */
	public static class BezeichnungSelektor implements Selector<Variante> {

		private String bezeichnung;

		public BezeichnungSelektor(String bezeichnung) {
			this.bezeichnung = bezeichnung;
			this.bezeichnung = bezeichnung;
		}

		@Override
		public boolean select(Variante item) {
			return bezeichnung.equalsIgnoreCase(item.bezeichnung);
		}
	}
}
