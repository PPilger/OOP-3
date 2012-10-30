import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Ermoeglicht die ueberpruefung ob sich Zeitraeume ueberschneiden.
 * 
 * GOOD: Schwache Objektkopplung. Es wird auf keine anderen Klassen des
 * Programmes zugegriffen.
 * 
 * Invariante: zeitpunkte ist ungleich NULL und enthaelt keine Elemente gleich
 * NULL
 * 
 * Invariante: zeitpunkte ist aufsteigend sortiert
 * 
 * @author Peter Pilgerstorfer
 */
public class Zeitraum implements Serializable {
	private static final long serialVersionUID = 1L;

	List<Date> zeitpunkte = new ArrayList<Date>();

	/**
	 * Vorbedingung: zeitpunkte enthaelt keine Elemente gleich NULL
	 * Vorbedingung: zeitpunkte ist aufsteigend sortiert
	 * 
	 * Nachbedingung: der neue Zeitraum ist aus den Intervallen der Zeitpunkte
	 * zusammengesetzt. Zeitpunkte mit geraden Index (0,2,...) sind
	 * Intervallanfaenge, Zeitpunkte mit ungeradem Index (1,3,...) sind
	 * Intervallenden.
	 * 
	 * @param zeitpunkte
	 */
	public Zeitraum(Date... zeitpunkte) {
		this.zeitpunkte.addAll(Arrays.asList(zeitpunkte));
	}

	/**
	 * Vorbedingung: orig ist ungleich NULL
	 * 
	 * Nachbedingung: der neue Zeitraum ist eine Kopie von orig
	 * 
	 * @param orig
	 */
	public Zeitraum(Zeitraum orig) {
		this.zeitpunkte.addAll(orig.zeitpunkte);
	}

	public Date getFirst() {
		if (zeitpunkte.isEmpty()) {
			return null;
		}
		return zeitpunkte.get(0);
	}

	public Date getLast() {
		if (zeitpunkte.isEmpty()) {
			return null;
		}
		return zeitpunkte.get(zeitpunkte.size() - 1);
	}

	/**
	 * Vorbedingung: zeitpunkt ist ungleich NULL
	 * 
	 * @param zeitpunkt
	 * @return
	 */
	public boolean inZeitraum(Date zeitpunkt) {
		if (zeitpunkte.isEmpty()) {
			return true;
		}

		// NOTE: Ist der Zeitpunkt in einem Intervall enthalten?
		for (int i = 0; i < zeitpunkte.size() - 1; i += 2) {
			Date von = zeitpunkte.get(i);
			Date bis = zeitpunkte.get(i + 1);

			if (inZeitraum(zeitpunkt, von, bis)) {
				return true;
			}
		}

		// Zusicherung: keines der geschlossenen Intervalle beinhaltet zeitpunkt

		// NOTE: Ist das Zeitintervall am Ende offen?
		if ((zeitpunkte.size() & 0x1) == 1) {
			Date von = zeitpunkte.get(zeitpunkte.size() - 1);
			return !von.after(zeitpunkt);
		}

		// Zusicherung: kein Intervall beinhaltet zeitpunkt

		return false;
	}

	/**
	 * Vorbedingung: other ist ungleich NULL
	 * 
	 * @param other
	 * @return
	 */
	public boolean enthaelt(Zeitraum other) {
		if (zeitpunkte.isEmpty()) {
			return true; // NOTE: alles ist enthalten
		} else if (other.zeitpunkte.isEmpty()) {
			return false; // NOTE:etwas beschraenktes kann nicht alles enthalten
		}

		// NOTE: Es muessen alle Intervalle von other in einem Intervall von
		// this
		// enthalten sein
		for (int i = 0; i < other.zeitpunkte.size() - 1; i += 2) {
			Date von = other.zeitpunkte.get(i);
			Date bis = other.zeitpunkte.get(i + 1);

			if (!enthaelt(von, bis)) {
				return false;
			}
		}

		// Zusicherung: alle geschlossenen Intervalle von other sind im Zeitraum
		// enthalten

		// NOTE: Ueberpruefung des offenen Endintervalls
		if ((other.zeitpunkte.size() & 0x1) == 1) {
			Date von = other.zeitpunkte.get(other.zeitpunkte.size() - 1);
			return enthaelt(von);
		}

		// Zusicherung: other ist im Zeitraum enthalten

		return true;
	}

