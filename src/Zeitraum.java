import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Ermoeglicht die ueberpruefung ob sich Zeitraeume ueberschneiden.
 * 
 * Invariante: nur getFirst() und getLast() koennen NULL zurueck geben
 * 
 * @author Peter Pilgerstorfer
 */
public class Zeitraum implements Serializable {
	private static final long serialVersionUID = 1L;

	//Darf niemals NULL werden, oder Elemente enthalten die NULL sind
	List<Date> zeitpunkte = new ArrayList<Date>();

	/**
	 * Vorbedingung: Parameter duerfen nicht NULL sein
	 * 
	 * @param zeitpunkte
	 */
	public Zeitraum(Date... zeitpunkte) {
		this.zeitpunkte.addAll(Arrays.asList(zeitpunkte));
	}

	/**
	 * Vorbedingung: Parameter duerfen nicht NULL sein
	 * 
	 * @param orig
	 */
	public Zeitraum(Zeitraum orig) {
		this.zeitpunkte.addAll(orig.zeitpunkte);
	}

	/**
	 * Nachbedinung: kann NULL zurueck geben
	 * 
	 * @return Element 0
	 */
	public Date getFirst() {
		if (zeitpunkte.isEmpty()) {
			return null;
		}
		return zeitpunkte.get(0);
	}

	/**
	 * Nachbedinung: kann NULL zurueck geben
	 * 
	 * @return letztes Element (size - 1) 
	 */
	public Date getLast() {
		if (zeitpunkte.isEmpty()) {
			return null;
		}
		return zeitpunkte.get(zeitpunkte.size() - 1);
	}

	/**
	 * Vorbedingung: Parameter darf nicht NULL sein
	 * 
	 * @param zeitpunkt
	 * @return
	 */
	public boolean inZeitraum(Date zeitpunkt) {
		if (zeitpunkte.isEmpty()) {
			return true;
		}

		// Ist der Zeitpunkt in einem Intervall enthalten?
		for (int i = 0; i < zeitpunkte.size() - 1; i += 2) {
			Date von = zeitpunkte.get(i);
			Date bis = zeitpunkte.get(i + 1);

			if (inZeitraum(zeitpunkt, von, bis)) {
				return true;
			}
		}

		// Ist das Zeitintervall am Ende offen?
		if ((zeitpunkte.size() & 0x1) == 1) {
			Date von = zeitpunkte.get(zeitpunkte.size() - 1);
			return !von.after(zeitpunkt);
		}

		return false;
	}

	/**
	 * Vorbedingung: Parameter darf nicht NULL sein
	 * 
	 * @param other
	 * @return
	 */
	public boolean enthaelt(Zeitraum other) {
		if (zeitpunkte.isEmpty()) {
			return true; // alles ist enthalten
		} else if (other.zeitpunkte.isEmpty()) {
			return false; // etwas beschraenktes kann nicht alles enthalten
		}

		// Es muessen alle Intervalle von other in einem Intervall von this
		// enthalten sein
		for (int i = 0; i < other.zeitpunkte.size() - 1; i += 2) {
			Date von = other.zeitpunkte.get(i);
			Date bis = other.zeitpunkte.get(i + 1);

			if (!enthaelt(von, bis)) {
				return false;
			}
		}

		// Ueberpruefung des offenen Endintervalls
		if ((other.zeitpunkte.size() & 0x1) == 1) {
			Date von = other.zeitpunkte.get(other.zeitpunkte.size() - 1);
			return enthaelt(von);
		}

		return true;
	}

	@Override
	public String toString() {
		return toString(DateFormat.getDateInstance());
	}

	/**
	 * Vorbedingung: Parameter darf nicht NULL sein
	 * 
	 * @param format
	 * @return
	 */
	public String toString(DateFormat format) {
		StringBuilder builder = new StringBuilder();

		builder.append('[');

		// Ausgabe des ersten Intervalls
		if (0 < zeitpunkte.size() - 1) {
			Date von = zeitpunkte.get(0);
			Date bis = zeitpunkte.get(1);

			builder.append(format.format(von));
			builder.append(" - ");
			builder.append(format.format(bis));
		}

		// Ausgabe der folgenden Intervalle
		for (int i = 2; i < zeitpunkte.size() - 1; i += 2) {
			Date von = zeitpunkte.get(i);
			Date bis = zeitpunkte.get(i + 1);

			builder.append(", ");
			builder.append(format.format(von));
			builder.append(" - ");
			builder.append(format.format(bis));
		}

		// Ausgabe des offenen Endintervalls
		if ((zeitpunkte.size() & 0x1) == 1) {
			Date von = zeitpunkte.get(zeitpunkte.size() - 1);

			// ", " nur anhaengen, wenn es Intervalle davor gibt.
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
	 * Vorbedingung: Parameter duerfen nicht NULL sein
	 * 
	 * @param t
	 * @param von
	 * @param bis
	 * @return
	 */
	private static boolean inZeitraum(Date t, Date von, Date bis) {
		return !bis.before(t) && !von.after(t);
	}

	/**
	 * Vorbedingung: Parameter darf nicht NULL sein
	 * 
	 * @param von
	 * @return
	 */
	private boolean enthaelt(Date von) {
		if (zeitpunkte.isEmpty()) {
			return true;
		}

		// Ist das Zeitraumintervall am Ende geschlossen?
		if ((zeitpunkte.size() & 0x1) == 0) {
			return false;
		}

		// Das uebergebene (offene) Intervall ist enthalten,
		// wenn es im offenen Intervall des Zeitraums enthalten ist
		Date v = zeitpunkte.get(zeitpunkte.size() - 1);
		return !v.after(von);
	}

	/**
	 * NOTE: Parameter duerfen nicht NULL sein
	 * 
	 * Vorbedingung: der Zeitpunkt <code>von</code> liegt vor <code>bis</code>! Parameter duerfen nicht NULL sein.
	 */
	private boolean enthaelt(Date von, Date bis) {
		if (zeitpunkte.isEmpty()) {
			return true;
		}

		// Ist das Intervall in den Zeitraumintervallen enthalten?
		for (int i = 0; i < zeitpunkte.size() - 1; i += 2) {
			Date v = zeitpunkte.get(i);
			Date b = zeitpunkte.get(i + 1);

			if (inZeitraum(von, v, b) && inZeitraum(bis, v, b)) {
				return true;
			}
		}

		// Ist das Zeitraumintervall am Ende offen?
		if ((zeitpunkte.size() & 0x1) == 1) {
			Date v = zeitpunkte.get(zeitpunkte.size() - 1);
			return !v.after(von);
		}

		return false;
	}
}
