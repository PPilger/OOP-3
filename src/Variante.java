import java.io.Serializable;

/**
 * Repraesentiert eine Variante eines Songs.
 * 
 * Invariante: bezeichnung ist ungleich null
 * 
 * Invariante: laenge ist >= 0
 * 
 * @author Peter Pilgerstorfer
 */
public class Variante implements Serializable {
	private static final long serialVersionUID = 1L;

	private String bezeichnung;
	private int laenge;

	/**
	 * Vorbedingung: bezeichnung ist ungleich null
	 * 
	 * Vorbedingung: laenge ist >= 0
	 */
	public Variante(String bezeichnung, int laenge) {
		this.bezeichnung = bezeichnung;
		this.laenge = laenge;
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist ungleich null
	 */
	public String toString() {
		return String.format("%s: %d:%02d", bezeichnung, laenge / 60,
				laenge % 60);
	}

	/**
	 * NOTE: Selektiert Elemente mit gleicher Bezeichnung, ignoreCase
	 * 
	 * Invarinate: bezeichnung ist ungleich null
	 * 
	 * @author VHD
	 */
	public static class BezeichnungSelektor implements Selector<Variante> {

		private String bezeichnung;

		/**
		 * Vorbedingung: bezeichnung ist ungleich null
		 */
		public BezeichnungSelektor(String bezeichnung) {
			this.bezeichnung = bezeichnung;
		}

		@Override
		/**
		 * Vorbedingung: item ist ungleich null
		 */
		public boolean select(Variante item) {
			return bezeichnung.equalsIgnoreCase(item.bezeichnung);
		}
	}
}
