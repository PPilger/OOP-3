import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * BAD: Staerkere Objektkopplung mit Mitglied, Termin und Termine. Das kann man
 * dadurch reduzieren, dass man den Terminvorschlag als innere Klasse von
 * Termine sieht. An dieser Stelle kann man ihn als Teil des
 * Hinzufuege-Prozesses verstehen.
 * 
 * Invariante: termin, target und offen sind ungleich null.
 * 
 * Invariante: offen enthaelt keine Elemente gleich null. offen enthaelt keine
 * doppelten Eintraege.
 * 
 * @author Peter Pilgerstorfer
 */
public class Terminvorschlag implements Serializable {
	private static final long serialVersionUID = 1L;

	private Termin termin;
	private Termine target;

	private List<Mitglied> offen;

	/**
	 * NOTE: Legt neuen Termin
	 * 
	 * Vorbedingung: termin und target sind ungleich null.
	 * 
	 * Nachbedingung: offen enthaelt alle Teilnehmer von termin
	 * 
	 * @param termin
	 *            an, der bei Zustimmung zu
	 * @param target
	 *            angegebenen Terminen hinzugefuegt wird
	 */
	public Terminvorschlag(Termin termin, Termine target) {
		this.termin = termin;
		this.target = target;
		this.offen = new ArrayList<Mitglied>(termin.getTeilnehmer());
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist ungleich null
	 */
	public Termin getTermin() {
		return termin;
	}

	/**
	 * NOTE: Einzelnes Mitglied akzeptiert Termin, muss niemand mehr akzeptieren
	 * wird der Termin zu Termine hinzugefuegt.
	 * 
	 * Vorbedingung: mitglied ist ungleich null
	 * 
	 * Nachbedingung: mitglied ist nicht mehr in offen enthalten
	 * 
	 * @param mitglied
	 *            Das Mitglied das den Termin akzeptiert
	 */
	public void accept(Mitglied mitglied) {
		offen.remove(mitglied);
		if (accepted()) {
			target.add(this);
		}
	}

	/**
	 * NOTE: Terminvorschlag wird abgelehnt und verworfen
	 * 
	 * Vorbedingung: mitglied ist ungleich null
	 * 
	 * Nachbedingung: Wenn mitglied Teilnehmer des Termins ist, ist der
	 * Terminvorschlag aus den Terminvorschlag-Queues der Teilnehmer entfernt
	 * worden und alle Teilnehmer haben eine Nachricht ueber das Ablehnen des
	 * Termins erhalten.
	 * 
	 * @param mitglied
	 *            Das ablehnende Mitglied
	 * @param nachricht
	 *            Grund der Ablehnung
	 */
	public void decline(Mitglied mitglied, String nachricht) {
		if (termin.getTeilnehmer().contains(mitglied)) {
			for (Mitglied m : termin.getTeilnehmer()) {
				m.revidiere(this);
				if (m != mitglied) {
					m.sende(mitglied + ": " + nachricht + " - " + termin);
				}
			}
		}
	}

	public boolean accepted() {
		return offen.isEmpty();
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist ungleich null
	 */
	public String toString() {
		return termin.toString();
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist ungleich null
	 */
	public String toDetailString() {
		return termin.toDetailString() + ", Ausstehend: " + offen;
	}
}
