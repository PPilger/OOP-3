import java.io.Serializable;
import java.util.List;

/**
 * NOTE: Speichert Ort mit zugehoeriger Infrastruktur
 * 
 * Invariante: gibt keine NULL Werte zurueck
 * 
 * @author VHD
 */
public class Ort implements Serializable {
	private static final long serialVersionUID = 1L;

	private String bezeichnung;
	private List<String> infrastruktur;

	public String toString() {
		return bezeichnung;
	}

	public String toDetailString() {
		return bezeichnung + " " + infrastruktur.toString();
	}

	/**
	 * Vorbedingung: Parameter darf nicht NULL sein, oder Elemente enthalten die NULL sind
	 * 
	 * @param bezeichnung
	 * @param infrastruktur
	 */
	public Ort(String bezeichnung, List<String> infrastruktur) {
		this.bezeichnung = bezeichnung;
		this.infrastruktur = infrastruktur;
	}

	/**
	 * 
	 * @author Koegler Alexander
	 * 
	 */
	public static class BezeichnungSelektor implements Selector<Ort> {

		private String name;
		private boolean enthaelt;

		/**
		 * NOTE: Stellt den Selektor so ein, dass Elemente erfolgreich verglichen
		 * werden, wenn die bezeichnung einander eins zu eins gleichen
		 * 
		 * Vorbedingung: Parameter darf nicht NULL sein
		 * 
		 * @param bezeichnung
		 *            die zu pruefende bezeichnung
		 */
		public BezeichnungSelektor(String bezeichnung) {
			this.name = bezeichnung;
			enthaelt = false;
		}

		/**
		 * Stellt den Selektor so ein, dass Elemente erfolgreich verglichen
		 * werden, sobald die bezeichnung den im Parameter angegeben String
		 * enthaelt
		 * 
		 * Vorbedingung: Parameter duerfen nicht NULL sein
		 * 
		 * @param bezeichnung
		 *            die zu pruefende bezeichnung
		 * @param okIfContains
		 *            Wenn True so wird nur darauf geachtet das die bezeichnung
		 *            enthalten ist, bei False muessen die bezeichnungn komplett
		 *            gleich sein.
		 */
		public BezeichnungSelektor(String bezeichnung, boolean okIfContains) {
			this.name = bezeichnung;
			this.enthaelt = okIfContains;
		}

		@Override
		public boolean select(Ort item) {
			if (enthaelt) {
				// kommt der String nicht vor, wird -1 zurueckgegeben
				return item.bezeichnung.indexOf(name) != -1;
			} else {
				return item.bezeichnung.equalsIgnoreCase(name);
			}
		}
	}
	public static class InfrastrukturSelektor implements Selector<Ort> {
		private String name;

		/**
		 * Vorbedingung: Parameter duerfen nicht NULL sein
		 * 
		 * @author VHD
		 */
		public InfrastrukturSelektor(String einrichtung) {
			this.name = einrichtung;
		}

		@Override
		/**
		 * NOTE: ueberprueft ob die Liste der infrastrukuren ein element mit der selben bezeichnung enthaelt
		 * 
		 * Vorbedingung: Parameter duerfen nicht NULL sein
		 */
		public boolean select(Ort item) {
			for (String str : item.infrastruktur) {
				if (str.equalsIgnoreCase(name)) {
					return true;
				}
			}
			return false;
		}

	}
}
