import java.io.Serializable;
import java.util.List;

/**
 * NOTE: Speichert Ort mit zugehoeriger Infrastruktur
 * 
 * GOOD: Der Klassen-Zusammenhalt dieser Klasse ist niedrig. Es gibt wenige Abhaengigkeiten
 * 		 die aufeinander aufbauen. Nur der Selektor benštigt die Attribute um einen 
 * 		 Vergleich darzustellen. Somit wuerde das weglassen dieses Attributs ein nicht finden 
 * 		 des Selektors bedeuten.
 * 
 * GOOD: Die Objekt-Kopplung ist schwach. Ein Ort-Objekt interagiert mit wenigen anderen Objekten. 
 * 		 Somit ist gewaehrleistet, dass beim Loeschen eines Ort-Objekts nicht mit negativen Einfluessen 
 * 		 zu rechnen ist.
 * 
 * Invariante: bezeichnung und infrastruktur sind ungleich null. infrastruktur enthaelt keine Elemente gleich null.
 * 
 * @author VHD
 */
public class Ort implements Serializable {
	private static final long serialVersionUID = 1L;

	private String bezeichnung;
	private List<String> infrastruktur;

	/**
	 * Nachbedingung: der Rueckgabewert ist ungleich null
	 */
	public String toString() {
		return bezeichnung;
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist ungleich null
	 */
	public String toDetailString() {
		return bezeichnung + " " + infrastruktur.toString();
	}

	/**
	 * Vorbedingung: bezeichnung und infrastruktur sind ungleich null
	 * 
	 * @param bezeichnung
	 * @param infrastruktur
	 */
	public Ort(String bezeichnung, List<String> infrastruktur) {
		this.bezeichnung = bezeichnung;
		this.infrastruktur = infrastruktur;
	}

	public static class BezeichnungSelektor implements Selector<Ort> {

		private String name;
		private boolean enthaelt;

		/**
		 * NOTE: Stellt den Selektor so ein, dass Elemente erfolgreich verglichen
		 * werden, wenn die bezeichnung einander eins zu eins gleichen
		 * 
		 * @param bezeichnung
		 *            die zu pruefende bezeichnung
		 */
		public BezeichnungSelektor(String bezeichnung) {
			this.name = bezeichnung;
			enthaelt = false;
		}

		/**
		 * NOTE: Stellt den Selektor so ein, dass Elemente erfolgreich verglichen
		 * werden, sobald die bezeichnung den im Parameter angegeben String
		 * enthaelt
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
		/**
		 * Vorbedingung: item ist ungleich null
		 */
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

		public InfrastrukturSelektor(String einrichtung) {
			this.name = einrichtung;
		}

		@Override
		/**
		 * NOTE: ueberprueft ob die Liste der infrastrukuren ein element mit der selben bezeichnung enthaelt
		 * 
		 * Vorbedingung: item ist ungleich null
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
