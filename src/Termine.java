import java.util.Iterator;
import java.util.List;

/**
 * Eine Sammlung von Terminen.
 * 
 * @author Peter Pilgerstorfer
 * 
 */
public class Termine extends Selection<Termin> {
	private static final long serialVersionUID = 1L;

	/**
	 * Nachbedingung: das neue Termine Objekt enthaelt keine Termine
	 */
	public Termine() {
	}

	/**
	 * NOTE: Erstelle eine neue Termin Sammlung die auf den selben Daten wie
	 * <code>base</code> arbeitet. Es sind jedoch nur Elemente sichtbar, die von
	 * den Selektoren selektiert werden.
	 * 
	 * Vorbedingung: base und selectors sind ungleich null. selectors enthaelt
	 * keine Elemente gleich null.
	 * 
	 * Nachbedinung: das neue Termine Objekt arbeitet auf den selben Elementen
	 * wie base. Es sind jedoch nur Elemente sichtbar, die mit den selectors
	 * selektiert werden.
	 */
	private Termine(Termine base, List<Selector<Termin>> selectors) {
		super(base, selectors);
	}

	/**
	 * NOTE: Liefert eine Selektion der in diesem Objekt gespeicherten Termine.
	 * Mit den uebergebenen Selektoren kann bestimmt werden, welche Termine
	 * selektiert werden. Aenderungen in der zurueckgegebenen Selektion wirken
	 * sich direkt auf das Original aus.
	 * 
	 * Vorbedingung: selectors ist ungleich null und enthaelt keine Elemente
	 * gleich null.
	 * 
	 * Nachbedinung: das zurueckgegebene Termine Objekt arbeitet auf den selben
	 * Elementen wie this. Es sind jedoch nur Elemente sichtbar, die mit den
	 * selectors selektiert werden.
	 */
	public Termine select(List<Selector<Termin>> selectors) {
		return new Termine(this, selectors);
	}

	/**
	 * NOTE: Parameter darf nicht NULL sein Fuegt einen neuen Termin hinzu,
	 * sofern dieser keine Teilnehmer besitzt. Um einen Termin mit Teilnehmern
	 * anzulegen sollte <code>Band.sendeTerminvorschlag()</code> verwendet
	 * werden.
	 * 
	 * Vorbedingung: termin ist ungleich null
	 * 
	 * Nachbedingung: Wenn der Rueckgabewert true ist, wurde termin zur Liste
	 * hinzugefuegt.
	 * 
	 * @return true, wenn der Termin hinzugefuegt wurde, false wenn er
	 *         Teilnehmer hat.
	 */
	@Override
	public boolean add(Termin termin) {
		if (termin.getTeilnehmer().isEmpty()) {
			return super.add(termin);
		}

		return false;
	}

	/**
	 * NOTE: Fuegt einen Termin zur Liste hinzu, wenn alle Teilnehmer dem
	 * entsprechenden Terminvorschlag zugestimmt haben.
	 * 
	 * Vorbedingung: vorschlag ist ungleich null
	 * 
	 * @param vorschlag
	 */
	public void add(Terminvorschlag vorschlag) {
		if (vorschlag.accepted()) {
			super.add(vorschlag.getTermin());
		}
	}

	/**
	 * NOTE: Entfernt alle selektierten Termine und benachrichtigt alle
	 * Teilnehmer.
	 * 
	 * Nachbedingung: der Rueckgabewert ist >= 0
	 * 
	 * @return die Anzahl der entfernten Termine
	 */
	@Override
	public int remove() {
		int removed = 0;

		Iterator<Termin> iter = iterator();
		while (iter.hasNext()) {
			Termin termin = iter.next();

			for (Mitglied teilnehmer : termin.getTeilnehmer()) {
				teilnehmer.sende(termin + " wurde entfernt!");
			}

			iter.remove();
			removed++;
		}

		return removed;
	}

	/**
	 * NOTE: Berechnet den Gewinn aller selektierten Termine.
	 */
	public double getGewinn() {
		double gewinn = 0;

		for (Termin termin : this) {
			gewinn += termin.getEinnahmen() - termin.getAusgaben();
		}

		return gewinn;
	}

	/**
	 * NOTE: Berechnet die Kosten aller selektierten Termine.
	 * 
	 * Nachbedingung: der Rueckgabewert ist >= 0
	 */
	public double getKosten() {
		double kosten = 0;

		for (Termin termin : this) {
			kosten += termin.getAusgaben();
		}

		return kosten;
	}
}
