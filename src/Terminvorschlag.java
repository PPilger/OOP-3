import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Terminvorschlag implements Serializable {
	private static final long serialVersionUID = 1L;

	private Termin termin;
	private Termine target;
	
	//Liste darf nie NULL werden oder Elemente die NULL sind enthalten
	private List<Mitglied> offen;

	//Parameter duerfen nicht NULL sein
	/**
	 * Legt neuen Termin 
	 * @param termin an, der bei Zustimmung zu 
	 * @param target angegebenen Terminen hinzugefuegt wird
	 */
	public Terminvorschlag(Termin termin, Termine target) {
		this.termin = termin;
		this.target = target;
		this.offen = new ArrayList<Mitglied>(termin.getTeilnehmer());
	}

	public Termin getTermin() {
		return termin;
	}

	
	/**Einzelnes Mitglied akzeptiert Termin, muss niemand mehr akzeptieren wird der Termin zu Termine hinzugefuegt.
	 * Parameter darf nicht NULL sein
	 * @param mitglied Das Mitglied das den Termin akzeptiert
	 */
	public void accept(Mitglied mitglied) {
		offen.remove(mitglied);
		if (accepted()) {
			target.add(this);
		}
	}

	/**
	 * Terminvorschlag wird abgelehnt und verworfen
	 * @param mitglied Das ablehnende Mitglied
	 * @param nachricht Grund der Ablehnung
	 */
	public void decline(Mitglied mitglied, String nachricht) {
		if (termin.getTeilnehmer().contains(mitglied)) {
			for (Mitglied m : termin.getTeilnehmer()) {
				assert(m!= null);
				m.revidiere(this);
				if(m != mitglied) {
					m.sende(mitglied + ": " + nachricht + " - " + termin);
				}
			}
		}
	}

	public boolean accepted() {
		return offen.isEmpty();
	}

	public String toString() {
		return termin.toString();
	}

	public String toDetailString() {
		return termin.toDetailString() + ", Ausstehend: " + offen;
	}
}
