import java.util.List;

public class Orte extends Selection<Ort> {
	private static final long serialVersionUID = 1L;

	public Orte() {
	}

	/**
	 * Vorbedingung: Parameter darf nicht NULL sein, oder Elemente die NULL sind enthalten
	 * 
	 * @param base
	 * @param selectors
	 */
	private Orte(Orte base, List<Selector<Ort>> selectors) {
		super(base, selectors);
	}

	/**
	 * NOTE: Liefert eine Selektion der in diesem Objekt gespeicherten Orte. Mit den
	 * uebergebenen Selektoren kann bestimmt werden, welche Orte selektiert
	 * werden. Aenderungen in der zurueckgegebenen Selektion wirken sich direkt
	 * auf das Original aus.
	 * 
	 * Vorbedingung: Parameter darf nicht NULL sein, oder Elemente die NULL sind enthalten
	 * 
	 * @param selectors
	 * @return
	 */
	public Orte select(List<Selector<Ort>> selectors) {
		return new Orte(this, selectors);
	}
}