	@Override
	/**
	 * Nachbedingung: der Rueckgabewert ist ungleich NULL
	 */
	public String toString() {
		return toString(DateFormat.getDateInstance());
	}

	/**
	 * Vorbedingung: format ist ungleich NULL
	 * 
	 * Nachbedingung: der Rueckgabewert ist ungleich NULL
	 * 
	 * @param format
	 * @return
	 */
	public String toString(DateFormat format) {
		StringBuilder builder = new StringBuilder();

		builder.append('[');

		// NOTE: Ausgabe des ersten Intervalls
		if (0 < zeitpunkte.size() - 1) {
			Date von = zeitpunkte.get(0);
			Date bis = zeitpunkte.get(1);

			builder.append(format.format(von));
			builder.append(" - ");
			builder.append(format.format(bis));
		}

		// NOTE: Ausgabe der folgenden Intervalle
		for (int i = 2; i < zeitpunkte.size() - 1; i += 2) {
			Date von = zeitpunkte.get(i);
			Date bis = zeitpunkte.get(i + 1);

			builder.append(", ");
			builder.append(format.format(von));
			builder.append(" - ");
			builder.append(format.format(bis));
		}

		// NOTE: Ausgabe des offenen Endintervalls
		if ((zeitpunkte.size() & 0x1) == 1) {
			Date von = zeitpunkte.get(zeitpunkte.size() - 1);

			// NOTE: ", " nur anhaengen, wenn es Intervalle davor gibt.
			if (zeitpunkte.size() > 1) {
				builder.append(", ");
			}

			builder.append(format.format(von));
			builder.append(" - ");
		}

		builder.append(']');

		return builder.toString();
	}

	/**
	 * Vorbedingung: t, von und bis sind ungleich NULL
	 * 
	 * Vorbedingung: der Zeitpunkt von liegt vor dem Zeitpunkt bis
	 */
	private static boolean inZeitraum(Date t, Date von, Date bis) {
		return !bis.before(t) && !von.after(t);
	}

	/**
	 * Vorbedingung: von ist ungleich NULL
	 */
	private boolean enthaelt(Date von) {
		if (zeitpunkte.isEmpty()) {
			return true;
		}

		// NOTE: Ist das Zeitraumintervall am Ende geschlossen?
		if ((zeitpunkte.size() & 0x1) == 0) {
			return false;
		}

		// Zusicherung: der Zeitraum ist offen (das letzte Intervall ist offen)

		// NOTE: Das uebergebene (offene) Intervall ist enthalten,
		// NOTE: wenn es im offenen Intervall des Zeitraums enthalten ist
		Date v = zeitpunkte.get(zeitpunkte.size() - 1);
		return !v.after(von);
	}

	/**
	 * NOTE: Parameter duerfen nicht NULL sein
	 * 
	 * Vorbedingung: von und bis sind ungleich NULL
	 * 
	 * Vorbedingung: der Zeitpunkt von liegt vor dem Zeitpunkt bis
	 */
	private boolean enthaelt(Date von, Date bis) {
		if (zeitpunkte.isEmpty()) {
			return true;
		}

		// NOTE: Ist das Intervall in den Zeitraumintervallen enthalten?
		for (int i = 0; i < zeitpunkte.size() - 1; i += 2) {
			Date v = zeitpunkte.get(i);
			Date b = zeitpunkte.get(i + 1);

			if (inZeitraum(von, v, b) && inZeitraum(bis, v, b)) {
				return true;
			}
		}

		// Zusicherung: das Intervall [von, bis] ist nicht in den geschlossenen
		// Intervallen enthalten

		// NOTE: Ist das Zeitraumintervall am Ende offen?
		if ((zeitpunkte.size() & 0x1) == 1) {
			Date v = zeitpunkte.get(zeitpunkte.size() - 1);
			return !v.after(von);
		}

		// Zusicherung: das Intervall [von, bis] ist nicht im Zietraum enthalten

		return false;
	}
}
