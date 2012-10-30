import java.util.List;

public class Orte extends Selection<Ort> {
	private static final long serialVersionUID = 1L;

	/**
	 * Nachbedingung: das neue Orte Objekt enthaelt keine Orte
	 */
	public Orte() {
	}

	/**
	 * Vorbedingung: base und selectors sind ungleich null. selectors enthaelt
	 * keine Elemente gleich null.
	 * 
	 * Nachbedinung: das neue Orte Objekt arbeitet auf den selben Elementen wie
	 * base. Es sind jedoch nur Elemente sichtbar, die mit den selectors
	 * selektiert werden.
	 */
	private Orte(Orte base, List<Selector<Ort>> selectors) {
		super(base, selectors);
	}

	/**
	 * NOTE: Liefert eine Selektion der in diesem Objekt gespeicherten Orte. Mit
	 * den uebergebenen Selektoren kann bestimmt werden, welche Orte selektiert
	 * werden. Aenderungen in der zurueckgegebenen Selektion wirken sich direkt
	 * auf das Original aus.
	 * 
	 * Vorbedingung: selectors ist ungleich null und enthaelt keine Elemente
	 * gleich null.
	 * 
	 * Nachbedinung: das zurueckgegebene Orte Objekt arbeitet auf den selben
	 * Elementen wie this. Es sind jedoch nur Elemente sichtbar, die mit den
	 * selectors selektiert werden.
	 */
	public Orte select(List<Selector<Ort>> selectors) {
		return new Orte(this, selectors);
	}
}
