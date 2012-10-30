import java.util.List;

/**
 * NOTE: Eine Sammlung von Mitgliedern.
 * 
 * @author Peter Pilgerstorfer
 */
public class Mitglieder extends Selection<Mitglied> {
	private static final long serialVersionUID = 1L;

	public Mitglieder() {
	}

	/**
	 * NOTE: Erstelle eine neue Mitglieder Sammlung die auf den selben Daten wie
	 * <code>base</code> arbeitet. Es sind jedoch nur Elemente sichtbar, die von
	 * den Selektoren selektiert werden.
	 * 
	 * Vorbedingung: Parameter durfen nicht NULL sein, Listen keine NULL Elemente enthalten
	 * 
	 * @param base
	 * @param selectors
	 */
	private Mitglieder(Mitglieder base, List<Selector<Mitglied>> selectors) {
		super(base, selectors);
	}

	/**
	 * NOTE: Liefert eine Selektion der in diesem Objekt gespeicherten Mitglieder. Mit
	 * den uebergebenen Selektoren kann bestimmt werden, welche Mitglieder
	 * selektiert werden. Aenderungen in der zurueckgegebenen Selektion wirken
	 * sich direkt auf das Original aus.
	 * 
	 * Vorbedingung: Parameter durfen nicht NULL sein, Listen keine NULL Elemente enthalten
	 * 
	 * @param selectors
	 * @return
	 */
	public Mitglieder select(List<Selector<Mitglied>> selectors) {
		return new Mitglieder(this, selectors);
	}
}