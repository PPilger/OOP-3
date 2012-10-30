import java.util.List;

/**
 * NOTE: Eine Sammlung von Mitgliedern.
 * 
 * @author Peter Pilgerstorfer
 */
public class Mitglieder extends Selection<Mitglied> {
	private static final long serialVersionUID = 1L;

	/**
	 * Nachbedingung: das neue Mitglieder Objekt enthaelt keine Mitglieder
	 */
	public Mitglieder() {
	}

	/**
	 * NOTE: Erstelle eine neue Mitglieder Sammlung die auf den selben Daten wie
	 * <code>base</code> arbeitet. Es sind jedoch nur Elemente sichtbar, die von
	 * den Selektoren selektiert werden.
	 * 
	 * Vorbedingung: base und selectors sind ungleich null. selectors enthaelt
	 * keine Elemente gleich null.
	 * 
	 * Nachbedinung: das neue Mitglieder Objekt arbeitet auf den selben
	 * Elementen wie base. Es sind jedoch nur Elemente sichtbar, die mit den
	 * selectors selektiert werden.
	 */
	private Mitglieder(Mitglieder base, List<Selector<Mitglied>> selectors) {
		super(base, selectors);
	}

	/**
	 * NOTE: Liefert eine Selektion der in diesem Objekt gespeicherten
	 * Mitglieder. Mit den uebergebenen Selektoren kann bestimmt werden, welche
	 * Mitglieder selektiert werden. Aenderungen in der zurueckgegebenen
	 * Selektion wirken sich direkt auf das Original aus.
	 * 
	 * Vorbedingung: selectors ist ungleich null und enthaelt keine Elemente
	 * gleich null.
	 * 
	 * Nachbedinung: das zurueckgegebene Mitglieder Objekt arbeitet auf den
	 * selben Elementen wie this. Es sind jedoch nur Elemente sichtbar, die mit
	 * den selectors selektiert werden.
	 */
	public Mitglieder select(List<Selector<Mitglied>> selectors) {
		return new Mitglieder(this, selectors);
	}
}