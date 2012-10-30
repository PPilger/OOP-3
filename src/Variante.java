import java.io.Serializable;

/**
 * Repraesentiert eine Variante eines Songs.
 * 
 * Invariante: keine NULL Werte werden zurueck gegeben
 * 
 * @author Peter Pilgerstorfer
 */
public class Variante implements Serializable {
	private static final long serialVersionUID = 1L;

	private String bezeichnung;
	private int laenge;

	/**
	 * Vorbedingung: Parameter duerfen nicht NULL sein
	 * 
	 * @param bezeichnung
	 * @param laenge sollte >= 0 sein
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
	 * NOTE: Selektiert Elemente mit gleicher Bezeichnung, ignoreCase
	 * 
	 * Vorbedingung: Parameter duerfen nicht NULL sein
	 * 
	 * @author VHD
	 */
	public static class BezeichnungSelektor implements Selector<Variante> {

		private String bezeichnung;

		public BezeichnungSelektor(String bezeichnung) {
			this.bezeichnung = bezeichnung;
		}

		@Override
		public boolean select(Variante item) {
			return bezeichnung.equalsIgnoreCase(item.bezeichnung);
		}
	}
}
